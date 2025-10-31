package com.example.demo.dao;

import java.util.List;

import com.example.demo.model.Order;
/**
 * OrderDAO 介面
 *
 * <p>此介面繼承 {@link BaseDAO}，負責定義與訂單（{@link Order}）相關的資料存取操作。</p>
 *
 * <p>主要功能包含：</p>
 * <ul>
 *   <li>基本的訂單 CRUD 操作（建立、查詢、更新、刪除）</li>
 *   <li>根據顧客 ID 查詢該顧客的所有訂單紀錄</li>
 * </ul>
 *
 * <p>此介面通常由 {@code OrderService} 呼叫，用於：
 * <ul>
 *   <li>從購物車建立訂單</li>
 *   <li>顯示顧客歷史訂單列表</li>
 *   <li>查詢單筆訂單詳細資訊</li>
 * </ul>
 * </p>
 */
public interface OrderDAO extends BaseDAO<Order> {
	List<Order> findByCustomerId(Long customerId);
	
}
