package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Order;

public interface OrderService {
	Order createOrder(Long userId);
	
	void saveOrder(Order order);

    Order getOrderById(Long id);

    List<Order> getAllOrders();

    List<Order> getOrderByCustomerId(Long customerId);
}
