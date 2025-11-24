package com.example.campusexpensemanager.Data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.campusexpensemanager.Data.DatabaseContract;
import com.example.campusexpensemanager.Data.DatabaseHelper;
import com.example.campusexpensemanager.models.Budget;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;

public class BudgetDAO {
    private DatabaseHelper dbhelper;
    public BudgetDAO(Context context) {
        dbhelper = new DatabaseHelper(context);
    }

    //ADD BUDGET - thêm chi tiêu
    public void addBudget(int userId, int categoryId, double budgetAmount, int month, int year) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.BudgetTable.COLUMN_USER_ID, userId);
        values.put(DatabaseContract.BudgetTable.COLUMN_CATEGORY_ID, categoryId);
        values.put(DatabaseContract.BudgetTable.COLUMN_BUDGET_AMOUNT, budgetAmount);
        values.put(DatabaseContract.BudgetTable.COLUMN_MONTH, month);
        values.put(DatabaseContract.BudgetTable.COLUMN_YEAR, year);
        db.insert(DatabaseContract.BudgetTable.TABLE_NAME, null, values);
    }

    //GET ALL BUDGETS
    public List<Budget> getAllBudgets() {
        List<Budget> budgetList = new ArrayList<>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String sql =
                "SELECT * FROM " + DatabaseContract.BudgetTable.TABLE_NAME + " " +
                "ORDER BY " + DatabaseContract.BudgetTable.COLUMN_CATEGORY_ID + " ASC";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.BudgetTable.COLUMN_ID));
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.BudgetTable.COLUMN_USER_ID));
                int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.BudgetTable.COLUMN_CATEGORY_ID));
                double budgetAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.BudgetTable.COLUMN_BUDGET_AMOUNT));
                double remainingBudget = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.BudgetTable.COLUMN_REMAINING_BUDGET));
                int month = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.BudgetTable.COLUMN_MONTH));
                int year = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.BudgetTable.COLUMN_YEAR));
                budgetList.add(new Budget(id, userId, categoryId, budgetAmount, remainingBudget, month, year));
                } while (cursor.moveToNext());
        }

        cursor.close();
        return budgetList;
    }

    // Check over budget

    //GET BUDGET - lấy danh sách chi tiêu theo tháng
    public List<Budget> getBudgetsByMonth(int userId, int month, int year) {
        List<Budget> budgetList = new ArrayList<>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String sql =
                "SELECT * FROM " + DatabaseContract.BudgetTable.TABLE_NAME + " " +
                "WHERE " + DatabaseContract.BudgetTable.COLUMN_USER_ID + " = ? " +
                        "AND " + DatabaseContract.BudgetTable.COLUMN_MONTH + " = ? " +
                        "AND " + DatabaseContract.BudgetTable.COLUMN_YEAR + " = ? " +
                "ORDER BY " + DatabaseContract.BudgetTable.COLUMN_CATEGORY_ID + " ASC";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId), String.valueOf(month), String.valueOf(year)});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.BudgetTable.COLUMN_ID));
                int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.BudgetTable.COLUMN_CATEGORY_ID));
                double budgetAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.BudgetTable.COLUMN_BUDGET_AMOUNT));
                double remainingBudget = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.BudgetTable.COLUMN_REMAINING_BUDGET));

                budgetList.add(new Budget(id, userId, categoryId, budgetAmount, remainingBudget, month, year));
                } while (cursor.moveToNext());
        }

        cursor.close();
        return budgetList;
    }

    //UPDATE BUDGET - sửa chi tiêu
    public void updateBudget(int id, double remainingBudget) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.BudgetTable.COLUMN_REMAINING_BUDGET, remainingBudget);
        db.update(DatabaseContract.BudgetTable.TABLE_NAME, values,
                DatabaseContract.BudgetTable.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    //DELETE BUDGET - xóa chi tiêu
    public void deleteBudget(int id) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.BudgetTable.COLUMN_IS_DELETED, 1);
        db.update(DatabaseContract.BudgetTable.TABLE_NAME, values,
                DatabaseContract.BudgetTable.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    //GET BUDGET BY CATEGORY ID
    public Budget getBudgetByCategoryId(int categoryId){
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String sql =
                "SELECT * FROM " + DatabaseContract.BudgetTable.TABLE_NAME + " " +
                        "WHERE " + DatabaseContract.BudgetTable.COLUMN_CATEGORY_ID + " = ? ";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(categoryId)});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.BudgetTable.COLUMN_ID));
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.BudgetTable.COLUMN_USER_ID));
            double budgetAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.BudgetTable.COLUMN_BUDGET_AMOUNT));
            double remainingBudget = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.BudgetTable.COLUMN_REMAINING_BUDGET));
            int month = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.BudgetTable.COLUMN_MONTH));
            int year = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.BudgetTable.COLUMN_YEAR));

            return new Budget(id, userId, categoryId, budgetAmount, remainingBudget, month, year);
        } else {
            return null;
        }
    }

    //GET BUDGET BY MONTH AND YEAR
    public Budget getBudgetByMonthAndYear(int userId, int month, int year){
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String sql =
                "SELECT * FROM " + DatabaseContract.BudgetTable.TABLE_NAME + " " +
                        "WHERE " + DatabaseContract.BudgetTable.COLUMN_USER_ID + " = ? " +
                        "AND " + DatabaseContract.BudgetTable.COLUMN_MONTH + " = ? " +
                        "AND " + DatabaseContract.BudgetTable.COLUMN_YEAR + " = ? ";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId), String.valueOf(month), String.valueOf(year)});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.BudgetTable.COLUMN_ID));
            int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.BudgetTable.COLUMN_CATEGORY_ID));
            double budgetAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.BudgetTable.COLUMN_BUDGET_AMOUNT));
            double remainingBudget = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.BudgetTable.COLUMN_REMAINING_BUDGET));

            return new Budget(id, userId, categoryId, budgetAmount, remainingBudget, month, year);
        } else {
            return null;
        }
    }

    //GET HIGHEST YEAR IN BUDGET
    public int getHighestYearInBudget(int userId){
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String sql =
                "SELECT MAX(" + DatabaseContract.BudgetTable.COLUMN_YEAR + ") FROM " + DatabaseContract.BudgetTable.TABLE_NAME + " " +
                        "WHERE " + DatabaseContract.BudgetTable.COLUMN_USER_ID + " = ? ";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        } else {
            return -1;
        }
    }

    //GET SUM AMOUNT BUDGET BY CATEGORY
    public double getSumAmountBudgetByCategory(int categoryId, int userId){
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String sql =
                "SELECT SUM(" + DatabaseContract.BudgetTable.COLUMN_BUDGET_AMOUNT + ") FROM " + DatabaseContract.BudgetTable.TABLE_NAME + " " +
                        "WHERE " + DatabaseContract.BudgetTable.COLUMN_CATEGORY_ID + " = ? " +
                        "AND " + DatabaseContract.BudgetTable.COLUMN_USER_ID + " = ? ";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(categoryId), String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            return cursor.getDouble(0);
        } else {
            return -1;
        }
    }

}
