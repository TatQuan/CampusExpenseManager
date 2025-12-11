package com.example.campusexpensemanager.models;

public class    Budget {
    private int id;
    private int userId;
    private int categoryId;
    private double budgetAmount;
    private double remainingBudget;
    private int month;
    private int year;

    // Constructor, getters, setters
    public Budget(int id, int userId, int categoryId, double budgetAmount, double remainingBudget, int month, int year) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.budgetAmount = budgetAmount;
        this.remainingBudget = remainingBudget;
        this.month = month;
        this.year = year;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getCategoryId() { return categoryId; }
    public double getBudgetAmount() { return budgetAmount; }
    public double getRemainingBudget() { return remainingBudget; }
    public int getMonth() { return month; }
    public int getYear() { return year; }


    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public void setBudgetAmount(double budgetAmount) { this.budgetAmount = budgetAmount; }
    public void setRemainingBudget(double remainingBudget) { this.remainingBudget = remainingBudget; }
    public void setMonth(int month) { this.month = month; }
    public void setYear(int year) { this.year = year; }
}
