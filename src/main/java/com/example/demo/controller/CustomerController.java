package com.example.demo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

	@GetMapping("/account/profile")
	public String customerProfile(HttpSession session, Model model) {
		User user = (User) session.getAttribute("loggedInUser");
		Customer customer = customerService.getCustomerByUserId(user.getId());
		model.addAttribute("customer", customer);
		model.addAttribute("user", user);
		return "customer-profile";
	}

	@PostMapping("/update")
	public String updateUser(@ModelAttribute("customer") Customer customer, HttpSession session,
			RedirectAttributes redirectAttributes) {
		User user = (User) session.getAttribute("loggedInUser");
		customerService.updateCustomerInfo(user.getId(), customer);
		redirectAttributes.addFlashAttribute("message", "更新成功！");
		return "redirect:account/profile";
	}
	
}
