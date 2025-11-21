package com.example.campusexpensemanager.view;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.models.Category;

public class ViewCategoryActivity extends BaseActivity {

    public static final String EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID";
    public static final String EXTRA_CATEGORY_NAME = "EXTRA_CATEGORY_NAME";
    public static final String EXTRA_CATEGORY_DESC = "EXTRA_CATEGORY_DESC";

    private String categoryName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_view_category);


        // Lấy dữ liệu từ Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String name = extras.getString(EXTRA_CATEGORY_NAME, "");

            categoryName = name;
        }

        //Set title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(categoryName);
        }
    }
}
