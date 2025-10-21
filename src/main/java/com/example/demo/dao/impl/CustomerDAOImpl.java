package com.example.demo.dao.impl;

import org.hibernate.Session;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.dao.CustomerDAO;
import com.example.demo.model.Customer;

/**
 * CustomerDAO 的實作類別
 *
 * 此類別負責與資料庫互動，透過 Hibernate Session 操作顧客（Customer）資料。
 *
 * 功能包含：
 * - 儲存或更新顧客
 * - 根據 ID 查詢顧客
 * - 根據 User ID 查詢對應顧客
 */

@Repository
public class CustomerDAOImpl implements CustomerDAO {
	/** Logger，用於記錄操作資訊與錯誤訊息 */
    private static final Logger logger = LoggerFactory.getLogger(CustomerDAOImpl.class);
    
    /** Hibernate SessionFactory，用於取得資料庫 Session */
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
     * 取得目前 Hibernate Session
     *
     * @return 當前的 Session 物件
     */
	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	/**
     * 儲存或更新顧客資料
     *
     * @param customer 要儲存或更新的顧客物件
     */
	@Override
	public void save(Customer customer) {
		getCurrentSession().saveOrUpdate(customer);
	}
	 /**
     * 根據顧客 ID 查詢顧客
     *
     * @param id 顧客唯一識別碼
     * @return Customer 物件，若查無資料則回傳 null
     */
	@Override
	public Customer findById(Long id) {
		return getCurrentSession().get(Customer.class, id);
	}
	 /**
     * 根據 User ID 查詢對應顧客
     *
     * 使用 HQL 查詢 Customer 表中關聯的 User ID。
     * 若查詢失敗或找不到資料，會記錄錯誤並回傳 null。
     *
     * @param userId 使用者的唯一識別碼
     * @return Customer 物件，若找不到或發生例外則回傳 null
     */
	@Override
	public Customer findByUserId(Long userId) {
		try {
			return getCurrentSession()
					.createQuery("FROM Customer c WHERE c.user.id = :userId", Customer.class)
					.setParameter("userId", userId)
					.uniqueResult(); // 找不到會回傳 null
		} catch (Exception e) {
			logger.error("查詢顧客時發生錯誤，User ID：{}，錯誤訊息：{}", userId, e.getMessage(), e);
			return null; // 發生例外也回傳 null
		}
	}

}
