package com.simple.stockservice;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import com.simple.stockservice.model.OrderCMDStatus;
import com.simple.stockservice.model.OrderEvent;
import com.simple.stockservice.model.OrderEventType;
import com.simple.stockservice.model.Stock;
import com.simple.stockservice.service.StockService;

@RestController
@EnableBinding(StockSource.class)
@EnableAutoConfiguration
@SpringBootApplication
public class StockServiceApplication {
	
	@Autowired
	private StockService stockService;

	private SubscribableChannel source;
	public static MessageChannel outgngsource;
	public static SubscribableChannel rollable;
	
	private final Log log=LogFactory.getLog(getClass());
	
	public StockServiceApplication(StockSource channel) {
		super();
		this.source=channel.ordersOut();//this channel gets the messages from the order-service
		outgngsource=channel.ordersIn();//this channel pushes validated messages to shipping-service
		rollable=channel.rollbackInput();//this gets the messages from the shipping-service
		
	}
	
	@GetMapping("/stock/")
	public void checkStock() {
		List<Stock> results=stockService.getAllStocks();
				
		log.info(results.toString());
	}
	
	

	
	@Component
	@EnableBinding(StockSource.class)
	@EnableAutoConfiguration
	public static class SimpleTest {
		private final Log log=LogFactory.getLog(getClass());
		
		@Autowired
		private StockService stockService;

	    @StreamListener(target = StockSource.INPUT)
	    public void inputPayload(OrderEvent orderEvent) {
	    	log.info("\n"+orderEvent.getOrder().getOrderId()+"\n"+orderEvent.getOrder().getProName()+"\n"+orderEvent.getOrder().getQty()+"\nOrder Status \t"+orderEvent.getStatus().toString());
	    	//this is simple hardcoded logic for the stream processing and you 
	    	//can go depth with this as you please
	    	
	    	
    		Optional<Stock> stockobj=stockService.getStockById(Integer.parseInt(orderEvent.getOrder().getOrderId()));
    		
    			
    		
	    	if(stockobj.get().getQty() >= orderEvent.getOrder().getQty()) {
	    		orderEvent.setStatus(OrderEventType.APRROVED);
	    		
	    		log.info("reserving the stock and updating the database.......................................");
				
				int updatedQty=stockobj.get().getQty()-orderEvent.getOrder().getQty();
	    		
	    		stockService.updateStock(new Stock(Integer.parseInt(orderEvent.getOrder().getOrderId()),orderEvent.getOrder().getProName(),updatedQty));
	    		log.info("\n"+orderEvent.getOrder().getOrderId()+"\n"+orderEvent.getOrder().getProName()+"\n"+orderEvent.getOrder().getQty()+"\nOrder Status \t"+orderEvent.getStatus().toString()+"\nOrder Status \t"+orderEvent.getCmdStatus() +"\nstock service has updated the entity after reserving");
		    	
	    		
				//stockService.addStock(new Stock(orderEvent.getOrder().getOrderId(),orderEvent.getOrder().getProName(),orderEvent.getOrder().getQty()));
				log.info(orderEvent.getOrder().getOrderId()+"\n available qty for the given product after reservation in stock : "+updatedQty);
				
				log.info("\nsending the validated order to the shipping-service.......................................");
		
				Message<OrderEvent> orderin=MessageBuilder.withPayload(orderEvent).build();
				outgngsource.send(orderin);
				
				
	    		
	    	}else {
	    		orderEvent.setStatus(OrderEventType.REJECTED);	
		    	
	    	}
	    	
    		
	    	/*	
	    	if(orderEvent.getOrder().getQty()<10) {
	    		orderEvent.setStatus(OrderEventType.APRROVED);
	    		
	    	}else {
	    		orderEvent.setStatus(OrderEventType.REJECTED);	
		    	
	    	}
	    	
	    	Message<OrderEvent> orderin=MessageBuilder.withPayload(orderEvent).build();
			outgngsource.send(orderin);
			
			log.info("reserving the stock and updating the database.......................................");
			
			int updatedQty=stockobj.get().getQty()-orderEvent.getOrder().getQty();
    		
    		stockService.updateStock(new Stock(Integer.parseInt(orderEvent.getOrder().getOrderId()),orderEvent.getOrder().getProName(),updatedQty));
    		log.info("\n"+orderEvent.getOrder().getOrderId()+"\n"+orderEvent.getOrder().getProName()+"\n"+orderEvent.getOrder().getQty()+"\nOrder Status \t"+orderEvent.getStatus().toString()+"\nOrder Status \t"+orderEvent.getCmdStatus() +"\nstock service has updated the entity after reserving");
	    	
    		
			//stockService.addStock(new Stock(orderEvent.getOrder().getOrderId(),orderEvent.getOrder().getProName(),orderEvent.getOrder().getQty()));
			log.info(orderEvent.getOrder().getOrderId()+"\nadded the stock entity to the database.......................................");
			
			log.info("\nsending the validated order to the shipping-service.......................................");
			*/
	    }
	    
	    
	    //undoing the changes for the stock
	    
	    @StreamListener(target = StockSource.ROLLBACKINPUT)
	    public void rollbackStockChanges(OrderEvent orderEvent) {
	    	
	    	log.info("changed CMD status Order came from the shipping-service.......................................");
	    	
	    	log.info("\n"+orderEvent.getOrder().getOrderId()+"\n"+orderEvent.getOrder().getProName()+"\n"+orderEvent.getOrder().getQty()+"\nOrder Status \t"+orderEvent.getStatus().toString()+"\nOrder Status \t"+orderEvent.getCmdStatus());
	    	//this is simple hardcoded logic for the stream processing and you 
	    	//can go depth with this as you please
	    	if( orderEvent.getCmdStatus().toString().equals(OrderCMDStatus.ABORT.toString()) ) {
	    		//here undoing the changes the 
	    		//Stock stockobj=new Stock(orderEvent.getOrder().getOrderId(),orderEvent.getOrder().getProName(),orderEvent.getOrder().getQty());
	    		log.info("\n"+orderEvent.getOrder().getOrderId()+"\n"+orderEvent.getOrder().getProName()+"\n"+orderEvent.getOrder().getQty()+"\nOrder Status \t"+orderEvent.getStatus().toString()+"\nOrder Status \t"+orderEvent.getCmdStatus() +"the stock is going to be updated with the rollback");
		    	
	    		Optional<Stock> prevobj=stockService.getStockById(Integer.parseInt(orderEvent.getOrder().getOrderId()));
	    		int updatedQty=orderEvent.getOrder().getQty()+prevobj.get().getQty();
	    		log.info("changed and updated qty that will be used in the rollback operation :  "+ updatedQty);
	    		stockService.updateStock(new Stock(Integer.parseInt(orderEvent.getOrder().getOrderId()),orderEvent.getOrder().getProName(),updatedQty));
	    		log.info("\n"+orderEvent.getOrder().getOrderId()+"\n"+orderEvent.getOrder().getProName()+"\n"+orderEvent.getOrder().getQty()+"\nOrder Status \t"+orderEvent.getStatus().toString()+"\nOrder Status \t"+orderEvent.getCmdStatus() +"the stock has been updated with the rollback");
		    	
	    		
	    	}else {
	    		
	    		log.info("\n"+orderEvent.getOrder().getOrderId()+"\n"+orderEvent.getOrder().getProName()+"\n"+orderEvent.getOrder().getQty()+"\nOrder Status \t"+orderEvent.getStatus().toString()+"\nOrder Status \t"+orderEvent.getCmdStatus() +"the order is ready to checkout no rollback to the DB");
		    	
	    		
	    	}
	    	
	    	//Message<OrderEvent> orderin=MessageBuilder.withPayload(orderEvent).build();
			//outgngsource.send(orderin);
			
			
	    }
	    
	    
	   

	}
	 
	
	
	public static void main(String[] args) {
		SpringApplication.run(StockServiceApplication.class, args);
		
	}
	
	
	



}


///////////////////////////////////////////////End Of ApplicationJava class///////////////////////////////////////////




interface StockSource {
	//this channel recieves the message to process from the order service	
	String INPUT="ordersOut";	
	@Input(StockSource.INPUT)
	SubscribableChannel ordersOut();
	
	String ROLLBACKINPUT="rollbackInput";	
	@Input(StockSource.ROLLBACKINPUT)
	SubscribableChannel rollbackInput();

	
	//this channel pushes the validated orders to shipping-service
	String OUTPUT="ordersIn";	
	@Output(StockSource.OUTPUT)
	MessageChannel ordersIn();


	}

