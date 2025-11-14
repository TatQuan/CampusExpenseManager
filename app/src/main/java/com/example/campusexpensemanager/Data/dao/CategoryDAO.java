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
    private  DatabaseHelper dbhelper;
    public CategoryDAO(Context context) {
        dbhelper = new DatabaseHelper(context);
    }

    //GET ALL CATEGORY - lấy danh sách category
    public  List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        String[] columns = {
                DatabaseContract.CategoryTable.COLUMN_ID,
                DatabaseContract.CategoryTable.COLUMN_NAME,
                DatabaseContract.CategoryTable.COLUMN_DESCRIPTION,
                DatabaseContract.CategoryTable.COLUMN_ICON_NAME
        };

        Cursor cursor = db.query(
                DatabaseContract.CategoryTable.TABLE_NAME,
                columns,
                DatabaseContract.CategoryTable.COLUMN_IS_DELETED + "=?",
                new String[]{"0"},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.CategoryTable.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CategoryTable.COLUMN_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CategoryTable.COLUMN_DESCRIPTION));
                String iconName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CategoryTable.COLUMN_ICON_NAME));

                categories.add(new Category(id, name, description, iconName));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return categories;
    }

    //ADD CATEGORY - thêm category
    public long insertCategory(Category category) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.CategoryTable.COLUMN_NAME, category.getName());
        values.put(DatabaseContract.CategoryTable.COLUMN_DESCRIPTION, category.getDescription());
        values.put(DatabaseContract.CategoryTable.COLUMN_ICON_NAME, category.getIconName());
        values.put(DatabaseContract.CategoryTable.COLUMN_IS_DELETED, 0);

        long newId = db.insert(DatabaseContract.CategoryTable.TABLE_NAME, null, values);
        db.close();
        return newId; // trả về ID của dòng vừa thêm
    }

    //UPDATE CATEGORY - sửa category (cần phải sửa)
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
        db.update(
            DatabaseContract.CategoryTable.TABLE_NAME,
            values,
            DatabaseContract.CategoryTable.COLUMN_ID + "=?",
            new String[]{String.valueOf(id)}
        );
        db.close();
    }

    //GET CATEGORY BY ID - lấy category theo id
    public Category getCategoryById(int id) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String[] columns = {
                DatabaseContract.CategoryTable.COLUMN_ID,
                DatabaseContract.CategoryTable.COLUMN_NAME,
                DatabaseContract.CategoryTable.COLUMN_DESCRIPTION,
                DatabaseContract.CategoryTable.COLUMN_ICON_NAME
        };

        Cursor cursor = db.query(
                DatabaseContract.CategoryTable.TABLE_NAME,
                columns,
                DatabaseContract.CategoryTable.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null
        );

        Category category = null;
            if (cursor.moveToFirst()) {
                int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.CategoryTable.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CategoryTable.COLUMN_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CategoryTable.COLUMN_DESCRIPTION));
                String iconName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CategoryTable.COLUMN_ICON_NAME));
                category = new Category(categoryId, name, description, iconName);
            }

            cursor.close();
            db.close();
            return category;
        }
}
