package com.example.demo.dao.impl;

import com.example.demo.dao.ProductDAO;
import com.example.demo.model.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ProductDAOImpl implements ProductDAO {
	/** Hibernate 的 SessionFactory，用於取得與資料庫互動的 Session */
	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * 取得當前的 Hibernate Session。
	 *
	 * @return 目前作用中的 Session 物件，用於進行資料庫操作。
	 */
	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 查詢所有商品資料。
	 *
	 * @return List<Product> 所有商品的列表。
	 */
	@Override
	public List<Product> findAll() {
		return getCurrentSession().createQuery("FROM Product", Product.class).list();
	}

	/**
	 * 根據商品 ID 查詢單一商品。
	 *
	 * @param id 商品的唯一識別碼。
	 * @return Product 物件，若查無資料則回傳 null。
	 */
	@Override
	public Product findById(Long id) {
		return getCurrentSession().get(Product.class, id);
	}

	/**
	 * 儲存或更新商品資料。
	 *
	 * 若商品已存在於資料庫中，則執行更新；否則新增新商品。
	 *
	 * @param product 要儲存或更新的商品物件。
	 */
	@Override
	public void save(Product product) {
		getCurrentSession().saveOrUpdate(product);
	}

	/**
	 * 根據商品 ID 刪除該商品。
	 *
	 * 在刪除前會先確認該商品是否存在，避免發生錯誤。
	 *
	 * @param id 要刪除的商品 ID。
	 */
	@Override
	public void delete(Long id) {
		Product product = getCurrentSession().get(Product.class, id);
		if (product != null) {
			getCurrentSession().delete(product);
		}
	}

	/**
	 * 分頁查詢商品列表。
	 *
	 * 使用 Hibernate 的查詢語法（HQL）搭配 `setFirstResult` 與 `setMaxResults` 來實現分頁效果。
	 *
	 * @param page 目前頁碼（從 1 開始計算）。
	 * @param size 每頁要顯示的商品數量。
	 * @return List<Product> 該頁的商品資料列表。
	 */
	@Override
	public List<Product> findPaginated(int page, int size) {
		return getCurrentSession()
				.createQuery("FROM Product", Product.class)
				.setFirstResult((page - 1) * size) // 計算從哪一筆開始
				.setMaxResults(size) // 每頁顯示多少筆
				.list();
	}
}
