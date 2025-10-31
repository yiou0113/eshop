package com.example.demo.dao;

import com.example.demo.model.User;

/**
 * UserDAO 介面
 *
 * <p>此介面繼承 {@link BaseDAO}，負責定義與使用者（{@link User}）相關的資料存取操作。</p>
 *
 * <p>主要功能包含：</p>
 * <ul>
 *   <li>基本的使用者 CRUD 操作（建立、讀取、更新、刪除）</li>
 *   <li>依電子郵件（Email）查詢使用者資料</li>
 * </ul>
 *
 */
public interface UserDAO extends BaseDAO<User> {
    User findByEmail(String email);
}