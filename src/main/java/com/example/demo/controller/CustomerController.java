package com.example.demo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Customer;
import com.example.demo.model.User;
import com.example.demo.service.CustomerService;

@Controller
@RequestMapping("/user")
public class CustomerController {
	@Autowired
	private CustomerService customerService;

	@GetMapping("/verify/password")
	public String verifyPasswordForm() {
		return "verify-password";
	}

	@PostMapping("/verify/password")
	public String verifyPassword(@RequestParam("password") String password, HttpSession session,
			RedirectAttributes redirectAttributes) {
		User user = (User) session.getAttribute("loggedInUser");
		if (user == null) {
			return "redirect:/login";
		}
		boolean isCorrect = customerService.checkPassword(user.getId(), password);
		if (isCorrect) {
			// 驗證成功 → 導向修改密碼頁
			session.setAttribute("verifiedPassword", true);
			return "redirect:/user/account/password";
		} else {
			// 驗證失敗 → 帶錯誤訊息返回原頁
			redirectAttributes.addFlashAttribute("error", "密碼錯誤");
			return "redirect:/user/verify/password";
		}
	}

	@GetMapping("/account/profile")
	public String customerProfile(HttpSession session, Model model) {
		User user = (User) session.getAttribute("loggedInUser");
		Customer customer = customerService.getCustomerByUserId(user.getId());
		model.addAttribute("customer", customer);
		model.addAttribute("user", user);
		return "customer-profile";
	}

	@GetMapping("/account/password")
	public String changePsswordForm() {
		return "change-password";
	}

	@PostMapping("/account/password")
	public String changePassword(@RequestParam("password") String password, HttpSession session,
			RedirectAttributes redirectAttributes) {

		User user = (User) session.getAttribute("loggedInUser");
		if (user == null) {
			return "redirect:/login";
		}

		Boolean verified = (Boolean) session.getAttribute("verifiedPassword");
		if (verified == null || !verified) {
			redirectAttributes.addFlashAttribute("error", "請先驗證密碼！");
			return "redirect:/user/verify/password";
		}

		// 先檢查密碼是否為空
		if (password == null || password.trim().isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "密碼不得為空白！");
			return "redirect:/user/account/password"; // 返回修改頁
		}

		// 更新密碼
		customerService.updateCustomerPassword(user.getId(), password);

		// 修改完成後清除驗證標記
		session.removeAttribute("verifiedPassword");

		redirectAttributes.addFlashAttribute("message", "密碼修改成功！");
		return "redirect:/user/account/profile"; // 成功後返回個人資料頁
	}

	@PostMapping("/update")
	public String updateUser(@ModelAttribute("customer") Customer customer, HttpSession session,
			RedirectAttributes redirectAttributes) {
		User user = (User) session.getAttribute("loggedInUser");
		customerService.updateCustomerInfo(user.getId(), customer);
		redirectAttributes.addFlashAttribute("message", "更新成功！");
		return "redirect:/user/account/profile";
	}

}
