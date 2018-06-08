package com.simple.stockservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simple.stockservice.model.Stock;

@Repository
public interface StockRepo extends JpaRepository<Stock,String> {

}
