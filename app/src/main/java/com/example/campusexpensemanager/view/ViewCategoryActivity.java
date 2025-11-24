package com.example.campusexpensemanager.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.Data.dao.ExpenseDAO;
import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.adapters.ExpenseCategory;
import com.example.campusexpensemanager.models.Category;
import com.example.campusexpensemanager.models.Expense;
import com.example.campusexpensemanager.session.Session;

import java.util.ArrayList;
import java.util.List;

public class ViewCategoryActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID";
    public static final String EXTRA_CATEGORY_NAME = "EXTRA_CATEGORY_NAME";
    public static final String EXTRA_CATEGORY_DESC = "EXTRA_CATEGORY_DESC";

    private String categoryName;
    private int categoryId;
    private int useId;
    private RecyclerView rvExpense;
    private ExpenseCategory expenseCategoryAdapter;
    private ExpenseDAO expenseDAO;
    private Session session;

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

        //Set title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(categoryName);
        }

        //View expense
        rvExpense = findViewById(R.id.rv_expense_categories);

        session = new Session(this);
        useId = session.getUserId();

        expenseDAO = new ExpenseDAO(this);
        List<Expense> expenseList = new ArrayList<>();
        expenseList = expenseDAO.getAllExpensesByCategory(categoryId, useId);

        // Set adapter
        expenseCategoryAdapter = new ExpenseCategory(this, expenseList,
                expense -> {
                    //TODO: Handle click on expense
                }
        );

        rvExpense.setLayoutManager(new LinearLayoutManager(this));
        rvExpense.setAdapter(expenseCategoryAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu; thêm các item vào ActionBar
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Xử lý click menu
        int id = item.getItemId();
        if (id == R.id.editBudget) {
            Toast.makeText(this, "Edit budget clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
