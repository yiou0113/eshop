package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.CartDAO;
import com.example.demo.dao.CustomerDAO;
import com.example.demo.dao.ProductDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Customer;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.service.CartService;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private CartDAO cartDAO;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private CustomerDAO customerDAO;

    @Override
    public void addToCart(Long customerId, Long productId, int quantity) {
        Cart cart = cartDAO.findByCustomerId(customerId)
                .orElseGet(() -> createCartForUser(customerId));

        Product product = productDAO.findById(productId);
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
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

    @Override
    public BigDecimal getCartTotal(Long customerId) {
        Cart cart = cartDAO.findByCustomerId(customerId).orElseThrow();
        return cart.getItems().stream()
                .peek(CartItem::updateSubtotal) // 確保 subtotal 更新
                .map(CartItem::getSubtotal)     // 取得 BigDecimal
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    @Override
    public void removeItem(Long customerId, Long productId) {
        Cart cart = cartDAO.findByCustomerId(customerId).orElseThrow();
        cart.getItems().removeIf(i -> i.getProduct().getId().equals(productId));
        cartDAO.save(cart);
    }

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

    @Override
    public Cart createCartForUser(Long customerId) {
        // 取得 Customer，若找不到拋例外
        Customer customer = customerDAO.findById(customerId);

        Cart cart = new Cart();
        cart.setCustomer(customer);

        // 儲存 Cart
        cartDAO.save(cart);

        return cart;
    }
    
    @Override
    public void clearCart(Long customerId) {
        Cart cart = cartDAO.findByCustomerId(customerId).orElseThrow();
        cart.getItems().clear();
        cartDAO.save(cart);
    }
    @Override
    public Cart getCartByCustomerId(Long customerId) {
        Cart cart = cartDAO.findByCustomerId(customerId).orElseGet(() -> createCartForUser(customerId));
        cart.getItems().size(); // 強制初始化 items list，避免 lazy load 問題
        return cart;
    }

}