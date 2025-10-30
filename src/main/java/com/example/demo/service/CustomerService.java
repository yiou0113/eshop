package com.example.demo.service;

import com.example.demo.model.Customer;
import com.example.demo.model.User;
/**
 * CustomerService 介面 — 定義顧客（Customer）相關的業務邏輯操作。
 *
 * 此介面負責處理顧客的註冊、查詢與使用者帳號的關聯等功能。
 */
public interface CustomerService {
    void registerCustomer(String name, String email, String password, String phone, String address);
    
    Customer getCustomerByUserId(Long userId);
    
    void updateCustomerPassword(Long id, String newPassword);
    
    void updateCustomerInfo(Long id, Customer updatedCustomer);
    
    boolean checkPassword(Long id,String oldPassword);
}
