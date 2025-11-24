package com.example.campusexpensemanager.service;

import android.content.Context;

import com.example.campusexpensemanager.Data.dao.BudgetDAO;
import com.example.campusexpensemanager.Data.dao.ExpenseDAO;
import com.example.campusexpensemanager.Data.dao.NotificationDAO;
import com.example.campusexpensemanager.models.Budget;

public class BudgetNotificationHelper {

    // Gọi hàm này sau khi add expense hoặc khi mở màn report
    public static void checkBudgetNotification(
            Context context,
            int userId,
            int categoryId,
            int year,
            int month
    ) {
        ExpenseDAO expenseDAO = new ExpenseDAO(context);
        BudgetDAO budgetDAO = new BudgetDAO(context);
        NotificationDAO notificationDAO = new NotificationDAO(context);

        double totalExpense = expenseDAO.getTotalExpenseByMonthAndYear(userId, categoryId, year, month);

        // 1. Lấy budget của category trong tháng
        Budget budget = budgetDAO.getBudgetByMonthAndYear(userId, categoryId, month, year);
        if (budget == null) {
            if (totalExpense > 0) {
                notificationDAO.addNotification(
                        userId,
                        categoryId,
                        "Bạn chưa đặt ngân sách cho tháng " + month + "/" + year
                                + ", nhưng đã chi tiêu " + (long)totalExpense + " VND trong danh mục này."
                );
            }
            return;
        }

        double budgetAmount = budget.getBudgetAmount();

        // 2. Lấy tổng chi tiêu tháng đó (có cả định kỳ)



        if (budgetAmount <= 0) {
            return; // tránh chia 0
        }

        double ratio = (totalExpense / budgetAmount) * 100.0;

        String message = null;

        // Tùy Ren chọn ngưỡng, Mei gợi ý:
        if (ratio >= 100) {
            message = "Bạn đã vượt quá ngân sách của tháng " + month + "/" + year +
                    " cho danh mục này. Tổng chi tiêu: " + (long)totalExpense +
                    " / Ngân sách: " + (long)budgetAmount + " VND";
        } else if (ratio >= 80) {
            message = "Bạn đã sử dụng " + (long)totalExpense +
                    " / " + (long)budgetAmount +
                    " VND (" + (int)ratio + "%) ngân sách của tháng " + month + "/" + year +
                    " cho danh mục này.";
        }

        // 3. Nếu có message -> ghi vào bảng Notification
        if (message != null) {
            notificationDAO.addNotification(userId, categoryId, message);
        }
    }
}
