package com.example.campusexpensemanager.Data;

public class DatabaseContract {
    private DatabaseContract() {}

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "CampusExpenseManager.db";

    //User table
    public static class UserTable {
        public static final String TABLE_NAME = "User";
        public static final String COLUMN_ID = "user_id";
        public static final String COLUMN_USERNAME = "user_name";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_ROLE = "role";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_IS_DELETED = "is_deleted";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USERNAME + " TEXT NOT NULL," +
                COLUMN_PASSWORD + " TEXT NOT NULL," +
                COLUMN_EMAIL + " TEXT," +
                COLUMN_ROLE + " TEXT NOT NULL DEFAULT 'STUDENT'," +
                COLUMN_CREATED_AT + " DATE DEFAULT (date('now'))," +
                COLUMN_IS_DELETED + " INTEGER DEFAULT 0" +
                ");";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    //Category table
    public static class CategoryTable {
        public static final String TABLE_NAME = "Category";
        public static final String COLUMN_ID = "category_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_ICON_NAME = "icon_name";
        public static final String COLUMN_IS_DELETED = "is_deleted";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT NOT NULL," +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_ICON_NAME + " TEXT," + // tên icon
                COLUMN_IS_DELETED + " INTEGER DEFAULT 0" +
                ");";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    //Expense table
    public static class ExpenseTable {
        public static final String TABLE_NAME = "Expense";
        public static final String COLUMN_ID = "expense_id";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_START_DATE = "start_date";
        public static final String COLUMN_END_DATE = "end_date";
        public static final String COLUMN_CATEGORY_ID = "categoryId";
        public static final String COLUMN_IS_RECURRING = "isRecurring";
        public static final String COLUMN_USER_ID = "userId";
        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_AMOUNT + " REAL," +
                COLUMN_DATE + " DATE," + // ngày phát sinh (chi tiêu 1 lần hoặc ngày ghi nhận)
                COLUMN_START_DATE + " DATE," + // ngày bắt đầu (cho định kỳ)
                COLUMN_END_DATE + " DATE DEFAULT NULL ," + // ngày kết thúc (cho định kỳ, có thể null)
                COLUMN_CATEGORY_ID + " INTEGER," +
                COLUMN_USER_ID + " INTEGER," +
                COLUMN_IS_RECURRING + " INTEGER DEFAULT 0," + // khoản chi định kỳ, 0=false, 1=true
                "FOREIGN KEY(" + COLUMN_CATEGORY_ID + ") REFERENCES " +
                CategoryTable.TABLE_NAME + "(" + CategoryTable.COLUMN_ID + ")," +
                "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " +
                UserTable.TABLE_NAME + "(" + CategoryTable.COLUMN_ID + ")" +
                ");";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    //Budget table
    public static class BudgetTable {
        public static final String TABLE_NAME = "Budget";
        public static final String COLUMN_ID = "budgetId";
        public static final String COLUMN_USER_ID = "userId";
        public static final String COLUMN_CATEGORY_ID = "categoryId";
        public static final String COLUMN_BUDGET_AMOUNT = "budgetAmount";
        public static final String COLUMN_REMAINING_BUDGET = "remainingBudget";
        public static final String COLUMN_MONTH = "month";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_IS_DELETED = "isDeleted";


        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USER_ID + " INTEGER," +
                COLUMN_CATEGORY_ID + " INTEGER," +
                COLUMN_BUDGET_AMOUNT + " REAL," +
                COLUMN_REMAINING_BUDGET + " REAL," +
                COLUMN_MONTH + " INTEGER," +
                COLUMN_YEAR + " INTEGER," +
                COLUMN_IS_DELETED + " INTEGER DEFAULT 0," +
                "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " +
                UserTable.TABLE_NAME + "(" + UserTable.COLUMN_ID + ")," +
                "FOREIGN KEY (" + COLUMN_CATEGORY_ID + ") REFERENCES " +
                CategoryTable.TABLE_NAME + "(" + CategoryTable.COLUMN_ID + ")" +
                ");";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    //MonthlyReport table
    public static class MonthlyReportTable {
        public static final String TABLE_NAME = "MonthlyReport";
        public static final String COLUMN_ID = "reportId";
        public static final String COLUMN_CATEGORY_ID = "categoryId";
        public static final String COLUMN_PERIOD = "period";
        public static final String COLUMN_BUDGET = "budget";
        public static final String COLUMN_TOTAL_EXPENSE = "totalExpense";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CATEGORY_ID + " INTEGER," +
                COLUMN_PERIOD + " TEXT," +
                COLUMN_BUDGET + " REAL," +
                COLUMN_TOTAL_EXPENSE + " REAL," +
                "FOREIGN KEY (" + COLUMN_CATEGORY_ID + ") REFERENCES " +
                CategoryTable.TABLE_NAME + "(" + CategoryTable.COLUMN_ID + ")" +
                ");";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    //Notification table
    public static class NotificationTable {
        public static final String TABLE_NAME = "Notification";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_MESSAGE = "message";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_IS_READ = "is_read";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USER_ID + " INTEGER," +
                COLUMN_CATEGORY_ID + " INTEGER," +
                COLUMN_MESSAGE + " TEXT NOT NULL," +
                COLUMN_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL," +
                COLUMN_IS_READ + " INTEGER DEFAULT 0," +
                "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " +
                UserTable.TABLE_NAME + "(" + UserTable.COLUMN_ID + ")," +
                "FOREIGN KEY (" + COLUMN_CATEGORY_ID + ") REFERENCES " +
                CategoryTable.TABLE_NAME + "(" + CategoryTable.COLUMN_ID + ")" +
                ");";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
