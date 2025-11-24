package com.example.campusexpensemanager.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.Data.dao.BudgetDAO;
import com.example.campusexpensemanager.Data.dao.CategoryDAO;
import com.example.campusexpensemanager.Data.dao.ExpenseDAO;
import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.adapters.CategoryBudgetAdapter;
import com.example.campusexpensemanager.models.Category;
import com.example.campusexpensemanager.session.Session;

import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private TextView tvBudgetAmount;
    private TextView tvTotalAmount;
    private RecyclerView rvCategories;

    private Session session;
    private BudgetDAO budgetDAO;
    private ExpenseDAO expenseDAO;
    private CategoryDAO categoryDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Report");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Ánh xạ view
        tvBudgetAmount = findViewById(R.id.tv_budget_amount);
        tvTotalAmount = findViewById(R.id.tv_total_amount);
        rvCategories = findViewById(R.id.rvCategories);

        // Session + DAO
        session = new Session(this);
        int userId = session.getUserId();

        budgetDAO = new BudgetDAO(this);
        expenseDAO = new ExpenseDAO(this);
        categoryDAO = new CategoryDAO(this);

        double totalBudget = budgetDAO.getTotalBudget(userId);
        tvBudgetAmount.setText(String.valueOf(totalBudget) + " VND");
        double totalExpense = expenseDAO.getTotalExpense(userId);
        tvTotalAmount.setText(String.valueOf(totalExpense) + " VND");

        // SETUP RecyclerView CATEGORY + BUDGET (dùng CategoryBudgetAdapter)
        List<Category> categoryList = categoryDAO.getAllCategories();

        CategoryBudgetAdapter adapter = new CategoryBudgetAdapter(
                this,
                categoryList,
                userId,
                category -> {
                    // TODO: nếu muốn mở màn chi tiết category khi click
                    // ví dụ: chuyển sang ViewCategoryActivity
                     Intent i = new Intent(this, ViewCategoryActivity.class);
                     i.putExtra(ViewCategoryActivity.EXTRA_CATEGORY_ID, category.getId());
                     i.putExtra(ViewCategoryActivity.EXTRA_CATEGORY_NAME, category.getName());
                     startActivity(i);
                }
        );

        rvCategories.setLayoutManager(new LinearLayoutManager(this));
        rvCategories.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
