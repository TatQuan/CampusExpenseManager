package com.example.campusexpensemanager.view;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.Data.dao.NotificationDAO;
import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.adapters.NotificationAdapter;
import com.example.campusexpensemanager.models.Notification;
import com.example.campusexpensemanager.session.Session;

import java.util.List;

public class NotificationActivity extends AddExpenseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Set action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Notification");
        }


        Session session = new Session(this);
        int userId = session.getUserId();


        RecyclerView rvNotifications = findViewById(R.id.rv_notification);
        rvNotifications.setLayoutManager(new LinearLayoutManager(this));

        NotificationDAO notificationDAO = new NotificationDAO(this);
        List<Notification> list = notificationDAO.getNotificationsByUser(userId);

        NotificationAdapter adapter = new NotificationAdapter(this, list, ntf -> {
            if (ntf.getIsRead() != 1) {
                notificationDAO.markNotificationAsRead(ntf.getId());
            }
        });

        rvNotifications.setAdapter(adapter);

    }
}