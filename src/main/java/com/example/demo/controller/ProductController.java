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

@Controller
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping
	public String listProducts(@RequestParam(defaultValue = "1") int page, Model model) {
		int pageSize = 12; // 每頁幾筆
		List<Product> products = productService.getProductsByPage(page, pageSize);
		int totalProducts = productService.getAllProducts().size();
		int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
		
		model.addAttribute("products", products);
		model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", totalPages);
		return "product-list"; // 對應 product-list.html
	}

	@GetMapping("/{id}")
	public String viewProductDetail(@PathVariable Long id, Model model) {
		Product product = productService.getProductById(id);
		model.addAttribute("product", product);
		return "product-detail"; // 對應 product-detail.html
	}

}
