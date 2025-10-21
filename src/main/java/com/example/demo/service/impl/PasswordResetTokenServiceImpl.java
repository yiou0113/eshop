package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

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
	// 生成 token
    public PasswordResetToken createTokenForEmail(String email) {
        User user = userService.getUserByEmail(email);
        if (user == null) return null;

        String tokenValue = UUID.randomUUID().toString();
        PasswordResetToken token = new PasswordResetToken();  // 使用無參數建構子
        token.setToken(tokenValue);
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        tokenDAO.save(token);
        return token;
    }
}
