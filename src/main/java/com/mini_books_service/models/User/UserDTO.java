package com.mini_books_service.models.User;

import java.util.Base64;

public class UserDTO {
    private String idHash;
    private String name;
    private String email;

    public UserDTO(User user) {
        this.idHash = Base64.getEncoder().encodeToString(String.valueOf(user.getId()).getBytes());
        this.name = user.getName();
        this.email = user.getEmail();
    }

    // Getters
    public String getIdHash() {
        return idHash;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }

    // Setters
    public void setIdHash(String idHash) {
        this.idHash = idHash;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
