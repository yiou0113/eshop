package com.example.demo.service.impl;

import com.example.demo.dao.CustomerDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.model.Customer;
import com.example.demo.model.User;
import com.example.demo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private CustomerDAO customerDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void registerCustomer(String name, String email, String password, String phone, String address) {
    	String encodedPassword = passwordEncoder.encode(password);
        // 建立 User
        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);
        //user.setRole("ROLE_USER");

        // 建立 Customer
        Customer customer = new Customer();
        customer.setName(name);
        customer.setPhone(phone);
        customer.setAddress(address);
        customer.setUser(user);

        // 綁定雙向關係
        user.setCustomer(customer);

        // 儲存（會自動保存關聯對象）
        userDAO.save(user);
    }
    public Customer getCustomerByUserId(Long userId) {
        return customerDAO.findByUserId(userId);
    }
}
