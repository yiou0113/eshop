package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * LoginController 負責處理使用者登入相關的流程
 *
 * 功能包含：
 * -  顯示登入頁面
 * -  驗證登入錯誤訊息
 * -  顯示登出成功訊息
 * -  顯示密碼變更成功訊息
 *
 */
@Controller
public class LoginController {
	/**
     * 處理 GET /login 請求
     *
     * @param error   當登入失敗時，Spring Security 會附帶 ?error 參數
     * @param logout  當使用者登出成功時，Spring Security 會附帶 ?logout 參數
     * @param changed 當使用者剛修改密碼時，可加上 ?changed 參數
     * @param model   Spring MVC Model，用來傳資料到 view
     * @return 返回 login.html 
     */
	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout,
			@RequestParam(value = "changed", required = false) String changed, Model model) {
		if (error != null) {
			model.addAttribute("error", "帳號或密碼錯誤");
		}
		if (logout != null) {
			model.addAttribute("message", "您已成功登出");
		}
		if (changed != null) {
			model.addAttribute("message", "密碼變更成功，請重新登入");
		}
		return "login"; 
	}

}
