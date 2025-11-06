package com.example.demo.test;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import com.example.demo.config.WebMvcConfig;
import com.example.demo.model.User;
import com.example.demo.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebMvcConfig.class })
@WebAppConfiguration
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
	private PasswordEncoder passwordEncoder;
    @Test
    public void testGetAllUsers() {
        assertNotNull("UserService 未注入", userService);
        List<User> users = userService.getAllUsers();
        assertNotNull("使用者列表為空", users);
        assertTrue("使用者列表應該有至少 1 個使用者", users.size() > 0);
    }

    @Test
    public void testGetUserById() {
        User user = userService.getUserById(1L); // 假設 DB 有 ID = 1
        assertNotNull("使用者不存在", user);
        assertEquals(1L, user.getId().longValue());
    }

    @Test
    public void testSaveUser() {
        User newUser = new User();
        newUser.setEmail("test2@example.com");
        newUser.setPassword("encryptedPassword123");

        userService.saveUser(newUser);

        assertNotNull("新增使用者失敗", newUser.getId());
        User savedUser = userService.getUserById(newUser.getId());
        assertEquals("test2@example.com", savedUser.getEmail());
        assertTrue(passwordEncoder.matches("encryptedPassword123", savedUser.getPassword()));
    }

    @Test
    public void testUpdateUser() {
        User user = userService.getUserById(1L);
        String oldPassword = user.getPassword();
        user.setPassword("newEncryptedPassword");

        userService.updateUser(user.getId(), user);

        User updatedUser = userService.getUserById(1L);
        assertEquals("newEncryptedPassword", updatedUser.getPassword());
        assertNotEquals(oldPassword, updatedUser.getPassword());
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setEmail("delete@example.com");
        user.setPassword("toBeDeleted");
        userService.saveUser(user);

        Long id = user.getId();
        assertNotNull(id);

        userService.deleteUser(id);

        User deletedUser = userService.getUserById(id);
        assertNull("使用者刪除失敗", deletedUser);
    }

    @Test
    public void testGetUserByEmail() {
        User user = userService.getUserByEmail("Annie@example.com"); // 假設 DB 有這個 Email
        assertNotNull("使用者不存在", user);
        assertEquals("Annie@example.com", user.getEmail());
    }
}
