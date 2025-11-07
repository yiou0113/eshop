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

/**
 * AdminProductController
 * 管理員後台用於「新增商品」的控制器。
 * 
 * 功能：
 *  - 顯示新增商品的表單頁面
 *  - 接收表單提交並保存商品資料（含圖片）
 */
@Controller
public class AdminProductController {
	@Autowired
	private ProductService productService;

	@Autowired
	private CategoryService categoryService;
	
	/**
     * 顯示「新增商品」頁面。
     * 
     * 
     *  1. 建立一個空的 Product 物件，並預設一個空的 Category 以供表單綁定。
     *  2. 從分類中取得所有最底層分類。
     *  3. 將 Product 物件與分類清單放入 Model 中。
     *  4. 返回模 "product-add"。
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
     * 處理「新增商品」的表單提交。
     * 
     * 
     *  - @ModelAttribute Product product ：自動綁定表單欄位到 Product 物件。
     *  - @RequestParam("imageFile") MultipartFile imageFile ：接收上傳的圖片檔案。
     *  - Model model ：用於回傳資料或錯誤訊息到前端。
     * 
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
