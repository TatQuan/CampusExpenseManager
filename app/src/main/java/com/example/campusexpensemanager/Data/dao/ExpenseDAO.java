package com.example.campusexpensemanager.Data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.campusexpensemanager.Data.DatabaseContract;
import com.example.campusexpensemanager.Data.DatabaseHelper;
import com.example.campusexpensemanager.models.Expense;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Handler;

public class ExpenseDAO {
    private DatabaseHelper dbHelper;

    // Constructor - Nhận Context
    public ExpenseDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    //ADD EXPENSE - Thêm chi tiêu
    public void addExpense(String description, double amount, String date, String startDate, String endDate, int categoryId, int userId, int isRecurring) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "INSERT INTO " + DatabaseContract.ExpenseTable.TABLE_NAME +
                " (" + DatabaseContract.ExpenseTable.COLUMN_DESCRIPTION + ", "
                + DatabaseContract.ExpenseTable.COLUMN_AMOUNT + ", "
                + DatabaseContract.ExpenseTable.COLUMN_DATE + ", "
                + DatabaseContract.ExpenseTable.COLUMN_START_DATE + ", "
                + DatabaseContract.ExpenseTable.COLUMN_END_DATE + ", "
                + DatabaseContract.ExpenseTable.COLUMN_CATEGORY_ID + ", "
                + DatabaseContract.ExpenseTable.COLUMN_USER_ID + ", "
                + DatabaseContract.ExpenseTable.COLUMN_IS_RECURRING + ") VALUES('" +
                description + "', '" + amount + "', '" + date + "', '" + startDate + "', '" + endDate + "', '" + categoryId + "', '" + userId + "', '" + isRecurring + "');";
        db.execSQL(sql);
    }

    //GET EXPENSE - lấy danh sách chi tiêu theo category
    public List<Expense> getAllExpensesByCategory(int categoryId, int userId) {
        List<Expense> expenseList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql =
                "SELECT " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_ID + " AS expenseId, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_DESCRIPTION + " AS description, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_AMOUNT + " AS amount, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_DATE + " AS date, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_START_DATE + " AS startDate, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_END_DATE + " AS endDate, " +
                        "c." + DatabaseContract.CategoryTable.COLUMN_NAME + " AS categoryName, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_IS_RECURRING + " AS isRecurring " +
                "FROM " + DatabaseContract.ExpenseTable.TABLE_NAME + " e " +
                "JOIN " + DatabaseContract.CategoryTable.TABLE_NAME + " c ON e." + DatabaseContract.ExpenseTable.COLUMN_CATEGORY_ID + " = c." + DatabaseContract.CategoryTable.COLUMN_ID + " " +
                "WHERE e." + DatabaseContract.ExpenseTable.COLUMN_USER_ID + " = " + userId + " " +
                        "AND e." + DatabaseContract.ExpenseTable.COLUMN_CATEGORY_ID + " = " + categoryId + " " +
                "ORDER BY e." + DatabaseContract.ExpenseTable.COLUMN_DATE + " DESC;";

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int expenseId = cursor.getInt(cursor.getColumnIndexOrThrow("expenseId"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String startDate = cursor.getString(cursor.getColumnIndexOrThrow("startDate"));
                String endDate = cursor.getString(cursor.getColumnIndexOrThrow("endDate"));
                String categoryName = cursor.getString(cursor.getColumnIndexOrThrow("categoryName"));
                int isRecurring = cursor.getInt(cursor.getColumnIndexOrThrow("isRecurring"));


                expenseList.add(new Expense(expenseId, description, amount, date, startDate, endDate, categoryId, categoryName, isRecurring));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return expenseList;
    }

    // GET EXPENSE - lấy chi tiêu theo category + tháng + năm
    public List<Expense> getExpensesByCategoryAndMonthYearRange(int categoryId, int userId, int month, int year) {
        List<Expense> expenseList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 01 của tháng đang chọn – dùng làm "ngày đại diện" của tháng
        String monthStr = (month < 10 ? "0" : "") + month;          // "10"
        String yearStr  = String.valueOf(year);                     // "2025"
        String targetDate = yearStr + "-" + monthStr + "-01";       // "2025-10-01"

        String sql =
                "SELECT " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_ID + " AS expenseId, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_DESCRIPTION + " AS description, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_AMOUNT + " AS amount, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_DATE + " AS date, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_START_DATE + " AS startDate, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_END_DATE + " AS endDate, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_CATEGORY_ID + " AS categoryId, " +
                        "c." + DatabaseContract.CategoryTable.COLUMN_NAME + " AS categoryName, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_IS_RECURRING + " AS isRecurring " +
                        "FROM " + DatabaseContract.ExpenseTable.TABLE_NAME + " e " +
                        "JOIN " + DatabaseContract.CategoryTable.TABLE_NAME + " c " +
                        "ON e." + DatabaseContract.ExpenseTable.COLUMN_CATEGORY_ID + " = c." + DatabaseContract.CategoryTable.COLUMN_ID + " " +
                        "WHERE e." + DatabaseContract.ExpenseTable.COLUMN_USER_ID + " = ? " +
                        "AND e." + DatabaseContract.ExpenseTable.COLUMN_CATEGORY_ID + " = ? " +
                        "AND (" +
                        // Chi tiêu 1 lần: lọc theo date như cũ
                        "(e." + DatabaseContract.ExpenseTable.COLUMN_IS_RECURRING + " = 0 " +
                        "AND strftime('%Y', e." + DatabaseContract.ExpenseTable.COLUMN_DATE + ") = ? " +
                        "AND strftime('%m', e." + DatabaseContract.ExpenseTable.COLUMN_DATE + ") = ? " +
                        ") " +
                        "OR " +
                        // Chi tiêu định kỳ: tháng đang xem nằm giữa start_date và end_date
                        "(e." + DatabaseContract.ExpenseTable.COLUMN_IS_RECURRING + " = 1 " +
                        "AND date(?) BETWEEN " +
                        "date(e." + DatabaseContract.ExpenseTable.COLUMN_START_DATE + ") " +
                        "AND COALESCE(date(e." + DatabaseContract.ExpenseTable.COLUMN_END_DATE + "), date('9999-12-31'))" +
                        ")" +
                        ") " +
                        "ORDER BY e." + DatabaseContract.ExpenseTable.COLUMN_DATE + " DESC;";

        Cursor cursor = db.rawQuery(sql, new String[] {
                String.valueOf(userId),    // ?
                String.valueOf(categoryId),// ?
                yearStr,                   // ? (strftime '%Y' date)
                monthStr,                  // ? (strftime '%m' date)
                targetDate                 // ? (date(?) BETWEEN start_date AND end_date)
        });

        if (cursor.moveToFirst()) {
            do {
                int expenseId     = cursor.getInt(cursor.getColumnIndexOrThrow("expenseId"));
                String description= cursor.getString(cursor.getColumnIndexOrThrow("description"));
                double amount     = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
                String date       = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String startDate  = cursor.getString(cursor.getColumnIndexOrThrow("startDate"));
                String endDate    = cursor.getString(cursor.getColumnIndexOrThrow("endDate"));
                int catId         = cursor.getInt(cursor.getColumnIndexOrThrow("categoryId"));
                String categoryName = cursor.getString(cursor.getColumnIndexOrThrow("categoryName"));
                int isRecurring   = cursor.getInt(cursor.getColumnIndexOrThrow("isRecurring"));

                Expense e = new Expense(
                        expenseId,
                        description,
                        amount,
                        date,
                        startDate,
                        endDate,
                        catId,
                        categoryName,
                        isRecurring
                );
                expenseList.add(e);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return expenseList;
    }




    // SUM ALL EXPENSE - lấy tổng chi tiêu theo category
    public double sumAllExpenses(int userId , int categoryId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql =
                "SELECT SUM(" + DatabaseContract.ExpenseTable.COLUMN_AMOUNT + ") AS total " +
                        "FROM " + DatabaseContract.ExpenseTable.TABLE_NAME + " " +
                        "WHERE " + DatabaseContract.ExpenseTable.COLUMN_USER_ID + " = ? " +

                        "AND " + DatabaseContract.ExpenseTable.COLUMN_CATEGORY_ID + " = ? ";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId), String.valueOf(categoryId)});
        if (cursor.moveToFirst()) {
            return cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
        }
        return 0;
    }

    // SUM ALL EXPENSE - lấy tổng chi tiêu theo category, ngày bất đầu và kết thúc
    public double sumAllExpenses(int userId , int categoryId, String startDate, String endDate) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql =
                "SELECT SUM(" + DatabaseContract.ExpenseTable.COLUMN_AMOUNT + ") AS total " +
                        "FROM " + DatabaseContract.ExpenseTable.TABLE_NAME + " " +
                        "WHERE " + DatabaseContract.ExpenseTable.COLUMN_USER_ID + " = ? " +
                        "AND " + DatabaseContract.ExpenseTable.COLUMN_CATEGORY_ID + " = ? " +
                        "AND " + DatabaseContract.ExpenseTable.COLUMN_DATE + " BETWEEN ? AND ? ";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId), String.valueOf(categoryId), startDate, endDate});
        if (cursor.moveToFirst()) {
            return cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
        }
        return 0;
    }

    //GET EXPENSE - lấy danh sách chi tiêu
    public List<Expense> getAllExpenses( int userId) {
        List<Expense> expenseList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql =
                "SELECT " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_ID + " AS expenseId, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_DESCRIPTION + " AS description, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_AMOUNT + " AS amount, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_DATE + " AS date, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_START_DATE + " AS startDate, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_END_DATE + " AS endDate, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_CATEGORY_ID + " AS categoryId, " +
                        "c." + DatabaseContract.CategoryTable.COLUMN_NAME + " AS categoryName, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_IS_RECURRING + " AS isRecurring " +
                "FROM " + DatabaseContract.ExpenseTable.TABLE_NAME + " e " +
                "JOIN " + DatabaseContract.CategoryTable.TABLE_NAME + " c ON e." + DatabaseContract.ExpenseTable.COLUMN_CATEGORY_ID + " = c." + DatabaseContract.CategoryTable.COLUMN_ID + " " +
                "WHERE e." + DatabaseContract.ExpenseTable.COLUMN_USER_ID + " = " + userId + " " +
                "ORDER BY e." + DatabaseContract.ExpenseTable.COLUMN_DATE + " DESC;";


        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int expenseId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ExpenseTable.COLUMN_ID));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ExpenseTable.COLUMN_DESCRIPTION));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.ExpenseTable.COLUMN_AMOUNT));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ExpenseTable.COLUMN_DATE));
                String startDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ExpenseTable.COLUMN_START_DATE));
                String endDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ExpenseTable.COLUMN_END_DATE));
                int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ExpenseTable.COLUMN_CATEGORY_ID));
                String categoryName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CategoryTable.COLUMN_NAME));
                int isRecurring = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ExpenseTable.COLUMN_IS_RECURRING));

                expenseList.add(new Expense(expenseId, description, amount, date, startDate, endDate,categoryId, categoryName, isRecurring));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return expenseList;
    }

    //GET EXPENSE - lấy danh sách chi tiêu theo tháng
    public List<Expense> getExpensesByMonth(int userId, int year, int month) {
        List<Expense> expenseList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Đảm bảo month luôn có 2 chữ số
        String monthStr = (month < 10 ? "0" : "") + month;

        // SQLite query: lọc theo userId và tháng
        String sql =
                "SELECT e." + DatabaseContract.ExpenseTable.COLUMN_ID + " AS expenseId, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_DESCRIPTION + " AS description, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_AMOUNT + " AS amount, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_DATE + " AS date, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_START_DATE + " AS startDate, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_END_DATE + " AS endDate, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_IS_RECURRING + " AS isRecurring, " +
                        "e." + DatabaseContract.ExpenseTable.COLUMN_CATEGORY_ID + " AS categoryId, " +
                        "c." + DatabaseContract.CategoryTable.COLUMN_NAME + " AS categoryName " +
                        "FROM " + DatabaseContract.ExpenseTable.TABLE_NAME + " e " +
                        "JOIN " + DatabaseContract.CategoryTable.TABLE_NAME + " c " +
                        "ON e." + DatabaseContract.ExpenseTable.COLUMN_CATEGORY_ID + " = c." + DatabaseContract.CategoryTable.COLUMN_ID + " " +
                        "WHERE e." + DatabaseContract.ExpenseTable.COLUMN_USER_ID + " = " + userId + " " +
                        "AND strftime('%Y', e." + DatabaseContract.ExpenseTable.COLUMN_DATE + ") = '" + year + "' " +
                        "AND strftime('%m', e." + DatabaseContract.ExpenseTable.COLUMN_DATE + ") = '" + monthStr + "' " +
                        "ORDER BY e." + DatabaseContract.ExpenseTable.COLUMN_DATE + " DESC;";

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                int expenseId     = cursor.getInt(cursor.getColumnIndexOrThrow("expenseId"));
                String description= cursor.getString(cursor.getColumnIndexOrThrow("description"));
                double amount     = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
                String date       = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String startDate  = cursor.getString(cursor.getColumnIndexOrThrow("startDate"));
                String endDate    = cursor.getString(cursor.getColumnIndexOrThrow("endDate"));
                int categoryId    = cursor.getInt(cursor.getColumnIndexOrThrow("categoryId"));
                String categoryName = cursor.getString(cursor.getColumnIndexOrThrow("categoryName"));
                int isRecurring  = cursor.getInt(cursor.getColumnIndexOrThrow("isRecurring"));

                expenseList.add(new Expense(expenseId, description, amount, date, startDate, endDate, categoryId, categoryName, isRecurring));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return expenseList;
    }

    // SUM ALL EXPENSE - lấy tổng chi tiêu
    public double getTotalExpense(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql =
                "SELECT SUM(" + DatabaseContract.ExpenseTable.COLUMN_AMOUNT + ") AS total " +
                        "FROM " + DatabaseContract.ExpenseTable.TABLE_NAME + " " +
                        "JOIN " + DatabaseContract.CategoryTable.TABLE_NAME + " ON " +
                        DatabaseContract.ExpenseTable.COLUMN_CATEGORY_ID + " = " +
                        DatabaseContract.CategoryTable.COLUMN_ID + " " +
                        "WHERE " + DatabaseContract.ExpenseTable.COLUMN_USER_ID + " = ? " +
                        "AND " + DatabaseContract.CategoryTable.COLUMN_IS_DELETED + " = 0";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            return cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
        }
        return 0;
    }

    //GET EXPENSE - lấy tổng chi tiêu theo tháng năm
    public double getTotalExpenseByMonthAndYear(int userId, int categoryId, int year, int month) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Tính ngày đầu & ngày cuối của tháng (YYYY-MM-DD)
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.YEAR, year);
        cal.set(java.util.Calendar.MONTH, month - 1); // Calendar: 0-based
        cal.set(java.util.Calendar.DAY_OF_MONTH, 1);

        int lastDay = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);

        String monthStart = String.format("%04d-%02d-%02d", year, month, 1);
        String monthEnd = String.format("%04d-%02d-%02d", year, month, lastDay);

        String sql =
                "SELECT SUM(" + DatabaseContract.ExpenseTable.COLUMN_AMOUNT + ") AS total " +
                        "FROM " + DatabaseContract.ExpenseTable.TABLE_NAME + " " +
                        "WHERE " + DatabaseContract.ExpenseTable.COLUMN_USER_ID + " = ? " +
                        "AND " + DatabaseContract.ExpenseTable.COLUMN_CATEGORY_ID + " = ? " +
                        "AND ( " +
                        // Chi tiêu thường: date nằm trong tháng
                        "   (" + DatabaseContract.ExpenseTable.COLUMN_IS_RECURRING + " = 0 " +
                        "    AND " + DatabaseContract.ExpenseTable.COLUMN_DATE + " BETWEEN ? AND ?) " +
                        "   OR " +
                        // Chi tiêu định kỳ: khoảng [start_date, end_date] giao với [monthStart, monthEnd]
                        "   (" + DatabaseContract.ExpenseTable.COLUMN_IS_RECURRING + " = 1 " +
                        "    AND " + DatabaseContract.ExpenseTable.COLUMN_START_DATE + " <= ? " +
                        "    AND (" + DatabaseContract.ExpenseTable.COLUMN_END_DATE + " IS NULL " +
                        "         OR " + DatabaseContract.ExpenseTable.COLUMN_END_DATE + " >= ?)" +
                        "   ) " +
                        ")";

        Cursor cursor = db.rawQuery(sql, new String[]{
                String.valueOf(userId),
                String.valueOf(categoryId),
                monthStart,   // cho điều kiện date BETWEEN ? AND ?
                monthEnd,
                monthEnd,     // cho điều kiện start_date <= ?
                monthStart    // cho điều kiện end_date >= ?
        });

        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
        }
        cursor.close();
        return total;
    }

    public int updateExpense(Expense expense) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseContract.ExpenseTable.COLUMN_DESCRIPTION, expense.getDescription());
        values.put(DatabaseContract.ExpenseTable.COLUMN_AMOUNT, expense.getAmount());
        // Nếu sau này muốn cho sửa date thì put thêm:
        // values.put(DatabaseContract.ExpenseTable.COLUMN_DATE, expense.getDate());

        String whereClause = DatabaseContract.ExpenseTable.COLUMN_ID + " = ?";
        String[] whereArgs = { String.valueOf(expense.getId()) };

        int rows = db.update(DatabaseContract.ExpenseTable.TABLE_NAME, values, whereClause, whereArgs);
        db.close();
        return rows;
    }



}
