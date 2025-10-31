package com.example.demo.service;

import java.math.BigDecimal;

import com.example.demo.model.Cart;
/**
 * CartService 介面 — 定義購物車（Cart）相關的業務邏輯操作。
 *
 * 提供客戶購物車新建與將商品加入購物車及判斷庫存是否足夠功能
 */
public interface CartService {

    void addToCart(Long userId, Long productId, int quantity);

    BigDecimal getCartTotal(Long userId);

    void removeItem(Long customerId, Long productId);

    void updateQuantity(Long customerId, Long productId, int quantity);
    
    Cart createCartForCustomer(Long customerId);
    
    void clearCart(Long customerId);
    
    Cart getCartByCustomerId(Long customerId);
    
    boolean isCartEmpty(Long customerId);
    
    boolean addToCart(Long productId, int quantity);
}
