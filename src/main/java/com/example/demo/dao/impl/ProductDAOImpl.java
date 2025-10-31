package com.example.demo.dao.impl;

import com.example.demo.dao.ProductDAO;
import com.example.demo.model.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ProductDAO 的實作類別
 *
 * <p>此類別繼承 {@link BaseDAOImpl} 並實作 {@link ProductDAO}，
 * 負責與資料庫互動，提供商品（{@link Product}）的 CRUD、分頁查詢與庫存操作。</p>
 *
 * <p>主要功能：</p>
 * <ul>
 *   <li>查詢所有商品</li>
 *   <li>依 ID 查詢單一商品</li>
 *   <li>新增或更新商品</li>
 *   <li>刪除商品</li>
 *   <li>分頁查詢商品列表</li>
 *   <li>依分類 ID 查詢商品並分頁</li>
 *   <li>計算指定分類或名稱的商品數量</li>
 *   <li>依關鍵字搜尋商品名稱</li>
 *   <li>扣減商品庫存（若庫存足夠）</li>
 * </ul>
 */
@Repository
public class ProductDAOImpl extends BaseDAOImpl<Product> implements ProductDAO {
	/**
     * 分頁查詢所有商品。
     *
     * @param page 當前頁碼（從 1 開始）
     * @param size 每頁顯示筆數
     * @return 商品列表
     */
	@Override
	public List<Product> findPaginated(int page, int size) {
		return getCurrentSession()
				.createQuery("FROM Product", Product.class)
				.setFirstResult((page - 1) * size) // 計算從哪一筆開始
				.setMaxResults(size) // 每頁顯示多少筆
				.list();
	}
	/**
     * 根據分類 ID 列表分頁查詢商品。
     *
     * @param categoryIds 分類 ID 列表
     * @param page 當前頁碼（從 1 開始）
     * @param size 每頁顯示筆數
     * @return 商品列表；若 categoryIds 為 null 則回傳空集合
     */
	@Override
	public List<Product> findByCategoryIds(List<Long> categoryIds, int page, int size) {
	    if (categoryIds == null) {
	        return List.of();
	    }
	    return getCurrentSession()
	            .createQuery("FROM Product p WHERE p.category.id IN :ids", Product.class)
	            .setParameter("ids", categoryIds)
	            .setFirstResult((page - 1) * size)
	            .setMaxResults(size)
	            .list();
	}
	/**
     * 計算指定分類 ID 列表的商品總數。
     *
     * @param categoryIds 分類 ID 列表
     * @return 商品數量；若 categoryIds 為 null 則回傳 0
     */
	@Override
	public int countByCategoryIds(List<Long> categoryIds) {
	    if (categoryIds == null) {
	        return 0;
	    }
	    Long count = getCurrentSession()
	            .createQuery("SELECT COUNT(p.id) FROM Product p WHERE p.category.id IN :ids", Long.class)
	            .setParameter("ids", categoryIds)
	            .uniqueResult();
	    return count != null ? count.intValue() : 0;
	}
	/**
     * 根據關鍵字搜尋商品名稱並分頁。
     *
     * @param keyword 搜尋關鍵字
     * @param page 當前頁碼（從 1 開始）
     * @param size 每頁顯示筆數
     * @return 符合條件的商品列表；若 keyword 為 null 或空字串則查詢所有商品
     */
	@Override
	public List<Product> searchProductsByName(String keyword, int page, int size){
		if (keyword == null || keyword.trim().isEmpty()) {
            return getCurrentSession()
            		.createQuery("FROM Product", Product.class)
                    .getResultList();
        }

        return getCurrentSession()
        		.createQuery("FROM Product p WHERE LOWER(p.name) LIKE :keyword", Product.class)
                .setParameter("keyword", "%" + keyword.toLowerCase() + "%")
                .setFirstResult((page - 1) * size)
	            .setMaxResults(size)
                .getResultList();
    }
	/**
     * 計算符合關鍵字的商品數量。
     *
     * @param keyword 搜尋關鍵字
     * @return 商品數量
     */
	@Override
	public int countProductsByName(String keyword) {
	    Long count = getCurrentSession()
	            .createQuery("SELECT COUNT(p.id) FROM Product p WHERE LOWER(p.name) LIKE LOWER(:keyword)", Long.class)
	            .setParameter("keyword", "%" + keyword + "%")
	            .uniqueResult();
	    return count != null ? count.intValue() : 0;
	}
	/**
     * 扣減指定商品的庫存。
     *
     * <p>僅在庫存大於等於扣減數量時才會執行扣減操作。</p>
     *
     * @param productId 商品 ID
     * @param quantity 扣減數量
     * @return {@code true} 扣減成功，{@code false} 扣減失敗（庫存不足）
     */
	@Override
	public boolean reduceStock(Long productId, int quantity) {
	    String hql = "UPDATE Product p SET p.stock = p.stock - :qty " +
	                 "WHERE p.id = :id AND p.stock >= :qty";
	    int updated = getCurrentSession()
	                  .createQuery(hql)
	                  .setParameter("qty", quantity)
	                  .setParameter("id", productId)
	                  .executeUpdate();

	    return updated > 0; // >0 表示扣成功
	}
}
