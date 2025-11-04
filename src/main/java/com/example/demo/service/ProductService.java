package com.example.demo.service;

import com.example.demo.model.Product;
import java.util.List;

/**
 * ProductService 介面 — 定義商品（Product）相關的業務邏輯操作。
 *
 * 提供商品的基本 CRUD 操作與分頁查詢、關鍵字查詢等功能。
 * 
 */
public interface ProductService {

	List<Product> getAllProducts();

	Product getProductById(Long id);

	void saveProduct(Product product);

	void deleteProduct(Long id);

	List<Product> searchProductsByNameAndCategoryWithPage(String keyword, Long categoryId, int page, int pageSize);

	int countProductsByNameAndCategory(String keyword, Long categoryId);

}
