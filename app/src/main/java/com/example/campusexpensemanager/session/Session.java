package com.example.campusexpensemanager.session;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {

    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USERNAME = "USERNAME";
    private static final String KEY_EMAIL = "EMAIL";
    private static final String KEY_USER_ID = "USER_ID";
    private static final String KEY_ROLE = "ROLE";
    private static final String KEY_CREATED_AT = "CREATED_AT";


    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public Session(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // Lưu session
    public void saveSession(int id, String username, String email, String role, String createdAt){
        editor.putInt("SESSION_ID", id);
        editor.putString("SESSION_USERNAME", username);
        editor.putString("SESSION_EMAIL", email);
        editor.putString("SESSION_ROLE", role);
        editor.putString("SESSION_CREATED_AT", createdAt);

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

    public String getRole() {
        return prefs.getString(KEY_ROLE, "");
    }

    public String getCreatedAt() {
        return prefs.getString(KEY_CREATED_AT, "");
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
