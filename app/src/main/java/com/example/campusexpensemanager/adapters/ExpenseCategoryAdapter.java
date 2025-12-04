package com.example.campusexpensemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.models.Expense;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ExpenseCategoryAdapter extends RecyclerView.Adapter<ExpenseCategoryAdapter.ExpenseViewHolder> {

    public interface OnExpenseClickListener {
        void onExpenseClick(Expense expense);
    }

    private final Context context;
    private final LayoutInflater inflater;
    private final OnExpenseClickListener listener;
    private final List<Expense> expenseList;

    public ExpenseCategoryAdapter(Context context,
                                  List<Expense> expenseList,
                                  OnExpenseClickListener listener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
        // tránh null + copy list đầu vào
        this.expenseList = new ArrayList<>();
        if (expenseList != null) {
            this.expenseList.addAll(expenseList);
        }
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);

        // Format số tiền: vd: 120,000 VND
        String amountText = formatAmount(expense.getAmount()) + " VND";
        holder.tvAmount.setText(amountText);

        // Ngày
        holder.tvDate.setText(expense.getDate());

        // Description: nếu description trống thì fallback sang categoryName
        String desc;
        if (expense.getDescription() != null && !expense.getDescription().isEmpty()) {
            desc = expense.getDescription();
        } else {
            desc = expense.getCategoryName();
        }
        holder.tvDescription.setText(desc);

        // Recurring badge
        if (expense.getIsRecurring() == 1) {
            holder.tvRecurring.setVisibility(View.VISIBLE);
        } else {
            holder.tvRecurring.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onExpenseClick(expense);
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    // Hàm update list dùng chung
    public void updateExpenseList(List<Expense> newList) {
        expenseList.clear();
        if (newList != null) {
            expenseList.addAll(newList);
        }
        notifyDataSetChanged();
    }

    private String formatAmount(double amount) {
        // 120000 -> 120,000
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(amount);
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {

        TextView tvAmount;
        TextView tvDate;
        TextView tvRecurring;
        TextView tvDescription;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvRecurring = itemView.findViewById(R.id.tvRecurring);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }
}
