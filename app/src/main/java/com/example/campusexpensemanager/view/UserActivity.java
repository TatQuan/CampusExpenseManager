package com.example.campusexpensemanager.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.Data.dao.UserDAO;
import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.adapters.UserViewApdater;
import com.example.campusexpensemanager.models.User;
import com.example.campusexpensemanager.session.Session;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    private RecyclerView rvUsers;
    private UserViewApdater adapter;
    private UserDAO userDAO;
    private EditText etSearchUser;

    private List<User> userList;

    private UserViewApdater.OnUserClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Users");
        }

        rvUsers = findViewById(R.id.rvUsers);
        etSearchUser = findViewById(R.id.etSearchUser);

        rvUsers.setLayoutManager(new LinearLayoutManager(this));

        userDAO = new UserDAO(this);
        userList = userDAO.getAllUsers();

        // listener chung
        listener = new UserViewApdater.OnUserClickListener() {
            @Override
            public void onUserClick(User user) {
                // nếu sau này muốn mở chi tiết user thì làm ở đây
            }

            @Override
            public void onMoreClick(User user, View anchorView) {
                showUserMenu(user, anchorView);
            }
        };

        adapter = new UserViewApdater(this, userList, listener);
        rvUsers.setAdapter(adapter);

        // Search filter
        etSearchUser.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUsers(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void reloadUserList() {
        userList = userDAO.getAllUsers();
        adapter = new UserViewApdater(this, userList, listener);
        rvUsers.setAdapter(adapter);
    }

    private void filterUsers(String query) {
        List<User> filtered = new ArrayList<>();

        for (User u : userList) {
            if (u.getUsername().toLowerCase().contains(query.toLowerCase()) ||
                    u.getEmail().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(u);
            }
        }

        adapter = new UserViewApdater(this, filtered, listener);
        rvUsers.setAdapter(adapter);
    }

    private void showUserMenu(User user, View anchorView) {
        androidx.appcompat.widget.PopupMenu popupMenu =
                new androidx.appcompat.widget.PopupMenu(this, anchorView);
        popupMenu.getMenuInflater().inflate(R.menu.menu_user_item, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_edit_user) {
                showEditUserDialog(user);
                return true;
            } else if (id == R.id.action_delete_user) {
                // xóa mềm
                userDAO.deleteUser(user.getUserId());
                reloadUserList();
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    private void showEditUserDialog(User user) {
        androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Edit User");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_user, null);
        EditText etUsername = dialogView.findViewById(R.id.et_edit_username);
        EditText etEmail = dialogView.findViewById(R.id.et_edit_email);
        EditText etPassword = dialogView.findViewById(R.id.et_edit_password);
        Spinner spRole = dialogView.findViewById(R.id.sp_edit_role);

        // set dữ liệu cũ
        etUsername.setText(user.getUsername());
        etEmail.setText(user.getEmail());
        etPassword.setText(user.getPassword()); // nếu không lưu plain thì để trống

        // Chuẩn bị dữ liệu role cho Spinner
        String[] roles = new String[]{"STUDENT", "ADMIN"};
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                roles
        );
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRole.setAdapter(roleAdapter);

        // set selection theo role hiện tại của user
        String currentRole = user.getRole();
        int selectedIndex = 0; // default STUDENT
        if ("ADMIN".equalsIgnoreCase(currentRole)) {
            selectedIndex = 1;
        }
        spRole.setSelection(selectedIndex);

        builder.setView(dialogView);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newUsername = etUsername.getText().toString().trim();
            String newEmail = etEmail.getText().toString().trim();
            String newPassword = etPassword.getText().toString().trim();
            String newRole = spRole.getSelectedItem().toString();

            // validate đơn giản
            if (newUsername.isEmpty() || newEmail.isEmpty()) {
                // có thể thêm Toast nếu muốn
                return;
            }

            userDAO.updateUser(user.getUserId(), newUsername, newEmail, newPassword, newRole);
            reloadUserList();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_add, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem addUser = menu.findItem(R.id.addUser);
        Session session = new Session(this);

        if ("STUDENT".equals(session.getRole())) {
            addUser.setVisible(false);
        } else {
            addUser.setVisible(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addUser) {
            startActivityForResult(new Intent(this, AddUserActivity.class), 1001);
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}




