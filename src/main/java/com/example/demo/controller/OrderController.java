package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.model.User;
import com.example.demo.model.Customer;
import com.example.demo.service.CustomerService;
import com.example.demo.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;
    
    // 顯示顧客所有訂單
    @GetMapping
    public String viewOrders(HttpSession session, Model model) {
    	User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        Customer customer = customerService.getCustomerByUserId(user.getId());
        List<Order> orders = orderService.getOrderByCustomerId(customer.getId());
        if (orders.isEmpty()) {
            model.addAttribute("message", "尚未成立訂單");
        } else {
            model.addAttribute("orders", orders);
        }
        return "order-list";
    }

    // 建立新訂單（由購物車結帳）
    @PostMapping("/create")
    public String createOrder(HttpSession session, Model model) {
    	User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        Customer customer = customerService.getCustomerByUserId(user.getId());

        try {
            Order order = orderService.createOrder(customer.getId());
            model.addAttribute("order", order);
            return "order-success"; // 成功頁面
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "cart"; // 回到購物車頁面顯示錯誤
        }
    }

    // 查看單一訂單詳情
    @GetMapping("/{id}")
    public String viewOrderDetail(@PathVariable("id") Long id, HttpSession session, Model model) {
    	User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        Customer customer = customerService.getCustomerByUserId(user.getId());

        Order order = orderService.getOrderById(id);
        if (order == null || !order.getCustomer().getId().equals(customer.getId())) {
            return "redirect:/orders";
        }

        model.addAttribute("order", order);
        return "order-detail";
    }
}
