package com.example.campusexpensemanager.models;

public class MonthlyReport {
    private int reportId;
    private int categoryId;
    private String period;        // vd: "2025-10"
    private double budget;
    private double totalExpense;

    public MonthlyReport() {
    }


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

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }

}
