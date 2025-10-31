package com.example.demo.dao;

import com.example.demo.model.PasswordResetToken;

/**
 * PasswordResetTokenDAO 介面
 *
 * <p>此介面繼承 {@link BaseDAO}，負責定義與「密碼重設（PasswordResetToken）」相關的
 * 資料存取操作（Data Access Operations）。</p>
 *
 * <p>主要功能包含：</p>
 * <ul>
 *   <li>依 Token 字串查詢重設權杖</li>
 *   <li>根據使用者 ID 刪除對應的 Token</li>
 *   <li>刪除已過期的 Token（依權杖到期時間）</li>
 * </ul>
 *
 */
public interface PasswordResetTokenDAO extends BaseDAO<PasswordResetToken> {
	PasswordResetToken findByToken(String token);
	
	void deleteByUserId(Long id);
	
	
	void deleteByExpiryDateBefore(java.time.LocalDateTime now);
}
