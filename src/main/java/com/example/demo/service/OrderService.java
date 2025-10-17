package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Order;

public interface OrderService {
	Order createOrder(Long userId);
	
	void saveOrder(Order order);

    Order findById(Long id);

    List<Order> getAllOrders();

    List<Order> findByCustomerId(Long customerId);
}
