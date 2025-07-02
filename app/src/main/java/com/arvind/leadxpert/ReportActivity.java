package com.arvind.leadxpert;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class ReportActivity extends AppCompatActivity {

    private TextView tvTotal, tvMonth, tvConversion;
    private Button btnExport;
    private FirebaseFirestore db;
    private String userId;
    private int total = 0, month = 0, converted = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        tvTotal = findViewById(R.id.tvTotalLeads);
        tvMonth = findViewById(R.id.tvThisMonth);
        tvConversion = findViewById(R.id.tvConversionRate);
        btnExport = findViewById(R.id.btnExportReport);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        computeStats();

        btnExport.setOnClickListener(v -> {
            // Placeholder for CSV/PDF export logic
            Toast.makeText(this, "Exported report (to be implemented)", Toast.LENGTH_SHORT).show();
        });
    }

    private void computeStats() {
        db.collection("leads")
                .document(userId)
                .collection("items")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    total = 0;
                    month = 0;
                    converted = 0;

                    Calendar current = Calendar.getInstance();
                    int currentMonth = current.get(Calendar.MONTH);
                    int currentYear = current.get(Calendar.YEAR);

                    for (DocumentSnapshot doc : querySnapshot) {
                        total++;

                        // Status
                        String status = doc.getString("status");
                        if (status != null && status.equalsIgnoreCase("Converted")) {
                            converted++;
                        }

                        // Month Check
                        Timestamp timestamp = doc.getTimestamp("timestamp");
                        if (timestamp != null) {
                            Date leadDate = timestamp.toDate();
                            Calendar leadCal = Calendar.getInstance();
                            leadCal.setTime(leadDate);
                            if (leadCal.get(Calendar.MONTH) == currentMonth &&
                                    leadCal.get(Calendar.YEAR) == currentYear) {
                                month++;
                            }
                        }
                    }

                    double conversionRate = (total > 0) ? ((double) converted / total) * 100 : 0;

                    tvTotal.setText("Total Leads: " + total);
                    tvMonth.setText("This Month: " + month);
                    tvConversion.setText("Conversion Rate: " + String.format("%.1f", conversionRate) + "%");
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error loading stats", Toast.LENGTH_SHORT).show());
    }
}
