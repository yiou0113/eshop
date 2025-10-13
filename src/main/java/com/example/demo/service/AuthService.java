package com.example.demo.service;

import com.example.demo.model.User;
import java.util.List;

public interface AuthService {
    User login(String email, String password);
}
