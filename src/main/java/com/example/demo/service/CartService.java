package com.example.demo.service;

import java.math.BigDecimal;

import com.example.demo.model.Cart;

public interface CartService {

    void addToCart(Long userId, Long productId, int quantity);

    BigDecimal getCartTotal(Long userId);

    void removeItem(Long customerId, Long productId);

    void updateQuantity(Long customerId, Long productId, int quantity);
    
    Cart createCartForCustomer(Long customerId);
    
    void clearCart(Long customerId);
    
    Cart getCartByCustomerId(Long customerId);

}
