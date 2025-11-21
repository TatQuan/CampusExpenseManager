package com.example.campusexpensemanager.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.session.Session;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends BaseActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setActivityLayout(R.layout.activity_home);

        Session session = new Session(this);

        String username = session.getUsername();
        String email = session.getEmail();
        int userId = session.getUserId();


        //Hide action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //Button event listener
        LinearLayout btnSetBudget = findViewById(R.id.btn_set_budget);
        btnSetBudget.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this, SetBudgetActivity.class));
        });

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
    }
}