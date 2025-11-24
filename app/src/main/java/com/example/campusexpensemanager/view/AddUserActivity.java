package com.example.campusexpensemanager.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.Data.dao.UserDAO;
import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.session.Session;

public class AddUserActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword;
    private Spinner spRole;
    private Button btnCancel, btnSave;

    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        // Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add User");
        }

        userDAO = new UserDAO(this);

        etUsername = findViewById(R.id.et_add_username);
        etEmail = findViewById(R.id.et_add_email);
        etPassword = findViewById(R.id.et_add_password);
        spRole = findViewById(R.id.sp_add_role);
        btnCancel = findViewById(R.id.btn_cancel_add_user);
        btnSave = findViewById(R.id.btn_save_add_user);

        setupRoleSpinner();
        setupButtons();
    }

    private void setupRoleSpinner() {
        String[] roles = new String[]{"STUDENT", "ADMIN"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                roles
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRole.setAdapter(adapter);
    }

    private void setupButtons() {
        btnCancel.setOnClickListener(v -> {
            finish();
        });

        btnSave.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String role = spRole.getSelectedItem().toString();

            if (TextUtils.isEmpty(username)) {
                etUsername.setError("Username is required");
                return;
            }

            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Email is required");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                etPassword.setError("Password is required");
                return;
            }

            // Insert user vào DB
            userDAO.insertUser(username, password, email, role);

            Toast.makeText(AddUserActivity.this, "User added successfully", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        });
    }

}
