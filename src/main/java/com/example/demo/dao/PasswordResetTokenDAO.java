package com.example.demo.dao;

import com.example.demo.model.PasswordResetToken;

public interface PasswordResetTokenDAO extends BaseDAO<PasswordResetToken> {
	PasswordResetToken findByToken(String token);
	
	void deleteByUserId(Long id);
	
	
	void deleteByExpiryDateBefore(java.time.LocalDateTime now);
}
