package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 此控制器負責處理商品列表與商品詳細資訊的展示。 所有對應的 URL 都以 "/products" 開頭。
 *
 * 功能包含： 
 * - 商品列表（分頁顯示） 
 * - 商品詳細頁面
 */
@Controller
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	/**
	 * 顯示商品列表（支援分頁）
	 *
	 * 當使用者訪問 /products 時，會顯示商品列表頁面， 每頁顯示固定數量(目前設定為12)的商品，並計算總頁數。
	 *
	 * @param page  目前頁碼，預設為 1
	 * @param model Spring MVC 的 Model，用於傳遞資料到視圖
	 * @return 返回商品列表的模板名稱 "product-list" 對應 product-list.html
	 */
	@GetMapping
	public String listProducts(@RequestParam(defaultValue = "1") int page, Model model) {
		int pageSize = 12; // 每頁幾筆
		List<Product> products = productService.getProductsByPage(page, pageSize);
		int totalProducts = productService.getAllProducts().size();
		int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

		model.addAttribute("products", products);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		return "product-list";
	}

	/**
	 * 顯示單一商品的詳細資訊
	 *
	 * 當使用者訪問 /products/{id} 時，顯示該商品的詳細頁面。
	 *
	 * @param id    商品 ID
	 * @param model Spring MVC 的 Model，用於傳遞商品資料到視圖
	 * @return 返回商品詳細頁面的模板名稱 "product-detail" 對應 product-detail.html
	 */
	@GetMapping("/{id}")
	public String viewProductDetail(@PathVariable Long id, Model model) {
		Product product = productService.getProductById(id);
		model.addAttribute("product", product);
		return "product-detail";
	}

}
