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

@Service
@Transactional
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
	@Autowired
	private PasswordResetTokenDAO tokenDAO;
	@Autowired
	private UserService userService;
	
	private static final int TOKEN_BYTE_SIZE = 32;
	// 產生安全 token 並存 hash
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
    @Override
    public PasswordResetToken getByToken(String token) {
    	String tokenHash = sha256(token);
    	return tokenDAO.findByToken(tokenHash);
    }
    
    @Override
    public boolean validateToken(String token) {
        String tokenHash = sha256(token);
        PasswordResetToken resetToken = tokenDAO.findByToken(tokenHash);
        return resetToken != null && resetToken.getExpiryDate().isAfter(LocalDateTime.now());
    }

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
