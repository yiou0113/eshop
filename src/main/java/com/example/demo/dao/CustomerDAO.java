package com.example.demo.dao;

import com.example.demo.model.Customer;
/**
 * CustomerDAO 介面
 *
 * <p>此介面繼承 {@link BaseDAO}，負責定義顧客（{@link Customer}）相關的資料存取操作。</p>
 *
 * <p>主要功能包含：</p>
 * <ul>
 *   <li>基本的顧客 CRUD 操作（建立、查詢、更新、刪除）</li>
 *   <li>根據使用者 ID 查詢對應顧客資料</li>
 * </ul>
 */
public interface CustomerDAO extends BaseDAO<Customer> {
    Customer findByUserId(Long userId);

}
