package com.example.campusexpensemanager.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.session.Session;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseActivity extends AppCompatActivity {

    protected BottomNavigationView bottomNavigationView;
    protected Session session; // <--- thêm session

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        session = new Session(this); // <--- khởi tạo session

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        setupBottomNavigation();

        // Nếu chưa login → chuyển về LoginActivity
        if (!session.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;  // Đảm bảo activity không tiếp tục khởi tạo
        }

        // Kiểm tra userId hợp lệ
        int userId = session.getUserId();
        if (userId == -1) {
            // Nếu userId không hợp lệ, chuyển hướng tới màn hình đăng nhập
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            // Kiểm tra đang ở activity nào → không start lại
            if (id == R.id.nav_home && !(this instanceof HomeActivity)) {
                startActivity(new Intent(BaseActivity.this, HomeActivity.class));

            } else if (id == R.id.nav_category && !(this instanceof CategoryActivity)) {
                startActivity(new Intent(BaseActivity.this, CategoryActivity.class));

            } else if (id == R.id.nav_addexpense && !(this instanceof AddExpenseActivity)) {
                startActivity(new Intent(BaseActivity.this, AddExpenseActivity.class));

            } else if (id == R.id.nav_notification && !(this instanceof NotificationActivity)) {
                startActivity(new Intent(BaseActivity.this, NotificationActivity.class));

            } else if (id == R.id.nav_profile && !(this instanceof ProfileActivity)) {
                startActivity(new Intent(BaseActivity.this, ProfileActivity.class));
            }
            return true;
        });
    }

    protected void setActivityLayout(int layoutResID) {
        FrameLayout container = findViewById(R.id.container);
        getLayoutInflater().inflate(layoutResID, container, true);
    }

    // Các method tiện lợi lấy user info
    protected int getUserId() {
        int userId = session.getUserId();
        if (userId == -1) {
            // Xử lý nếu userId không hợp lệ, có thể yêu cầu đăng nhập lại
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            return -1;  // Trả về -1 nếu không có userId hợp lệ
        }
        return userId;
    }

    protected String getUsername() {
        return session.getUsername();
    }

    protected String getEmail() {
        return session.getEmail();
    }
}

