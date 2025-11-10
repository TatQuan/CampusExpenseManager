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
import java.util.logging.Handler;

public class ExpenseDAO {
    private DatabaseHelper dbHelper;

    // Constructor - Nhận Context
    public ExpenseDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    //ADD EXPENSE - Thêm chi tiêu
    public void addExpense(String description, double amount, String date, String startDate, String endDate, int categoryId, int userId, boolean isRecurring) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "INSERT INTO " + DatabaseContract.ExpenseTable.TABLE_NAME +
                " (description, amount, date, startDate, endDate, categoryId, userId, isRecurring) VALUES('" +
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
                int expenseId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ExpenseTable.COLUMN_ID));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ExpenseTable.COLUMN_DESCRIPTION));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.ExpenseTable.COLUMN_AMOUNT));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ExpenseTable.COLUMN_DATE));
                String startDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ExpenseTable.COLUMN_START_DATE));
                String endDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ExpenseTable.COLUMN_END_DATE));
                int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ExpenseTable.COLUMN_CATEGORY_ID));
                String categoryName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CategoryTable.COLUMN_NAME));
                int isRecurring = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ExpenseTable.COLUMN_IS_RECURRING));

                expenseList.add(new Expense(expenseId, description, amount, date, startDate, endDate, categoryId, categoryName, isRecurring));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return expenseList;
    }


}
