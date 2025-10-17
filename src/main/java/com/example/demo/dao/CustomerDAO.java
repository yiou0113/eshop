package com.example.demo.dao;

import com.example.demo.model.Customer;

public interface CustomerDAO {
	void save(Customer customer);
	
	Customer findById(Long id);
	
    Customer findByUserId(Long userId);

}
