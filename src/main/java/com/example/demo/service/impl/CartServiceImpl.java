package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.CartDAO;
import com.example.demo.dao.ProductDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
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
    private UserDAO userDAO;

    @Override
    public void addToCart(Long userId, Long productId, int quantity) {
        Cart cart = cartDAO.findByUserId(userId)
                .orElseGet(() -> createCartForUser(userId));

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
    public BigDecimal getCartTotal(Long userId) {
        Cart cart = cartDAO.findByUserId(userId).orElseThrow();
        return cart.getItems().stream()
                .peek(CartItem::updateSubtotal) // 確保 subtotal 更新
                .map(CartItem::getSubtotal)     // 取得 BigDecimal
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    @Override
    public void removeItem(Long userId, Long productId) {
        Cart cart = cartDAO.findByUserId(userId).orElseThrow();
        cart.getItems().removeIf(i -> i.getProduct().getId().equals(productId));
        cartDAO.save(cart);
    }

    @Override
    public void updateQuantity(Long userId, Long productId, int quantity) {
        Cart cart = cartDAO.findByUserId(userId).orElseThrow();
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(quantity);
                item.updateSubtotal();
            }
        }
        cartDAO.save(cart);
    }

    @Override
    public Cart createCartForUser(Long userId) {
        // 取得 User，若找不到拋例外
        User user = userDAO.findById(userId);

        Cart cart = new Cart();
        cart.setUser(user);

        // 儲存 Cart
        cartDAO.save(cart);

        return cart;
    }
    
    @Override
    public void clearCart(Long userId) {
        Cart cart = cartDAO.findByUserId(userId).orElseThrow();
        cart.getItems().clear();
        cartDAO.save(cart);
    }
    @Override
    public Cart getCartByUserId(Long userId) {
        Cart cart = cartDAO.findByUserId(userId).orElseGet(() -> createCartForUser(userId));
        cart.getItems().size(); // 強制初始化 items list，避免 lazy load 問題
        return cart;
    }

}