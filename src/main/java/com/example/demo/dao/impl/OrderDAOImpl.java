package com.example.demo.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.dao.OrderDAO;
import com.example.demo.model.Order;

/**
 * OrderDAO 的實作類別
 *
 * 此類別負責與資料庫進行互動，透過 Hibernate Session 操作訂單（Order）的 CRUD 功能與查詢功能。
 *
 * 主要功能包含： 
 * - 儲存或更新訂單
 * - 根據 ID 查詢單一訂單 
 * - 查詢所有訂單 
 * - 根據顧客 ID 查詢該顧客的所有訂單
 */
@Repository
public class OrderDAOImpl implements OrderDAO {
	/** Logger，用於記錄執行過程與錯誤訊息 */
    private static final Logger logger = LoggerFactory.getLogger(OrderDAOImpl.class);
    
	/** Hibernate 的 SessionFactory，用於建立與資料庫互動的 Session。 */
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * 取得目前的 Hibernate Session。
	 *
	 * @return 目前作用中的 Session，用於進行資料庫操作。
	 */
	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 儲存或更新訂單資料。
	 *
	 * 若該訂單在資料庫中已存在，則執行更新；否則新增新紀錄。
	 *
	 * @param order 要儲存或更新的訂單物件。
	 */
	public void save(Order order) {
		getSession().saveOrUpdate(order);
		logger.info("儲存或更新訂單 ID: {}", order.getId());
	}

	/**
	 * 根據訂單 ID 查詢單一訂單。
	 *
	 * @param id 訂單的唯一識別碼。
	 * @return Order 物件，若查無資料則回傳 null。
	 */
	public Order findById(Long id) {
		return getSession().get(Order.class, id);
	}

	/**
	 * 查詢所有訂單資料。
	 *
	 * @return List<Order> 所有訂單的列表。
	 */
	public List<Order> findAll() {
		return getSession().createQuery("From Order", Order.class).list();
	}

	/**
	 * 根據顧客 ID 查詢該顧客的所有訂單。
	 *
	 * 使用 HQL（Hibernate Query Language）查詢 `Order` 表中 關聯到指定 `Customer` 的所有訂單資料。
	 *
	 * 若查詢過程中發生例外，會回傳一個空的列表。
	 *
	 * @param customerId 顧客的唯一識別碼。
	 * @return List<Order> 該顧客的訂單列表，若查無資料或發生錯誤則回傳空集合。
	 */
	@Override
	public List<Order> findByCustomerId(Long customerId) {
		try {
			List<Order> orders = getSession()
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
