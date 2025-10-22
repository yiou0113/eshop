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
 * 此控制器負責處理顧客訂單的瀏覽、建立與詳細頁面。
 * 包含：
 * - 查看顧客所有訂單
 * - 由購物車結帳建立新訂單
 * - 查看單一訂單詳情
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
     *
     * 1. 從 HttpSession 取得已登入的使用者
     * 2. 根據使用者 ID 取得對應的顧客資料
     * 3. 查詢該顧客的所有訂單
     * 4. 若無訂單則在 model 中加入提示訊息
     * 5. 將訂單列表或訊息放入 Model 傳給前端模板
     *
     * @param session HttpSession，用於取得已登入使用者
     * @param model Spring MVC Model，用於傳遞資料到模板
     * @return 返回訂單列表頁面 "order-list"
     */
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

    /**
     * 由購物車結帳建立新訂單
     *
     * 1. 從 HttpSession 取得已登入使用者
     * 2. 根據使用者 ID 取得顧客資料
     * 3. 呼叫 OrderService 建立訂單
     * 4. 若建立成功，將訂單與顧客名稱放入 Model，返回成功頁面
     * 5. 若建立失敗（例如購物車為空），將錯誤訊息放入 Model，返回購物車頁面
     *
     * @param session HttpSession，用於取得已登入使用者
     * @param model Spring MVC Model，用於傳遞資料到模板
     * @return 成功返回 "order-success"，失敗返回 "cart"
     */
    @PostMapping("/create")
    public String createOrder(HttpSession session, Model model) {
    	User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        Customer customer = customerService.getCustomerByUserId(user.getId());

        try {
        	String customerName = customer.getName();
            Order order = orderService.createOrder(customer.getId());
            model.addAttribute("customerName", customerName);
            model.addAttribute("order", order);
            return "order-success"; // 成功頁面
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "cart"; // 回到購物車頁面顯示錯誤
        }
    }

    /**
     * 查看單一訂單詳細資訊
     *
     * 1. 從 HttpSession 取得已登入使用者
     * 2. 根據使用者 ID 取得顧客資料
     * 3. 根據訂單 ID 取得訂單資訊
     * 4. 驗證該訂單是否屬於該顧客，若不屬於，導向訂單列表頁
     * 5. 將訂單資料放入 Model，返回訂單詳情頁面
     *
     * @param id 訂單 ID
     * @param session HttpSession，用於取得已登入使用者
     * @param model Spring MVC Model，用於傳遞資料到模板
     * @return 返回訂單詳情頁面 "order-detail"，或訂單不屬於該客戶時導向 "/orders"
     */
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
