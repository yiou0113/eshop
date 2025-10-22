package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.CartDAO;
import com.example.demo.dao.OrderDAO;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.service.OrderService;
/**
 * OrderService 的實作類別
 *
 * 此類別主要負責處理訂單的業務邏輯：
 * - 從購物車建立訂單
 * - 查詢、儲存訂單
 * - 根據顧客 ID 查詢訂單
 *
 * 透過 @Transactional 確保方法在資料庫操作時具有事務一致性。
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private CartDAO cartDAO;
	
	/**
     * 根據顧客 ID 建立新訂單。
     *
     * 此方法會：
     * 1. 讀取顧客的購物車。
     * 2. 檢查購物車是否為空。
     * 3. 將購物車內的商品轉換為訂單項目。
     * 4. 計算總金額並儲存訂單。
     * 5. 清空購物車。
     *
     * @param customerId 顧客的唯一識別碼
     * @return 建立完成的訂單物件
     * @throws RuntimeException 當購物車為空或不存在時拋出
     */
	public Order createOrder(Long customerId) {
		try {
            Optional<Cart> optionalCart = cartDAO.findByCustomerId(customerId);
            Cart cart = optionalCart.orElse(null); // 若找不到則為 null

            if (cart == null || cart.getItems().isEmpty()) {
                logger.warn("顧客 ID {} 的購物車為空，無法建立訂單。", customerId);
                throw new RuntimeException("購物車為空");
            }

            Order order = new Order();
            order.setCustomer(cart.getCustomer());
            order.setStatus("待付款");

            BigDecimal total = BigDecimal.ZERO; // BigDecimal 為物件型別，用於精確金額計算

            // 將購物車項目轉為訂單項目
            for (CartItem cartItem : cart.getItems()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPrice(cartItem.getPrice());
                orderItem.updateSubtotal(); // 小計 = 單價 × 數量

                total = total.add(orderItem.getSubtotal()); // 累加總金額
                order.getItems().add(orderItem);
            }

            order.setTotalAmount(total);
            orderDAO.save(order);
            logger.info("成功建立訂單（ID：{}），總金額：{}", order.getId(), total);

            // 清空購物車
            cart.getItems().clear();
            cartDAO.save(cart);
            logger.info("顧客 ID {} 的購物車已清空。", customerId);

            return order;
        } catch (Exception e) {
            logger.error("建立訂單時發生錯誤（顧客 ID {}）: {}", customerId, e.getMessage(), e);
            throw e; // 再拋出讓上層統一處理
        }
	}
	/**
     * 儲存或更新訂單資料。
     *
     * @param order 要儲存的訂單物件
     */
	@Override
	public void saveOrder(Order order) {
		orderDAO.save(order);
	}
	/**
     * 根據訂單 ID 查詢訂單。
     *
     * 若有 Lazy Loading（延遲載入），
     * 會在此強制載入 orderItems 以避免 LazyInitializationException。
     *
     * @param id 訂單 ID
     * @return Order 物件，若查無則回傳 null
     */
	@Override
	public Order getOrderById(Long id) {
		Order order = orderDAO.findById(id);
        if (order != null) {
            order.getItems().size(); // 強制載入關聯項目
            logger.info("成功查詢訂單，ID：{}", id);
        } else {
            logger.warn("查無訂單，ID：{}", id);
        }
        return order;
	}
	/**
     * 查詢所有訂單。
     *
     * @return 所有訂單列表
     */
	@Override
	public List<Order> getAllOrders() {
		List<Order> orders = orderDAO.findAll();
        logger.info("已查詢所有訂單，共 {} 筆。", orders.size());
        return orders;
	}
	/**
     * 根據顧客 ID 查詢訂單。
     *
     * 若查無訂單，則回傳空集合而非 null。
     *
     * @param customerId 顧客 ID
     * @return 該顧客的訂單列表
     */
	@Override
	public List<Order> getOrderByCustomerId(Long customerId) {
		List<Order> orders = orderDAO.findByCustomerId(customerId);
        if (orders == null || orders.isEmpty()) {
            logger.info("顧客 ID {} 尚無訂單紀錄。", customerId);
            return List.of(); // Java 9+：更節省記憶體的不可變空集合
        }
        logger.info("查詢到顧客 ID {} 的訂單，共 {} 筆。", customerId, orders.size());
        return orders;
	}

}
