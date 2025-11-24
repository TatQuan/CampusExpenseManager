package com.example.campusexpensemanager.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.Data.dao.CategoryDAO;
import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.models.Category;

public class EditCategoryActivity extends AppCompatActivity {

    private EditText etCategoryName;
    private EditText etCategoryDescription;
    private EditText etIconName;
    private ImageView ivIconPreview;

    private Button btnCancel;
    private Button btnSaveCategory;
    private int categoryId;
    private CategoryDAO categoryDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_and_add_category);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Edit Category");
        }

        //Get intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            categoryId = extras.getInt(ViewCategoryActivity.EXTRA_CATEGORY_ID, 0);
        }

        //Get category by id
        categoryDAO = new CategoryDAO(this);
        Category category = categoryDAO.getCategoryById(categoryId);

        etCategoryName = findViewById(R.id.et_category_name);
        etCategoryDescription = findViewById(R.id.et_category_description);
        etIconName = findViewById(R.id.et_icon_name);

        etCategoryName.setText(category.getName());
        etCategoryDescription.setText(category.getDescription());
        etIconName.setText(category.getIconName());

        ivIconPreview = findViewById(R.id.iv_icon_preview);

        if (category.getIconName().isEmpty()) {
            ivIconPreview.setImageResource(R.drawable.ic_default_category);
            return;
        }

        // Ở trong Activity thì dùng thế này cho chắc chắn:
        int resId = getResources().getIdentifier(
                category.getIconName(),
                "drawable",
                getPackageName()
        );

        if (resId != 0) {
            ivIconPreview.setImageResource(resId);
        } else {
            ivIconPreview.setImageResource(R.drawable.ic_default_category);
        }

        // Event button
        btnCancel = findViewById(R.id.btn_cancel);
        btnSaveCategory = findViewById(R.id.btn_save_category);

        btnCancel.setOnClickListener(v -> {
            finish();
        });

        btnSaveCategory.setOnClickListener(v -> {
            String name = etCategoryName.getText().toString();
            String description = etCategoryDescription.getText().toString();
            String iconName = etIconName.getText().toString();

            // Update category
            categoryDAO.updateCategory(categoryId, name, description, iconName);
            finish();
        });
    }
}