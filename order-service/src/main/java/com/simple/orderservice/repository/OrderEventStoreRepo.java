package com.simple.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simple.orderservice.model.OrderEventStore;

@Repository
public interface OrderEventStoreRepo extends JpaRepository<OrderEventStore,String> {

}
