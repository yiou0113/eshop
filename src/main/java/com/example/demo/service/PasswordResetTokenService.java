package com.example.demo.service;

import com.example.demo.model.PasswordResetToken;

public interface PasswordResetTokenService {
	PasswordResetToken createTokenForEmail(String email);	
	
}
