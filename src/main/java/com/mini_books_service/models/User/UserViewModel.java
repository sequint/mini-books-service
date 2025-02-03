package com.mini_books_service.models.User;

public class UserViewModel {
    private String idHash;
    private String name;
    private String email;
    private String password;

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
    public String getPassword() {
        return password;
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
    public void setPassword(String password) {
        this.password = password;
    }
}
