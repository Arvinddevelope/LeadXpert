package com.arvind.leadxpert.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arvind.leadxpert.R;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class LeadAdapter extends RecyclerView.Adapter<LeadAdapter.LeadViewHolder> {

    // Listener interface for click actions
    public interface OnLeadClickListener {
        void onLeadClick(DocumentSnapshot doc);
    }

    private List<DocumentSnapshot> leadList;
    private OnLeadClickListener listener;

    // Constructor
    public LeadAdapter(List<DocumentSnapshot> leadList, OnLeadClickListener listener) {
        this.leadList = leadList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LeadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lead, parent, false);
        return new LeadViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LeadViewHolder holder, int position) {
        DocumentSnapshot doc = leadList.get(position);
        String name = doc.getString("name");
        String phone = doc.getString("phone");
        String status = doc.getString("status");

        holder.nameText.setText("👤 " + (name != null ? name : "N/A"));
        holder.phoneText.setText("📞 " + (phone != null ? phone : "N/A"));
        holder.statusText.setText("📌 " + (status != null ? status : "Unknown"));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onLeadClick(doc);
        });
    }

    @Override
    public int getItemCount() {
        return leadList.size();
    }

    // ViewHolder class
    static class LeadViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, phoneText, statusText;

        public LeadViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.leadName);
            phoneText = itemView.findViewById(R.id.leadPhone);
            statusText = itemView.findViewById(R.id.leadStatus);
        }
    }
}
