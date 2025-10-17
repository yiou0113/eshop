package com.example.demo.dao;

import java.util.Optional;

import com.example.demo.model.Cart;

public interface CartDAO {
	Optional<Cart> findByCustomerId(Long custId);

    void save(Cart cart);

    void delete(Cart cart);
    
}
