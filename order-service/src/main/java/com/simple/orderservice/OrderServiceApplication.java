package com.simple.orderservice;

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
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.support.SendToMethodReturnValueHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.simple.orderservice.model.OrderCMDStatus;
import com.simple.orderservice.model.OrderEvent;
import com.simple.orderservice.model.OrderEventStore;
import com.simple.orderservice.model.Orders;
import com.simple.orderservice.service.OrderEventStoreService;
import com.simple.orderservice.service.OrderService;


@RestController
@EnableBinding(OrderSource.class)
@EnableAutoConfiguration
@SpringBootApplication
public class OrderServiceApplication {

	@Autowired
	private  OrderService orderService;
	
	@Autowired
	private  OrderEventStoreService orderEventStoreService;
	
	
	//private OrderSource source;
	
	public static MessageChannel source;
	//private SubscribableChannel incomingsource;
	//public static MessageChannel rollback;
	
	
	public OrderServiceApplication(OrderSource channels) {
		source=channels.ordersOut();
		//this.incomingsource=channels.ordersIn();
		//rollback=channels.rollback();
	}

	@PostMapping("/msg/")
	public void publish(@RequestBody OrderEvent orderEvent) {
		
		System.out.println("\n"+orderEvent.getOrder().getOrderId()+"\n"+orderEvent.getOrder().getProName()+"\n"+orderEvent.getOrder().getQty()+"\nOrder Status \t"+orderEvent.getStatus().toString()+"\nOrder Cmd Status \t"+orderEvent.getCmdStatus());
	    
	    
		Message<OrderEvent> orderin=MessageBuilder.withPayload(orderEvent).build();
		source.send(orderin);
		orderService.addOrder((Orders)orderEvent.getOrder());
		//System.out.println((Orders)orderEvent.getOrder());
		orderEventStoreService.addOrderEvent(new OrderEventStore(orderEvent.getOrder().getOrderId(),
				orderEvent.getOrder().getProName(),
				orderEvent.getOrder().getQty(),
				orderEvent.getStatus().toString(),
				orderEvent.getCmdStatus().toString())
				);
		
		
	}
	
	//creating the listener
	
	@Component
	@EnableBinding(OrderSource.class)
	@EnableAutoConfiguration
	public static class SimpleTest {
		
		private final Log log=LogFactory.getLog(getClass());
		//////
		/*@StreamListener(target = OrderSource.INPUT)
	    public void inputPayload(OrderEvent orderEvent) {
	    	log.info("\n"+orderEvent.getOrder().getOrderId()+"\n"+orderEvent.getOrder().getProName()+"\n"+orderEvent.getOrder().getQty()+"\nOrder Status \t"+orderEvent.getStatus().toString()+"\nOrder Cmd Status \t"+orderEvent.getCmdStatus());
	    }*/
	    //////
		
		
	  /*  @StreamListener(target = OrderSource.ROLLBACK)
	    public void rollbackPayload(OrderEvent orderEvent) {
	    	log.info("\n"+orderEvent.getOrder().getOrderId()+"\n"+orderEvent.getOrder().getProName()+"\n"+orderEvent.getOrder().getQty()+"\nOrder Status \t"+orderEvent.getStatus().toString()+"\nOrder Cmd Status \t"+orderEvent.getCmdStatus());
	    
	    	
	    	if(orderEvent.getCmdStatus().toString().equals(OrderCMDStatus.CHECKOUT.toString())) {
	    		//to handle when the order is processed without aborting
	    		orderService.updateOrder(orderEvent.getOrder());
	    		log.info("\n"+orderEvent.getOrder().toString()+"Order Cmd Status"+orderEvent.getCmdStatus());
	    	    
		    		
	    		
	    	}else if(orderEvent.getCmdStatus().toString().equals(OrderCMDStatus.ABORT.toString())) {
	    		//this is where we rollback the transaction and undo the stock changes
	    		Message<OrderEvent> orderrollback=MessageBuilder.withPayload(orderEvent).build();
	    		//this.rollback.send(orderrollback);
	    		orderService.deleteOrder(orderEvent.getOrder());
	    		
	    		log.info("\n"+orderEvent.getOrder().toString()+"Order Cmd Status"+orderEvent.getCmdStatus());
	    		rollback.send(orderrollback);
	    		
	    	}
	    	
			orderEventStoreService.addOrderEvent(new OrderEventStore(orderEvent.getOrder().getOrderId(),
					orderEvent.getOrder().getProName(),
					orderEvent.getOrder().getQty(),
					orderEvent.getStatus().toString(),
					orderEvent.getCmdStatus().toString())
					);

	    }*/

	}
	
	
	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
		
		
	}

	
}

//////////////////////////////////////End of the Application Class/////////////////////////////////////////////////////


interface OrderSource {
	
/*String INPUT="ordersIn";	
@Input(OrderSource.INPUT)
SubscribableChannel ordersIn();
*/

String OUTPUT="ordersOut";
@Output(OrderSource.OUTPUT)
MessageChannel ordersOut();


/*String ROLLBACK="rollback";
@Output(OrderSource.ROLLBACK)
MessageChannel rollback();
	*/
}
