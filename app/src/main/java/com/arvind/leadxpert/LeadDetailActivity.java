package com.arvind.leadxpert;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

public class LeadDetailActivity extends AppCompatActivity {

    private TextView tvName, tvPhone, tvEmail, tvSource;
    private Spinner statusSpinner;
    private EditText notesInput;
    private Button btnCall, btnMessage, btnFollowUp, btnSaveUpdates;
    private FirebaseFirestore db;
    private String userId, leadId;
    private DocumentReference leadRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_detail);

        // Initialize views
        tvName = findViewById(R.id.tvCustomerName);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvSource = findViewById(R.id.tvSource);
        statusSpinner = findViewById(R.id.statusSpinner);
        notesInput = findViewById(R.id.notesInput);
        btnCall = findViewById(R.id.btnCall);
        btnMessage = findViewById(R.id.btnMessage);
        btnFollowUp = findViewById(R.id.btnFollowUp);
        btnSaveUpdates = findViewById(R.id.btnSaveUpdates);

        // Setup spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"New", "In Progress", "Converted", "Lost"});
        statusSpinner.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        leadId = getIntent().getStringExtra("leadId");
        leadRef = db.collection("leads").document(userId).collection("items").document(leadId);

        // Button actions
        btnCall.setOnClickListener(v -> {
            String phone = tvPhone.getText().toString().replace("📞 ", "");
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone)));
        });

        btnMessage.setOnClickListener(v -> {
            String phone = tvPhone.getText().toString().replace("📞 ", "");
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phone)));
        });

        btnFollowUp.setOnClickListener(v -> {
            Intent i = new Intent(this, FollowUpActivity.class);
            i.putExtra("leadId", leadId);
            startActivity(i);
        });

        btnSaveUpdates.setOnClickListener(v -> saveUpdates());

        loadLeadDetails();
    }

    private void loadLeadDetails() {
        leadRef.get().addOnSuccessListener(doc -> {
            if (doc.exists()) {
                tvName.setText(doc.getString("name"));
                tvPhone.setText("📞 " + doc.getString("phone"));
                tvEmail.setText("📧 " + doc.getString("email"));
                tvSource.setText("Source: " + doc.getString("source"));
                notesInput.setText(doc.getString("notes"));
                String status = doc.getString("status");
                if (status != null) {
                    ArrayAdapter adapter = (ArrayAdapter) statusSpinner.getAdapter();
                    int index = adapter.getPosition(status);
                    if (index >= 0) statusSpinner.setSelection(index);
                }
            } else {
                Toast.makeText(this, "Lead not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void saveUpdates() {
        String notes = notesInput.getText().toString().trim();
        String status = statusSpinner.getSelectedItem().toString();

        leadRef.update("notes", notes, "status", status)
                .addOnSuccessListener(a ->
                        Toast.makeText(this, "Lead updated successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
