package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.model.Customer;
import com.example.demo.model.User;
import com.example.demo.service.CartService;
import com.example.demo.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
/**
 * CartController 負責處理與購物車相關的頁面顯示與操作。
 * 包含：
 * - 顯示購物車內容
 * - 加入商品至購物車
 * - 更新商品數量
 * - 刪除商品
 */
@Controller
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService cartService;
	@Autowired
	private CustomerService customerSecvice;

	/**
     * 顯示目前使用者的購物車內容。
     * 若使用者未登入，則會導向登入頁面。
     *
     * @param session  用來取得目前登入的使用者資訊
     * @param model    用來將購物車資料傳遞給前端頁面
     * @return         回傳到 cart.html 顯示購物車畫面
     */
	@GetMapping
	public String viewCart(HttpSession session, Model model) {
		// 從 Session 取得目前登入的使用者
		User user = (User) session.getAttribute("loggedInUser");
		if (user == null) {
			return "redirect:/login"; 
		}
		// 根據使用者 ID 查找對應的顧客資料
		Customer customer = customerSecvice.getCustomerByUserId(user.getId());
		if (customer == null) {
			return "redirect:/login";
		}
		// 取得顧客的購物車
		Cart cart = cartService.getCartByCustomerId(customer.getId());
		if (cart == null) {
			cart = new Cart();
		}
		model.addAttribute("cart", cart);
		model.addAttribute("total", cartService.getCartTotal(customer.getId()));
		return "cart";
	}

	/**
     * 新增商品到購物車。
     * 若未登入則導向登入頁面。
     *
     * @param productId 商品 ID
     * @param quantity  購買數量
     * @param session   用於取得登入的使用者
     * @return          重新導向至商品列表頁（/products）
     */
	@PostMapping("/add")
	public String addToCart(@RequestParam Long productId, @RequestParam int quantity, HttpSession session) {
		// 從 Session 取得目前登入的使用者
		User user = (User) session.getAttribute("loggedInUser");
		if (user == null)
			return "redirect:/login";
		// 根據使用者 ID 查找對應的顧客資料
		Customer customer = customerSecvice.getCustomerByUserId(user.getId());
		if (customer == null) {
			return "redirect:/login";
		}
		cartService.addToCart(customer.getId(), productId, quantity);
		return "redirect:/products";
	}
	/**
     * 更新購物車中某商品的數量。
     * 若未登入則導向登入頁面。
     *
     * @param productId 商品 ID
     * @param quantity  新數量
     * @param session   用於取得登入的使用者
     * @return          重新導向至購物車頁面
     */
	// 更新商品數量
	@PostMapping("/update")
	public String updateQuantity(@RequestParam("productId") Long productId, @RequestParam("quantity") int quantity,
			HttpSession session) {
		// 從 Session 取得目前登入的使用者
		User user = (User) session.getAttribute("loggedInUser");
		if (user == null)
			return "redirect:/login";
		// 根據使用者 ID 查找對應的顧客資料
		Customer customer = customerSecvice.getCustomerByUserId(user.getId());
		if (customer == null) {
			return "redirect:/login";
		}
		cartService.updateQuantity(customer.getId(), productId, quantity);
		return "redirect:/cart?userId=" + customer.getId();
	}
	
	/**
     * 從購物車中移除某商品。
     * 若未登入則導向登入頁面。
     *
     * @param productId 商品 ID
     * @param session   用於取得登入的使用者
     * @return          重新導向至購物車頁面
     */
	@PostMapping("/remove")
	public String removeItem(@RequestParam("productId") Long productId, HttpSession session) {
		// 從 Session 取得目前登入的使用者
		User user = (User) session.getAttribute("loggedInUser");
		if (user == null)
			return "redirect:/login";
		// 根據使用者 ID 查找對應的顧客資料
		Customer customer = customerSecvice.getCustomerByUserId(user.getId());
		if (customer == null) {
			return "redirect:/login";
		}
		cartService.removeItem(customer.getId(), productId);
		return "redirect:/cart?userId=" + customer.getId();
	}
}
