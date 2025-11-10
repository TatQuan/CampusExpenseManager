package com.example.campusexpensemanager.view;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.campusexpensemanager.Data.dao.UserDAO;
import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.models.User;

public class RegisterActivity extends AppCompatActivity {

    TextView loginLink;
    EditText edtEmail;
    EditText edtUsername;
    EditText edtPassword;
    EditText edtReenterPassword;

    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Setup toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Register");
        }

        //Back login by click on link login below
        loginLink = findViewById(R.id.tv_login);
        loginLink.setOnClickListener(view -> {
            onBackPressed();
        });

        //Register
        btnRegister = findViewById(R.id.btn_enter);
        edtEmail = findViewById(R.id.et_email);
        edtUsername = findViewById(R.id.et_username);
        edtPassword = findViewById(R.id.et_password);
        edtReenterPassword = findViewById(R.id.et_reente_password);

        btnRegister.setOnClickListener(view -> {
            String email = edtEmail.getText().toString();
            String username = edtUsername.getText().toString();
            String password = edtPassword.getText().toString();
            String reenterPassword = edtReenterPassword.getText().toString();

            if (email.isEmpty() || username.isEmpty() || password.isEmpty() || reenterPassword.isEmpty()) {
                edtEmail.setError("Email is required");
                edtUsername.setError("Username is required");
                edtPassword.setError("Password is required");
                edtReenterPassword.setError("Re-enter password is required");
                return;
            }

            if (password.length() < 6) {
                edtPassword.setError("Password must be at least 6 characters");
                return;
            }

            if (!password.equals(reenterPassword)) {
                edtReenterPassword.setError("Password does not match");
                return;
            }

            //Insert user if all done
            try{
                UserDAO userDAO = new UserDAO(this);
                userDAO.insertUser(username, password, email);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {
                Toast toast = Toast.makeText(RegisterActivity.this, "Register successfully", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }


        });

    }

    //Back to login by click on back button on toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}