package com.example.campusexpensemanager.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.Data.dao.BudgetDAO;
import com.example.campusexpensemanager.Data.dao.CategoryDAO;
import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.models.Category;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SetBudgetActivity extends AppCompatActivity {

    Spinner spCategory;
    EditText etBudgetAmount;
    EditText etYear;
    EditText etMonth;

    List<Category> categories;
    CategoryDAO categoryDAO;

    // Calendar để lấy ngày hiện tại và dùng cho DatePickerDialog
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_budget);

        // Setup toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Set Budget");
        }

        spCategory = findViewById(R.id.sp_category);
        etBudgetAmount = findViewById(R.id.et_budget_amount);
        etMonth = findViewById(R.id.et_month);
        etYear = findViewById(R.id.et_year);

        // Get data category
        categoryDAO = new CategoryDAO(this);
        categories = categoryDAO.getAllCategories();

        // Drop-down list for category selection
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for (Category category : categories) {
            adapter.add(category.getName());
        }

        spCategory.setAdapter(adapter);

        // Set mặc định năm / tháng hiện tại
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // 0–11 -> +1

        etYear.setText(String.valueOf(currentYear));
        etMonth.setText(String.format(Locale.getDefault(), "%02d", currentMonth));

        // Chọn năm bằng DatePickerDialog (chỉ lấy year)
        etYear.setOnClickListener(v -> showYearPickerDialog());

        // Chọn tháng bằng DatePickerDialog (chỉ lấy month)
        etMonth.setOnClickListener(v -> showMonthPickerDialog());

        // Save button logic
        Button btnSaveBudget = findViewById(R.id.btn_save_budget);
        btnSaveBudget.setOnClickListener(v -> {
            String budgetAmountText = etBudgetAmount.getText().toString().trim();
            String yearText = etYear.getText().toString().trim();
            String monthText = etMonth.getText().toString().trim();

            if (budgetAmountText.isEmpty() || yearText.isEmpty() || monthText.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            double budgetAmount;
            int selectedYear;
            int selectedMonth;

            try {
                budgetAmount = Double.parseDouble(budgetAmountText);
                selectedYear = Integer.parseInt(yearText);
                selectedMonth = Integer.parseInt(monthText);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number format.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy danh mục được chọn
            int selectedCategoryPosition = spCategory.getSelectedItemPosition();
            if (selectedCategoryPosition < 0 || selectedCategoryPosition >= categories.size()) {
                Toast.makeText(this, "Please select a category.", Toast.LENGTH_SHORT).show();
                return;
            }
            Category selectedCategory = categories.get(selectedCategoryPosition);

            // Lưu ngân sách vào DB
            BudgetDAO budgetDAO = new BudgetDAO(this);
            // Giả sử userId là 1
            budgetDAO.addBudget(1, selectedCategory.getId(), budgetAmount, selectedMonth, selectedYear);

            Toast.makeText(this, "Budget saved successfully!", Toast.LENGTH_SHORT).show();
            // Có thể finish() hoặc chuyển màn khác nếu cần
             finish();
        });

        Button btnCancelBudget = findViewById(R.id.btn_cancel_budget);
        btnCancelBudget.setOnClickListener(v -> {
            finish();
        });
    }

    private void showYearPickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePicker view, int y, int m, int d) -> {
                    etYear.setText(String.valueOf(y));
                    calendar.set(Calendar.YEAR, y);
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    private void showMonthPickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePicker view, int y, int m, int d) -> {
                    int selectedMonth = m + 1; // 0–11 -> +1
                    etMonth.setText(String.format(Locale.getDefault(), "%02d", selectedMonth));
                    calendar.set(Calendar.MONTH, m);
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Xử lý nút back trên toolbar
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
