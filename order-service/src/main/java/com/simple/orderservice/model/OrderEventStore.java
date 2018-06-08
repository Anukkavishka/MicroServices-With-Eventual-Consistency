package com.simple.orderservice.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OrderEventStore {
	
	@Id
	private String orderId;
	private String proName;
	private int qty;
	private String orderStatus;
	private String orderCmdStatus;
	
	
	
	
	public OrderEventStore(String orderId, String proName, int qty, String orderStatus, String orderCmdStatus) {
		super();
		this.orderId = orderId;
		this.proName = proName;
		this.qty = qty;
		this.orderStatus = orderStatus;
		this.orderCmdStatus = orderCmdStatus;
	}
	public OrderEventStore() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderCmdStatus() {
		return orderCmdStatus;
	}
	public void setOrderCmdStatus(String orderCmdStatus) {
		this.orderCmdStatus = orderCmdStatus;
	}

	
	
	
	

}
