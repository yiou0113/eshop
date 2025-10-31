package com.example.demo.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.demo.model.Customer;
import com.example.demo.model.User;
import com.example.demo.service.CustomerService;

@Component
public class CustomerInterceptor implements HandlerInterceptor {

    @Autowired
    private CustomerService customerService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedInUser") : null;

        // 前一層LoginInterceptor已保證user不為null，但為安全仍可再檢查一次
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        Customer customer = customerService.getCustomerByUserId(user.getId());
        if (customer == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        session.setAttribute("loggedInCustomer", customer);
        return true;
    }
}

