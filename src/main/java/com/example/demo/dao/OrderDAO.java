package com.example.demo.dao;

import java.util.List;

import com.example.demo.model.Order;
/**
 * OrderDAO 介面 — 定義訂單（Order）資料存取操作（Data Access Object）。
 *
 * 此介面負責與資料庫互動，提供訂單的儲存與查詢操作及依顧客查詢訂單的功能。
 */
public interface OrderDAO extends BaseDAO<Order> {
	List<Order> findByCustomerId(Long customerId);
	
}
