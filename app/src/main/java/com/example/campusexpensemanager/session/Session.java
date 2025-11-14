package com.example.campusexpensemanager.session;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {

    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USERNAME = "USERNAME";
    private static final String KEY_EMAIL = "EMAIL";
    private static final String KEY_USER_ID = "USER_ID";

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public Session(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // Lưu session
    public void saveUserSession(int userId, String username, String email) {
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    // Lấy userId
    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    public String getUsername() {
        return prefs.getString(KEY_USERNAME, "");
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, "");
    }

    // Kiểm tra đăng nhập
    public boolean isLoggedIn() {
        return prefs.contains(KEY_USER_ID);
    }

    // Xoá session (logout)
    public void logout() {
        editor.clear();
        editor.apply();
    }
}
