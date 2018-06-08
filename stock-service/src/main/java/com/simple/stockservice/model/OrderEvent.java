package com.simple.stockservice.model;

public class OrderEvent {
	
	private Orders order;
	private OrderEventType status;
	private OrderCMDStatus cmdStatus;
	
	public OrderCMDStatus getCmdStatus() {
		return cmdStatus;
	}
	public void setCmdStatus(OrderCMDStatus cmdStatus) {
		this.cmdStatus = cmdStatus;
	}
	public Orders getOrder() {
		return order;
	}
	public void setOrder(Orders order) {
		this.order = order;
	}
	public OrderEventType getStatus() {
		return status;
	}
	public void setStatus(OrderEventType status) {
		this.status = status;
	}
	public OrderEvent() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OrderEvent(Orders order, OrderEventType status,OrderCMDStatus cmdStatus) {
		super();
		this.order = order;
		this.status = status;
		this.cmdStatus=cmdStatus;
	}
	
	
	

}
