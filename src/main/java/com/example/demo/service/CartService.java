package com.example.demo.service;

import java.math.BigDecimal;

import com.example.demo.model.Cart;
/**
 * CartService 介面 — 定義購物車（Cart）相關的業務邏輯操作。
 *
 * 此介面負責管理顧客購物車，包括：
 * <ul>
 *     <li>新增商品到購物車</li>
 *     <li>移除商品或更新商品數量</li>
 *     <li>計算購物車總金額</li>
 *     <li>建立、查詢與清空購物車</li>
 * </ul>
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
}
