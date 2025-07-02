package com.arvind.leadxpert.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arvind.leadxpert.R;
import com.arvind.leadxpert.models.ReportModel;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private final List<ReportModel> reportList;

    public ReportAdapter(List<ReportModel> reportList) {
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_report_entry, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        ReportModel model = reportList.get(position);

        holder.tvMonth.setText(model.getMonth());
        holder.tvTotal.setText("Total: " + model.getTotalLeads());
        holder.tvConverted.setText("Converted: " + model.getConvertedLeads());
        holder.tvRate.setText("Conversion: " + model.getConversionRate() + "%");
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView tvMonth, tvTotal, tvConverted, tvRate;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMonth = itemView.findViewById(R.id.tvMonth);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvConverted = itemView.findViewById(R.id.tvConverted);
            tvRate = itemView.findViewById(R.id.tvRate);
        }
    }
}
