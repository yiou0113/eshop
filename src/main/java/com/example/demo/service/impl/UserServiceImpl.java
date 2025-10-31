package com.example.demo.service.impl;

import com.example.demo.dao.UserDAO;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * UserService 的實作類別
 * 
 * 此類別負責處理與使用者（User）相關的業務邏輯，
 * 並透過 UserDAO 與資料庫進行互動。
 * 
 * 功能包含：
 * - 取得所有使用者
 * - 依 ID 查詢使用者
 * - 新增使用者（含密碼加密）
 * - 更新使用者資料（含密碼修改時重新加密）
 * - 刪除使用者
 * - 依 Email 查詢使用者
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private PasswordEncoder passwordEncoder; // 用於密碼加密的工具類別
	/**
	 * 取得所有使用者清單
	 *
	 * @return List<User> 所有使用者的列表
	 */
	@Override
	public List<User> getAllUsers() {
		return userDAO.findAll();
	}
	/**
	 * 根據使用者 ID 取得特定使用者
	 *
	 * @param id 使用者的唯一識別碼
	 * @return User 物件，如果找不到則回傳 null
	 */
	@Override
	public User getUserById(Long id) {
		return userDAO.findById(id);
	}
	/**
	 * 新增使用者資料
	 * 
	 * 在儲存之前會先將密碼進行加密再存入資料庫。
	 *
	 * @param user 要新增的使用者物件
	 */
	@Override
	public void saveUser(User user) {
		// 將明碼密碼轉為加密格式
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		// 呼叫 DAO 儲存使用者
		userDAO.save(user);
	}
	/**
	 * 更新現有使用者的資料
	 * 
	 * 若新資料中包含密碼欄位，會重新加密後再更新。
	 *
	 * @param id 使用者 ID
	 * @param updatedUser 更新後的使用者資料
	 */
	@Override
	public void updateUser(Long id, User updatedUser) {
		User existingUser = userDAO.findById(id);
		if (existingUser != null) {
			existingUser.setEmail(updatedUser.getEmail());
			// 若密碼有更新則重新加密
			if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
				String encodedPassword = passwordEncoder.encode(updatedUser.getPassword());
				existingUser.setPassword(encodedPassword);
			}
			// 儲存更新後的資料	
			userDAO.save(existingUser);
		}
	}
	/**
	 * 根據使用者 ID 刪除該使用者
	 *
	 * @param id 使用者 ID
	 */
	@Override
	public void deleteUser(Long id) {
		userDAO.delete(id);
	}
	/**
	 * 根據 Email 查詢使用者
	 *
	 * @param email 使用者的電子郵件地址
	 * @return User 物件，如果不存在則回傳 null
	 */
	@Override
	public User getUserByEmail(String email) {
		return userDAO.findByEmail(email);
	}
}
