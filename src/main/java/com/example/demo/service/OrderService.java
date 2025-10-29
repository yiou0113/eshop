package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Order;
/**
 * OrderService 介面 — 定義訂單（Order）相關的業務邏輯操作。
 *
 * 提供訂單的基本功能與與額外功能，例如依照客戶id查詢訂單。
 */
public interface OrderService {
	Order createOrder(Long userId);
	
	void saveOrder(Order order);

    Order getOrderById(Long id);

    List<Order> getAllOrders();

    List<Order> getOrderByCustomerId(Long customerId);
    
    void cancelOrder(Long orderId);
    
    void payOrder(Long orderId);
}
