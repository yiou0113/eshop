package com.example.demo.controller;

import com.example.demo.dto.RegisterDTO;
import com.example.demo.service.CustomerService;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
/**
 * 註冊相關的 Controller
 *
 * 此控制器負責處理「顧客註冊」相關的 HTTP 請求，
 * 包含顯示註冊表單（GET）與處理註冊提交（POST）。
 *
 * 使用 {@code @Controller} 表示此類別為 Spring MVC 的控制器，
 * 可返回 Thymeleaf 等模板頁面。
 */
@Controller
public class RegisterController {
	
	/** 注入 CustomerService，用於處理顧客註冊的業務邏輯 */
    @Autowired
    private CustomerService customerService;
    
    /**
     * 顯示註冊頁面
     *
     * 當使用者訪問 `/register` 時（GET 請求），
     * 系統會返回註冊表單頁面 `register.html`。
     *
     * @return 返回註冊頁面的檔名（Thymeleaf 模板）
     */
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
    	model.addAttribute("form", new RegisterDTO());
        return "register";
    }
    /**
     * 處理註冊表單提交
     *
     * 當使用者提交註冊表單（POST 到 `/register`）時，
     * 此方法會接收表單中的使用者輸入，
     * 呼叫 {@link CustomerService#registerCustomer(String, String, String, String, String)}
     * 進行註冊，最後導向至登入頁面。
     *
     * @param name     使用者姓名
     * @param email    使用者電子郵件
     * @param password 使用者密碼（明文）
     * @param phone    使用者電話
     * @param address  使用者地址
     * @return 導向至登入頁面 `/login`
     */
    @PostMapping("/register")
    public String register(
    		@Valid @ModelAttribute("form") RegisterDTO form,
            BindingResult bindingResult,
            Model model) {

    	// 若驗證失敗，回傳註冊頁並顯示錯誤訊息
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            customerService.registerCustomer(
                form.getName(),
                form.getEmail(),
                form.getPassword(),
                form.getPhone(),
                form.getAddress()
            );
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }
    
}
