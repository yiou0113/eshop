package com.example.demo.service.impl;

import com.example.demo.dao.UserDAO;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
@Transactional
public class UserServiceImpl implements UserService {
	
    @Autowired
    private UserDAO userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
        
    @Override
    public List<User> getAllUsers() { 	
    	return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void saveUser(User user) {
        // 加密密碼
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);
    }


    @Override
    public void updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id);
        if (existingUser != null) {
            existingUser.setEmail(updatedUser.getEmail());
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(updatedUser.getPassword());
                existingUser.setPassword(encodedPassword);
            }
            userRepository.save(existingUser);
        }
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.delete(id);
    }
    
    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
