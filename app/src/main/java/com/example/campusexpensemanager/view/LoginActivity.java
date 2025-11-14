package com.example.campusexpensemanager.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.example.campusexpensemanager.session.Session;

import java.util.LinkedHashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText edtUsername;
    EditText edtPassword;
    UserDAO userDAO;
    User user;

    SharedPreferences prefs;

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
            Map<EditText, String> fields = new LinkedHashMap<>();
            fields.put(edtUsername, "Username is required");
            fields.put(edtPassword, "Password is required");

            for (Map.Entry<EditText, String> entry : fields.entrySet()) {
                EditText field = entry.getKey();
                String message = entry.getValue();

                if (field.getText().toString().trim().isEmpty()) {
                    field.setError(message);
                    field.requestFocus();
                    return;
                }
            }

            //Check username and password is valid
            try{
                user = userDAO.login(username, password);
                if (user != null) {
                    Session session = new Session(LoginActivity.this);
                    session.saveUserSession(
                            user.getUserId(),
                            username,
                            user.getEmail()
                    );

                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}