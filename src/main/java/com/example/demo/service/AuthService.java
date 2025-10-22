package com.example.demo.service;

import com.example.demo.model.User;
/**
 * AuthService 介面 — 定義使用者認證相關的業務邏輯操作。
 *
 * 此介面主要負責處理使用者登入驗證。
 */
public interface AuthService {
    User login(String email, String password);
}
