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
// INSERT - Thêm user mới (có role)
    public void insertUser(String username, String password, String email, String role) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.UserTable.COLUMN_USERNAME, username);
        values.put(DatabaseContract.UserTable.COLUMN_EMAIL, email);
        values.put(DatabaseContract.UserTable.COLUMN_PASSWORD, password);
        values.put(DatabaseContract.UserTable.COLUMN_ROLE, role);

        db.insert(DatabaseContract.UserTable.TABLE_NAME, null, values);
    }


    // READ - lấy danh sách user
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql =
                "SELECT * FROM " + DatabaseContract.UserTable.TABLE_NAME +
                        " WHERE " + DatabaseContract.UserTable.COLUMN_IS_DELETED + " = 0";

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_ID));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_USERNAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_EMAIL));
                String role = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_ROLE));
                String createdAt = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_CREATED_AT));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_PASSWORD));

                // CHÚ Ý: đúng thứ tự constructor User(int id, String username, String email, String role, String createdAt, String password)
                userList.add(new User(id, username, email, role, createdAt, password));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return userList;
    }


    // UPDATE - sửa user
    public void updateUser(int userId, String username, String email, String password, String role) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql = "UPDATE " + DatabaseContract.UserTable.TABLE_NAME +
                " SET " +
                DatabaseContract.UserTable.COLUMN_USERNAME + " = '" + username + "', " +
                DatabaseContract.UserTable.COLUMN_EMAIL + " = '" + email + "', " +
                DatabaseContract.UserTable.COLUMN_PASSWORD + " = '" + password + "', " +
                DatabaseContract.UserTable.COLUMN_ROLE + " = '" + role + "' " +
                " WHERE " + DatabaseContract.UserTable.COLUMN_ID + " = " + userId + ";";

        db.execSQL(sql);
    }


    // DELETE - xóa user (xóa mền
    public void deleteUser(int userId) {
        int isDeleted = 1;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "UPDATE " + DatabaseContract.UserTable.TABLE_NAME +
                " SET " + DatabaseContract.UserTable.COLUMN_IS_DELETED + "='" + isDeleted +
                "' WHERE " + DatabaseContract.UserTable.COLUMN_ID + "=" + userId + ";";
        db.execSQL(sql);
    }

    // LOGIN - kiểm tra username/password
    public User login(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + DatabaseContract.UserTable.TABLE_NAME +
                " WHERE " + DatabaseContract.UserTable.COLUMN_USERNAME + "='" + username + "'" +
                " AND " + DatabaseContract.UserTable.COLUMN_PASSWORD + "='" + password + "'" +
                " AND " + DatabaseContract.UserTable.COLUMN_IS_DELETED + " = 0" +
                " LIMIT 1;";

        Cursor cursor = db.rawQuery(sql, null);
        User user = null;

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_ID));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_EMAIL));
            String role = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_ROLE));
            String createdAt = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_CREATED_AT));

            user = new User(id, username, email, role, createdAt, password);
        }

        cursor.close();
        return user;

    }

    //GET USER BY NAME
    public User getUserByName(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + DatabaseContract.UserTable.TABLE_NAME +
                " WHERE " + DatabaseContract.UserTable.COLUMN_USERNAME + "='" + username + "'" +
                " AND " + DatabaseContract.UserTable.COLUMN_IS_DELETED + " = 0" +
                " LIMIT 1;";

        Cursor cursor = db.rawQuery(sql, null);
        User user = null;

        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_ID));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_EMAIL));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_PASSWORD));
            String role = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_ROLE));
            String createdAt = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_CREATED_AT));

            user = new User(userId, username, email, role, createdAt, password);
        }

        cursor.close();
        return user;
    }

}
