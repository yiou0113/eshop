package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * HomeController
 *
 * 此控制器負責處理網站首頁 ("/") 的請求。 
 * 使用者進入網站根路徑時，會自動導向 home.html 頁面。
 */
@Controller
public class HomeController {
	/**
	 * 處理根路徑 ("/") 的 GET 請求。
	 *
	 * @return 導向到 home.html
	 */
	@GetMapping("/")
	public String home() {
		return "home";
	}
}
