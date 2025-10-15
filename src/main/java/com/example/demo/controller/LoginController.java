package com.example.demo.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.User;
import com.example.demo.service.AuthService;

@Controller
public class LoginController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // 對應 login.html
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttrs) {
        User user = authService.login(email, password);
        if (user != null) {
            session.setAttribute("loggedInUser", user);
            return "redirect:/welcome";
        } else {
            redirectAttrs.addAttribute("error", "Invalid email or password");
            return "redirect:/login";
        }
    }

    @GetMapping("/welcome")
    public String showWelcome(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user != null) {
            model.addAttribute("userName", user.getName());
            return "welcome"; // 導向 welcome.html
        }
        return "redirect:/login"; // 未登入回 login
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}


