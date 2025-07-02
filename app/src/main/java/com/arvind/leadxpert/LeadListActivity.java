package com.arvind.leadxpert;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arvind.leadxpert.adapters.LeadAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LeadListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private String userId;
    private List<DocumentSnapshot> leads = new ArrayList<>();
    private LeadAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_list);

        recyclerView = findViewById(R.id.leadRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new LeadAdapter(leads, doc -> {
            String leadId = doc.getId();
            Intent intent = new Intent(this, LeadDetailActivity.class);
            intent.putExtra("leadId", leadId);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadLeads();
    }

    private void loadLeads() {
        db.collection("leads").document(userId).collection("items")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    leads.clear();
                    leads.addAll(querySnapshot.getDocuments());
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle error
                });
    }
}
