package com.arvind.leadxpert.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.arvind.leadxpert.R;
import com.arvind.leadxpert.models.FollowUp;

import java.util.List;

public class FollowUpAdapter extends RecyclerView.Adapter<FollowUpAdapter.FollowUpViewHolder> {

    private final List<FollowUp> list;

    public FollowUpAdapter(List<FollowUp> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public FollowUpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_follow_up, parent, false);
        return new FollowUpViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowUpViewHolder holder, int position) {
        FollowUp f = list.get(position);
        holder.date.setText("📅 " + f.getDate());
        holder.time.setText("⏰ " + f.getTime());
        holder.note.setText("📝 " + f.getNote());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class FollowUpViewHolder extends RecyclerView.ViewHolder {
        TextView date, time, note;

        FollowUpViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.followUpDateText);
            time = itemView.findViewById(R.id.followUpTimeText);
            note = itemView.findViewById(R.id.followUpNoteText);
        }
    }
}
