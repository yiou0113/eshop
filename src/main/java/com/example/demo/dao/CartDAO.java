package com.example.demo.dao;

import java.util.Optional;

import com.example.demo.model.Cart;
/**
 * CartDAO 介面 — 定義購物車（Cart）資料存取操作。
 *
 * 此介面負責與資料庫互動，提供購物車的基本資料操作：
 * - 根據顧客 ID 查詢購物車
 * - 儲存或更新購物車
 * - 刪除整個購物車
 * - 刪除購物車中指定商品
 */
public interface CartDAO {
	Optional<Cart> findByCustomerId(Long custId);

    void save(Cart cart);

    void delete(Cart cart);
    
    void deleteByCartIdAndProductId(Long cartId, Long productId);
}
