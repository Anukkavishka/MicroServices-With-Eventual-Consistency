package com.simple.stockservice.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simple.stockservice.model.Stock;
import com.simple.stockservice.repository.StockRepo;

@Transactional
@Service
public class StockService {
	
	@Autowired
	private StockRepo stockRepo;
	
	
	public StockService() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public Optional<Stock> getStockById(int stockId) {
		Optional<Stock> obj = stockRepo.findById(stockId);
		//Stock obj = stockRepo.getOne(stockId);
		return obj;
	}	
	
	public List<Stock> getAllStocks(){
		return stockRepo.findAll();
	}

	public void addStock(Stock stock){
		stockRepo.save(stock);
    	        
	}
	
	public void updateStock(Stock stock) {
		stockRepo.save(stock);
	}
	
	public void deleteStock(Stock stock) {
		stockRepo.delete(stock);
	}

	
	

}
