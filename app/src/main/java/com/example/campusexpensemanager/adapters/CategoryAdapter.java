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
import com.example.campusexpensemanager.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> categoryList;
    private int selectedPosition = RecyclerView.NO_POSITION; // lưu vị trí được chọn

    // Constructor
    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Nạp layout recycler_view_item.xml
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_category_custom, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);

        // Gán tên danh mục
        holder.tvCategoryName.setText(category.getName());

        // Lấy resource ID từ iconName
        int iconResId = category.getIconResId(context);

        if (position == selectedPosition) {
            holder.itemView.setBackgroundResource(R.drawable.bg_category_item_selected); // nền khi chọn
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bg_category_item); // nền mặc định
        }

        // Nếu tìm thấy icon thì hiển thị, không thì dùng icon mặc định
        if (iconResId != 0) {
            holder.ivCategoryIcon.setImageResource(iconResId);
        } else {
            holder.ivCategoryIcon.setImageResource(R.drawable.ic_default_category);
        }

        // Xử lý click item
        holder.itemView.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition(); // cập nhật vị trí chọn
            notifyDataSetChanged(); // refresh để highlight
            if (listener != null) {
                listener.onCategoryClick(category); // trả dữ liệu về activity
            }
        });
    }



    @Override
    public int getItemCount() {
        return categoryList != null ? categoryList.size() : 0;
    }

    // Lớp ViewHolder
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCategoryIcon;
        TextView tvCategoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCategoryIcon = itemView.findViewById(R.id.ivCategoryIcon);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }

    // Interface callback khi click vào category
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.listener = listener;
    }
}
