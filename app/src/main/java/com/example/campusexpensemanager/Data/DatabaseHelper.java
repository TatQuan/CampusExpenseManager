package com.example.campusexpensemanager.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.campusexpensemanager.Data.DatabaseContract;
import com.example.campusexpensemanager.models.User;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CampusExpenseManager.db";
    private static final int DATABASE_VERSION = 2; // tăng phiên bản vì thêm cột role

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
        insertDataExpenseForUser2(db);
        insertDataBudgetForUser2(db);
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

            /* ==========================================================
               ==================== THÁNG 12 / 2025 =====================
               ========================================================== */
                // FOOD (1)
                "('Bánh mì buổi sáng', 35000, '2025-12-03', NULL, NULL, 1, 1, 0)," +
                "('Lunch Phở bò', 55000, '2025-12-10', NULL, NULL, 1, 1, 0)," +
                "('Cơm gà tối', 45000, '2025-12-20', NULL, NULL, 1, 1, 0)," +

                // TRANSPORTATION (2)
                "('Bus ticket', 12000, '2025-12-01', NULL, NULL, 2, 1, 0)," +
                "('Grab bike', 25000, '2025-12-15', NULL, NULL, 2, 1, 0)," +
                "('Train ticket', 40000, '2025-12-25', NULL, NULL, 2, 1, 0)," +

                // HEALTHCARE / FITNESS (3)
                "('Mua thuốc cảm', 90000, '2025-12-05', NULL, NULL, 3, 1, 0)," +
                "('Clinic check', 150000, '2025-12-18', NULL, NULL, 3, 1, 0)," +
                "('Gym Membership', 500000, '2025-12-03', '2025-01-01', '2025-12-31', 3, 1, 1)," +

                // EDUCATION (4)
                "('Buy notebook', 15000, '2025-12-02', NULL, NULL, 4, 1, 0)," +
                "('Buy textbook', 120000, '2025-12-10', NULL, NULL, 4, 1, 0)," +
                "('Online course fee', 300000, '2025-12-22', NULL, NULL, 4, 1, 0)," +

                // CLOTHING (5)
                "('T-shirt basic', 120000, '2025-12-08', NULL, NULL, 5, 1, 0)," +
                "('Jeans Uniqlo', 350000, '2025-12-19', NULL, NULL, 5, 1, 0)," +
                "('Jacket mùa đông', 500000, '2025-12-28', NULL, NULL, 5, 1, 0)," +

                // UTILITIES (6)
                "('Electricity Bill', 750000, '2025-12-15', '2025-01-01', '2025-12-31', 6, 1, 1)," +
                "('Nước tháng 12', 150000, '2025-12-16', NULL, NULL, 6, 1, 0)," +
                "('Internet tháng 12', 200000, '2025-12-05', NULL, NULL, 6, 1, 0)," +

                // RENT (7)
                "('Tiền nhà tháng 12', 3500000, '2025-12-01', '2025-01-01', '2025-12-31', 7, 1, 1)," +
                "('Dọn phòng', 100000, '2025-12-10', NULL, NULL, 7, 1, 0)," +
                "('Sửa bóng đèn', 50000, '2025-12-20', NULL, NULL, 7, 1, 0)," +

                // COSMETICS (8)
                "('Kem dưỡng ẩm', 85000, '2025-12-09', NULL, NULL, 8, 1, 0)," +
                "('Shampoo', 60000, '2025-12-17', NULL, NULL, 8, 1, 0)," +
                "('Bộ skincare', 250000, '2025-12-23', NULL, NULL, 8, 1, 0)," +

            /* ==========================================================
               ==================== THÁNG 11 / 2025 =====================
               ========================================================== */

                // FOOD
                "('Bánh cuốn', 40000, '2025-11-04', NULL, NULL, 1, 1, 0)," +
                "('Mì udon', 65000, '2025-11-11', NULL, NULL, 1, 1, 0)," +
                "('Cơm thịt nướng', 70000, '2025-11-22', NULL, NULL, 1, 1, 0)," +

                // TRANSPORTATION
                "('Bus tháng 11', 12000, '2025-11-02', NULL, NULL, 2, 1, 0)," +
                "('Taxi short ride', 30000, '2025-11-13', NULL, NULL, 2, 1, 0)," +
                "('Metro ticket', 15000, '2025-11-26', NULL, NULL, 2, 1, 0)," +

                // HEALTHCARE
                "('Vitamin C', 50000, '2025-11-05', NULL, NULL, 3, 1, 0)," +
                "('Khám da liễu', 200000, '2025-11-14', NULL, NULL, 3, 1, 0)," +
                "('Massage vai gáy', 180000, '2025-11-27', NULL, NULL, 3, 1, 0)," +

                // EDUCATION
                "('Bút highlight', 25000, '2025-11-03', NULL, NULL, 4, 1, 0)," +
                "('Tài liệu PDF', 90000, '2025-11-12', NULL, NULL, 4, 1, 0)," +
                "('Phí thư viện', 50000, '2025-11-29', NULL, NULL, 4, 1, 0)," +

            /* ==========================================================
               ==================== THÁNG 10 / 2025 =====================
               ========================================================== */

                // FOOD
                "('Bento convenience store', 55000, '2025-10-03', NULL, NULL, 1, 1, 0)," +
                "('Ramen', 80000, '2025-10-14', NULL, NULL, 1, 1, 0)," +
                "('Sushi lunch', 120000, '2025-10-25', NULL, NULL, 1, 1, 0)," +

                // TRANSPORTATION
                "('Bus ticket', 12000, '2025-10-02', NULL, NULL, 2, 1, 0)," +
                "('Bike parking fee', 10000, '2025-10-18', NULL, NULL, 2, 1, 0)," +
                "('Taxi night', 45000, '2025-10-30', NULL, NULL, 2, 1, 0)," +

                // HEALTHCARE
                "('Khám tổng quát', 300000, '2025-10-05', NULL, NULL, 3, 1, 0)," +
                "('Mua thuốc đau bụng', 40000, '2025-10-12', NULL, NULL, 3, 1, 0)," +
                "('Mua khẩu trang', 30000, '2025-10-21', NULL, NULL, 3, 1, 0);"
        );
    }



    public void insertDataBudget(SQLiteDatabase db) {

        db.execSQL("INSERT INTO Budget (userId, categoryId, budgetAmount, remainingBudget, month, year, isDeleted) VALUES " +

                /* ==================== Tháng 12 / 2025 ==================== */
                "(1, 1, 2000000, 2000000, 12, 2025, 0)," +
                "(1, 2, 600000, 600000, 12, 2025, 0)," +
                "(1, 3, 500000, 500000, 12, 2025, 0)," +
                "(1, 4, 400000, 400000, 12, 2025, 0)," +
                "(1, 5, 800000, 800000, 12, 2025, 0)," +
                "(1, 6, 900000, 900000, 12, 2025, 0)," +
                "(1, 7, 3500000, 3500000, 12, 2025, 0)," +
                "(1, 8, 300000, 300000, 12, 2025, 0)," +

                /* ==================== Tháng 11 / 2025 ==================== */
                "(1, 1, 1800000, 1800000, 11, 2025, 0)," +
                "(1, 2, 500000, 500000, 11, 2025, 0)," +
                "(1, 3, 400000, 400000, 11, 2025, 0)," +
                "(1, 4, 300000, 300000, 11, 2025, 0)," +
                "(1, 5, 600000, 600000, 11, 2025, 0)," +
                "(1, 6, 850000, 850000, 11, 2025, 0)," +
                "(1, 7, 3500000, 3500000, 11, 2025, 0)," +
                "(1, 8, 250000, 250000, 11, 2025, 0)," +

                /* ==================== Tháng 10 / 2025 ==================== */
                "(1, 1, 1500000, 1500000, 10, 2025, 0)," +
                "(1, 2, 500000, 500000, 10, 2025, 0)," +
                "(1, 3, 450000, 450000, 10, 2025, 0)," +
                "(1, 4, 250000, 250000, 10, 2025, 0)," +
                "(1, 5, 500000, 500000, 10, 2025, 0)," +
                "(1, 6, 800000, 800000, 10, 2025, 0)," +
                "(1, 7, 3500000, 3500000, 10, 2025, 0)," +
                "(1, 8, 200000, 200000, 10, 2025, 0);"
        );
    }


    public void insertDataExpenseForUser2(SQLiteDatabase db) {

        db.execSQL("INSERT INTO Expense (description, amount, date, start_date, end_date, categoryId, userId, isRecurring) VALUES " +

        /* ==========================================================
           ==================== THÁNG 12 / 2025 =====================
           ========================================================== */
                        // FOOD (1)
                        "('Bánh mì buổi sáng', 35000, '2025-12-04', NULL, NULL, 1, 2, 0)," +
                        "('Lunch Phở bò', 55000, '2025-12-11', NULL, NULL, 1, 2, 0)," +
                        "('Cơm gà tối', 45000, '2025-12-21', NULL, NULL, 1, 2, 0)," +

                        // TRANSPORTATION (2)
                        "('Bus ticket', 12000, '2025-12-02', NULL, NULL, 2, 2, 0)," +
                        "('Grab bike', 25000, '2025-12-16', NULL, NULL, 2, 2, 0)," +
                        "('Train ticket', 40000, '2025-12-26', NULL, NULL, 2, 2, 0)," +

                        // HEALTHCARE / FITNESS (3)
                        "('Mua thuốc cảm', 90000, '2025-12-06', NULL, NULL, 3, 2, 0)," +
                        "('Clinic check', 150000, '2025-12-19', NULL, NULL, 3, 2, 0)," +
                        "('Gym Membership', 500000, '2025-12-04', '2025-01-01', '2025-12-31', 3, 2, 1)," +

                        // EDUCATION (4)
                        "('Buy notebook', 15000, '2025-12-03', NULL, NULL, 4, 2, 0)," +
                        "('Buy textbook', 120000, '2025-12-11', NULL, NULL, 4, 2, 0)," +
                        "('Online course fee', 300000, '2025-12-23', NULL, NULL, 4, 2, 0)," +

                        // CLOTHING (5)
                        "('T-shirt basic', 120000, '2025-12-09', NULL, NULL, 5, 2, 0)," +
                        "('Jeans Uniqlo', 350000, '2025-12-20', NULL, NULL, 5, 2, 0)," +
                        "('Jacket mùa đông', 500000, '2025-12-29', NULL, NULL, 5, 2, 0)," +

                        // UTILITIES (6)
                        "('Electricity Bill', 750000, '2025-12-16', '2025-01-01', '2025-12-31', 6, 2, 1)," +
                        "('Nước tháng 12', 150000, '2025-12-17', NULL, NULL, 6, 2, 0)," +
                        "('Internet tháng 12', 200000, '2025-12-06', NULL, NULL, 6, 2, 0)," +

                        // RENT (7)
                        "('Tiền nhà tháng 12', 3500000, '2025-12-02', '2025-01-01', '2025-12-31', 7, 2, 1)," +
                        "('Dọn phòng', 100000, '2025-12-11', NULL, NULL, 7, 2, 0)," +
                        "('Sửa bóng đèn', 50000, '2025-12-21', NULL, NULL, 7, 2, 0)," +

                        // COSMETICS (8)
                        "('Kem dưỡng ẩm', 85000, '2025-12-10', NULL, NULL, 8, 2, 0)," +
                        "('Shampoo', 60000, '2025-12-18', NULL, NULL, 8, 2, 0)," +
                        "('Bộ skincare', 250000, '2025-12-24', NULL, NULL, 8, 2, 0)," +


        /* ==========================================================
           ==================== THÁNG 11 / 2025 =====================
           ========================================================== */

                        // FOOD
                        "('Bánh cuốn', 40000, '2025-11-05', NULL, NULL, 1, 2, 0)," +
                        "('Mì udon', 65000, '2025-11-12', NULL, NULL, 1, 2, 0)," +
                        "('Cơm thịt nướng', 70000, '2025-11-23', NULL, NULL, 1, 2, 0)," +

                        // TRANSPORTATION
                        "('Bus tháng 11', 12000, '2025-11-03', NULL, NULL, 2, 2, 0)," +
                        "('Taxi short ride', 30000, '2025-11-14', NULL, NULL, 2, 2, 0)," +
                        "('Metro ticket', 15000, '2025-11-27', NULL, NULL, 2, 2, 0)," +

                        // HEALTHCARE
                        "('Vitamin C', 50000, '2025-11-06', NULL, NULL, 3, 2, 0)," +
                        "('Khám da liễu', 200000, '2025-11-15', NULL, NULL, 3, 2, 0)," +
                        "('Massage vai gáy', 180000, '2025-11-28', NULL, NULL, 3, 2, 0)," +

                        // EDUCATION
                        "('Bút highlight', 25000, '2025-11-04', NULL, NULL, 4, 2, 0)," +
                        "('Tài liệu PDF', 90000, '2025-11-13', NULL, NULL, 4, 2, 0)," +
                        "('Phí thư viện', 50000, '2025-11-30', NULL, NULL, 4, 2, 0)," +


        /* ==========================================================
           ==================== THÁNG 10 / 2025 =====================
           ========================================================== */

                        // FOOD
                        "('Bento convenience store', 55000, '2025-10-04', NULL, NULL, 1, 2, 0)," +
                        "('Ramen', 80000, '2025-10-15', NULL, NULL, 1, 2, 0)," +
                        "('Sushi lunch', 120000, '2025-10-26', NULL, NULL, 1, 2, 0)," +

                        // TRANSPORTATION
                        "('Bus ticket', 12000, '2025-10-03', NULL, NULL, 2, 2, 0)," +
                        "('Bike parking fee', 10000, '2025-10-19', NULL, NULL, 2, 2, 0)," +
                        "('Taxi night', 45000, '2025-10-31', NULL, NULL, 2, 2, 0)," +

                        // HEALTHCARE
                        "('Khám tổng quát', 300000, '2025-10-06', NULL, NULL, 3, 2, 0)," +
                        "('Mua thuốc đau bụng', 40000, '2025-10-13', NULL, NULL, 3, 2, 0)," +
                        "('Mua khẩu trang', 30000, '2025-10-22', NULL, NULL, 3, 2, 0);"
        );
    }

    public void insertDataBudgetForUser2(SQLiteDatabase db) {

        db.execSQL("INSERT INTO Budget (userId, categoryId, budgetAmount, remainingBudget, month, year, isDeleted) VALUES " +

                /* ==================== Tháng 12 / 2025 ==================== */
                "(2, 1, 2000000, 2000000, 12, 2025, 0)," +
                "(2, 2, 600000, 600000, 12, 2025, 0)," +
                "(2, 3, 500000, 500000, 12, 2025, 0)," +
                "(2, 4, 400000, 400000, 12, 2025, 0)," +
                "(2, 5, 800000, 800000, 12, 2025, 0)," +
                "(2, 6, 900000, 900000, 12, 2025, 0)," +
                "(2, 7, 3500000, 3500000, 12, 2025, 0)," +
                "(2, 8, 300000, 300000, 12, 2025, 0)," +

                /* ==================== Tháng 11 / 2025 ==================== */
                "(2, 1, 1800000, 1800000, 11, 2025, 0)," +
                "(2, 2, 500000, 500000, 11, 2025, 0)," +
                "(2, 3, 400000, 400000, 11, 2025, 0)," +
                "(2, 4, 300000, 300000, 11, 2025, 0)," +
                "(2, 5, 600000, 600000, 11, 2025, 0)," +
                "(2, 6, 850000, 850000, 11, 2025, 0)," +
                "(2, 7, 3500000, 3500000, 11, 2025, 0)," +
                "(2, 8, 250000, 250000, 11, 2025, 0)," +

                /* ==================== Tháng 10 / 2025 ==================== */
                "(2, 1, 1500000, 1500000, 10, 2025, 0)," +
                "(2, 2, 500000, 500000, 10, 2025, 0)," +
                "(2, 3, 450000, 450000, 10, 2025, 0)," +
                "(2, 4, 250000, 250000, 10, 2025, 0)," +
                "(2, 5, 500000, 500000, 10, 2025, 0)," +
                "(2, 6, 800000, 800000, 10, 2025, 0)," +
                "(2, 7, 3500000, 3500000, 10, 2025, 0)," +
                "(2, 8, 200000, 200000, 10, 2025, 0);"
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
