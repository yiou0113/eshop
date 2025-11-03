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


/**
 * 訂單相關的 Controller
 *
 * 此控制器負責處理顧客訂單的瀏覽、建立、付款或取消與詳細頁面。
 * 包含：
 * - 查看顧客所有訂單
 * - 由購物車結帳建立新訂單
 * - 查看單一訂單詳情
 * - 訂單成立付款
 * - 訂單取消
 */
@Controller
@RequestMapping("/orders")
public class OrderController {
	
	/** 注入 OrderService，用於操作訂單相關業務邏輯 */
    @Autowired
    private OrderService orderService;
    
    /** 注入 CustomerService，用於取得顧客資訊 */
    @Autowired
    private CustomerService customerService;
    
    /**
     * 顯示顧客所有訂單
     * 若使用者為登入導回登入畫面
     *
     * @param session HttpSession，用於取得已登入使用者
     * @param model Spring MVC Model，用於傳遞資料到模板
     * @return 返回訂單頁面
     */
    @GetMapping
    public String viewOrders(HttpSession session, Model model) {
    	User user = (User) session.getAttribute("loggedInUser");
        Customer customer = customerService.getCustomerByUserId(user.getId());
        List<Order> orders = orderService.getOrderByCustomerId(customer.getId());
        if (orders.isEmpty()) {
            model.addAttribute("message", "尚未成立訂單");
        } else {
            model.addAttribute("orders", orders);
        }
        return "order-list";
    }

    /**
     * 由購物車結帳建立新訂單
     * 建立訂單時確認商品庫存並
     *
     * @param selectedProductIds 用於取得以選取商品ID
     * @param session HttpSession，用於取得已登入使用者
     * @param model Spring MVC Model，用於傳遞資料到模板
     * @return 返回成功頁面
     */
    @PostMapping("/create")
    public String createOrder(@RequestParam(value="selectedProducts", required=false) List<Long> selectedProductIds,HttpSession session, Model model) {
    	User user = (User) session.getAttribute("loggedInUser");
        Customer customer = customerService.getCustomerByUserId(user.getId());

        // 如果沒有選擇任何商品
        if (selectedProductIds == null || selectedProductIds.isEmpty()) {
            model.addAttribute("error", "請至少選擇一個商品建立訂單！");
            return "cart";
        }

        try {
            String customerName = customer.getName();

            // 呼叫 Service 建立訂單，只傳入被勾選的商品
            Order order = orderService.createOrder(customer.getId(), selectedProductIds);

            model.addAttribute("customerName", customerName);
            model.addAttribute("order", order);
            return "redirect:/orders/" + order.getId();
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "cart"; // 回到購物車頁面顯示錯誤
        }
    }
    
    /**
     * 查看單一訂單詳細資訊
     *
     *
     * @param id 訂單 ID
     * @param session HttpSession，用於取得已登入使用者
     * @param model Spring MVC Model，用於傳遞資料到模板
     * @return 返回訂單詳情頁面 "order-detail"，或訂單不屬於該客戶時導向 "/orders"
     */
    @GetMapping("/{id}")
    public String viewOrderDetail(@PathVariable("id") Long id, HttpSession session, Model model) {
    	User user = (User) session.getAttribute("loggedInUser");
        Customer customer = customerService.getCustomerByUserId(user.getId());
        Order order = orderService.getOrderById(id);
        if (order == null || !order.getCustomer().getId().equals(customer.getId())) {
            return "redirect:/orders";
        }
        
        model.addAttribute("order", order);
        return "order-detail";
    }
    /**
     * 對訂單進行付款
     * @param id 訂單 ID
     * @param session HttpSession，用於取得已登入使用者
     * @param model Spring MVC Model，用於傳遞資料到模板
     * @return 訂單成立後返回訂單列表
     */
    @PostMapping("/{id}/pay")
    public String payOrder(@PathVariable("id") Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        Customer customer = customerService.getCustomerByUserId(user.getId());
        Order order = orderService.getOrderById(id);

        if (order == null || !order.getCustomer().getId().equals(customer.getId())) {
            return "redirect:/orders";
        }

        // 模擬付款成功
        orderService.payOrder(order.getId());

        model.addAttribute("order", order);
        model.addAttribute("message", "付款成功！");
        return "redirect:/orders";
    }
    /**
     * 對訂單進行取消
     * @param id 訂單 ID
     * @param session HttpSession，用於取得已登入使用者
     * @param model Spring MVC Model，用於傳遞資料到模板
     * @return 訂單取消後返回訂單列表
     */
    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable("id") Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        Customer customer = customerService.getCustomerByUserId(user.getId());
        Order order = orderService.getOrderById(id);

        if (order == null || !order.getCustomer().getId().equals(customer.getId())) {
            return "redirect:/orders";
        }

        orderService.cancelOrder(order.getId());
        model.addAttribute("message", "訂單已取消");
        return "redirect:/orders";
    }
}
