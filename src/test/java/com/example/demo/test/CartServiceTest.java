package com.example.demo.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.example.demo.config.WebMvcConfig;
import com.example.demo.model.Cart;
import com.example.demo.service.CartService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebMvcConfig.class })
@WebAppConfiguration
public class CartServiceTest {

	/*
	 * @Autowired private CartService cartService;
	 * 
	 * @Test public void testCreateCartForCustomer() { Long customerId = 4L; // 假設
	 * DB 有此客戶 Cart cart = cartService.createCartForCustomer(customerId);
	 * assertNotNull("購物車建立失敗", cart); assertEquals(customerId,
	 * cart.getCustomer().getId()); }
	 * 
	 * @Test public void testAddToCart() { Long customerId = 4L; Long productId =
	 * 5L; int quantity = 2;
	 * 
	 * cartService.addToCart(customerId, productId, quantity); Cart cart =
	 * cartService.getCartByCustomerId(customerId); assertNotNull(cart);
	 * assertFalse("購物車應該不為空", cart.getItems().isEmpty());
	 * 
	 * boolean isEmpty = cartService.isCartEmpty(customerId); assertFalse(isEmpty);
	 * }
	 * 
	 * @Test public void testGetCartTotal() { Long customerId = 4L; BigDecimal total
	 * = cartService.getCartTotal(customerId); assertNotNull(total);
	 * assertTrue("購物車總價應大於等於0", total.compareTo(BigDecimal.ZERO) >= 0); }
	 * 
	 * @Test public void testRemoveItem() { Long customerId = 4L; Long productId =
	 * 5L;
	 * 
	 * cartService.removeItem(customerId, productId); Cart cart =
	 * cartService.getCartByCustomerId(customerId); if (cart != null) {
	 * cart.getItems().forEach(item -> assertNotEquals(productId,
	 * item.getProduct().getId())); } }
	 * 
	 * @Test public void testUpdateQuantity() { Long customerId = 4L; Long productId
	 * = 5L; int quantity = 2; cartService.addToCart(customerId, productId,
	 * quantity); int newQuantity = 5;
	 * 
	 * cartService.updateQuantity(customerId, productId, newQuantity); Cart cart =
	 * cartService.getCartByCustomerId(customerId); cart.getItems().forEach(item ->
	 * { if (item.getProduct().getId().equals(productId)) {
	 * assertEquals(newQuantity, item.getQuantity()); } }); }
	 * 
	 * @Test public void testClearCart() { Long customerId = 4L;
	 * cartService.clearCart(customerId);
	 * 
	 * Cart cart = cartService.getCartByCustomerId(customerId); assertTrue(cart ==
	 * null || cart.getItems().isEmpty()); }
	 * 
	 * @Test public void testIsCartEmpty() { Long customerId = 4L; boolean empty =
	 * cartService.isCartEmpty(customerId); assertTrue(empty || !empty); //
	 * 只要能正確呼叫方法 }
	 * 
	 * @Test public void testAddToCartWithoutUser() { Long productId = 3L; int
	 * quantity = 1;
	 * 
	 * boolean result = cartService.checkStockBeforeAdd(productId, quantity);
	 * assertTrue(result || !result); // 只驗證方法可執行，回傳 boolean }
	 */
}
