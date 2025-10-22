package com.example.demo.service;

import com.example.demo.model.User;
import java.util.List;
/**
 * UserService 介面 — 定義與使用者（User）相關的業務邏輯操作。
 * 
 * 提供使用者基本的 CRUD 操作與額外的查詢功能，例如依照 email 查詢使用者。
 * 
 */
public interface UserService {
    
    List<User> getAllUsers();
    
    User getUserById(Long id);
    
    void saveUser(User user);
    
    void updateUser(Long id, User updatedUser);
    
    void deleteUser(Long id);
    
    User getUserByEmail(String email);

}
