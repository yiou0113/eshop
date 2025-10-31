package com.example.demo.dao;

import java.util.Optional;

import com.example.demo.model.Cart;
/**
 * CartDAO 介面
 *
 * <p>此介面繼承 {@link BaseDAO}，負責定義購物車（{@link Cart}）相關的資料存取操作。</p>
 *
 * <p>主要功能包含：</p>
 * <ul>
 *   <li>基本的購物車 CRUD 操作（建立、查詢、更新、刪除）</li>
 *   <li>根據顧客 ID 查詢購物車內容</li>
 *   <li>刪除整個購物車</li>
 *   <li>刪除購物車中特定商品</li>
 * </ul>
 *
 */
public interface CartDAO extends BaseDAO<Cart> {
	Optional<Cart> findByCustomerId(Long custId);
  
    void deleteByCartIdAndProductId(Long cartId, Long productId);
}
