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

    // Lưu session - DÙNG CHÍNH CÁC KEY Ở TRÊN
    public void saveSession(int id, String username, String email, String role, String createdAt){
        editor.putInt(KEY_USER_ID, id);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ROLE, role);
        editor.putString(KEY_CREATED_AT, createdAt);
        editor.apply();
    }

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

    public boolean isLoggedIn() {
        return prefs.contains(KEY_USER_ID);
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}

