package com.example.campusexpensemanager.models;

public class Notification {
    private int id;
    private int userId;
    private int categoryId;
    private String message;
    private String date;
    private int isRead;

    public Notification(int id, int userId, int categoryId, String message, String date, int isRead) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.message = message;
        this.date = date;
        this.isRead = isRead;
    }

    // getters...
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getCategoryId() { return categoryId; }
    public String getMessage() { return message; }
    public String getDate() { return date; }
    public int getIsRead() { return isRead; }

    public void setIsRead(int isRead) { this.isRead = isRead; }
}

