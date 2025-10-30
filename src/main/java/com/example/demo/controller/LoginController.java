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

	/** 注入 AuthService，負責使用者驗證邏輯 */
	@Autowired
	private AuthService authService;

	/**
	 * 顯示登入表單
	 *
	 * @return 導向 login.html
	 */
	@GetMapping("/login")
	public String showLoginForm(Model model) {
	    if (!model.containsAttribute("loginDTO")) {
	        model.addAttribute("loginDTO", new LoginDTO());
	    }
	    return "login"; // 對應 login.html
	}

	@PostMapping("/login")
	public String login(@Valid @ModelAttribute("loginDTO") LoginDTO dto, BindingResult result, HttpSession session,
			RedirectAttributes redirectAttrs) {
		if (result.hasErrors()) {
			redirectAttrs.addFlashAttribute("error", result.getAllErrors().get(0).getDefaultMessage());
			return "redirect:/login";
		}

		User user = authService.login(dto.getEmail(), dto.getPassword());
		if (user == null) {
			redirectAttrs.addFlashAttribute("error", "Email 或密碼錯誤");
			return "redirect:/login";
		}

		session.setAttribute("loggedInUser", user);
		return "redirect:/products";
	}

	/**
	 * 處理登出請求
	 *
	 * 清除 Session 以結束登入狀態，並重新導回登入頁。
	 *
	 * @param session 當前使用者 Session
	 * @return 導向 login.html
	 */
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();// 清空 Session
		return "redirect:/login";
	}
}
