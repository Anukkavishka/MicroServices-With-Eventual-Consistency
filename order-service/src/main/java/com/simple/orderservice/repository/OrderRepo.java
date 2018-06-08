package com.simple.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simple.orderservice.model.Orders;

@Repository
public interface OrderRepo extends JpaRepository<Orders,String> {

}
