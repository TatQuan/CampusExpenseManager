package com.example.campusexpensemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.models.User;

import java.util.List;

public class UserViewApdater extends RecyclerView.Adapter<UserViewApdater.UserViewHolder> {

    private Context context;
    private List<User> userList;
    private OnUserClickListener listener;

    public interface OnUserClickListener {
        void onUserClick(User user);
        void onMoreClick(User user, View view);
    }

    public UserViewApdater(Context context, List<User> userList, OnUserClickListener listener) {
        this.context = context;
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        holder.tvName.setText(user.getUsername());
        holder.tvEmail.setText(user.getEmail());

        // 👇 set role cho item
        String role = user.getRole();
        // Nếu muốn format đẹp:
        if ("ADMIN".equalsIgnoreCase(role)) {
            role = "Admin";
        } else if ("STUDENT".equalsIgnoreCase(role)) {
            role = "Student";
        }
        holder.tvRole.setText(role);

        holder.itemView.setOnClickListener(v -> listener.onUserClick(user));
        holder.btnMore.setOnClickListener(v -> listener.onMoreClick(user, holder.btnMore));
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvEmail, tvRole;
        ImageView btnMore, imgAvatar;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvUserName);
            tvEmail = itemView.findViewById(R.id.tvUserEmail);
            tvRole = itemView.findViewById(R.id.tvUserRole);   // 👈 thêm dòng này
            btnMore = itemView.findViewById(R.id.btnMore);
            imgAvatar = itemView.findViewById(R.id.imgUserAvatar);
        }
    }

}
