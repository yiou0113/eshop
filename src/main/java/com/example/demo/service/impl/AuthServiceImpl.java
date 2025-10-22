package com.example.demo.service.impl;

import com.example.demo.model.User;
import com.example.demo.service.AuthService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
/**
 * AuthService 的實作類別
 *
 * 此類別負責處理使用者認證相關業務邏輯，
 * 包含登入功能。
 */
@Service
public class AuthServiceImpl implements AuthService {
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;// 用於密碼加密的工具類別
	
	/**
     * 使用電子郵件與密碼進行登入
     *
     * 1. 根據電子郵件查詢使用者
     * 2. 使用 PasswordEncoder 比對輸入密碼與資料庫密碼
     * 3. 若比對成功回傳 User 物件，否則回傳 null
     *
     * @param email 使用者電子郵件
     * @param Password 使用者輸入密碼（明文）
     * @return User 物件，若認證成功回傳；若失敗回傳 null
     */
	@Override
	public User login(String email, String Password) {
		User user = userService.getUserByEmail(email);
		if (user != null && passwordEncoder.matches(Password, user.getPassword())) {
			return user; // 密碼正確，登入成功
		}
		return null; // 登入失敗
	}
}
