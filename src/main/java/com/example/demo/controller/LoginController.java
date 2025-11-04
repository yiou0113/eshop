package com.example.demo.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.LoginDTO;
import com.example.demo.model.User;
import com.example.demo.service.AuthService;

/**
 * LoginController 負責處理登入、登出與登入後歡迎頁的控制流程。
 *
 * 功能包含： - 顯示登入頁面 - 驗證登入資訊 - 管理登入 Session - 登出功能
 */
@Controller
public class LoginController {
	@GetMapping("/login")
    public String login() {
        return "login"; // 對應 src/main/webapp/WEB-INF/views/login.html
    }

}
