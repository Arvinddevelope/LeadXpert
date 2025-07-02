package com.arvind.leadxpert.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arvind.leadxpert.R;
import com.arvind.leadxpert.adapters.LeadAdapter;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.*;

public class MonthLeadsFragment extends Fragment {

    private RecyclerView recyclerView;
    private LeadAdapter adapter;
    private List<DocumentSnapshot> leadList = new ArrayList<>();
    private FirebaseFirestore db;
    private String userId;

    public MonthLeadsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_month_leads, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.monthLeadRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Pass empty listener or handle click
        adapter = new LeadAdapter(leadList, doc -> {
            // Optional: handle lead item click here
        });

        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadMonthLeads();
    }

    private void loadMonthLeads() {
        Calendar calNow = Calendar.getInstance();
        int thisMonth = calNow.get(Calendar.MONTH);
        int thisYear = calNow.get(Calendar.YEAR);

        db.collection("leads").document(userId).collection("items")
                .get().addOnSuccessListener(querySnapshot -> {
                    leadList.clear();
                    for (DocumentSnapshot doc : querySnapshot) {
                        Timestamp ts = doc.getTimestamp("timestamp");
                        if (ts != null) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(ts.toDate());
                            if (cal.get(Calendar.MONTH) == thisMonth && cal.get(Calendar.YEAR) == thisYear) {
                                leadList.add(doc);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}
