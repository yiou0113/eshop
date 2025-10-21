package com.example.demo.dao.impl;

import com.example.demo.dao.UserDAO;
import com.example.demo.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

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
public class UserDAOImpl implements UserDAO {
	/** Hibernate 的 SessionFactory，用來建立與資料庫的連線 Session */
	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * 取得當前的 Hibernate Session。
	 *
	 * @return 目前作用中的 Session 物件，用於資料庫操作。
	 */
	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 查詢所有使用者資料。
	 *
	 * @return List<User> 所有使用者的列表。
	 */
	@Override
	public List<User> findAll() {
		return getCurrentSession().createQuery("FROM User", User.class).list();
	}

	/**
	 * 根據使用者 ID 查詢單一使用者。
	 *
	 * @param id 使用者的唯一識別碼。
	 * @return User 物件，若查無資料則回傳 null。
	 */
	@Override
	public User findById(Long id) {
		return getCurrentSession().get(User.class, id);
	}

	/**
	 * 儲存或更新使用者資料。
	 *
	 * 若資料庫中已有該使用者，則執行更新；若無，則新增。
	 *
	 * @param user 要儲存的使用者物件。
	 */
	@Override
	public void save(User user) {
		getCurrentSession().saveOrUpdate(user);
	}

	/**
	 * 根據使用者 ID 刪除該使用者。
	 *
	 * 在刪除前會先確認該使用者是否存在。
	 *
	 * @param id 要刪除的使用者 ID。
	 */
	@Override
	public void delete(Long id) {
		User user = getCurrentSession().get(User.class, id);
		if (user != null) {
			getCurrentSession().delete(user);
		}
	}

	/**
	 * 根據 Email 查詢使用者。
	 *
	 * 使用 HQL（Hibernate Query Language）查詢特定 email 的使用者。
	 *
	 * @param email 使用者的電子郵件地址。
	 * @return User 物件，若找不到則回傳 null。
	 */
	@Override
	public User findByEmail(String email) {
		return getCurrentSession().createQuery("FROM User WHERE email = :email", User.class)
				.setParameter("email", email).uniqueResult();
	}
}
