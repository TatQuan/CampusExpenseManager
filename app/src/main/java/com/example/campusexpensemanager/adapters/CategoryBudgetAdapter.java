package com.example.campusexpensemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.Data.dao.BudgetDAO;
import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.models.Budget;
import com.example.campusexpensemanager.models.Category;

import java.util.List;

public class CategoryBudgetAdapter extends RecyclerView.Adapter<CategoryBudgetAdapter.ViewHolder> {

    private Context context;
    private List<Category> categoryList;
    private int userId; //Nạp user vào để tìm budget theo userId
    private OnItemClickListener listener;
    private BudgetDAO budgetDAO;

    private double budgetTotal;


    public interface OnItemClickListener {
        void onItemClick(Category category);
    }

    public CategoryBudgetAdapter(Context context, List<Category> categoryList, int userId,
                                 OnItemClickListener listener) {
        this.context = context;
        this.categoryList = categoryList;
        this.userId = userId;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_category_budget_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categoryList.get(position);


        // Tìm budget theo categoryId và userId
        budgetTotal = 0;
        budgetDAO = new BudgetDAO(context);
        budgetTotal = budgetDAO.getSumAmountBudgetByCategory(category.getId(), userId);

        holder.tvCategoryName.setText(category.getName());

        if (budgetTotal != 0) {
            holder.tvBudgetAmount.setText(String.valueOf(budgetTotal) + " VND");
        } else {
            holder.tvBudgetAmount.setText("0 VND");
        }


        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(category);
            }
        });

        int iconResId = category.getIconResId(context);
        holder.icCategory.setImageResource(iconResId != 0 ? iconResId : R.drawable.ic_default_category);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName, tvBudgetAmount;
        ImageView icCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategory);
            tvBudgetAmount = itemView.findViewById(R.id.tvBudget);
            icCategory = itemView.findViewById(R.id.icCategory);
        }
    }


}
