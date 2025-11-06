package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;

@Controller
public class AdminProductController {
	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;
	
	/**
     * 顯示新增商品頁面
     */
    @GetMapping("/admin/product/add")
    public String showAddProductPage(Model model,HttpServletRequest request) {
        Product product = new Product();
        product.setCategory(new Category());
        List<Category> leafCategories = categoryService.getAllLeafCategories();

        model.addAttribute("product", product);
        model.addAttribute("categories", leafCategories);

        return "product-add"; // 對應 Thymeleaf 模板
    }
    /**
     * 處理新增商品表單
     */
    @PostMapping("/admin/product/add")
    public String handleAddProduct(
            @ModelAttribute Product product,
            @RequestParam("imageFile") MultipartFile imageFile,
            Model model
    ) {
        try {
            productService.addProduct(product, imageFile);
            
            return "redirect:/products"; // 上傳成功後導向商品列表
        } catch (Exception e) {
            // 上傳失敗或其他錯誤，回到新增頁面並顯示錯誤訊息
            model.addAttribute("errorMessage", "新增商品失敗：" + e.getMessage());
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.getAllLeafCategories());
            return "product-add";
        }
    }
}
