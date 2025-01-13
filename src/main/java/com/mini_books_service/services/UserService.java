package com.mini_books_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mini_books_service.models.User;

@Service
public class UserService {
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createNewUser(User user, String password) {
        String hash = passwordEncoder.encode(password);
        user.setPasswordHash(hash);
        // Save user to db
    }

    public boolean authenticateUser(String email, String password) {
        User user = new User(); // Replace with getting user from db
        return passwordEncoder.matches(password, user.getPasswordHash());
    }
}
