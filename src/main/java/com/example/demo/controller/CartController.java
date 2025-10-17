package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.model.Customer;
import com.example.demo.model.User;
import com.example.demo.service.CartService;
import com.example.demo.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private CustomerService customerSecvice;
    // 顯示購物車
    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; // 未登入回 login
        }
    	Customer customer = customerSecvice.findByUserId(user.getId());
    	if (customer == null) {
            return "redirect:/login";
        }
    	Cart cart = cartService.getCartByCustomerId(customer.getId());
        if (cart == null) {
            cart = new Cart();
        }
        model.addAttribute("cart", cart);
        model.addAttribute("total", cartService.getCartTotal(customer.getId()));
        return "cart";
    }


   /* @PostMapping("/add")
    public String addToCart(@RequestParam("userId") Long userId,
                            @RequestParam("productId") Long productId,
                            @RequestParam("quantity") int quantity) {
        cartService.addToCart(userId, productId, quantity);
        return "redirect:/cart?userId=" + userId;
    }*/
    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam int quantity,
                            HttpSession session) {
    	User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        Customer customer = customerSecvice.findByUserId(user.getId());
    	if (customer == null) {
            return "redirect:/login";
        }
        cartService.addToCart(customer.getId(), productId, quantity);
        return "redirect:/products";
    }

    // 更新商品數量
    @PostMapping("/update")
    public String updateQuantity(@RequestParam("productId") Long productId,
                                 @RequestParam("quantity") int quantity,
                                 HttpSession session) {
    	User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        Customer customer = customerSecvice.findByUserId(user.getId());
    	if (customer == null) {
            return "redirect:/login";
        }
        cartService.updateQuantity(customer.getId(), productId, quantity);
        return "redirect:/cart?userId=" + customer.getId();
    }

    // 刪除商品
    @PostMapping("/remove")
    public String removeItem(@RequestParam("productId") Long productId,
                             HttpSession session) {
    	User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";
        Customer customer = customerSecvice.findByUserId(user.getId());
    	if (customer == null) {
            return "redirect:/login";
        }
        cartService.removeItem(customer.getId(), productId);
        return "redirect:/cart?userId=" + customer.getId();
    }
}
