package com.example.demo.dao;

import com.example.demo.model.Customer;
/**
 * CustomerDAO 介面 — 定義顧客（Customer）資料存取操作（Data Access Object）。
 *
 * 此介面負責與資料庫互動，提供顧客的儲存與查詢操作及依使用者查詢顧客的功能。
 */
public interface CustomerDAO extends BaseDAO<Customer> {
    Customer findByUserId(Long userId);

}
