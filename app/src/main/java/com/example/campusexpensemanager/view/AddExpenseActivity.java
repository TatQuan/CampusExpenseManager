package com.example.campusexpensemanager.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.session.Session;
import com.example.campusexpensemanager.Data.dao.CategoryDAO;
import com.example.campusexpensemanager.Data.dao.ExpenseDAO;
import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.adapters.SelectCategoryAdapter;
import com.example.campusexpensemanager.models.Category;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText etDate;
    private EditText etDescription;
    private EditText etExpenditureAmount;
    private EditText etStartDate;
    private EditText etEndDate;
    private Button btnEnter;
    private CheckBox checkBoxRecurring;

    private CategoryDAO categoryDAO;
    private RecyclerView rvCategories;
    private SelectCategoryAdapter selectCategoryAdapter;
    private List<Category> categories;
    private Category selectedCategory;
    private Session session;

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // Setup toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add Expense");
        }

        etExpenditureAmount = findViewById(R.id.et_expenditure_amount);
        etDate = findViewById(R.id.et_date);
        etDate.setText(dateFormat.format(calendar.getTime()));
        etDescription = findViewById(R.id.et_description);
        etStartDate = findViewById(R.id.et_start_date);
        etStartDate.setText(dateFormat.format(calendar.getTime()));
        etEndDate = findViewById(R.id.et_end_date);
        checkBoxRecurring = findViewById(R.id.checkboxRecurring);
        rvCategories = findViewById(R.id.rvCategories);
        btnEnter = findViewById(R.id.btn_add_expense);

        rvCategories.setLayoutManager(new GridLayoutManager(this, 4));

        // Load categories
        categoryDAO = new CategoryDAO(this);
        categories = categoryDAO.getAllCategories();
        selectCategoryAdapter = new SelectCategoryAdapter(this, categories);
        rvCategories.setAdapter(selectCategoryAdapter);

        // Set date pickers for start and end date EditText
        etStartDate.setOnClickListener(v -> showDatePickerDialog(etStartDate));
        etEndDate.setOnClickListener(v -> showDatePickerDialog(etEndDate));
        etDate.setOnClickListener(v -> showDatePickerDialog(etDate));


        // Adapter for category selection
        selectCategoryAdapter.setOnCategoryClickListener(category -> {
            selectedCategory = category; // Save the selected category
        });

        // Show start and end dates when recurring is checked
        checkBoxRecurring.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etStartDate.setVisibility(View.VISIBLE);
                etEndDate.setVisibility(View.VISIBLE);

                // Gợi ý ngày bắt đầu = hôm nay
                etStartDate.setText(dateFormat.format(calendar.getTime()));

                // Ví dụ gợi ý end date = sau 1 tháng
                Calendar endCal = (Calendar) calendar.clone();
                endCal.add(Calendar.MONTH, 1);
                etEndDate.setText(dateFormat.format(endCal.getTime()));
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

            session = new Session(this);
            int userId = session.getUserId();
            // Insert expense into database
            expenseDAO.addExpense(description, amount, date, startDate, endDate, selectedCategory.getId(), userId, isRecurring ? 1 : 0);

            Toast.makeText(this, "Add expense successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void showDatePickerDialog(final EditText dateField) {
        // Get the current date for default date in the DatePickerDialog
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Set the date on the selected EditText
                        calendar.set(year, monthOfYear, dayOfMonth);
                        dateField.setText(dateFormat.format(calendar.getTime())); // Format date as yyyy-MM-dd
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
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
