package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.CartDAO;
import com.example.demo.dao.OrderDAO;
import com.example.demo.dao.ProductDAO;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.Product;
import com.example.demo.service.OrderService;

/**
 * OrderService 的實作類別
 *
 * 此類別主要負責處理訂單的業務邏輯： - 從購物車建立訂單 - 查詢、儲存訂單 - 根據顧客 ID 查詢訂單
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private CartDAO cartDAO;

	@Autowired
	private ProductDAO productDAO;

	/**
	 * 根據顧客 ID 建立新訂單。
	 *
	 * 此方法會： 1. 讀取顧客的購物車。 2. 檢查購物車是否為空。 3. 判斷商品庫存量是否足夠並加入購物車。 4. 計算總金額並儲存訂單。 5.
	 * 清空購物車。
	 *
	 * @param customerId 顧客ID
	 * @return 建立完成的訂單物件
	 * @throws RuntimeException 當購物車為空或不存在時及庫存量或商品不存在時拋出
	 */
	@Override
	public Order createOrder(Long customerId, List<Long> selectedProductIds) {

		Optional<Cart> optionalCart = cartDAO.findByCustomerId(customerId);
		Cart cart = optionalCart.orElse(null); // 若找不到則為 null

		if (cart == null || cart.getItems().isEmpty()) {
			logger.warn("顧客 ID {} 的購物車為空，無法建立訂單。", customerId);
			throw new RuntimeException("購物車為空");
		}
		List<CartItem> selectedItems = cart.getItems().stream()
				.filter(item -> selectedProductIds.contains(item.getProduct().getId())).collect(Collectors.toList());
		if (selectedItems.isEmpty()) {
			logger.warn("[Order Log] 顧客ID {} 未選擇商品建立訂單。", customerId);
			throw new RuntimeException("未選擇任何商品建立訂單");
		}
		Order order = new Order();
		order.setCustomer(cart.getCustomer());
		order.setStatus("待付款");
		BigDecimal total = BigDecimal.ZERO;

		for (CartItem cartItem : selectedItems) {
			Product product = productDAO.findById(cartItem.getProduct().getId());
			if (product == null){
	            logger.error("[Order Log] 顧客ID {} 嘗試下單，但商品ID {} 不存在。", customerId, cartItem.getProduct().getId());
				throw new RuntimeException("商品不存在");
			}
			boolean success = productDAO.reduceStock(cartItem.getProduct().getId(), cartItem.getQuantity());
			if (!success) {
	            logger.warn("[Order Log] 顧客ID {} 嘗試下單，商品 {} 庫存不足。", customerId, product.getName());
				throw new RuntimeException("庫存不足");
			}

			product.setStock(product.getStock() - cartItem.getQuantity());
			productDAO.save(product);

			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setProduct(product);
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setPrice(product.getPrice());
			orderItem.updateSubtotal();
			total = total.add(orderItem.getSubtotal());
			order.getItems().add(orderItem);
		}

		order.setTotalAmount(total);
		orderDAO.save(order);

		cart.getItems().removeIf(item -> selectedProductIds.contains(item.getProduct().getId()));
		cartDAO.save(cart);
		logger.info("[Order Log] 顧客ID {} 成功建立訂單ID {}，商品數量 {}，總價 {}",
	            customerId,
	            order.getId(),
	            order.getItems().size(),
	            order.getTotalAmount());
		
		return order;
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
	 * 若有 Lazy Loading（延遲載入）， 會在此強制載入 orderItems 以避免 LazyInitializationException。
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
			return List.of();
		}
		logger.info("查詢到顧客 ID {} 的訂單，共 {} 筆。", customerId, orders.size());
		return orders;
	}

	/**
	 * 取消訂單
	 * 
	 * @param orderId 訂單ID
	 */
	@Override
	public void cancelOrder(Long orderId) {
		Order order = getOrderById(orderId);
		for(OrderItem item : order.getItems()) {
			Product product = item.getProduct();
			int newStock = product.getStock() + item.getQuantity();
			product.setStock(newStock);
			productDAO.save(product);
		}
		order.setStatus("訂單取消");
		orderDAO.save(order);
		logger.info("[Order Log] 顧客ID {} 訂單ID {} 已取消，總價 {}",
	            order.getCustomer().getId(),
	            order.getId(),
	            order.getTotalAmount());
	}

	/**
	 * 訂單付款
	 * 
	 * @param orderId 訂單ID
	 */
	@Override
	public void payOrder(Long orderId) {
		Order order = getOrderById(orderId);
		order.setStatus("付款完畢");
		orderDAO.save(order); 
		logger.info("[Order Log] 顧客ID {} 訂單ID {} 已付款，總價 {}",
	            order.getCustomer().getId(),
	            order.getId(),
	            order.getTotalAmount());
	}

}
