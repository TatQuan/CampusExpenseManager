package com.example.campusexpensemanager.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.Data.dao.BudgetDAO;
import com.example.campusexpensemanager.Data.dao.CategoryDAO;
import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.adapters.CategoryBudgetAdapter;
import com.example.campusexpensemanager.models.Budget;
import com.example.campusexpensemanager.models.Category;
import com.example.campusexpensemanager.session.Session;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView rvCategories;
    private CategoryBudgetAdapter adapter;

    private CategoryDAO categoryDAO;
    private BudgetDAO budgetDAO;

    private List<Category> categoryList;
    private List<Budget> budgetList;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Setup toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Category");
        }

        rvCategories = findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(new LinearLayoutManager(this));
        // DAO
        categoryDAO = new CategoryDAO(this);
        budgetDAO = new BudgetDAO(this);

        // Get data category
        categoryList = categoryDAO.getAllCategories();

        // Get budget of user logged in
        session = new Session(this);
        int userId = session.getUserId();
        budgetList = budgetDAO.getAllBudgets().stream()
                .filter(b -> b.getUserId() == userId)
                .collect(Collectors.toList());

        // Adapter
        adapter = new CategoryBudgetAdapter(
                this,
                categoryList,
                budgetList,
                userId,
                new CategoryBudgetAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Category category, Budget budget) {
                        Intent intent = new Intent(CategoryActivity.this, ViewCategoryActivity.class);
                        intent.putExtra(ViewCategoryActivity.EXTRA_CATEGORY_ID, category.getId());
                        intent.putExtra(ViewCategoryActivity.EXTRA_CATEGORY_NAME, category.getName());
                        intent.putExtra(ViewCategoryActivity.EXTRA_CATEGORY_DESC, category.getDescription());
                        startActivity(intent);
                    }
                }
        );

        rvCategories.setAdapter(adapter);
    }
}
