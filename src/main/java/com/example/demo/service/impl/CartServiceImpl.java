package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.CartDAO;
import com.example.demo.dao.CustomerDAO;
import com.example.demo.dao.ProductDAO;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Customer;
import com.example.demo.model.Product;
import com.example.demo.service.CartService;

/**
 * CartService 的實作類別
 *
 * 此類別負責處理購物車相關的業務邏輯： - 新增商品至購物車 - 計算購物車總金額 - 移除購物車商品 - 更新商品數量 - 清空購物車 - 根據顧客 ID
 * - 取得購物車
 */
@Service
@Transactional
public class CartServiceImpl implements CartService {
	@Autowired
	private CartDAO cartDAO;

	@Autowired
	private ProductDAO productDAO;

	@Autowired
	private CustomerDAO customerDAO;

	/**
	 * 將商品加入購物車
	 *
	 * 若購物車不存在，會自動建立。 若商品已存在，則累加數量並更新小計。
	 *
	 * @param customerId 顧客 ID
	 * @param productId  商品 ID
	 * @param quantity   數量
	 */
	@Override
	public void addToCart(Long customerId, Long productId, int quantity) {
		Cart cart = cartDAO.findByCustomerId(customerId).orElseGet(() -> createCartForCustomer(customerId));

		Product product = productDAO.findById(productId);
		Optional<CartItem> existingItem = cart.getItems().stream().filter(i -> i.getProduct().getId().equals(productId))
				.findFirst();

		if (existingItem.isPresent()) {
			CartItem item = existingItem.get();
			item.setQuantity(item.getQuantity() + quantity);
			item.updateSubtotal();
		} else {
			CartItem item = new CartItem();
			item.setCart(cart);
			item.setProduct(product);
			item.setPrice(product.getPrice());
			item.setQuantity(quantity);
			item.updateSubtotal();
			cart.getItems().add(item);
		}

		cartDAO.save(cart);
	}

	/**
	 * 計算購物車總金額
	 *
	 * @param customerId 顧客 ID
	 * @return 購物車總金額（BigDecimal）
	 */
	@Override
	public BigDecimal getCartTotal(Long customerId) {
		Cart cart = cartDAO.findByCustomerId(customerId).orElseThrow();
		return cart.getItems().stream()
			    .map(CartItem::getSubtotal)
			    .reduce(BigDecimal.ZERO, BigDecimal::add);

	}

	/**
	 * 移除購物車中指定商品
	 *
	 * @param customerId 顧客 ID
	 * @param productId  商品 ID
	 */
	@Override
	public void removeItem(Long customerId, Long productId) {
		Cart cart = cartDAO.findByCustomerId(customerId).orElseThrow(() -> new RuntimeException("購物車不存在"));
		cartDAO.deleteByCartIdAndProductId(cart.getId(), productId);

	}

	/**
	 * 更新購物車中指定商品的數量
	 *
	 * @param customerId 顧客 ID
	 * @param productId  商品 ID
	 * @param quantity   新的數量
	 */
	@Override
	public void updateQuantity(Long customerId, Long productId, int quantity) {
		Cart cart = cartDAO.findByCustomerId(customerId).orElseThrow();
		for (CartItem item : cart.getItems()) {
			if (item.getProduct().getId().equals(productId)) {
				item.setQuantity(quantity);
				item.updateSubtotal();
			}
		}
		cartDAO.save(cart);
	}

	/**
	 * 為顧客建立新的購物車
	 *
	 * @param customerId 顧客 ID
	 * @return 建立完成的購物車
	 */
	@Override
	public Cart createCartForCustomer(Long customerId) {
		return cartDAO.findByCustomerId(customerId).orElseGet(() -> {
			Customer customer = customerDAO.findById(customerId);
			Cart cart = new Cart();
			cart.setCustomer(customer);
			cartDAO.save(cart);
			return cart;
		});
	}

	/**
	 * 清空購物車
	 *
	 * @param customerId 顧客 ID
	 */
	@Override
	public void clearCart(Long customerId) {
		Cart cart = cartDAO.findByCustomerId(customerId).orElseThrow();
		cart.getItems().clear();
		cartDAO.save(cart);
	}

	/**
	 * 根據顧客 ID 取得購物車
	 *
	 * 若購物車不存在，會自動建立。 同時強制初始化 items list 避免 lazy load 問題。
	 *
	 * @param customerId 顧客 ID
	 * @return 購物車物件
	 */
	@Override
	public Cart getCartByCustomerId(Long customerId) {
		Cart cart = createCartForCustomer(customerId);
	    cart.getItems().size(); // 初始化 items
	    return cart;
	}


	/**
	 * 判斷購物車是否為空
	 * 
	 * @param customerId 顧客Id
	 * @return true或false
	 */
	@Override
	public boolean isCartEmpty(Long customerId) {
		Cart cart = getCartByCustomerId(customerId);
		return cart.getItems().isEmpty();
	}

	/**
	 * 判斷商品庫存量是否足夠購買
	 * 
	 * @return true或false
	 */
	@Override
	public boolean checkStockBeforeAdd(Long productId, int quantity) {
		Product product = productDAO.findById(productId);
		if (product == null || quantity > product.getStock()) {
			return false; // 超過庫存或商品不存在
		}

		return true;
	}

}