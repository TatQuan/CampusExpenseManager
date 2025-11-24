package com.example.campusexpensemanager.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.session.Session;

public class ProfileActivity extends AddExpenseActivity {

    TextView tvUsername, tvEmail, tvCreatedAt;
    ImageView btnEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Set action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Profile");
        }

        // Initialize views
        tvUsername = findViewById(R.id.tv_edit_title);
        tvEmail = findViewById(R.id.et_email);
        tvCreatedAt = findViewById(R.id.tv_createdAt);
        btnEditProfile = findViewById(R.id.btn_edit_profile);

        // Get session
        Session session = new Session(this);
        int userId = session.getUserId();
        String username = session.getUsername();
        String email = session.getEmail();
        String createdAt = session.getCreatedAt();

        tvUsername.setText(username);
        tvEmail.setText(email);
        tvCreatedAt.setText(createdAt);

        btnEditProfile.setOnClickListener(view -> {
            //TODO: Edit profile
        });


    }
}