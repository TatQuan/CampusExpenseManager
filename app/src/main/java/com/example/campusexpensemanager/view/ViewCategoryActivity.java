package com.example.campusexpensemanager.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.Data.dao.BudgetDAO;
import com.example.campusexpensemanager.Data.dao.CategoryDAO;
import com.example.campusexpensemanager.Data.dao.ExpenseDAO;
import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.adapters.ExpenseCategoryAdapter;
import com.example.campusexpensemanager.models.Budget;
import com.example.campusexpensemanager.models.Expense;
import com.example.campusexpensemanager.session.Session;

import java.util.ArrayList;
import java.util.List;

public class ViewCategoryActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID";
    public static final String EXTRA_CATEGORY_NAME = "EXTRA_CATEGORY_NAME";
    public static final String EXTRA_CATEGORY_DESC = "EXTRA_CATEGORY_DESC";

    TextView tvBudgetAmount;
    TextView tvTotalSpent;
    private String categoryName;
    private int categoryId;
    private int useId;
    private RecyclerView rvExpense;
    private ExpenseCategoryAdapter expenseCategoryAdapter;
    private ExpenseDAO expenseDAO;
    private Session session;
    private Spinner monthFilter;
    private Spinner yearFilter;
    private BudgetDAO budgetDAO;
    private Budget budget;

    private List<Integer> months = new ArrayList<>();
    private List<Integer> years = new ArrayList<>();

    // Cờ để bỏ qua lần onItemSelected đầu tiên
    private boolean isMonthFirstSelect = true;
    private boolean isYearFirstSelect = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);

        // Get data from Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String name = extras.getString(EXTRA_CATEGORY_NAME, "");
            int Id = extras.getInt(EXTRA_CATEGORY_ID, 0);

            categoryName = name;
            categoryId = Id;
        }

        // Set title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(categoryName);
        }

        // View expense
        rvExpense = findViewById(R.id.rv_expense_categories);

        session = new Session(this);
        useId = session.getUserId();  // chú ý: Session phải fix KEY_USER_ID như mình nói lúc nãy

        expenseDAO = new ExpenseDAO(this);
        budgetDAO = new BudgetDAO(this);


        // Lấy TẤT CẢ expense theo category ngay lần đầu: không filter theo tháng/năm
        List<Expense> expenseList = expenseDAO.getAllExpensesByCategory(categoryId, useId);

        // Set adapter với list full
        expenseCategoryAdapter = new ExpenseCategoryAdapter(
                this,
                expenseList,
                expense -> showEditExpenseDialog(expense)
        );

        rvExpense.setLayoutManager(new LinearLayoutManager(this));
        rvExpense.setAdapter(expenseCategoryAdapter);

        // View budget
        tvBudgetAmount = findViewById(R.id.tv_budget_amount);
        tvTotalSpent = findViewById(R.id.tv_total_amount);

        double budgetAmount = budgetDAO.getSumAmountBudgetByCategory(categoryId, useId);
        tvBudgetAmount.setText(String.valueOf(budgetAmount) + " VND");

        double totalSpent = expenseDAO.sumAllExpenses(useId, categoryId);
        tvTotalSpent.setText(String.valueOf(totalSpent) + " VND");

        // Filter time
        monthFilter = findViewById(R.id.sp_month_filter);
        yearFilter = findViewById(R.id.sp_year_filter);

        for (int i = 1; i <= 12; i++) {
            months.add(i);
        }

        budgetDAO = new BudgetDAO(this);
        int currentYear = budgetDAO.getHighestYearInBudget(useId);
        // nếu currentYear = 0 (chưa có budget nào) thì fallback về năm hiện tại
        if (currentYear == 0) {
            currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        }

        // Ví dụ: 5 năm trước tới 5 năm sau
        for (int i = currentYear - 5; i <= currentYear; i++) {
            years.add(i);
        }

        ArrayAdapter<Integer> monthAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                months
        );
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthFilter.setAdapter(monthAdapter);

        ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                years
        );
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearFilter.setAdapter(yearAdapter);

        // CÁCH 2: lúc đầu không filter, chỉ filter sau khi user chọn spinner

        monthFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (isMonthFirstSelect) {
                    isMonthFirstSelect = false;
                    return;
                }

                int selectedMonth = months.get(position);
                int selectedYear = years.get(yearFilter.getSelectedItemPosition());
                updateExpenseList(selectedMonth, selectedYear);

                // Lấy budget theo năm và tháng
                budget = budgetDAO.getBudgetByMonthAndYear(useId, categoryId, selectedMonth, selectedYear);
                if (budget != null) {
                    tvBudgetAmount.setText(String.valueOf(budget.getBudgetAmount()) + " VND");
                } else {
                    tvBudgetAmount.setText("0 VND");
                }

                // Lấy tổng chi tiêu theo tháng/năm
                double totalSpent = expenseDAO.getTotalExpenseByMonthAndYear(useId, categoryId, selectedYear, selectedMonth);
                tvTotalSpent.setText(String.valueOf(totalSpent) + " VND");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        yearFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Bỏ qua lần gọi đầu tiên
                if (isYearFirstSelect) {
                    isYearFirstSelect = false;
                    return;
                }

                int selectedMonth = months.get(monthFilter.getSelectedItemPosition());
                int selectedYear = years.get(position);
                updateExpenseList(selectedMonth, selectedYear);

                // Lấy budget theo năm và tháng
                budget = budgetDAO.getBudgetByMonthAndYear(useId, categoryId, selectedMonth, selectedYear);
                if (budget != null) {
                    tvBudgetAmount.setText(String.valueOf(budget.getBudgetAmount()) + " VND");
                } else {
                    tvBudgetAmount.setText("0 VND");
                }

                // Lấy tổng chi tiêu theo tháng/năm
                double totalSpent = expenseDAO.getTotalExpenseByMonthAndYear(useId, categoryId, selectedYear, selectedMonth);
                tvTotalSpent.setText(String.valueOf(totalSpent) + " VND");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // LÚC NÀY: màn hình sẽ hiển thị toàn bộ expense theo category,
        // filter chỉ bắt đầu chạy sau khi user chọn lại month/year.
    }

    private void updateExpenseList(int selectedMonth, int selectedYear) {
        List<Expense> filteredList =
                expenseDAO.getExpensesByCategoryAndMonthYearRange(
                        categoryId,
                        useId,
                        selectedMonth,
                        selectedYear
                );

        expenseCategoryAdapter.updateExpenseList(filteredList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu; thêm các item vào ActionBar
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Xử lý click menu
        CategoryDAO categoryDAO = new CategoryDAO(this);
        int id = item.getItemId();
        if (id == R.id.editBudget) {

            // Lấy tháng/năm đang chọn trên spinner
            int selectedMonth = months.get(monthFilter.getSelectedItemPosition());
            int selectedYear = years.get(yearFilter.getSelectedItemPosition());

            Intent intent = new Intent(this, EditBudgetActivity.class);
            intent.putExtra("EXTRA_CATEGORY_ID", categoryId);
            intent.putExtra("EXTRA_CATEGORY_NAME", categoryName);
            intent.putExtra("EXTRA_MONTH", selectedMonth);
            intent.putExtra("EXTRA_YEAR", selectedYear);
            startActivity(intent);
            return true;

        } else if (id == R.id.editCategory) {
            Intent intent = new Intent(this, EditCategoryActivity.class);
            intent.putExtra(ViewCategoryActivity.EXTRA_CATEGORY_ID, categoryId);
            startActivity(intent);
            return true;

        } else if (id == R.id.deleteCategory) {
            categoryDAO.deleteCategory(categoryId);
            finish();
            return true;

        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showEditExpenseDialog(Expense expense) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_expense, null);
        builder.setView(dialogView);

        EditText etAmount = dialogView.findViewById(R.id.etAmount);
        EditText etDescription = dialogView.findViewById(R.id.etDescription);

        // set sẵn giá trị hiện tại
        etAmount.setText(String.valueOf(expense.getAmount()));
        etDescription.setText(expense.getDescription());

        builder.setPositiveButton("Save", (dialog, which) -> {
            String amountStr = etAmount.getText().toString().trim();
            String descStr = etDescription.getText().toString().trim();

            if (amountStr.isEmpty()) {
                Toast.makeText(this, "Amount cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            double newAmount;
            try {
                newAmount = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cập nhật object
            expense.setAmount(newAmount);
            expense.setDescription(descStr);

            // Ghi xuống DB
            int rows = expenseDAO.updateExpense(expense);
            if (rows > 0) {
                Toast.makeText(this, "Update success", Toast.LENGTH_SHORT).show();

                // Refresh lại list theo tháng/năm đang chọn
                int selectedMonth = months.get(monthFilter.getSelectedItemPosition());
                int selectedYear = years.get(yearFilter.getSelectedItemPosition());

                updateExpenseList(selectedMonth, selectedYear);

                // Cập nhật lại budget + tổng chi
                budget = budgetDAO.getBudgetByMonthAndYear(useId, categoryId, selectedMonth, selectedYear);
                if (budget != null) {
                    tvBudgetAmount.setText(budget.getBudgetAmount() + " VND");
                } else {
                    tvBudgetAmount.setText("0 VND");
                }

                double totalSpent = expenseDAO.getTotalExpenseByMonthAndYear(
                        useId,
                        categoryId,
                        selectedYear,
                        selectedMonth
                );
                tvTotalSpent.setText(totalSpent + " VND");
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancle", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem editCategoryItem = menu.findItem(R.id.editCategory);
        MenuItem deleteCategoryItem = menu.findItem(R.id.deleteCategory);


        // Ẩn nếu role == "STUDENT"
        if (session.getRole().equals("STUDENT")) {
            editCategoryItem.setVisible(false);
            deleteCategoryItem.setVisible(false);
        } else {
            editCategoryItem.setVisible(true);
            deleteCategoryItem.setVisible(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

}
