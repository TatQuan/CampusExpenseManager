package com.example.campusexpensemanager.view;

import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.campusexpensemanager.R;

public class AddExpenseActivity extends AppCompatActivity {

    EditText etDate;
    EditText etDescription;
    EditText etExpenditureAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        //Setup toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add Expense");
        }



    }
}