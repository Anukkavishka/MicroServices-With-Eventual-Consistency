package com.simple.shippingservice.serde;

import org.apache.kafka.common.errors.SerializationException;

import com.simple.shippingservice.model.OrderCMDStatus;
import com.simple.shippingservice.model.OrderEvent;
import com.simple.shippingservice.model.OrderEventType;
import com.simple.shippingservice.model.Orders;

import java.nio.ByteBuffer;
import java.util.Map;

public class Deser implements org.apache.kafka.common.serialization.Deserializer<OrderEvent> {


    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public OrderEvent deserialize(String topic, byte[] data) {

        String encoding="UTF8";
        
        try {
            if (data == null){
                System.out.println("Null recieved at deserialize");
                return null;
            }

            ByteBuffer buf = ByteBuffer.wrap(data);
            

            int sizeOfOrderStatus;
            int sizeOfOrderCmdStatus;
            int sizeOfOrderId;
            int sizeOfProName;
            int sizeOfQty;
            
            OrderEventType newstatus=OrderEventType.CREATED;
            OrderCMDStatus newcmdstatus=OrderCMDStatus.PROCESS;
            
            byte[] bytesOrderId;//done
            byte[] bytesProName;//
            byte[] bytesQty;//
            byte[] bytesOrderStatus;//
            byte[] bytesOrderCmdStatus;
            
            //orderid
            sizeOfOrderId=buf.getInt();
            bytesOrderId=new byte[sizeOfOrderId];
            buf.get(bytesOrderId);
            String deserializedOrderId=new String(bytesOrderId,encoding);
            
            //proname
            sizeOfProName=buf.getInt();
            bytesProName=new byte[sizeOfProName];
            buf.get(bytesProName);
            String deserializedProName=new String(bytesProName,encoding);

            //qty
            sizeOfQty=buf.getInt();
            bytesQty=new byte[sizeOfQty];
            buf.get(bytesQty);
            int deserializeQty=Integer.parseInt(new String(bytesQty,encoding));
            
            //orderstatus
            sizeOfOrderStatus=buf.getInt();
            bytesOrderStatus=new byte[sizeOfOrderStatus];
            buf.get(bytesOrderStatus);
            String deserializedOrderStatus=new String(bytesOrderStatus,encoding);
            
            if(OrderEventType.APRROVED.toString().equals(deserializedOrderStatus)) {
            	newstatus=OrderEventType.APRROVED;
            	
            }else if(OrderEventType.CREATED.toString().equals(deserializedOrderStatus)) {
            	newstatus=OrderEventType.CREATED;
            	
            }else if(OrderEventType.REJECTED.toString().equals(deserializedOrderStatus)) {
            	newstatus=OrderEventType.REJECTED;
            	
            }
            
            //ordercmdstatus

            sizeOfOrderCmdStatus=buf.getInt();
            bytesOrderCmdStatus=new byte[sizeOfOrderCmdStatus];
            buf.get(bytesOrderCmdStatus);
            String deserializedOrderCmdStatus=new String(bytesOrderCmdStatus,encoding);
            
            if(OrderCMDStatus.PROCESS.toString().equals(deserializedOrderStatus)) {
            	newcmdstatus=OrderCMDStatus.PROCESS;
            	
            }else if(OrderCMDStatus.ABORT.toString().equals(deserializedOrderStatus)) {
            	newcmdstatus=OrderCMDStatus.ABORT;
            	
            }else if(OrderCMDStatus.CHECKOUT.toString().equals(deserializedOrderStatus)) {
            	newcmdstatus=OrderCMDStatus.CHECKOUT;
            	
            }

           return new OrderEvent(new Orders(deserializedOrderId,deserializedProName,deserializeQty),newstatus,newcmdstatus);



        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to OrderEvent");
        }
    }

    @Override
    public void close() {

    }
}
