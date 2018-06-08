package com.simple.shippingservice;

import java.util.List;
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

import com.simple.shippingservice.model.OrderCMDStatus;
import com.simple.shippingservice.model.OrderEvent;
import com.simple.shippingservice.model.OrderEventType;
import com.simple.shippingservice.model.Stock;
import com.simple.shippingservice.service.StockService;

@RestController
@EnableBinding(StockSource.class)
@EnableAutoConfiguration
@SpringBootApplication
public class ShippingServiceApplication {
	
	@Autowired
	private static StockService stockService;

	
	public static MessageChannel rollbackoutgng;
	public static SubscribableChannel validatedorderinput;
	
	private final Log log=LogFactory.getLog(getClass());
	
	public ShippingServiceApplication(StockSource channel) {
		super();
		rollbackoutgng=channel.rollback();
		validatedorderinput=channel.ordersIn();
		
	}
	
	//the listener
	@Component
	@EnableBinding(StockSource.class)
	@EnableAutoConfiguration
	public static class SimpleTest {
		private final Log log=LogFactory.getLog(getClass());
		
		//this listener changes the payload command status
	    @StreamListener(target = StockSource.INPUT)
	    public void inputPayload(OrderEvent orderEvent) {
	    	log.info("Quantity validated order came from the stock-service.......................................");
	    	
	    	log.info("\n"+orderEvent.getOrder().getOrderId()+"\n"+orderEvent.getOrder().getProName()+"\n"+orderEvent.getOrder().getQty()+"\nOrder Status \t"+orderEvent.getStatus().toString());
	    	//this is simple hardcoded logic for the stream processing and you 
	    		
	    		if(Integer.parseInt(orderEvent.getOrder().getOrderId()) < 5) {
		    	//just to cause success
		    		orderEvent.setCmdStatus(OrderCMDStatus.CHECKOUT);
		    		
		    	}else if(Integer.parseInt(orderEvent.getOrder().getOrderId()) > 5) {
		    	//just to cause failure
		    		orderEvent.setCmdStatus(OrderCMDStatus.ABORT);	
		    	}	

	    	Message<OrderEvent> orderout=MessageBuilder.withPayload(orderEvent).build();
	    	log.info("sending the validated order to the stock-service.......................................");
	    	
	    	rollbackoutgng.send(orderout);

	    	log.info("sent the validated order to the stock-service.......................................");
	    	
	    }

	}
	 
	
	
	public static void main(String[] args) {
		SpringApplication.run(ShippingServiceApplication.class, args);
		
	}
	
	
	



}


///////////////////////////////////////////////End Of ApplicationJava class///////////////////////////////////////////




interface StockSource {
	//this channel recieves the message to process from the order service	
	String INPUT="ordersIn";	
	@Input(StockSource.INPUT)
	SubscribableChannel ordersIn();
	
	//this channel provides the abort orders to stock serviceto roll back
	String OUTPUT="rollback";	
	@Output(StockSource.OUTPUT)
	MessageChannel rollback();
	

	}

