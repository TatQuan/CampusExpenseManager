package com.example.campusexpensemanager.models;

public class Expense {
    private int id;
    private String description;
    private double amount;
    private String date;
    private String startDate;
    private String endDate;
    private int categoryId;
    private String categoryName;
    private int isRecurring;


    // Constructor, getters, setters
    public Expense(int id, String description, double amount, String date, String startDate, String endDate,int categoryId, String categoryName, int isRecurring) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.startDate = startDate;
        this.endDate = endDate;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.isRecurring = isRecurring;
    }

    public int getId() { return id; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getCategoryName() { return categoryName; }
    public int getIsRecurring() { return isRecurring; }




    public void setId(int id) { this.id = id; }
    public void setDescription(String description) { this.description = description; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setDate(String date) { this.date = date; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public void setIsRecurring(int isRecurring) { this.isRecurring = isRecurring; }
}
