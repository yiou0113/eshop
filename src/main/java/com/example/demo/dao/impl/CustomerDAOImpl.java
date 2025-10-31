package com.example.demo.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.example.demo.dao.CustomerDAO;
import com.example.demo.model.Customer;

/**
 * CustomerDAO 的實作類別
 *
 * <p>此類別繼承 {@link BaseDAOImpl} 並實作 {@link CustomerDAO}，
 * 負責與資料庫互動，提供顧客（{@link Customer}）資料的 CRUD 與特定查詢操作。</p>
 *
 * <p>主要功能：</p>
 * <ul>
 *   <li>儲存或更新顧客資料</li>
 *   <li>依 ID 查詢顧客</li>
 *   <li>依使用者 ID 查詢對應的顧客</li>
 * </ul>
 *
 */
@Repository
public class CustomerDAOImpl extends BaseDAOImpl<Customer> implements CustomerDAO {
	private static final Logger logger = LoggerFactory.getLogger(CustomerDAOImpl.class);

	/**
     * 根據使用者 ID 查詢對應顧客。
     *
     * <p>此方法使用 HQL 查詢 Customer 表中關聯的 User ID。若查詢失敗或找不到資料，
     * 會記錄錯誤並回傳 {@code null}。</p>
     *
     * @param userId 使用者的唯一識別碼
     * @return {@link Customer} 物件；若找不到或發生例外則回傳 {@code null}
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
