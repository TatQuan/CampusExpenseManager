package com.example.campusexpensemanager.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.campusexpensemanager.Data.DatabaseContract;
import com.example.campusexpensemanager.models.User;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CampusExpenseManager.db";
    private static final int DATABASE_VERSION = 1; // tăng phiên bản vì thêm cột role

    // Tên bảng
    public static final String TABLE_USER = "User";
    public static final String TABLE_CATEGORY = "Category";
    public static final String TABLE_EXPENSE = "Expense";
    public static final String TABLE_BUDGET = "Budget";
    public static final String TABLE_REPORT = "Report";
    public static final String TABLE_NOTIFICATION = "Notification";
    public static final String TABLE_MONTHLY_REPORT = "MonthlyReport";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.UserTable.CREATE_TABLE);
        db.execSQL(DatabaseContract.CategoryTable.CREATE_TABLE);
        db.execSQL(DatabaseContract.ExpenseTable.CREATE_TABLE);
        db.execSQL(DatabaseContract.BudgetTable.CREATE_TABLE);
        db.execSQL(DatabaseContract.MonthlyReportTable.CREATE_TABLE);
        db.execSQL(DatabaseContract.NotificationTable.CREATE_TABLE);

        //Insert default database here
        InserAataCategory(db);
    }

    public void InserAataCategory(SQLiteDatabase db){
        db.execSQL("INSERT INTO " + TABLE_CATEGORY + " (" +
                DatabaseContract.CategoryTable.COLUMN_NAME + ", " +
                DatabaseContract.CategoryTable.COLUMN_DESCRIPTION + ", " +
                DatabaseContract.CategoryTable.COLUMN_ICON_NAME + ") " +
                "VALUES ('Food', 'Food', 'ic_food'), " +
                "('Transportation', 'Transportation', 'ic_transportation'), " +
                "('Healthcare', 'Healthcare', 'ic_healthcare'), " +
                "('Education', 'Education', 'ic_education'), " +
                "('Clothing', 'Clothing', 'ic_clothing'), " +
                "('Electricity', 'Electricity', 'ic_electricity'), " +
                "('Rent', 'Rent', 'ic_rent'), " +
                "('Cosnetics', 'Cosnetics', 'ic_cosnetics');");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng cũ và tạo lại
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MONTHLY_REPORT);
        onCreate(db);

        // Không xóa bảng, chỉ sửa bản và tăng version
//        if (oldVersion < 2) {
//            // Ví dụ: thêm column mới trong version 2
//             db.execSQL("ALTER TABLE User ADD COLUMN phone TEXT");
//        }
//        if (oldVersion < 3) {
//            // Migration cho version 3
//        }
    }
}
