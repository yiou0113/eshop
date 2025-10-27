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
	public List<Product> findByCategoryId(Long categoryId){
		    return getCurrentSession()
		            .createQuery("FROM Product p WHERE p.category.id = :categoryId", Product.class)
		            .setParameter("categoryId", categoryId)
		            .list();
	}
	@Override
	public List<Product> findByCategoryId(Long categoryId, int page, int size) {
	    if (categoryId == null) {
	        return List.of();
	    }
	    return getCurrentSession()
	            .createQuery("FROM Product p WHERE p.category.id = :id", Product.class)
	            .setParameter("id", categoryId)
	            .setFirstResult((page - 1) * size)
	            .setMaxResults(size)
	            .list();
	}

	@Override
	public int countByCategoryId(Long categoryId) {
	    if (categoryId == null) {
	        return 0;
	    }
	    Long count = getCurrentSession()
	            .createQuery("SELECT COUNT(p.id) FROM Product p WHERE p.category.id = :id", Long.class)
	            .setParameter("id", categoryId)
	            .uniqueResult();
	    return count != null ? count.intValue() : 0;
	}


}
