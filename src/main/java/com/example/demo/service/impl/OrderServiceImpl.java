package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private CartDAO cartDAO;

	public Order createOrder(Long customerId) {
		Optional<Cart> optionalCart = cartDAO.findByCustomerId(customerId);
		Cart cart = optionalCart.orElse(null); // 若找不到回傳 null

		if (cart == null || cart.getItems().isEmpty()) {
			throw new RuntimeException("購物車為空");
		}

		Order order = new Order();
		order.setCustomer(cart.getCustomer());
		order.setStatus("待付款");

		BigDecimal total = BigDecimal.ZERO;

		for (CartItem cartItem : cart.getItems()) {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setPrice(cartItem.getPrice());
			orderItem.updateSubtotal();

			total = total.add(orderItem.getSubtotal());
			order.getItems().add(orderItem);
		}

		order.setTotalAmount(total);
		orderDAO.save(order);

		// 清空購物車
		cart.getItems().clear();
		cartDAO.save(cart);

		return order;
	}

	@Override
	public void saveOrder(Order order) {
		orderDAO.save(order);
	}

	@Override
	public Order findById(Long id) {
		Order order = orderDAO.findById(id);
	    if (order != null) {
	        order.getItems().size(); // 強制載入關聯，避免 LazyInitializationException !!!重要
	    }
	    return order;
	}

	@Override
	public List<Order> getAllOrders() {
		return orderDAO.findAll();
	}

	@Override
	public List<Order> findByCustomerId(Long customerId) {
		List<Order> orders = orderDAO.findByCustomerId(customerId);
		if (orders == null) {
			return List.of(); // 避免回傳 null
		}
		return orders;
	}

}
