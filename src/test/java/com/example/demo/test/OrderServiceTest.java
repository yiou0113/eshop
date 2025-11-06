package com.example.demo.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.example.demo.config.WebMvcConfig;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebMvcConfig.class })
@WebAppConfiguration
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void testCreateOrder() {
        assertNotNull("OrderService 未注入", orderService);

        Long customerId = 1L; // 假設 DB 有這個客戶
        List<Long> productIds = Arrays.asList(1L, 2L); // 假設 DB 有這些商品

        Order order = orderService.createOrder(customerId, productIds);
        assertNotNull("訂單建立失敗", order);
        assertEquals(customerId, order.getCustomer().getId());
        assertEquals(2, order.getItems().size());
    }

    @Test
    public void testSaveOrder() {
        Order order = new Order();
        // 設定必要欄位
        orderService.saveOrder(order);
        assertNotNull("訂單儲存失敗", order.getId());

        Order savedOrder = orderService.getOrderById(order.getId());
        assertNotNull(savedOrder);
    }

    @Test
    public void testGetOrderById() {
        Order order = orderService.getOrderById(1L); // 假設 DB 有這個訂單
        assertNotNull("訂單不存在", order);
        assertEquals(1L, order.getId().longValue());
    }

    @Test
    public void testGetAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        assertNotNull("訂單列表為空", orders);
        assertTrue("訂單列表應至少有 1 筆", orders.size() > 0);
    }

    @Test
    public void testGetOrderByCustomerId() {
        Long customerId = 1L;
        List<Order> orders = orderService.getOrderByCustomerId(customerId);
        assertNotNull("查無訂單", orders);
        for (Order order : orders) {
            assertEquals(customerId, order.getCustomer().getId());
        }
    }

    @Test
    public void testCancelOrder() {
        Long orderId = 1L; // 假設 DB 有這個訂單
        orderService.cancelOrder(orderId);

        Order order = orderService.getOrderById(orderId);
        assertEquals("CANCELLED", order.getStatus()); // 假設取消後狀態是 CANCELLED
    }

    @Test
    public void testPayOrder() {
        Long orderId = 2L; // 假設 DB 有這個訂單
        orderService.payOrder(orderId);

        Order order = orderService.getOrderById(orderId);
        assertEquals("PAID", order.getStatus()); // 假設付款後狀態是 PAID
    }
}
