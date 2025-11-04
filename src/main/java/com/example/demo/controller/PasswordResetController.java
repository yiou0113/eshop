package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.PasswordResetToken;
import com.example.demo.model.User;
import com.example.demo.service.PasswordResetTokenService;
import com.example.demo.service.UserService;

/**
 * PasswordResetController 負責處理忘記密碼相關的頁面顯示與操作 包含： - 忘記密碼頁面及操作 - 重設密碼頁面及操作
 */
@Controller
@RequestMapping("/password")
public class PasswordResetController {
	@Autowired
	private PasswordResetTokenService tokenService;

	@Autowired
	private UserService userService;

	/**
	 * 顯示忘記密碼頁面
	 * 
	 * @return 導回忘記密碼頁面
	 */
	@GetMapping("/forgot")
	public String showForgotPasswordForm() {
		return "forgot-password";
	}

	/**
	 * 實作忘記密碼功能
	 * 
	 * @param email 使用者輸入信箱以變更密碼
	 * @param model 用來將資料傳到前端
	 * @return 導回忘記密碼頁面
	 */
	@PostMapping("/forgot")
	public String handleForgotPassword(@RequestParam("email") String email, Model model) {
		String token = tokenService.createTokenForEmail(email);
		if (token == null) {
			model.addAttribute("error", "Email not found");
			return "forgot-password";
		}
		model.addAttribute("message", "Password reset token:http://localhost:8080/echop/password/reset?token=" + token);
		return "forgot-password";
	}

	/**
	 * 顯示重製密碼頁面
	 * 
	 * @param token 獲取忘記密碼token
	 * @param model 用來將資料傳到前端
	 * @return 導回重製密碼頁面
	 */
	@GetMapping("/reset")
	public String showResetForm(@RequestParam("token") String token, Model model) {
		boolean valid = tokenService.validateToken(token);
		if (!valid) {
			model.addAttribute("error", "Invalid or expired token");
			return "reset-password";
		}
		model.addAttribute("token", token);
		return "reset-password";
	}

	/**
	 * 提交新密碼以重設
	 * 
	 * @param token    獲取忘記密碼token
	 * @param password 使用者輸入新密碼
	 * @param model    用來將資料傳到前端
	 * @return 導回重製密碼頁面
	 */
	@PostMapping("/reset")
	public String handleReset(@RequestParam("token") String token, @RequestParam("password") String password,
			Model model) {
		boolean valid = tokenService.validateToken(token);
		if (!valid) {
			model.addAttribute("error", "Invalid or expired token");
			return "reset-password";
		}

		// 取得使用者
		PasswordResetToken resetToken = tokenService.getByToken(token);
		User user = resetToken.getUser();
		userService.updateUser(user.getId(), user);
		tokenService.removeExpiredTokens();

		model.addAttribute("message", "Password reset successfully!");
		return "redirect:/login?changed=true";
	}
}
