package com.example.demo.dao.impl;

import java.util.Optional;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.example.demo.dao.CartDAO;
import com.example.demo.model.Cart;

/**
 * CartDAO 的實作類別
 *
 * 此類別負責與資料庫互動，透過 Hibernate Session 進行購物車（Cart）的 CRUD 操作。
 *
 * 功能包含： 
 * - 根據顧客 ID 查詢購物車 
 * - 儲存或更新購物車 
 * - 刪除購物車
 * - 根據購物車 ID 與商品 ID 刪除特定商品
 */
@Repository
public class CartDAOImpl extends BaseDAOImpl<Cart> implements CartDAO {
	/** Logger，用於紀錄操作資訊與錯誤訊息 */
	private static final Logger logger = LoggerFactory.getLogger(CartDAOImpl.class);

	/**
	 * 根據顧客 ID 查詢購物車
	 *
	 * @param customerId 顧客唯一識別碼
	 * @return Optional<Cart> 購物車物件，若找不到或發生例外則回傳 Optional.empty()
	 */
	@Override
	public Optional<Cart> findByCustomerId(Long customerId) {
		try {
			Cart cart = getCurrentSession()
					.createQuery("FROM Cart c WHERE c.customer.id = :customerId", Cart.class)
					.setParameter("customerId", customerId)
					.uniqueResult();
			logger.info("查詢顧客 ID {} 的購物車", customerId);
			return Optional.ofNullable(cart);
		} catch (Exception e) {
			logger.error("查詢顧客 ID {} 的購物車時發生錯誤: {}", customerId, e.getMessage(), e);
			return Optional.empty();
		}
	}

	/**
    * 根據購物車 ID 與商品 ID 刪除購物車中的特定商品
    *
    * @param cartId 購物車 ID
    * @param productId 商品 ID
    * @throws RuntimeException 若刪除過程發生例外則拋出
    */
	@Override
	public void deleteByCartIdAndProductId(Long cartId, Long productId) {
		try {
	        Session session = getCurrentSession();
	        int deletedCount = session
	        		.createQuery("DELETE FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.product.id = :productId")
	                .setParameter("cartId", cartId)
	                .setParameter("productId", productId)
	                .executeUpdate();
	        logger.info("刪除購物車 {} 中商品 {}，共刪除 {} 筆", cartId, productId, deletedCount);
	    } catch (Exception e) {
	        logger.error("刪除購物車 {} 中商品 {} 時發生錯誤: {}", cartId, productId, e.getMessage(), e);
	        throw new RuntimeException("刪除購物車商品失敗", e);
	    }
    }
}
