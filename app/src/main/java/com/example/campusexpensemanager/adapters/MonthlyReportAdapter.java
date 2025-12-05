package com.example.campusexpensemanager.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.Data.dao.CategoryDAO;
import com.example.campusexpensemanager.R;
import com.example.campusexpensemanager.models.Category;
import com.example.campusexpensemanager.models.MonthlyReport;

import java.text.DecimalFormat;
import java.util.List;

public class MonthlyReportAdapter extends RecyclerView.Adapter<MonthlyReportAdapter.ReportViewHolder> {

    private final Context context;
    private final List<MonthlyReport> reportList;
    private final DecimalFormat moneyFormat = new DecimalFormat("#,##0.##");

    public MonthlyReportAdapter(Context context, List<MonthlyReport> reportList) {
        this.context = context;
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_monthly_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        MonthlyReport report = reportList.get(position);

        String categoryName = getCategoryNameFromId(report.getCategoryId());
        holder.tvCategoryName.setText(categoryName);

        holder.tvBudget.setText("Budget: " + moneyFormat.format(report.getBudget()));
        holder.tvExpense.setText("Expensed: " + moneyFormat.format(report.getTotalExpense()));
        holder.tvRemaining.setText("Remaining: " + moneyFormat.format(report.getRemaining()));

        boolean overBudget = report.isOverBudget();
        if (overBudget) {
            holder.tvStatus.setText("Out Budget");
            holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E91E63")));
            holder.tvStatus.setTextColor(Color.WHITE);
        } else {
            holder.tvStatus.setText("In Budget");
            holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
            holder.tvStatus.setTextColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return reportList != null ? reportList.size() : 0;
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName, tvBudget, tvExpense, tvRemaining, tvStatus;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvBudget = itemView.findViewById(R.id.tvBudget);
            tvExpense = itemView.findViewById(R.id.tvExpense);
            tvRemaining = itemView.findViewById(R.id.tvRemaining);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }

    /**
     * TODO: Ở đây tạm thời chỉ hiển thị "Category #id".
     * Cậu có CategoryDAO thì có thể đổi sang lấy tên thật từ DB.
     */
    private String getCategoryNameFromId(int categoryId) {
//        return "Category #" + categoryId;
        // Ví dụ nếu có CategoryDAO:
         CategoryDAO dao = new CategoryDAO(context);
         Category c = dao.getCategoryById(categoryId);
         return c != null ? c.getName() : "Khác";
    }
}
