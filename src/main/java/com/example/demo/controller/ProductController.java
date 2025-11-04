package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.Customer;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.CategoryService;
import com.example.demo.service.CustomerService;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * 商品相關的 Controller
 *
 * 此控制器負責處理商品列表與商品詳細資訊的展示。 包含： - 商品列表（分頁顯示） - 商品詳細頁面
 */
@Controller
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CustomerService customerService;

	/**
	 * 顯示商品列表（支援分頁）
	 *
	 * 當使用者訪問 /products 時，會顯示商品列表頁面， 每頁顯示固定數量(目前設定為12)的商品，並計算總頁數。
	 *
	 * @param page       目前頁碼，預設為 1
	 * @param categoryId 目前選取分類
	 * @param model      用於傳遞資料到前端
	 * @return 返回商品列表的模板名稱 "product-list" 對應 product-list.html
	 */
	@GetMapping
	public String listProducts(@AuthenticationPrincipal CustomUserDetails userDetails,
			@RequestParam(defaultValue = "1") int page, @RequestParam(required = false) Long categoryId,@RequestParam(value = "keyword", required = false) String keyword,
			 Model model) {
		int pageSize = 12; // 每頁幾筆
		List<Product> products = productService.searchProductsByNameAndCategoryWithPage(keyword, categoryId, page, pageSize);
		int totalProducts = productService.countProductsByNameAndCategory(keyword, categoryId);
		
		int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
		if (totalPages == 0) {
			totalPages = 1;
		}
		

		// 用三層分類結構
		List<Category> categoryTree = categoryService.getThreeLevelCategories();
		if (userDetails != null) {
			User user = userDetails.getUser();
			Customer customer = null;
			if (user != null) {
				// 若使用者已登入，才嘗試查詢 customer
				customer = customerService.getCustomerByUserId(user.getId());
				model.addAttribute("customer", customer);
			}
		}
		model.addAttribute("products", products);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("categoryTree", categoryTree);
		model.addAttribute("selectedCategoryId", categoryId);
		model.addAttribute("keyword", keyword); // 必須加，讓搜尋欄能顯示 keyword
		return "product-list";
	}

	/**
	 * 顯示單一商品的詳細資訊
	 *
	 * 當使用者訪問 /products/{id} 時，顯示該商品的詳細頁面。
	 *
	 * @param id    商品 ID
	 * @param model 用於傳遞商品資料到前端
	 * @return 返回商品詳細頁面
	 */
	@GetMapping("/{id}")
	public String viewProductDetail(@PathVariable Long id, Model model) {
		Product product = productService.getProductById(id);
		model.addAttribute("product", product);
		return "product-detail";
	}
}
