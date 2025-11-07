package com.example.campusexpensemanager.Data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.campusexpensemanager.Data.DatabaseContract;
import com.example.campusexpensemanager.Data.DatabaseHelper;
import com.example.campusexpensemanager.models.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;


public class UserDAO {
    private DatabaseHelper dbHelper;

    // Constructor - Nhận Context
    public UserDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // INSERT - Thêm user mới
    public void insertUser(String username, String password, String email, String role) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql = "INSERT INTO " + DatabaseContract.UserTable.TABLE_NAME +
                " (username, email, password, role) VALUES('" +
                username + "', '" + email + "', '" + password + "', '" + role + "');";
        db.execSQL(sql);
    }

    // READ - lấy danh sách user
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql =
                "SELECT  " +
                        DatabaseContract.UserTable.COLUMN_ID + " AS userId, " +
                        DatabaseContract.UserTable.COLUMN_USERNAME + " AS username, " +
                        DatabaseContract.UserTable.COLUMN_EMAIL + " AS email, " +
                        DatabaseContract.UserTable.COLUMN_PASSWORD + " AS password, " +
                        DatabaseContract.UserTable.COLUMN_ROLE + " AS role, " +
                        DatabaseContract.UserTable.COLUMN_CREATED_AT + " AS createdAt " +
                "FROM " + DatabaseContract.UserTable.TABLE_NAME;
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
                String createdAt = cursor.getString(cursor.getColumnIndexOrThrow("createdAt"));

                userList.add(new User(id, username, email, password, role, createdAt));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return userList;
    }

    // UPDATE - sửa user
    public void updateUser(int userId, String username, String email, String password, String role) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "UPDATE " + DatabaseContract.UserTable.TABLE_NAME +
                " SET username='" + username +
                "', email='" + email +
                "', password='" + password +
                "', role='" + role +
                "' WHERE userId=" + userId + ";";
        db.execSQL(sql);
    }

    // DELETE - xóa user (xóa mền
    public void deleteUser(int userId) {
        int isDeleted = 1;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "UPDATE " + DatabaseContract.UserTable.TABLE_NAME +
                " SET isDeleted='" + isDeleted +
                "' WHERE userId=" + userId + ";";
        db.execSQL(sql);
    }

    // LOGIN - kiểm tra username/password
    public User login(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + DatabaseContract.UserTable.TABLE_NAME +
                " WHERE username='" + username + "' AND password='" + password + "' LIMIT 1;";
        Cursor cursor = db.rawQuery(sql, null);
        User user = null;

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
            String createdAt = cursor.getString(cursor.getColumnIndexOrThrow("createdAt"));

            user = new User(id, username, email, password, role, createdAt);
        }

        cursor.close();
        return user;

    }
}
