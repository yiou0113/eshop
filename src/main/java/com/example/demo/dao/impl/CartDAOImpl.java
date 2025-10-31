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
 * <p>此類別繼承 {@link BaseDAOImpl} 並實作 {@link CartDAO}，負責與資料庫互動，
 * 提供購物車（{@link Cart}）的 CRUD 操作及特定業務查詢。</p>
 *
 * <p>主要功能：</p>
 * <ul>
 *   <li>根據顧客 ID 查詢購物車</li>
 *   <li>儲存或更新購物車</li>
 *   <li>刪除整個購物車</li>
 *   <li>根據購物車 ID 與商品 ID 刪除特定商品</li>
 * </ul>
 *
 */
@Repository
public class CartDAOImpl extends BaseDAOImpl<Cart> implements CartDAO {

    /** Logger，用於紀錄操作資訊與錯誤訊息 */
    private static final Logger logger = LoggerFactory.getLogger(CartDAOImpl.class);

    /**
     * 根據顧客 ID 查詢購物車。
     *
     * <p>通常用於顯示使用者當前購物車內容，或在建立訂單前取得購物車資料。</p>
     *
     * @param customerId 顧客唯一識別碼
     * @return {@link Optional} 包含購物車物件；若找不到或發生例外則回傳 {@link Optional#empty()}
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
     * 根據購物車 ID 與商品 ID 刪除購物車中的特定商品。
     *
     * <p>此方法用於「移除購物車中特定商品」功能。若刪除過程發生例外，會拋出
     * {@link RuntimeException}。</p>
     *
     * @param cartId 購物車 ID
     * @param productId 商品 ID
     * @throws RuntimeException 若刪除過程發生錯誤
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
