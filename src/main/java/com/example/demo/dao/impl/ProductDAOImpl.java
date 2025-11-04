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
	@Override
	public List<Product> searchProductsByNameAndCategoryWithPage(String keyword, List<Long> categoryIds, int page, int pageSize) {
	    StringBuilder hql = new StringBuilder("FROM Product p WHERE 1=1");

	    if (keyword != null && !keyword.trim().isEmpty()) {
	        hql.append(" AND p.name LIKE :keyword");
	    }
	    if (categoryIds != null && !categoryIds.isEmpty()) {
	        hql.append(" AND p.category.id IN :categoryIds");
	    }

	    var query = sessionFactory.getCurrentSession().createQuery(hql.toString(), Product.class);

	    if (keyword != null && !keyword.trim().isEmpty()) {
	        query.setParameter("keyword", "%" + keyword.trim() + "%");
	    }
	    if (categoryIds != null && !categoryIds.isEmpty()) {
	        query.setParameterList("categoryIds", categoryIds);
	    }

	    query.setFirstResult((page - 1) * pageSize);
	    query.setMaxResults(pageSize);
	    return query.getResultList();
	}

	@Override
	public int countProductsByNameAndCategory(String keyword, List<Long> categoryIds) {
	    StringBuilder hql = new StringBuilder("SELECT COUNT(p) FROM Product p WHERE 1=1");

	    if (keyword != null && !keyword.trim().isEmpty()) {
	        hql.append(" AND p.name LIKE :keyword");
	    }
	    if (categoryIds != null && !categoryIds.isEmpty()) {
	        hql.append(" AND p.category.id IN :categoryIds");
	    }

	    var query = sessionFactory.getCurrentSession().createQuery(hql.toString(), Long.class);

	    if (keyword != null && !keyword.trim().isEmpty()) {
	        query.setParameter("keyword", "%" + keyword.trim() + "%");
	    }
	    if (categoryIds != null && !categoryIds.isEmpty()) {
	        query.setParameterList("categoryIds", categoryIds);
	    }

	    Long count = query.uniqueResult();
	    return count != null ? count.intValue() : 0;
	}


}
