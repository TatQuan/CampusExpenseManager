package com.example.campusexpensemanager.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.view.BaseActivity;
import com.example.campusexpensemanager.Data.dao.CategoryDAO;
import com.example.campusexpensemanager.Data.dao.ExpenseDAO;
import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.adapters.CategoryAdapter;
import com.example.campusexpensemanager.models.Category;
import com.example.campusexpensemanager.models.Expense;

import java.util.List;

public class AddExpenseActivity extends BaseActivity {

    private EditText etDate;
    private EditText etDescription;
    private EditText etExpenditureAmount;
    private EditText etStartDate;
    private EditText etEndDate;
    private Button btnEnter;
    private CheckBox checkBoxRecurring;
    private CategoryDAO categoryDAO;
    private RecyclerView rvCategories;
    private CategoryAdapter categoryAdapter;
    private List<Category> categories;

    private Category selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_add_expense);

        int userId = getUserId();
        // Setup toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add Expense");
        }

        etExpenditureAmount = findViewById(R.id.et_expenditure_amount);
        etDate = findViewById(R.id.et_date);
        etDescription = findViewById(R.id.et_description);
        etStartDate = findViewById(R.id.et_start_date);
        etEndDate = findViewById(R.id.et_end_date);
        checkBoxRecurring = findViewById(R.id.checkboxRecurring);
        rvCategories = findViewById(R.id.rvCategories);
        btnEnter = findViewById(R.id.btn_add_expense);

        rvCategories.setLayoutManager(new GridLayoutManager(this, 4));

        // Load categories
        categoryDAO = new CategoryDAO(this);
        categories = categoryDAO.getAllCategories();
        categoryAdapter = new CategoryAdapter(this, categories);
        rvCategories.setAdapter(categoryAdapter);

        // Adapter for category selection
        categoryAdapter.setOnCategoryClickListener(category -> {
            selectedCategory = category; // Save the selected category
        });

        // Show start and end dates when recurring is checked
        checkBoxRecurring.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etStartDate.setVisibility(View.VISIBLE);
                etEndDate.setVisibility(View.VISIBLE);
            } else {
                etStartDate.setVisibility(View.GONE);
                etEndDate.setVisibility(View.GONE);
            }
        });

        if (btnEnter == null) {
            Log.e("AddExpenseActivity", "Button not found with id: R.id.btn_add_expense");
        } else {
            Log.d("AddExpenseActivity", "Button found, setting click listener");
        }

        btnEnter.setOnClickListener(v -> {
            String date = etDate.getText().toString();
            String description = etDescription.getText().toString();
            String amountStr = etExpenditureAmount.getText().toString();
            String startDate = etStartDate.getText().toString();
            String endDate = etEndDate.getText().toString();
            boolean isRecurring = checkBoxRecurring.isChecked();

            if (selectedCategory == null) {
                Toast.makeText(this, "Please select a category!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (amountStr.isEmpty()) {
                etExpenditureAmount.setError("Amount is required!");
                return;
            }

            if (date.isEmpty()) {
                etDate.setError("Date is required!");
                return;
            }

            double amount = Double.parseDouble(amountStr);

            // If recurring, check that start and end dates are provided
            if (isRecurring && (startDate.isEmpty() || endDate.isEmpty())) {
                Toast.makeText(this, "Start and End dates are required for recurring expenses!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Add expense to the database
            ExpenseDAO expenseDAO = new ExpenseDAO(this);

            // If not recurring, set start and end date as null
            if (!isRecurring) {
                startDate = null;
                endDate = null;
            }

            // Insert expense into database
            expenseDAO.addExpense(description, amount, date, startDate, endDate, selectedCategory.getId(), userId, isRecurring ? 1 : 0);

            Toast.makeText(this, "Add expense successfully!", Toast.LENGTH_SHORT).show();
            finish();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
