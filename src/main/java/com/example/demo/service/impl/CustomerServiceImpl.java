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
	@Autowired
	private UserDAO userDAO;

	@Autowired
	private CustomerDAO customerDAO;

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

	    // --- 基本驗證 ---
	    if (name == null || name.isBlank()) throw new IllegalArgumentException("姓名不可為空");
	    if (name.length() > 150) throw new IllegalArgumentException("姓名過長");

	    if (email == null || email.isBlank()) throw new IllegalArgumentException("Email 不可為空");
	    if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) throw new IllegalArgumentException("Email 格式不正確");
	    
	    // --- 唯一性檢查 ---
	    if (userDAO.findByEmail(email) != null) {
	        throw new IllegalArgumentException("Email 已存在");
	    }

	    if (password == null || password.isBlank()) throw new IllegalArgumentException("密碼不可為空");
	    if (password.length() < 6) throw new IllegalArgumentException("密碼至少 6 個字元");

	    if (phone != null && phone.length() > 20) throw new IllegalArgumentException("電話過長");
	    if (address != null && address.length() > 255) throw new IllegalArgumentException("地址過長");

	    // --- 密碼加密 ---
	    String encodedPassword = passwordEncoder.encode(password);

	    // --- 建立 User ---
	    User user = new User();
	    user.setEmail(email);
	    user.setPassword(encodedPassword);
	    // user.setRole("ROLE_USER");

	    // --- 建立 Customer ---
	    Customer customer = new Customer();
	    customer.setName(name);
	    customer.setPhone(phone);
	    customer.setAddress(address);
	    customer.setUser(user);

	    // 設定雙向關聯
	    user.setCustomer(customer);

	    // --- 儲存 ---
	    userDAO.save(user);
	}

	/**
     * 根據使用者 ID 查詢對應的顧客資料
     *
     * @param userId 使用者唯一識別碼
     * @return 對應的 {@link Customer} 物件；若找不到則回傳 {@code null}
     */
	public Customer getCustomerByUserId(Long userId) {
		return customerDAO.findByUserId(userId);
	}
	
	@Override
	public void updateCustomerPassword(Long id, User updatedUser,String oldPassword) {
		User existingUser = userDAO.findById(id);
		if (existingUser == null) return;
		
		if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
	        if (passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
	            String encodedPassword = passwordEncoder.encode(updatedUser.getPassword());
	            existingUser.setPassword(encodedPassword);
	        } else {
	            throw new RuntimeException("舊密碼不正確");
	        }
	    }
			// 儲存更新後的資料	
			userDAO.save(existingUser);		
	}
	@Override
	public void updateCustomerInfo(Long id, Customer updatedCustomer) {
		User existingUser = userDAO.findById(id);
		if (existingUser == null) throw new RuntimeException("使用者不存在");		
		Customer existingCustomer = customerDAO.findByUserId(id);
		existingCustomer.setName(updatedCustomer.getName());
		existingCustomer.setPhone(updatedCustomer.getPhone());
		existingCustomer.setAddress(updatedCustomer.getAddress());
		customerDAO.save(existingCustomer);
	}
}
