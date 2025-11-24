package com.example.campusexpensemanager.models;

public class MonthlyReport {
    private int reportId;
    private int categoryId;
    private String period;        // vd: "2025-10"
    private double budget;
    private double totalExpense;

    public MonthlyReport(int reportId, int categoryId, String period, double budget, double totalExpense) {
        this.reportId = reportId;
        this.categoryId = categoryId;
        this.period = period;
        this.budget = budget;
        this.totalExpense = totalExpense;
    }

    public MonthlyReport(int categoryId, String period, double budget, double totalExpense) {
        this.categoryId = categoryId;
        this.period = period;
        this.budget = budget;
        this.totalExpense = totalExpense;
    }

    public int getReportId() {
        return reportId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getPeriod() {
        return period;
    }

    public double getBudget() {
        return budget;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public double getRemaining() {
        return budget - totalExpense;
    }

    public boolean isOverBudget() {
        return totalExpense > budget;
    }
}
