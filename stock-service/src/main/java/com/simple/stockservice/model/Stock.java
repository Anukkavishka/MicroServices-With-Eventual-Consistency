package com.simple.stockservice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Stock {
	
	@Id
	private int stockId;
	private String proName;
	private int qty;
	
	
	
	public Stock() {
		super();
		// TODO Auto-generated constructor stub
	}



	public Stock(int stockId, String proName, int qty) {
		super();
		this.stockId = stockId;
		this.proName = proName;
		this.qty = qty;
	}



	public int getStockId() {
		return stockId;
	}



	public void setStockId(int stockId) {
		this.stockId = stockId;
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
	
	
	

}
