package com.example.campusexpensemanager.Data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.campusexpensemanager.Data.DatabaseContract;
import com.example.campusexpensemanager.Data.DatabaseHelper;
import com.example.campusexpensemanager.models.Category;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;

public class CategoryDAO {
    private DatabaseHelper dbhelper;
    public CategoryDAO(Context context) {
        dbhelper = new DatabaseHelper(context);
    }

    //GET ALL CATEGORY - lấy danh sách category
    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String sql =
                "SELECT * FROM " + DatabaseContract.CategoryTable.TABLE_NAME + " " +
                "WHERE " + DatabaseContract.CategoryTable.COLUMN_IS_DELETED + " = 0 " +
                "ORDER BY " + DatabaseContract.CategoryTable.COLUMN_NAME + " ASC";

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.CategoryTable.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CategoryTable.COLUMN_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CategoryTable.COLUMN_DESCRIPTION));
                categoryList.add(new Category(id, name, description));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return categoryList;
    }

    //ADD CATEGORY - thêm category
    public void addCategory(String name, String description) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.CategoryTable.COLUMN_NAME, name);
        values.put(DatabaseContract.CategoryTable.COLUMN_DESCRIPTION, description);
        db.insert(DatabaseContract.CategoryTable.TABLE_NAME, null, values);
    }

    //UPDATE CATEGORY - sửa category
    public void updateCategory(int id, String name, String description) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.CategoryTable.COLUMN_NAME, name);
        values.put(DatabaseContract.CategoryTable.COLUMN_DESCRIPTION, description);
        db.update(DatabaseContract.CategoryTable.TABLE_NAME, values,
                DatabaseContract.CategoryTable.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    //DELETE CATEGORY - xóa category
    public void deleteCategory(int id) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.CategoryTable.COLUMN_IS_DELETED, 1);
        db.update(DatabaseContract.CategoryTable.TABLE_NAME, values,
                DatabaseContract.CategoryTable.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }
}
