package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.model.Customer;
import com.example.demo.model.User;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.CartService;
import com.example.demo.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * CartController 負責處理與購物車相關的頁面顯示與操作。
 * 包含：
 * - 顯示購物車內容
 * - 加入商品至購物車
 * - 更新購物車內商品數量
 * - 刪除購物車內商品
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
     * @param userDetails  用來取得目前登入的使用者資訊
     * @param model    用來將購物車資料傳遞給前端頁面
     * @return         回傳到 cart.html 顯示購物車畫面
     */
	@GetMapping
	public String viewCart(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		// 從 Session 取得目前登入的使用者	
		
		User user = (User) userDetails.getUser();
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
		//判斷購物車內是否為空，若為空回傳false
		model.addAttribute("isEmpty", cartService.isCartEmpty(customer.getId()));
		model.addAttribute("cart", cart);
		//取得購物車內總金額
		model.addAttribute("total", cartService.getCartTotal(customer.getId()));
		return "cart";
	}

	/**
     * 新增商品到購物車。
     * 若未登入則導向登入頁面。
     *
     * @param productId 商品 ID
     * @param quantity  購買數量
     * @param userDetails   用於取得登入的使用者
     * @param redirectAttributes 用於給予錯誤訊息
     * @return	導回產品列表頁面
     */
	@PostMapping("/add")
	public String addToCart(@AuthenticationPrincipal CustomUserDetails userDetails,@RequestParam Long productId, @RequestParam int quantity, RedirectAttributes redirectAttributes) {
		// 從 Session 取得目前登入的使用者
		User user = (User) userDetails.getUser();
		// 根據使用者 ID 查找對應的顧客資料
		Customer customer = customerSecvice.getCustomerByUserId(user.getId());
		//判斷購買數量是否符合庫存
	    boolean success = cartService.addToCart(productId, quantity);
	    if (!success) {
	        redirectAttributes.addFlashAttribute("error", "購買數量不能超過庫存！");
	        return "redirect:/products";
	    }
		cartService.addToCart(customer.getId(), productId, quantity);
		redirectAttributes.addFlashAttribute("message", "商品已加入購物車！");
		return "redirect:/products";
	}
	/**
     * 更新購物車中某商品的數量。
     * 若未登入則導向登入頁面。
     *
     * @param productId 商品 ID
     * @param quantity  新數量
     * @param userDetails   用於取得登入的使用者
     * @param redirectAttributes 用於給予錯誤訊息
     * @return 導回購物車頁面
     */
	// 更新商品數量
	@PostMapping("/update")
	public String updateQuantity(@AuthenticationPrincipal CustomUserDetails userDetails,@RequestParam("productId") Long productId, @RequestParam("quantity") int quantity,
			RedirectAttributes redirectAttributes) {
		// 從 Session 取得目前登入的使用者
		User user = (User) userDetails.getUser();
		// 根據使用者 ID 查找對應的顧客資料
		Customer customer = customerSecvice.getCustomerByUserId(user.getId());
		//判斷購買數量是否符合庫存
		boolean success = cartService.addToCart(productId, quantity);
	    if (!success) {
	        redirectAttributes.addFlashAttribute("error", "購買數量不能超過庫存！");
	        return "redirect:/cart?userId=" + customer.getId();
	    }
		cartService.updateQuantity(customer.getId(), productId, quantity);
		return "redirect:/cart?userId=" + customer.getId();
	}
	
	/**
     * 從購物車中移除某商品。
     * 若未登入則導向登入頁面。
     *
     * @param productId 商品 ID
     * @param userDetails   用於取得登入的使用者
     * @return 導回購物車頁面
     */
	@PostMapping("/remove")
	public String removeItem(@AuthenticationPrincipal CustomUserDetails userDetails,@RequestParam("productId") Long productId) {
		// 從 Session 取得目前登入的使用者
		User user = (User) userDetails.getUser();
		// 根據使用者 ID 查找對應的顧客資料
		Customer customer = customerSecvice.getCustomerByUserId(user.getId());
		cartService.removeItem(customer.getId(), productId);
		return "redirect:/cart?userId=" + customer.getId();
	}
}
