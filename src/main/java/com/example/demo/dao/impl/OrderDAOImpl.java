package com.example.demo.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.example.demo.dao.OrderDAO;
import com.example.demo.model.Order;

/**
 * OrderDAO 的實作類別
 *
 * <p>此類別繼承 {@link BaseDAOImpl} 並實作 {@link OrderDAO}，
 * 負責與資料庫互動，提供訂單（{@link Order}）的 CRUD 與特定查詢操作。</p>
 *
 * <p>主要功能：</p>
 * <ul>
 *   <li>儲存或更新訂單</li>
 *   <li>依 ID 查詢單一訂單</li>
 *   <li>查詢所有訂單</li>
 *   <li>依顧客 ID 查詢該顧客的所有訂單</li>
 * </ul>
 *
 */
@Repository
public class OrderDAOImpl extends BaseDAOImpl<Order> implements OrderDAO {
	/** Logger，用於記錄執行過程與錯誤訊息 */
    private static final Logger logger = LoggerFactory.getLogger(OrderDAOImpl.class);

    /**
     * 根據顧客 ID 查詢該顧客的所有訂單。
     *
     * <p>此方法使用 HQL（Hibernate Query Language）查詢 {@code Order} 表中
     * 關聯到指定 {@code Customer} 的所有訂單資料。</p>
     *
     * <p>若查詢過程中發生例外，會回傳一個空的列表以避免系統中斷。</p>
     *
     * @param customerId 顧客的唯一識別碼
     * @return {@link List} 包含該顧客的訂單；若查無資料或發生錯誤則回傳空集合
     */
	@Override
	public List<Order> findByCustomerId(Long customerId) {
		try {
			List<Order> orders = getCurrentSession()
					.createQuery("FROM Order o WHERE o.customer.id = :customerId", Order.class)
					.setParameter("customerId", customerId).getResultList();
			logger.info(" 用顧客ID: {},查詢到共 {} 筆",  customerId,orders.size());
			return orders;
		} catch (Exception e) {
			logger.error("查詢顧客ID {} 時出現錯誤: {}", customerId, e.getMessage(), e);
			return new ArrayList<>(); // 若出現錯誤，回傳空列表避免系統中斷
		}
	}

}
