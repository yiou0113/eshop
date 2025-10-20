package com.example.demo.dao;

import com.example.demo.model.PasswordResetToken;

public interface PasswordResetTokenDAO {
	PasswordResetToken findByToken(String token);
	
	void save(PasswordResetToken token);
	
	void delete(PasswordResetToken token);
}
