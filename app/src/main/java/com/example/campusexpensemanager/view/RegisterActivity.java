package com.example.campusexpensemanager.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MenuItem;
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

import com.example.campusexpensemanager.Data.dao.UserDAO;
import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.models.User;

import java.util.LinkedHashMap;
import java.util.Map;

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

            //Check all fields is empty
//            if (email.isEmpty()) {
//                edtEmail.setError("Email is required");
//                return;
//            } else if (username.isEmpty()) {
//                edtUsername.setError("Username is required");
//                return;
//            } else if (password.isEmpty()) {
//                edtPassword.setError("Password is required");
//                return;
//            } else if (reenterPassword.isEmpty()) {
//                edtReenterPassword.setError("Re-enter password is required");
//                return;
//            }

            Map<EditText, String> fields = new LinkedHashMap<>();
            fields.put(edtEmail, "Email is required");
            fields.put(edtUsername, "Username is required");
            fields.put(edtPassword, "Password is required");
            fields.put(edtReenterPassword, "Re-enter password is required");

            for (Map.Entry<EditText, String> entry : fields.entrySet()) {
                EditText field = entry.getKey();
                String message = entry.getValue();

                if (field.getText().toString().trim().isEmpty()) {
                    field.setError(message);
                    field.requestFocus();
                    return;
                }
            }

            //Check email is valid
            if (password.length() < 6) {
                edtPassword.setError("Password must be at least 6 characters");
                return;
            }

            //Check password and reenter password is match
            if (!password.equals(reenterPassword)) {
                edtReenterPassword.setError("Password does not match");
                return;
            }

            //Insert user if all done
            try{
                UserDAO userDAO = new UserDAO(this);
                userDAO.insertUser(username, password, email, "STUDENT");
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