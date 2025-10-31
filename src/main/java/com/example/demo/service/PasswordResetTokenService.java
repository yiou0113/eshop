package com.example.demo.service;

import com.example.demo.model.PasswordResetToken;

/**
 * PasswordResetTokenService 介面 — 定義忘記密碼（PasswordResetToken）相關的業務邏輯操作。
 * 
 * 提供使用者忘記密碼時透過Eamil進行重設、及驗證的相關功能
 */
public interface PasswordResetTokenService {
	String createTokenForEmail(String email);
	
	PasswordResetToken getByToken(String token);
	
	boolean validateToken(String token);

	void removeExpiredTokens();

}
