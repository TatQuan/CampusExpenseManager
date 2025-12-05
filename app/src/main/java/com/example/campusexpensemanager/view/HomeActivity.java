package com.example.campusexpensemanager.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;

import com.example.campusexpensemanager.Data.dao.CategoryDAO;
import com.example.campusexpensemanager.Data.dao.MonthlyReportDAO;
import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.models.Category;
import com.example.campusexpensemanager.models.MonthlyReport;
import com.example.campusexpensemanager.session.Session;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AddExpenseActivity {
    private Intent intent;
    private PieChart pieChart;
    private MonthlyReportDAO monthlyReportDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);


        int userId = getCurrentUserId();

        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        String period = String.format(Locale.getDefault(), "%04d-%02d", year, month);
        monthlyReportDAO = new MonthlyReportDAO(this);
        // 1. Generate report cho tháng hiện tại
        monthlyReportDAO.generateMonthlyReport(userId, month, year);

        // 2. Lấy list report
        List<MonthlyReport> reports = monthlyReportDAO.getMonthlyReportByPeriod(period);

        pieChart = findViewById(R.id.pieChartMonthly);
        setupPieChart(reports);

        //Get session
        Session session = new Session(this);
        String role = session.getRole();
        if (role.equals("ADMIN")) {
            findViewById(R.id.btn_user).setVisibility(LinearLayout.VISIBLE);
        }

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
//            startActivity(new Intent(HomeActivity.this, ReportActivity.class));
            startActivity(new Intent(this, MonthlyReportActivity.class));

        });

        LinearLayout btnUser = findViewById(R.id.btn_user);
        btnUser.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this, UserActivity.class));
        });
    }

    private void setupPieChart(List<MonthlyReport> reports) {
        List<PieEntry> entries = new ArrayList<>();
        CategoryDAO categoryDAO = new CategoryDAO(this);

        for (MonthlyReport r : reports) {
            if (r.getTotalExpense() > 0) {

                // Lấy tên category
                Category category = categoryDAO.getCategoryById(r.getCategoryId());
                String label = (category != null) ? category.getName() : "";

                // Thêm PieEntry
                entries.add(new PieEntry((float) r.getTotalExpense(), label));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setSliceSpace(2f);
        dataSet.setValueTextSize(11f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);

        // Tắt nhãn category trên biểu đồ
        pieChart.setDrawEntryLabels(false);

        dataSet.setDrawValues(false);

        // Ẩn mô tả
        pieChart.getDescription().setEnabled(false);

        pieChart.setUsePercentValues(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setCenterText("Expense");
        pieChart.setCenterTextColor(Color.parseColor("#E91E63"));
        pieChart.setCenterTextSize(14f);

        pieChart.invalidate();
    }


    private int getCurrentUserId() {
        // TODO: thay bằng logic thực tế của app
        // Ví dụ:
        Session session = new Session(this);
        return session.getUserId();
    }
}