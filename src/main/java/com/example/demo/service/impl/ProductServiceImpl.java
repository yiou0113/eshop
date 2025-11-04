package com.example.demo.service.impl;

import com.example.demo.dao.ProductDAO;
import com.example.demo.model.Product;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * ProductService 的實作類別
 *
 * 此類別負責處理與商品（Product）相關的業務邏輯， 並透過 ProductDAO 與資料庫互動。
 *
 * 功能包含：
 *  - 取得所有商品
 *  - 根據 ID 查詢商品 
 *  - 新增商品 
 *  - 刪除商品 
 *  - 分頁關鍵字查詢及分類取得商品清單
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDAO productDAO;
	@Autowired
	private CategoryService categoryService;
	/**
	 * 取得所有商品清單
	 *
	 * @return List<Product> 所有商品的列表
	 */
	@Override
	public List<Product> getAllProducts() {
		return productDAO.findAll();
	}

	/**
	 * 根據商品 ID 取得特定商品
	 *
	 * @param id 商品的唯一識別碼
	 * @return Product 物件，若找不到則回傳 null
	 */
	@Override
	public Product getProductById(Long id) {
		return productDAO.findById(id);
	}

	/**
	 * 新增商品資料
	 *
	 * @param product 要儲存的商品物件
	 */
	@Override
	public void saveProduct(Product product) {
		productDAO.save(product);
	}

	/**
	 * 根據商品 ID 刪除該商品
	 *
	 * @param id 商品的唯一識別碼
	 */
	@Override
	public void deleteProduct(Long id) {
		productDAO.delete(id);
	}
	
	/**
	 * 根據關鍵字與分類查詢商品，並支援分頁。
	 *
	 * @param keyword    搜尋關鍵字，可為商品名稱或部分名稱
	 * @param categoryId 商品分類 ID，可為 null，若指定則包含子分類商品
	 * @param page       分頁頁碼，從 1 開始
	 * @param pageSize   每頁顯示的商品數量
	 * @return 查詢到的商品列表
	 */
	@Override
	public List<Product> searchProductsByNameAndCategoryWithPage(String keyword, Long categoryId, int page, int pageSize) {
	    List<Long> categoryIds = null;
	    if (categoryId != null) {
	        categoryIds = categoryService.getAllChildCategoryIds(categoryId);
	    }
	    return productDAO.searchProductsByNameAndCategoryWithPage(keyword, categoryIds, page, pageSize);
	}
	
	/**
	 * 根據關鍵字與分類統計符合條件的商品數量。
	 *
	 * @param keyword    搜尋關鍵字，可為商品名稱或部分名稱
	 * @param categoryId 商品分類 ID，可為 null，若指定則包含子分類商品
	 * @return 符合條件的商品總數
	 */
	@Override
	public int countProductsByNameAndCategory(String keyword, Long categoryId) {
	    List<Long> categoryIds = null;
	    if (categoryId != null) {
	        categoryIds = categoryService.getAllChildCategoryIds(categoryId);
	    }
	    return productDAO.countProductsByNameAndCategory(keyword, categoryIds);
	}

}
