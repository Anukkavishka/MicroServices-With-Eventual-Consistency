package com.simple.orderservice.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.orderservice.model.Orders;
import com.simple.orderservice.repository.OrderRepo;

@Transactional
@Service
public class OrderService {
	
	@Autowired
	private OrderRepo orderRepo;
	
	
	
	public OrderService() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Orders getOrderById(String orderId) {
		Orders obj = orderRepo.getOne(orderId);
		return obj;
	}	
	
	public List<Orders> getAllOrders(){
		return orderRepo.findAll();
	}

	public void addOrder(Orders order){
        orderRepo.save(order);
    	        
	}
	
	public void updateOrder(Orders order) {
		orderRepo.save(order);
	}
	
	public void deleteOrder(Orders order) {
		orderRepo.delete(order);
	}

}
