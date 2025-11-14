package com.example.campusexpensemanager.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.Data.dao.CategoryDAO;
import com.example.campusexpensemanager.Data.dao.ExpenseDAO;
import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.adapters.CategoryAdapter;
import com.example.campusexpensemanager.models.Category;
import com.example.campusexpensemanager.models.Expense;

import java.util.List;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText etDate;
    private EditText etDescription;
    private EditText etExpenditureAmount;
    private Button btnEnter;
    private CategoryDAO categoryDAO;
    private RecyclerView rvCategories;
    private CategoryAdapter categoryAdapter;
    private List<Category> categories; // giả sử đã load từ DB

    private Category selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        //Setup toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add Expense");
        }

        etExpenditureAmount = findViewById(R.id.et_expenditure_amount);
        etDate = findViewById(R.id.et_date);
        etDescription = findViewById(R.id.et_description);
        rvCategories = findViewById(R.id.rvCategories);

        rvCategories.setLayoutManager(new GridLayoutManager(this, 4));
        //Load item category
        categoryDAO = new CategoryDAO(this);
        categories = categoryDAO.getAllCategories();
        categoryAdapter = new CategoryAdapter(this, categories);
        rvCategories.setAdapter(categoryAdapter);


        // Adapter
        categoryAdapter.setOnCategoryClickListener(category -> {
            selectedCategory = category; // lưu lại category được chọn
        });


        rvCategories.setAdapter(categoryAdapter);

        btnEnter.setOnClickListener(v -> {
            String date = etDate.getText().toString();
            String description = etDescription.getText().toString();
            String amountStr = etExpenditureAmount.getText().toString();

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

            // Tạo expense object (ví dụ)
//            Expense newExpense = new Expense(date, description, amount, selectedCategory.getId());
//
//            // Lưu vào DB
//            ExpenseDAO expenseDAO = new ExpenseDAO(this);
//            long result = expenseDAO.insertExpense(newExpense);
//
//            if (result != -1) {
//                Toast.makeText(this, "Thêm expense thành công!", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Lỗi khi thêm expense!", Toast.LENGTH_SHORT).show();
//            }
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