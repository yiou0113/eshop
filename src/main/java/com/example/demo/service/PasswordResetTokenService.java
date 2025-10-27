package com.example.demo.service;

import com.example.demo.model.PasswordResetToken;

public interface PasswordResetTokenService {
	String createTokenForEmail(String email);
	
	PasswordResetToken getByToken(String token);
	
	boolean validateToken(String token);

	void removeExpiredTokens();

}
