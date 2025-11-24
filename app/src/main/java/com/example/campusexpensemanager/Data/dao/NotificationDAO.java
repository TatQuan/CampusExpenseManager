package com.example.campusexpensemanager.Data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.campusexpensemanager.Data.DatabaseContract;
import com.example.campusexpensemanager.Data.DatabaseHelper;
import com.example.campusexpensemanager.models.Notification;

import java.util.ArrayList;
import java.util.List;


public class NotificationDAO {
    private DatabaseHelper dbHelper;

    public NotificationDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Thêm notification mới
    public void addNotification(int userId, int categoryId, String message) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.NotificationTable.COLUMN_USER_ID, userId);
        values.put(DatabaseContract.NotificationTable.COLUMN_CATEGORY_ID, categoryId);
        values.put(DatabaseContract.NotificationTable.COLUMN_MESSAGE, message);
        values.put(DatabaseContract.NotificationTable.COLUMN_IS_READ, 0);

        db.insert(DatabaseContract.NotificationTable.TABLE_NAME, null, values);
    }

    // Lấy danh sách notification theo userId
    public List<Notification> getNotificationsByUser(int userId) {
        List<Notification> notifications = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DatabaseContract.NotificationTable.COLUMN_ID,
                DatabaseContract.NotificationTable.COLUMN_USER_ID,
                DatabaseContract.NotificationTable.COLUMN_CATEGORY_ID,
                DatabaseContract.NotificationTable.COLUMN_MESSAGE,
                DatabaseContract.NotificationTable.COLUMN_DATE,
                DatabaseContract.NotificationTable.COLUMN_IS_READ
        };
        String selection = DatabaseContract.NotificationTable.COLUMN_USER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(userId) };
        Cursor cursor = db.query(
                DatabaseContract.NotificationTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                DatabaseContract.NotificationTable.COLUMN_DATE + " DESC"
        );
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.NotificationTable.COLUMN_ID));
            int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.NotificationTable.COLUMN_CATEGORY_ID));
            String message = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NotificationTable.COLUMN_MESSAGE));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.NotificationTable.COLUMN_DATE));
            int isRead = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.NotificationTable.COLUMN_IS_READ));
            notifications.add(new Notification(id, userId, categoryId, message, date, isRead));
        }
        cursor.close();
        return notifications;
    }

    // Đánh dấu notification đã đọc
    public void markNotificationAsRead(int notificationId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.NotificationTable.COLUMN_IS_READ, 1);
        String selection = DatabaseContract.NotificationTable.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(notificationId) };
        db.update(DatabaseContract.NotificationTable.TABLE_NAME, values, selection, selectionArgs);
    }
}
