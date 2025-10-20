package com.example.demo.dao.impl;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.demo.dao.PasswordResetTokenDAO;
import com.example.demo.model.PasswordResetToken;

@Repository
public class PasswordResetTokenDAOImpl implements PasswordResetTokenDAO {

	@Autowired
	private SessionFactory sessionFactory;

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public PasswordResetToken findByToken(String token) {
		try {
			return getCurrentSession()
					.createQuery("FROM PasswordResetToken t WHERE t.token = :token", PasswordResetToken.class)
					.setParameter("token", token).getSingleResult();
		} catch (NoResultException e) {
			return null; // 找不到就回傳 null
		}
	}

	@Override
	public void save(PasswordResetToken token) {
		if (token.getId() == null) {
			getCurrentSession().persist(token); // 新增
		} else {
			getCurrentSession().merge(token); // 更新
		}
	}

	@Override
	public void delete(PasswordResetToken token) {
		Session session = getCurrentSession();
		PasswordResetToken managedToken;

		// 判斷 token 是否已在 session
		if (session.contains(token)) {
			managedToken = token;
		} else {
			managedToken = (PasswordResetToken) session.merge(token);
		}

		// 然後刪除
		session.remove(managedToken);

	}
}