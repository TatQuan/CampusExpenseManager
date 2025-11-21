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

import com.example.campusexpensemanager.Data.dao.BudgetDAO;
import com.example.campusexpensemanager.Data.dao.CategoryDAO;
import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.models.Category;
import com.example.campusexpensemanager.view.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SetBudgetActivity extends BaseActivity {

    Spinner spCategory;
    EditText etBudgetAmount;
    EditText etStartDate;
    EditText etEndDate;

    List<Category> categories;
    CategoryDAO categoryDAO;

    // Initialize Calendar instance for date picking
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_set_budget);

        spCategory = findViewById(R.id.sp_category);
        etBudgetAmount = findViewById(R.id.et_budget_amount);
        etStartDate = findViewById(R.id.et_start_date);
        etEndDate = findViewById(R.id.et_end_date);

        // Get data category
        categoryDAO = new CategoryDAO(this);
        categories = categoryDAO.getAllCategories();

        // Set action bar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Set Budget");
        }

        // Drop-down list for category selection
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for (Category category : categories) {
            adapter.add(category.getName());
        }

        spCategory.setAdapter(adapter);

        // Set date pickers for start and end date EditTexts
        etStartDate.setOnClickListener(v -> showDatePickerDialog(etStartDate));
        etEndDate.setOnClickListener(v -> showDatePickerDialog(etEndDate));

        // Save button logic (TODO)
        Button btnSaveBudget = findViewById(R.id.btn_save_budget);
        btnSaveBudget.setOnClickListener(v -> {
            String startDateText = etStartDate.getText().toString();
            String endDateText = etEndDate.getText().toString();
            String budgetAmountText = etBudgetAmount.getText().toString();

            if (startDateText.isEmpty() || endDateText.isEmpty() || budgetAmountText.isEmpty()) {
                // Hiển thị thông báo nếu người dùng chưa nhập đầy đủ thông tin
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chuyển đổi số tiền ngân sách thành kiểu double
            double budgetAmount = Double.parseDouble(budgetAmountText);

            // Lấy danh mục được chọn
            int selectedCategoryPosition = spCategory.getSelectedItemPosition();
            Category selectedCategory = categories.get(selectedCategoryPosition);

            // Phân tích tháng và năm từ ngày bắt đầu (có thể chỉnh sửa lại theo nhu cầu)
            String[] startDateParts = startDateText.split("-");
            int startYear = Integer.parseInt(startDateParts[0]);
            int startMonth = Integer.parseInt(startDateParts[1]);

            // Lưu ngân sách vào cơ sở dữ liệu (có thể chỉnh sửa lại theo phương thức DAO của bạn)
            BudgetDAO budgetDAO = new BudgetDAO(this);
            budgetDAO.addBudget(1, selectedCategory.getId(), budgetAmount, startMonth, startYear);  // Giả sử userId là 1

            // Có thể hiển thị thông báo thành công hoặc điều hướng đến màn hình khác
            Toast.makeText(this, "Budget saved successfully!", Toast.LENGTH_SHORT).show();
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

    //Back to home by click on back button on toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
