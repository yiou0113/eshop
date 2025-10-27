package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.User;
import com.example.demo.service.PasswordResetTokenService;
import com.example.demo.service.UserService;
@Controller
@RequestMapping("/password")
public class PasswordResetController {
	  @Autowired
	    private PasswordResetTokenService tokenService;

	    @Autowired
	    private UserService userService;

	    // 顯示輸入 email 頁面
	    @GetMapping("/forgot")
	    public String showForgotPasswordForm() {
	        return "forgot-password";
	    }

	    // 提交 email 產生 token
	    @PostMapping("/forgot")
	    public String handleForgotPassword(@RequestParam("email") String email, Model model) {
	        String token = tokenService.createTokenForEmail(email);
	        if (token == null) {
	            model.addAttribute("error", "Email not found");
	            return "forgot-password";
	        }
	        // 模擬寄信：直接顯示 token
	        model.addAttribute("message", "Password reset token:http://localhost:8080/echop/password/reset?token=" + token);
	        return "forgot-password";
	    }

	    // 顯示輸入新密碼頁面
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

	    // 提交新密碼
	    @PostMapping("/reset")
	    public String handleReset(@RequestParam("token") String token,
	                              @RequestParam("password") String password,
	                              Model model) {
	        boolean valid = tokenService.validateToken(token);
	        if (!valid) {
	            model.addAttribute("error", "Invalid or expired token");
	            return "reset-password";
	        }

	        // 取得使用者
	        var resetToken = tokenService.getByToken(token); 
	        User user = resetToken.getUser();
	        userService.updateUser(user.getId(), user);
	        tokenService.removeExpiredTokens(); 

	        model.addAttribute("message", "Password reset successfully!");
	        return "reset-password";
	    }
}
