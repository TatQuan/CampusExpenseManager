package com.example.campusexpensemanager.view;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.campusexpensemanager.Data.DatabaseContract;
import com.example.campusexpensemanager.Data.dao.UserDAO;
import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.models.User;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText edtUsername;
    EditText edtPassword;
    UserDAO userDAO;
    User user;
    TextView registerLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        userDAO = new UserDAO(this);
        btnLogin = findViewById(R.id.btn_enter);
        edtUsername = findViewById(R.id.et_username);
        edtPassword = findViewById(R.id.et_password);
        registerLink = findViewById(R.id.tv_register);

        //Setup toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Login");
        }

        btnLogin.setOnClickListener(view -> {
            String username = edtUsername.getText().toString();
            String password = edtPassword.getText().toString();

            //Check username and password is empty
            if (username.isEmpty() || password.isEmpty()) {
                edtUsername.setError("Username is required");
                edtPassword.setError("Password is required");
                return;
            }

            //Check username and password is valid
            try{
                user = userDAO.login(username, password);
                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("USERNAME", username);
                    intent.putExtra("PASSWORD", password);
                    intent.putExtra("EMAIL", user.getEmail());
                    intent.putExtra("USER_ID", user.getUserId());
                    startActivity(intent);
                    finish();
                } else {
                    Toast toast = Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        });


        registerLink.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

    }
}