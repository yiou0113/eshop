package com.example.demo.service;

import java.math.BigDecimal;

import com.example.demo.model.Cart;

public interface CartService {

    void addToCart(Long userId, Long productId, int quantity);

    BigDecimal getCartTotal(Long userId);

    void removeItem(Long userId, Long productId);

    void updateQuantity(Long userId, Long productId, int quantity);
    
    Cart createCartForUser(Long userId);
    
    void clearCart(Long userId);
    
    Cart getCartByUserId(Long userId);

}
