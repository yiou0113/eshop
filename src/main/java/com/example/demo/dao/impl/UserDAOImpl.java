package com.example.demo.dao.impl;

import com.example.demo.dao.UserDAO;
import com.example.demo.model.User;
import org.springframework.stereotype.Repository;


/**
 * UserDAO 的實作類別
 *
 * 此類別負責與資料庫進行直接互動， 透過 Hibernate 的 Session 進行 CRUD 操作（建立、查詢、更新、刪除）。
 *
 * 提供以下功能：
 *  - 查詢所有使用者
 *  - 依 ID 查詢單一使用者
 *  - 儲存或更新使用者
 *  - 刪除使用者
 *  - 依 Email 查詢使用者
 */
@Repository
public class UserDAOImpl extends BaseDAOImpl<User> implements UserDAO {

	@Override
	public User findByEmail(String email) {
		return getCurrentSession().createQuery("FROM User WHERE email = :email", User.class)
				.setParameter("email", email).uniqueResult();
	}
}
