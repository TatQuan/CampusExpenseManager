package com.example.campusexpensemanager.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.R;

public class ProfileActivity extends BaseActivity {

    TextView tvUsername, tvEmail;
    Button btnEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_profile);

        tvUsername = findViewById(R.id.tv_edit_title);
        tvEmail = findViewById(R.id.et_email);

        String username = getUsername();
        String email = getEmail();
        int userId = getUserId();


        tvUsername.setText(username);
        tvEmail.setText(email);
    }
}