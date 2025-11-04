package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.model.User;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.UserService;
@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
    private UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	    User user = userService.getUserByEmail(email);
	    if (user == null) {
	        throw new UsernameNotFoundException("找不到使用者: " + email);
	    }
	    return new CustomUserDetails(user);
	}

}
