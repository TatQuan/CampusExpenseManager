package com.example.campusexpensemanager.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.Data.dao.BudgetDAO;
import com.example.campusexpensemanager.Data.dao.ExpenseDAO;
import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.models.Budget;
import com.example.campusexpensemanager.session.Session;

public class EditBudgetActivity extends AppCompatActivity {

    private TextView tvBudgetTitle, tvBudgetInfo, tvCurrentSpent;
    private EditText etBudgetAmount;
    private Button btnCancel, btnSave;

    private int categoryId;
    private String categoryName;
    private int month;
    private int year;
    private int userId;

    private BudgetDAO budgetDAO;
    private ExpenseDAO expenseDAO;
    private Budget currentBudget;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_budget);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Edit Budget");
        }

        // Nhận dữ liệu từ Intent
        categoryId = getIntent().getIntExtra(ViewCategoryActivity.EXTRA_CATEGORY_ID, 0);
        categoryName = getIntent().getStringExtra(ViewCategoryActivity.EXTRA_CATEGORY_NAME);
        month = getIntent().getIntExtra("EXTRA_MONTH", 1);
        year = getIntent().getIntExtra("EXTRA_YEAR", 2025);

        session = new Session(this);
        userId = session.getUserId();

        budgetDAO = new BudgetDAO(this);
        expenseDAO = new ExpenseDAO(this);

        tvBudgetTitle = findViewById(R.id.tvBudgetTitle);
        tvBudgetInfo = findViewById(R.id.tvBudgetInfo);
        tvCurrentSpent = findViewById(R.id.tvCurrentSpent);
        etBudgetAmount = findViewById(R.id.et_budget_amount);
        btnCancel = findViewById(R.id.btn_cancel_budget);
        btnSave = findViewById(R.id.btn_save_budget);

        // set info
        tvBudgetTitle.setText("Edit Budget");
        tvBudgetInfo.setText(categoryName + " • " + month + "/" + year);

        // Lấy budget hiện tại (nếu có)
        currentBudget = budgetDAO.getBudgetByMonthAndYear(userId, categoryId, month, year);
        if (currentBudget != null) {
            etBudgetAmount.setText(String.valueOf(currentBudget.getBudgetAmount()));
        }

        // Lấy tổng chi tiêu tháng đó để show thông tin
        double totalSpent = expenseDAO.getTotalExpenseByMonthAndYear(userId, categoryId, year, month);
        tvCurrentSpent.setText("Current spent: " + (long) totalSpent + " VND");

        btnCancel.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            String budgetStr = etBudgetAmount.getText().toString().trim();
            if (TextUtils.isEmpty(budgetStr)) {
                etBudgetAmount.setError("Budget is required");
                return;
            }

            double newBudgetAmount = Double.parseDouble(budgetStr);

            if (currentBudget == null) {
                // Chưa có budget cho tháng/năm này -> thêm mới
                budgetDAO.addBudget(userId, categoryId, newBudgetAmount, month, year);
            } else {
                // Đã có -> update budgetAmount
                budgetDAO.updateBudgetAmount(currentBudget.getId(), newBudgetAmount);
            }

            Toast.makeText(this, "Budget saved", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
