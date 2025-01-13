package com.mini_books_service.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    private Integer id;
    private String name;
    private String email;
    private String passwordHash;

    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPasswordHash(String password) {
        this.passwordHash = password;
    }

    // Getters
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getPasswordHash() {
        return passwordHash;
    }
}
