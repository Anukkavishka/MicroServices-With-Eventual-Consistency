package com.simple.orderservice.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.orderservice.model.OrderEventStore;
import com.simple.orderservice.repository.OrderEventStoreRepo;

@Transactional
@Service
public class OrderEventStoreService {
	
	@Autowired
	private OrderEventStoreRepo orderEventStoreRepo;
	
	
	public OrderEventStoreService() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OrderEventStore getOrderById(String orderId) {
		OrderEventStore obj = orderEventStoreRepo.getOne(orderId);
		return obj;
	}	
	
	public List<OrderEventStore> getAllOrderEvents(){
		return orderEventStoreRepo.findAll();
	}

	public void addOrderEvent(OrderEventStore orderEventStore){
		orderEventStoreRepo.save(orderEventStore);
    	        
	}

	
	
	

}
