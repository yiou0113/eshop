package com.example.demo.dao.impl;

import com.example.demo.dao.ProductDAO;
import com.example.demo.model.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ProductDAO 的實作類別
 *
 * 此類別負責與資料庫進行直接互動， 透過 Hibernate 的 Session 物件執行商品（Product）的各種 CRUD 操作。
 *
 * 功能包含：
 *  - 查詢所有商品
 *  - 根據 ID 查詢商品
 *  - 新增或更新商品 
 *  - 刪除商品 
 *  - 分頁查詢商品列表
 */
@Repository
public class ProductDAOImpl extends BaseDAOImpl<Product> implements ProductDAO {

	@Override
	public List<Product> findPaginated(int page, int size) {
		return getCurrentSession()
				.createQuery("FROM Product", Product.class)
				.setFirstResult((page - 1) * size) // 計算從哪一筆開始
				.setMaxResults(size) // 每頁顯示多少筆
				.list();
	}
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
	@Override
	public int countProductsByName(String keyword) {
	    Long count = getCurrentSession()
	            .createQuery("SELECT COUNT(p.id) FROM Product p WHERE LOWER(p.name) LIKE LOWER(:keyword)", Long.class)
	            .setParameter("keyword", "%" + keyword + "%")
	            .uniqueResult();
	    return count != null ? count.intValue() : 0;
	}
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
