package com.simple.shippingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simple.shippingservice.model.Stock;

public interface StockRepo extends JpaRepository<Stock,String> {

}
