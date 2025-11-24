package com.example.campusexpensemanager.Data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.campusexpensemanager.Data.DatabaseContract;
import com.example.campusexpensemanager.Data.DatabaseHelper;
import com.example.campusexpensemanager.models.Budget;
import com.example.campusexpensemanager.models.Expense;
import com.example.campusexpensemanager.models.MonthlyReport;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MonthlyReportDAO {

    private final DatabaseHelper dbHelper;
    private final Context context;

    public MonthlyReportDAO(Context context) {
        this.context = context.getApplicationContext();
        this.dbHelper = new DatabaseHelper(context);
    }

    /**
     * Tạo/cập nhật báo cáo cho 1 user theo tháng/năm.
     * Lưu vào bảng MonthlyReport theo từng category.
     */
    public void generateMonthlyReport(int userId, int month, int year) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // period dạng "YYYY-MM"
        String period = String.format(Locale.getDefault(), "%04d-%02d", year, month);

        // 1. Xoá report cũ cùng period (để tránh trùng)
        db.delete(DatabaseContract.MonthlyReportTable.TABLE_NAME,
                DatabaseContract.MonthlyReportTable.COLUMN_PERIOD + " = ?",
                new String[]{period});

        // 2. Lấy budgets của user cho tháng/năm đó
        BudgetDAO budgetDAO = new BudgetDAO(context);
        ExpenseDAO expenseDAO = new ExpenseDAO(context);

        List<Budget> budgets = budgetDAO.getBudgetsByMonth(userId, month, year);

        for (Budget b : budgets) {
            int categoryId = b.getCategoryId();
            double budgetAmount = b.getBudgetAmount();

            // 3. Tính tổng chi tiêu (bao gồm định kỳ) cho category + tháng/năm + user này
            double totalExpense = sumExpensesByCategoryAndMonthYear(expenseDAO, categoryId, userId, month, year);

            // 4. Insert vào bảng MonthlyReport
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.MonthlyReportTable.COLUMN_CATEGORY_ID, categoryId);
            values.put(DatabaseContract.MonthlyReportTable.COLUMN_PERIOD, period);
            values.put(DatabaseContract.MonthlyReportTable.COLUMN_BUDGET, budgetAmount);
            values.put(DatabaseContract.MonthlyReportTable.COLUMN_TOTAL_EXPENSE, totalExpense);

            db.insert(DatabaseContract.MonthlyReportTable.TABLE_NAME, null, values);
        }
    }

    /**
     * Tính tổng chi tiêu (sum amount) cho 1 category + user + month/year
     * sử dụng hàm getExpensesByCategoryAndMonthYearRange (đã xử lý recurring).
     */
    private double sumExpensesByCategoryAndMonthYear(ExpenseDAO expenseDAO,
                                                     int categoryId,
                                                     int userId,
                                                     int month,
                                                     int year) {
        List<Expense> expenses =
                expenseDAO.getExpensesByCategoryAndMonthYearRange(categoryId, userId, month, year);

        double total = 0;
        for (Expense e : expenses) {
            total += e.getAmount();
        }
        return total;
    }

    /**
     * Lấy danh sách report đã được generate cho 1 period cụ thể (vd "2025-10")
     */
    public List<MonthlyReport> getMonthlyReportByPeriod(String period) {
        List<MonthlyReport> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql =
                "SELECT " +
                        DatabaseContract.MonthlyReportTable.COLUMN_ID + " AS reportId, " +
                        DatabaseContract.MonthlyReportTable.COLUMN_CATEGORY_ID + " AS categoryId, " +
                        DatabaseContract.MonthlyReportTable.COLUMN_PERIOD + " AS period, " +
                        DatabaseContract.MonthlyReportTable.COLUMN_BUDGET + " AS budget, " +
                        DatabaseContract.MonthlyReportTable.COLUMN_TOTAL_EXPENSE + " AS totalExpense " +
                        "FROM " + DatabaseContract.MonthlyReportTable.TABLE_NAME + " " +
                        "WHERE " + DatabaseContract.MonthlyReportTable.COLUMN_PERIOD + " = ? ";

        Cursor cursor = db.rawQuery(sql, new String[]{period});

        if (cursor.moveToFirst()) {
            do {
                int reportId = cursor.getInt(cursor.getColumnIndexOrThrow("reportId"));
                int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("categoryId"));
                String p = cursor.getString(cursor.getColumnIndexOrThrow("period"));
                double budget = cursor.getDouble(cursor.getColumnIndexOrThrow("budget"));
                double totalExpense = cursor.getDouble(cursor.getColumnIndexOrThrow("totalExpense"));

                list.add(new MonthlyReport(reportId, categoryId, p, budget, totalExpense));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }
}
