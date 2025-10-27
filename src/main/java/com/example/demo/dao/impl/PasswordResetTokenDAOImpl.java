package com.example.demo.dao.impl;

import java.time.LocalDateTime;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.example.demo.dao.PasswordResetTokenDAO;
import com.example.demo.model.PasswordResetToken;

@Repository
public class PasswordResetTokenDAOImpl extends BaseDAOImpl<PasswordResetToken> implements PasswordResetTokenDAO {

	@Override
	public PasswordResetToken findByToken(String tokenHash) {
	    try {
	        return getCurrentSession()
	                .createQuery("FROM PasswordResetToken t WHERE t.token = :tokenHash", PasswordResetToken.class)
	                .setParameter("tokenHash", tokenHash)
	                .getSingleResult();
	    } catch (NoResultException e) {
	        return null;
	    }
	}

	@Override
	public void  deleteByUserId(Long id) {
		 getCurrentSession()
	        .createQuery("DELETE FROM PasswordResetToken t WHERE t.user.id = :userId")
	        .setParameter("userId", id)
	        .executeUpdate();
	}

	@Override
	public void deleteByExpiryDateBefore(LocalDateTime now) {
		getCurrentSession().createQuery("DELETE FROM PasswordResetToken t WHERE t.expiryDate < :now")
				.setParameter("now", now).executeUpdate();
	}

}