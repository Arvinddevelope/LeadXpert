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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;

public class StatusFilterFragment extends Fragment {

    private RecyclerView recyclerView;
    private LeadAdapter adapter;
    private List<DocumentSnapshot> leadList = new ArrayList<>();
    private FirebaseFirestore db;
    private String userId;
    private String statusFilter = "New"; // default

    public StatusFilterFragment() {}

    public void setStatusFilter(String status) {
        this.statusFilter = status;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_status_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.statusLeadRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter with click listener
        adapter = new LeadAdapter(leadList, doc -> {
            // Handle item click if needed
        });

        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadStatusLeads();
    }

    private void loadStatusLeads() {
        db.collection("leads").document(userId).collection("items")
                .whereEqualTo("status", statusFilter)
                .get().addOnSuccessListener(querySnapshot -> {
                    leadList.clear();
                    for (DocumentSnapshot doc : querySnapshot) {
                        leadList.add(doc);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}
