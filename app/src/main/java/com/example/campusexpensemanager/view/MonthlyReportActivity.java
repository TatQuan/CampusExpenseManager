package com.example.campusexpensemanager.view;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.Data.dao.MonthlyReportDAO;
import com.example.campusexpensemanager.adapters.MonthlyReportAdapter;
import com.example.campusexpensemanager.models.MonthlyReport;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.appbar.MaterialToolbar;

import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MonthlyReportActivity extends AppCompatActivity {

    private TextView tvCurrentPeriod, tvTotalBudget, tvTotalExpense, tvTotalRemaining;
    private RecyclerView rvMonthlyReport;
    private PieChart pieChart;
    private MonthlyReportDAO monthlyReportDAO;
    private final DecimalFormat moneyFormat = new DecimalFormat("#,##0.##");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_report);

        initViews();

        // TODO: Lấy userId thực tế từ Session / SharedPreferences tuỳ app của cậu
        int userId = getCurrentUserId();

        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        String period = String.format(Locale.getDefault(), "%04d-%02d", year, month);

        tvCurrentPeriod.setText(String.format(Locale.getDefault(),
                "Month %02d / %d", month, year));

        monthlyReportDAO = new MonthlyReportDAO(this);

        // 1. Generate report cho tháng hiện tại
        monthlyReportDAO.generateMonthlyReport(userId, month, year);

        // 2. Lấy list report
        List<MonthlyReport> reports = monthlyReportDAO.getMonthlyReportByPeriod(period);

        // 3. Bind list + tổng quan + chart
        setupSummary(reports);
        setupRecyclerView(reports);
        setupPieChart(reports);
    }

    private void initViews() {

        // Set action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Monthly Report");
        }

        tvCurrentPeriod = findViewById(R.id.tvCurrentPeriod);
        tvTotalBudget = findViewById(R.id.tvTotalBudget);
        tvTotalExpense = findViewById(R.id.tvTotalExpense);
        tvTotalRemaining = findViewById(R.id.tvTotalRemaining);
        rvMonthlyReport = findViewById(R.id.rvMonthlyReport);
        pieChart = findViewById(R.id.pieChartMonthly);
    }

    private int getCurrentUserId() {
        // TODO: thay bằng logic thực tế của app
        // Ví dụ:
        // Session session = new Session(this);
        // return session.getUserId();
        return 1;
    }

    private void setupSummary(List<MonthlyReport> reports) {
        double totalBudget = 0;
        double totalExpense = 0;
        for (MonthlyReport r : reports) {
            totalBudget += r.getBudget();
            totalExpense += r.getTotalExpense();
        }
        double remaining = totalBudget - totalExpense;

        tvTotalBudget.setText("Total budget: " + moneyFormat.format(totalBudget));
        tvTotalExpense.setText("Total expense: " + moneyFormat.format(totalExpense));
        tvTotalRemaining.setText("Remaining: " + moneyFormat.format(remaining));
    }

    private void setupRecyclerView(List<MonthlyReport> reports) {
        rvMonthlyReport.setLayoutManager(new LinearLayoutManager(this));
        MonthlyReportAdapter adapter = new MonthlyReportAdapter(this, reports);
        rvMonthlyReport.setAdapter(adapter);
    }

    private void setupPieChart(List<MonthlyReport> reports) {
        List<PieEntry> entries = new ArrayList<>();

        for (MonthlyReport r : reports) {
            if (r.getTotalExpense() > 0) {
                String label = "Category #" + r.getCategoryId();
                entries.add(new PieEntry((float) r.getTotalExpense(), label));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setSliceSpace(2f);
        dataSet.setValueTextSize(11f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(false);
        pieChart.setEntryLabelTextSize(11f);
        pieChart.setHoleColor(android.graphics.Color.WHITE);
        pieChart.setCenterText("Expense");
        pieChart.setCenterTextColor(Color.parseColor("#E91E63"));
        pieChart.setCenterTextSize(14f);

        pieChart.invalidate();
    }
}
