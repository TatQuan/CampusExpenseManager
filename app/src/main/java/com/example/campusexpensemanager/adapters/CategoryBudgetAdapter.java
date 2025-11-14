package com.example.campusexpensemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.models.Budget;
import com.example.campusexpensemanager.models.Category;

import java.util.List;

public class CategoryBudgetAdapter extends RecyclerView.Adapter<CategoryBudgetAdapter.ViewHolder> {

    private Context context;
    private List<Category> categoryList;
    private List<Budget> budgetList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Category category, Budget budget);
    }

    public CategoryBudgetAdapter(Context context, List<Category> categoryList,
                                 List<Budget> budgetList, OnItemClickListener listener) {
        this.context = context;
        this.categoryList = categoryList;
        this.budgetList = budgetList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recycler_view_category_vertical, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categoryList.get(position);

        // Tìm budget theo categoryId
        Budget matchedBudget = null;
        for (Budget b : budgetList) {
            if (b.getCategoryId() == category.getId()) {
                matchedBudget = b;
                break;
            }
        }

        holder.tvCategoryName.setText(category.getName());

        if (matchedBudget != null) {
            holder.tvBudgetAmount.setText("Budget: " + matchedBudget.getBudgetAmount());
        } else {
            holder.tvBudgetAmount.setText("Budget: Chưa đặt");
        }

        Budget finalBudget = matchedBudget;

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(category, finalBudget);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCategoryName, tvBudgetAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvBudgetAmount = itemView.findViewById(R.id.tvBudget);
        }
    }
}
