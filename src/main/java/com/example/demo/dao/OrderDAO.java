package com.example.demo.dao;

import java.util.List;

import com.example.demo.model.Order;

public interface OrderDAO {
	void save(Order order);
	
	Order findById(Long id);
	
	List<Order> findAll();
}
