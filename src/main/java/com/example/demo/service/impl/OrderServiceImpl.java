package com.example.demo.service.impl;

import java.math.BigDecimal;
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
    private OrderDAO orderDao;

    @Autowired
    private CartDAO cartDAO;

    public Order createOrder(Long userId) {
    	Optional<Cart> optionalCart = cartDAO.findByUserId(userId);
    	Cart cart = optionalCart.orElse(null); // 若找不到回傳 null

         
        if (cart == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("購物車為空");
        }

        Order order = new Order();
        order.setUser(cart.getUser());
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
        orderDao.save(order);

        // 清空購物車
        cart.getItems().clear();
        cartDAO.save(cart);

        return order;
    }
}
