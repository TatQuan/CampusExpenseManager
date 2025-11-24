package com.example.campusexpensemanager.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.models.Notification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    public interface OnNotificationClickListener {
        void onNotificationClick(Notification notification);
    }

    private Context context;
    private List<Notification> notificationList;
    private OnNotificationClickListener listener;

    public NotificationAdapter(Context context, List<Notification> notificationList,
                               OnNotificationClickListener listener) {
        this.context = context;
        this.notificationList = notificationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification ntf = notificationList.get(position);

        holder.tvMessage.setText(ntf.getMessage());
        holder.tvDate.setText(ntf.getDate());

        if (ntf.getIsRead() == 0) {
            // chưa đọc
            holder.unreadDot.setVisibility(View.VISIBLE);
            holder.tvMessage.setTypeface(null, Typeface.BOLD);
        } else {
            // đã đọc
            holder.unreadDot.setVisibility(View.INVISIBLE);
            holder.tvMessage.setTypeface(null, Typeface.NORMAL);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onNotificationClick(ntf);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList != null ? notificationList.size() : 0;
    }

    public void updateData(List<Notification> newList) {
        this.notificationList.clear();
        this.notificationList.addAll(newList);
        notifyDataSetChanged();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView tvMessage, tvDate;
        View unreadDot;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvDate = itemView.findViewById(R.id.tv_date);
            unreadDot = itemView.findViewById(R.id.view_unread_dot);
        }
    }
}
