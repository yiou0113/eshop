package com.example.demo.dao;

import com.example.demo.model.Product;

import java.util.List;

/**
 * ProductDAO 介面
 *
 * <p>
 * 此介面繼承 {@link BaseDAO}，負責定義商品（{@link Product}）的資料存取操作。
 * </p>
 *
 * <p>
 * 主要功能包含：
 * </p>
 * <ul>
 * <li>商品的分頁查詢</li>
 * <li>依分類 ID 查詢與計算商品數量</li>
 * <li>依商品名稱關鍵字搜尋與統計</li>
 * <li>扣減商品庫存數量</li>
 * </ul>
 *
 */
public interface ProductDAO extends BaseDAO<Product> {

	boolean reduceStock(Long productId, int quantity);

	List<Product> searchProductsByNameAndCategoryWithPage(String keyword, List<Long> categoryIds, int page,
			int pageSize);

	int countProductsByNameAndCategory(String keyword, List<Long> categoryIds);
}
