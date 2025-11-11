package com.example.campusexpensemanager.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.campusexpensemanager.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        //Hide action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //Button event listener
        LinearLayout btnAddExpense = findViewById(R.id.btn_add_expense);
        btnAddExpense.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this, AddExpenseActivity.class));
        });

        LinearLayout btnNotification = findViewById(R.id.btn_notification);
        btnNotification.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
        });

        LinearLayout btnCategory = findViewById(R.id.btn_category);
        btnCategory.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this, CategoryActivity.class));
        });

        LinearLayout btnProfile = findViewById(R.id.btn_profile);
        btnProfile.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
        });

        LinearLayout btnMonthlyReport = findViewById(R.id.btn_monthly_report);
        btnMonthlyReport.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this, MonthlyReportActivity.class));
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                
            } else if (id == R.id.nav_category) {
                startActivity(new Intent(HomeActivity.this, CategoryActivity.class));

            } else if (id == R.id.nav_addexpense) {
                startActivity(new Intent(HomeActivity.this, AddExpenseActivity.class));

            } else if (id == R.id.nav_notification) {
                startActivity(new Intent(HomeActivity.this, NotificationActivity.class));

            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));

            }
            return true;
        });
    }
}