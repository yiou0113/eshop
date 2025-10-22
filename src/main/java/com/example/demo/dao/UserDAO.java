package com.example.demo.dao;

import com.example.demo.model.User;
import java.util.List;
/**
 * UserDAO 介面 — 定義使用者（User）資料存取操作（Data Access Object）。
 *
 * 此介面負責與資料庫互動，提供使用者 CRUD操作與依信箱查詢使用者功能。
 */
public interface UserDAO {
    
    List<User> findAll();
    
    User findById(Long id);
    
    void save(User user);
    
    void delete(Long id);
    
    User findByEmail(String email);
}
