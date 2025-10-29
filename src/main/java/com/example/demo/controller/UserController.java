package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 使用者管理相關 Controller
 *
 * 此控制器負責處理「使用者 CRUD」相關的 HTTP 請求， 
 * 包含列出使用者、顯示新增/編輯表單、儲存更新以及刪除使用者。
 *
 */	
@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * 列出所有使用者
	 *
	 * 當使用者訪問 `/users` 時（GET 請求）， 系統會取得所有使用者資料，並傳給模板頁面顯示。
	 *
	 * @param model 用於將資料傳遞至 Thymeleaf 模板
	 * @return 導向使用者列表頁面 `users.html`
	 */
	@GetMapping
	public String listUsers(Model model) {
		model.addAttribute("users", userService.getAllUsers());
		return "users";
	}

	/**
	 * 顯示新增使用者表單
	 *
	 * 當使用者訪問 `/users/add` 時（GET 請求）， 系統會建立一個空的 User 物件，傳給模板綁定表單欄位。
	 *
	 * @param model 用於將空 User 物件傳至 Thymeleaf 模板
	 * @return 導向新增使用者頁面 `add-user.html`
	 */
	@GetMapping("/add")
	public String showAddForm(Model model) {
		model.addAttribute("user", new User());
		return "add-user";
	}

	/**
	 * 顯示編輯使用者表單
	 *
	 * 當使用者訪問 `/users/edit/{id}` 時（GET 請求）， 系統會依照使用者 ID 取得該使用者資料，傳給模板頁面顯示。
	 *
	 * @param id    要編輯的使用者 ID
	 * @param model 用於將使用者資料傳至 Thymeleaf 模板
	 * @return 導向編輯使用者頁面 `edit-user.html`
	 */
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable("id") Long id, Model model) {
		User user = userService.getUserById(id);
		model.addAttribute("user", user);
		return "edit-user"; // 對應 /WEB-INF/views/edit-user.html
	}

	/**
	 * 更新使用者資料
	 *
	 * 當使用者提交編輯表單（POST 到 `/users/update/{id}`）時， 系統會接收表單資料，呼叫 Service 更新使用者資訊，
	 * 更新完成後重定向回使用者列表頁面。
	 *
	 * @param id   要更新的使用者 ID
	 * @param user 表單提交的使用者資料
	 * @return 更新完成後重定向至 `/users`
	 */
	@PostMapping("/update/{id}")
	public String updateUser(@PathVariable("id") Long id, @ModelAttribute("user") User user) {
		userService.updateUser(id, user);
		return "redirect:/users";
	}

	/**
	 * 儲存新增的使用者
	 *
	 * 當使用者提交新增表單（POST 到 `/users/save`）時， 系統會接收表單資料，呼叫 Service 儲存新使用者，
	 * 儲存完成後重定向回使用者列表頁面。
	 *
	 * @param user 表單提交的使用者資料
	 * @return 儲存完成後重定向至 `/users`
	 */
	@PostMapping("/save")
	public String saveUser(@ModelAttribute User user) {
		userService.saveUser(user);
		return "redirect:/users";
	}

	/**
	 * 刪除使用者
	 *
	 * 當使用者訪問 `/users/delete/{id}` 時（GET 請求）， 系統會依照使用者 ID 刪除該使用者資料，
	 * 刪除完成後重定向回使用者列表頁面。
	 *
	 * @param id 要刪除的使用者 ID
	 * @return 刪除完成後重定向至 `/users`
	 */
	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") Long id) {
		userService.deleteUser(id);
		return "redirect:/users";
	}
}