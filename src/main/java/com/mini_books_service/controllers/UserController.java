package com.mini_books_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mini_books_service.models.User.UserDTO;
import com.mini_books_service.models.User.UserViewModel;
import com.mini_books_service.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/")
    public UserDTO createNewUser(@RequestBody UserViewModel userView) {
        return userService.createNewUser(userView);
    }

    @PostMapping("/login")
    public UserDTO loginUser(@RequestBody UserViewModel userView) {
        return userService.login(userView);
    }

    @PutMapping("/")
    public UserDTO updateUser(@RequestBody UserViewModel userView) {
        return userService.updateUser(userView);
    }

    @DeleteMapping("/{idHash}")
    public void deleteUserById(@PathVariable String idHash) {
        userService.deleteUser(idHash);
    }
}
