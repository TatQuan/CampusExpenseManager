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
        insertUser(db);
        insertDataExpense(db);
        insertDataBudget(db);
    }

    public void InserAataCategory(SQLiteDatabase db){
        db.execSQL("INSERT INTO " + DatabaseContract.CategoryTable.TABLE_NAME + " (" +
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

    public void insertUser(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + DatabaseContract.UserTable.TABLE_NAME + " (" +
                DatabaseContract.UserTable.COLUMN_USERNAME + ", " +
                DatabaseContract.UserTable.COLUMN_PASSWORD + ", " +
                DatabaseContract.UserTable.COLUMN_EMAIL + ", " +
                DatabaseContract.UserTable.COLUMN_ROLE + ") " +
                "VALUES ('admin', 'admin1', 'admin@gmail.com', 'ADMIN');");
    }

    public void insertDataExpense(SQLiteDatabase db) {

        db.execSQL("INSERT INTO Expense (description, amount, date, start_date, end_date, categoryId, userId, isRecurring) VALUES " +

                /* ------------------- MẪU CHI TIÊU 1 LẦN ------------------- */

                // Food - tháng 1
                "('Breakfast Bánh mì', 35000, '2025-01-05', NULL, NULL, 1, 1, 0)," +
                "('Lunch Phở', 55000, '2025-01-10', NULL, NULL, 1, 1, 0)," +

                // Transportation - tháng 1
                "('Bus ticket', 12000, '2025-01-02', NULL, NULL, 2, 1, 0)," +
                "('Train ticket', 20000, '2025-01-25', NULL, NULL, 2, 1, 0)," +

                // Healthcare - tháng 2
                "('Buy medicine', 90000, '2025-02-14', NULL, NULL, 3, 1, 0)," +

                // Education - tháng 2
                "('Buy notebook', 15000, '2025-02-09', NULL, NULL, 4, 1, 0)," +


                /* ------------------- MẪU CHI TIÊU ĐỊNH KỲ ------------------- */

                // Rent (tiền nhà) - recurring mỗi tháng
                "('Monthly Rent', 3500000, '2025-01-01', '2025-01-01', '2025-12-31', 7, 1, 1)," +

                // Electricity bill mỗi tháng
                "('Electricity Bill', 750000, '2025-01-15', '2025-01-01', '2025-12-31', 6, 1, 1)," +

                // Gym membership mỗi tháng
                "('Gym Membership', 500000, '2025-01-03', '2025-01-01', '2025-06-30', 3, 1, 1)," +


                /* ------------------- THÊM VÀI MẪU CHI TIÊU NHIỀU THÁNG ------------------- */

                // Tháng 3
                "('Lunch cơm gà', 45000, '2025-03-07', NULL, NULL, 1, 1, 0)," +
                "('Grab bike', 22000, '2025-03-20', NULL, NULL, 2, 1, 0)," +

                // Tháng 4
                "('Buy T-shirt', 120000, '2025-04-11', NULL, NULL, 5, 1, 0)," +

                // Tháng 5
                "('Cosmetics cream', 85000, '2025-05-10', NULL, NULL, 8, 1, 0);"
        );
    }

    public void insertDataBudget(SQLiteDatabase db) {

        db.execSQL("INSERT INTO Budget (userId, categoryId, budgetAmount, remainingBudget, month, year, isDeleted) VALUES " +

                // -------- Tháng 1/2025 --------
                "(1, 1, 2000000, 2000000, 1, 2025, 0)," +   // Food
                "(1, 2, 500000, 500000, 1, 2025, 0)," +     // Transportation
                "(1, 7, 3500000, 3500000, 1, 2025, 0)," +   // Rent
                "(1, 6, 800000, 800000, 1, 2025, 0)," +     // Electricity

                // -------- Tháng 2/2025 --------
                "(1, 1, 1800000, 1800000, 2, 2025, 0)," +   // Food
                "(1, 3, 400000, 400000, 2, 2025, 0)," +     // Healthcare
                "(1, 4, 300000, 300000, 2, 2025, 0)," +     // Education

                // -------- Tháng 3/2025 --------
                "(1, 1, 1500000, 1500000, 3, 2025, 0)," +
                "(1, 2, 600000, 600000, 3, 2025, 0)," +
                "(1, 5, 500000, 500000, 3, 2025, 0)," +

                // -------- Tháng 4/2025 --------
                "(1, 1, 2000000, 2000000, 4, 2025, 0)," +
                "(1, 8, 300000, 300000, 4, 2025, 0)," +

                // -------- Tháng 12/2024 --------
                "(1, 1, 2200000, 2200000, 12, 2024, 0)," +
                "(1, 7, 3500000, 3500000, 12, 2024, 0);"

        );
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng cũ và tạo lại
        db.execSQL(DatabaseContract.UserTable.DROP_TABLE);
        db.execSQL(DatabaseContract.CategoryTable.DROP_TABLE);
        db.execSQL(DatabaseContract.ExpenseTable.DROP_TABLE);
        db.execSQL(DatabaseContract.BudgetTable.DROP_TABLE);
        db.execSQL(DatabaseContract.MonthlyReportTable.DROP_TABLE);
        db.execSQL(DatabaseContract.NotificationTable.DROP_TABLE);
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
