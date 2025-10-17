package com.example.demo.test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import com.example.demo.config.WebMvcConfig;
import com.example.demo.model.Cart;
import com.example.demo.model.User;
import com.example.demo.service.CartService;
import com.example.demo.service.UserService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebMvcConfig.class }) // 包含 Web MVC 配置
@WebAppConfiguration // 必須加，告訴 Spring 測試用 WebApplicationContext
public class CartServiceTest {
   @Autowired
   private CartService cartService;
   @Test
   public void testGetCartByCustomerId() {
       // 確認 userService 被正確注入
       assertTrue("Caerservice 未注入", cartService != null);
       // 執行測試
       Cart cart=cartService.getCartByCustomerId(1L);

       assertNotNull("使用者不存在", cart);
       assertEquals("使用者 ID 不正確", 1, cart.getId().longValue());
   }
}

