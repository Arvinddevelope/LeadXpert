package com.arvind.leadxpert;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvTodayCount, tvYesterdayCount, tvMonthCount, tvTotalCount;
    private Button addLeadBtn, viewLeadsBtn, openReportsBtn;
    private Button openProfileBtn, openSettingsBtn;

    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize views
        tvTodayCount = findViewById(R.id.tvTodayCount);
        tvYesterdayCount = findViewById(R.id.tvYesterdayCount);
        tvMonthCount = findViewById(R.id.tvMonthCount);
        tvTotalCount = findViewById(R.id.tvTotalCount);

        addLeadBtn = findViewById(R.id.addLeadBtn);
        viewLeadsBtn = findViewById(R.id.viewLeadsBtn);
        openReportsBtn = findViewById(R.id.openReportsBtn);
        openProfileBtn = findViewById(R.id.openProfileBtn);
        openSettingsBtn = findViewById(R.id.openSettingsBtn);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Button Clicks
        addLeadBtn.setOnClickListener(v -> startActivity(new Intent(this, AddLeadActivity.class)));

        viewLeadsBtn.setOnClickListener(v -> startActivity(new Intent(this, LeadListActivity.class)));

        openReportsBtn.setOnClickListener(v -> startActivity(new Intent(this, ReportActivity.class)));

        openProfileBtn.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));

        openSettingsBtn.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        // Load Dashboard Stats
        loadStats();
    }

    private void loadStats() {
        db.collection("leads").document(userId).collection("items")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    int today = 0, yesterday = 0, month = 0, total = 0;

                    Calendar now = Calendar.getInstance();
                    int currentMonth = now.get(Calendar.MONTH);
                    int currentYear = now.get(Calendar.YEAR);

                    Calendar yesterdayCal = Calendar.getInstance();
                    yesterdayCal.add(Calendar.DAY_OF_YEAR, -1);
                    int yDay = yesterdayCal.get(Calendar.DAY_OF_MONTH);
                    int yMonth = yesterdayCal.get(Calendar.MONTH);
                    int yYear = yesterdayCal.get(Calendar.YEAR);

                    for (DocumentSnapshot doc : querySnapshot) {
                        Timestamp ts = doc.getTimestamp("timestamp");
                        if (ts == null) continue;

                        Date date = ts.toDate();
                        Calendar leadCal = Calendar.getInstance();
                        leadCal.setTime(date);

                        total++;

                        if (DateUtils.isToday(date.getTime())) {
                            today++;
                        } else if (
                                leadCal.get(Calendar.YEAR) == yYear &&
                                        leadCal.get(Calendar.MONTH) == yMonth &&
                                        leadCal.get(Calendar.DAY_OF_MONTH) == yDay) {
                            yesterday++;
                        }

                        if (
                                leadCal.get(Calendar.MONTH) == currentMonth &&
                                        leadCal.get(Calendar.YEAR) == currentYear) {
                            month++;
                        }
                    }

                    // Set stats
                    tvTodayCount.setText(String.valueOf(today));
                    tvYesterdayCount.setText(String.valueOf(yesterday));
                    tvMonthCount.setText(String.valueOf(month));
                    tvTotalCount.setText(String.valueOf(total));
                })
                .addOnFailureListener(e -> {
                    // Fallback values
                    tvTodayCount.setText("0");
                    tvYesterdayCount.setText("0");
                    tvMonthCount.setText("0");
                    tvTotalCount.setText("0");
                });
    }
}
