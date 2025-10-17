package com.example.demo.service;

import com.example.demo.model.Customer;

public interface CustomerService {
    void registerCustomer(String name, String email, String password, String phone, String address);
    
    Customer getCustomerByUserId(Long userId);
}
