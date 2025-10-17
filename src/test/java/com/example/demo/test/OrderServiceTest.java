package com.example.demo.test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import com.example.demo.config.WebMvcConfig;
import com.example.demo.model.Order;
import com.example.demo.model.User;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebMvcConfig.class }) // 包含 Web MVC 配置
@WebAppConfiguration // 必須加，告訴 Spring 測試用 WebApplicationContext
public class OrderServiceTest {
   @Autowired
   private OrderService orderService;
   @Test
   public void testGetUserById() {
       // 確認 userService 被正確注入
       assertTrue("OrderService 未注入", orderService != null);
       // 執行測試
       List<Order> order = orderService.findByCustomerId(1L);
       assertNotNull("使用者不存在", order);
   }
}

