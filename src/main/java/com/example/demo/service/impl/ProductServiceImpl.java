package com.example.demo.service.impl;

import com.example.demo.dao.ProductDAO;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * ProductService 的實作類別
 *
 * 此類別負責處理與商品（Product）相關的商業邏輯， 並透過 ProductDAO 與資料庫互動。
 *
 * 功能包含：
 *  - 取得所有商品
 *  - 根據 ID 查詢商品 
 *  - 新增商品 
 *  - 刪除商品 
 *  - 分頁取得商品清單
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDAO productDAO;

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
	 * 分頁取得商品清單
	 *
	 * @param page 目前頁碼（從 1 開始）
	 * @param size 每頁顯示的商品數量
	 * @return List<Product> 該頁的商品列表
	 */
	@Override
	public List<Product> getProductsByPage(int page, int size) {
		return productDAO.findPaginated(page, size);
	}
	
	@Override
	public List<Product> getProductsByCategoryId(Long categoryId){
		return productDAO.findByCategoryId(categoryId);
	}
	@Override
	public List<Product> getProductsByCategory(Long categoryId, int page, int size) {
	    if (categoryId == null) {
	        return List.of();
	    }
	    return productDAO.findByCategoryId(categoryId, page, size);
	}

	@Override
	public int countProductsByCategory(Long categoryId) {
	    if (categoryId == null) {
	        return 0;
	    }
	    return productDAO.countByCategoryId(categoryId);
	}


}
