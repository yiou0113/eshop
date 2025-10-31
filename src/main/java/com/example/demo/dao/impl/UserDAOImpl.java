package com.example.demo.dao.impl;

import com.example.demo.dao.UserDAO;
import com.example.demo.model.User;
import org.springframework.stereotype.Repository;


/**
 * UserDAO 的實作類別
 *
 * <p>此類別繼承 {@link BaseDAOImpl} 並實作 {@link UserDAO} 介面，負責與資料庫進行直接互動。</p>
 * <p>透過 Hibernate Session 提供使用者（{@link User}）的 CRUD 操作：</p>
 * <ul>
 *   <li>查詢所有使用者</li>
 *   <li>依 ID 查詢單一使用者</li>
 *   <li>儲存或更新使用者資料</li>
 *   <li>刪除使用者</li>
 *   <li>依 Email 查詢使用者</li>
 * </ul>
 */
@Repository
public class UserDAOImpl extends BaseDAOImpl<User> implements UserDAO {
	 /**
     * 根據電子郵件查詢使用者。
     *
     * <p>此方法使用 Hibernate HQL 查詢，並透過參數綁定避免 SQL 注入問題。</p>
     *
     * @param email 使用者的電子郵件地址
     * @return 若找到對應使用者則回傳 {@link User} 物件；若無則回傳 {@code null}
     */
	@Override
	public User findByEmail(String email) {
		return getCurrentSession().createQuery("FROM User WHERE email = :email", User.class)
				.setParameter("email", email).uniqueResult();
	}
}
