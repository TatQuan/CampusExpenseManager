package com.example.campusexpensemanager.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.campusexpensemanager.Data.dao.CategoryDAO;
import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.models.Category;

public class AddCategoryActivity extends AppCompatActivity {
    private EditText etCategoryName;
    private EditText etCategoryDescription;
    private EditText etIconName;
    private ImageView ivIconPreview;

    private CategoryDAO categoryDAO;
    private Category category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_and_add_category);

        // Set up action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add Category");
        }

        // Add category logic here
        etCategoryName = findViewById(R.id.et_category_name);
        etCategoryDescription = findViewById(R.id.et_category_description);
        etIconName = findViewById(R.id.et_icon_name);

        ivIconPreview = findViewById(R.id.iv_icon_preview);

        categoryDAO = new CategoryDAO(this);

        ivIconPreview = findViewById(R.id.iv_icon_preview);

//        if (category.getIconName().isEmpty()) {
//            ivIconPreview.setImageResource(R.drawable.ic_default_category);
//            return;
//        }
//
//        // Ở trong Activity thì dùng thế này cho chắc chắn:
//        int resId = getResources().getIdentifier(
//                category.getIconName(),
//                "drawable",
//                getPackageName()
//        );
//
//        if (resId != 0) {
//            ivIconPreview.setImageResource(resId);
//        } else {
//            ivIconPreview.setImageResource(R.drawable.ic_default_category);
//        }

        // Even button
        Button btnCancel = findViewById(R.id.btn_cancel);
        Button btnSaveCategory = findViewById(R.id.btn_save_category);

        btnCancel.setOnClickListener(v -> {
            finish();
        });

        btnSaveCategory.setOnClickListener(v -> {
            String name = etCategoryName.getText().toString();
            String description = etCategoryDescription.getText().toString();
            String iconName = etIconName.getText().toString();

            category = new Category(0, name, description, iconName);

            // Add category
            categoryDAO.insertCategory(category);
            finish();
        });

    }
}