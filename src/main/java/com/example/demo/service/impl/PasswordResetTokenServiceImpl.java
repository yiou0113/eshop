package com.example.demo.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.PasswordResetTokenDAO;
import com.example.demo.model.PasswordResetToken;
import com.example.demo.model.User;
import com.example.demo.service.PasswordResetTokenService;
import com.example.demo.service.UserService;

/**
 * PasswordResetTokenService 的實作類別
 * 
 * 此類別負責處理與忘記密碼 (PasswordResetToken) 相關的業務邏輯，並透過PasswordResetTokenDAO 與資料庫溝通
 * 
 * 功能包含：
 * 	- 創建重設密碼token
 * 	- 根據token查詢對應使用者
 * 	- 驗證token
 * 	- 刪除過期token
 */
@Service
@Transactional
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
	@Autowired
	private PasswordResetTokenDAO tokenDAO;
	@Autowired
	private UserService userService;
	
	private static final int TOKEN_BYTE_SIZE = 32;
	/**
	 * 透過Email產生專屬token以重設密碼
	 * 
	 * @param	email	使用者輸入email
	 * @return	回傳建出來的token
	 */
    @Override
    public String createTokenForEmail(String email) {
        User user = userService.getUserByEmail(email);
        if (user == null) return null;

        // 刪除舊 token
        tokenDAO.deleteByUserId(user.getId());

        // 生成隨機 token
        byte[] randomBytes = new byte[TOKEN_BYTE_SIZE];
        new SecureRandom().nextBytes(randomBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        // Hash token
        String tokenHash = sha256(token);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(tokenHash); // 存 hash
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));

        tokenDAO.save(resetToken);
        return token; // 返回原 token，用於 email
    }
    /**
     * 透過token找到對應使用者
     * 
     * @param	token	透過token找出對應使用者
     * @return	回傳物件回去
     */
    @Override
    public PasswordResetToken getByToken(String token) {
    	String tokenHash = sha256(token);
    	return tokenDAO.findByToken(tokenHash);
    }
    /**
     * 判斷token是否仍在使用期限內
     * 
     * @param	token	透過token找出對應使用者
     * @return	true或false
     */
    @Override
    public boolean validateToken(String token) {
        String tokenHash = sha256(token);
        PasswordResetToken resetToken = tokenDAO.findByToken(tokenHash);
        return resetToken != null && resetToken.getExpiryDate().isAfter(LocalDateTime.now());
    }
    
    /**
     * 移除過期token
     * 
     */
    @Override
    public void removeExpiredTokens() {
        tokenDAO.deleteByExpiryDateBefore(LocalDateTime.now());
    }

    // SHA-256 hash
    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Hash token failed", e);
        }
    }
}
