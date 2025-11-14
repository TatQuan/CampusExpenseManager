package com.example.campusexpensemanager.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.session.Session;

public class ProfileActivity extends AppCompatActivity {

    TextView tvUsername, tvEmail;
    Button btnEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvUsername = findViewById(R.id.tv_edit_title);
        tvEmail = findViewById(R.id.et_email);
        Session session = new Session(this);

        String username = session.getUsername();
        String email = session.getEmail();
        int userId = session.getUserId();


        tvUsername.setText(session.getUsername());
        tvEmail.setText(session.getEmail());
    }
}