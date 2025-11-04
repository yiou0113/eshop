package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.CustomerService;
/**
 * CustomerController 負責處理客戶相關的顯示頁面與操作
 * 包含：
 * - 客戶個人資訊顯示
 * - 驗證重設密碼時輸入密碼是否正確
 * - 客戶密碼重設
 * - 客戶個人資訊重設
 * 
 */
@Controller
@RequestMapping("/user")
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	
	/**
	 * 列出客戶個人資訊
	 * 
	 * @param session 用來取得目前登入的使用者資訊
	 * @param model	用來將資訊傳道前端
	 * @return	回傳道customer-profile頁面
	 */
	@GetMapping("/account/profile")
	public String customerProfile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		User user = (User) userDetails.getUser();
		Customer customer = customerService.getCustomerByUserId(user.getId());
		model.addAttribute("customer", customer);
		model.addAttribute("user", user);
		return "customer-profile";
	}
	
	/**
	 * 用於顯示驗證密碼頁面
	 * 
	 * @return 導向驗證密碼頁面
	 */
	@GetMapping("/verify/password")
	public String verifyPasswordForm() {
		return "verify-password";
	}

	/**
	 * 驗證密碼是否正確
	 * 
	 * @param password 	使用者輸入密碼進行比對
	 * @param session	用來取得目前登入的使用者資訊與密碼輸入正確存入資訊
	 * @param redirectAttributes	用於給予錯誤訊息
	 * @return	導回修改密碼頁面
	 */
	@PostMapping("/verify/password")
	public String verifyPassword(@AuthenticationPrincipal CustomUserDetails userDetails,@RequestParam("password") String password,HttpSession session, 
			RedirectAttributes redirectAttributes) {
		User user = (User) userDetails.getUser();
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

	
	/**
	 * 用於顯示修改密碼頁面
	 * 
	 * @return 導向修改密碼頁面
	 */
	@GetMapping("/account/password")
	public String changePsswordForm() {
		return "change-password";
	}

	/**
	 * 讓使用者更新密碼
	 * @param password	使用者輸入想更改的密碼
	 * @param session	用來取得目前登入的使用者資訊與密碼輸入正確資訊
	 * @param redirectAttributes	用於給予錯誤訊息
	 * @return	導回個人資訊頁面
	 */
	@PostMapping("/account/password")
	public String changePassword(@AuthenticationPrincipal CustomUserDetails userDetails,@RequestParam("password") String password, HttpSession session,
			RedirectAttributes redirectAttributes) {

		User user = (User) userDetails.getUser();
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

	/**
	 * 更新使用者個人資訊
	 * 
	 * @param customer	取得客戶資訊
	 * @param session	用來取得目前登入的使用者資訊
	 * @param redirectAttributes	用於給予錯誤訊息
	 * @return	導回個人資訊頁面
	 */
	@PostMapping("/update")
	public String updateUser(@AuthenticationPrincipal CustomUserDetails userDetails,@ModelAttribute("customer") Customer customer, 
			RedirectAttributes redirectAttributes,HttpServletRequest request) {
		User user = (User) userDetails.getUser();
		customerService.updateCustomerInfo(user.getId(), customer);
		redirectAttributes.addFlashAttribute("message", "更新成功！");
		return "redirect:/user/account/profile";
	}

}
