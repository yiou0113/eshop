package com.example.demo.service.impl;

import com.example.demo.dao.CustomerDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.model.Customer;
import com.example.demo.model.User;
import com.example.demo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CustomerService 的實作類別
 *
 * 此類別負責顧客相關的業務邏輯，例如： 
 * - 註冊新顧客 
 * - 根據使用者 ID 查詢顧客資訊
 *
 * 使用 @Transactional 確保資料庫操作具有事務一致性。
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
	/** 注入 UserDAO，用於操作使用者資料 */
	@Autowired
	private UserDAO userDAO;

	/** 注入 CustomerDAO，用於操作顧客資料 */
	@Autowired
	private CustomerDAO customerDAO;

	/** 注入 PasswordEncoder，用於密碼加密 */
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * 註冊新顧客
	 *
	 * 此方法會： 1. 將使用者密碼加密 2. 建立對應的 User 物件 3. 建立對應的 Customer 物件 4. 建立 User 與 Customer
	 * 的關聯 5. 儲存 User（會自動保存關聯的 Customer）
	 *
	 * @param name     顧客姓名
	 * @param email    顧客電子郵件
	 * @param password 顧客密碼（明文，會自動加密）
	 * @param phone    顧客電話
	 * @param address  顧客地址
	 */
	@Override
	public void registerCustomer(String name, String email, String password, String phone, String address) {
		String encodedPassword = passwordEncoder.encode(password);

		// 建立 User
		User user = new User();
		user.setEmail(email);
		user.setPassword(encodedPassword);
		// user.setRole("ROLE_USER");

		// 建立 Customer
		Customer customer = new Customer();
		customer.setName(name);
		customer.setPhone(phone);
		customer.setAddress(address);
		customer.setUser(user);
		// 設定雙向關聯
		user.setCustomer(customer);

		// 儲存（會自動保存關聯對象）
		userDAO.save(user);
	}

	public Customer getCustomerByUserId(Long userId) {
		return customerDAO.findByUserId(userId);
	}
}
