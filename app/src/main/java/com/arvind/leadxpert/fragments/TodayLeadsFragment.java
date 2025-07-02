package com.arvind.leadxpert.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arvind.leadxpert.R;
import com.arvind.leadxpert.adapters.LeadAdapter;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodayLeadsFragment extends Fragment {

    private RecyclerView recyclerView;
    private LeadAdapter adapter;
    private List<DocumentSnapshot> leadList = new ArrayList<>();
    private FirebaseFirestore db;
    private String userId;

    public TodayLeadsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_today_leads, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.todayLeadRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new LeadAdapter(leadList, doc -> {
            // Optional: handle lead click here
        });

        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadTodayLeads();
    }

    private void loadTodayLeads() {
        db.collection("leads").document(userId).collection("items")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    leadList.clear();

                    for (DocumentSnapshot doc : querySnapshot) {
                        Timestamp ts = doc.getTimestamp("timestamp");
                        if (ts != null) {
                            Date leadDate = ts.toDate();
                            if (android.text.format.DateUtils.isToday(leadDate.getTime())) {
                                leadList.add(doc);
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                });
    }
}
