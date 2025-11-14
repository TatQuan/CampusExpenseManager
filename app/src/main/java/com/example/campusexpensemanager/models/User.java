package com.example.campusexpensemanager.models;

public class User {
    private int userId;
    private String username;
    private String email;
    private String role;
    private String createdAt;
    private String password;

    // Constructor, getters, setters
    public User(int userId, String username, String email, String role, String createdAt, String password) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
        this.password = password;
    }

    // Getter and setter
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getCreatedAt() { return createdAt; }


    public void setUserId(int userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }



}
