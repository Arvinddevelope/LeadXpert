package com.arvind.leadxpert;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddLeadActivity extends AppCompatActivity {

    private EditText customerNameInput, phoneInput, emailInput, notesInput;
    private Spinner statusSpinner;
    private Button saveLeadBtn;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lead);

        // Initialize Views
        customerNameInput = findViewById(R.id.customerNameInput);
        phoneInput = findViewById(R.id.phoneInput);
        emailInput = findViewById(R.id.emailInput);
        notesInput = findViewById(R.id.notesInput);
        statusSpinner = findViewById(R.id.statusSpinner);
        saveLeadBtn = findViewById(R.id.saveLeadBtn);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Setup spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"New", "In Progress", "Converted", "Lost"});
        statusSpinner.setAdapter(adapter);

        // Button click
        saveLeadBtn.setOnClickListener(v -> saveLead());
    }

    private void saveLead() {
        String name = customerNameInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String notes = notesInput.getText().toString().trim();
        String status = statusSpinner.getSelectedItem().toString();

        // Validation
        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Customer name and phone are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare data
        Map<String, Object> lead = new HashMap<>();
        lead.put("name", name);
        lead.put("phone", phone);
        lead.put("email", email);
        lead.put("notes", notes);
        lead.put("status", status);
        lead.put("source", "App");
        lead.put("timestamp", Timestamp.now());

        // Add to Firestore
        db.collection("leads")
                .document(userId)
                .collection("items")
                .add(lead)
                .addOnSuccessListener(docRef -> {
                    Toast.makeText(this, "Lead added successfully", Toast.LENGTH_SHORT).show();
                    finish(); // go back to previous screen
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}
