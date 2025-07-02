package com.arvind.leadxpert;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arvind.leadxpert.adapters.FollowUpAdapter;
import com.arvind.leadxpert.models.FollowUp;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.*;

public class FollowUpActivity extends AppCompatActivity {

    private EditText dateInput, timeInput, noteInput;
    private Button btnScheduleFollowUp;
    private RecyclerView recyclerView;
    private FollowUpAdapter adapter;
    private List<FollowUp> list = new ArrayList<>();
    private FirebaseFirestore db;
    private String userId, leadId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_up);

        dateInput = findViewById(R.id.followUpDate);
        timeInput = findViewById(R.id.followUpTime);
        noteInput = findViewById(R.id.followUpNote);
        btnScheduleFollowUp = findViewById(R.id.btnScheduleFollowUp);
        recyclerView = findViewById(R.id.followUpRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FollowUpAdapter(list);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        leadId = getIntent().getStringExtra("leadId");

        dateInput.setOnClickListener(v -> showDatePicker());
        timeInput.setOnClickListener(v -> showTimePicker());

        btnScheduleFollowUp.setOnClickListener(v -> saveFollowUp());

        loadFollowUps();
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) ->
                        dateInput.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1),
                year, month, day);
        dpd.show();
    }

    private void showTimePicker() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(this,
                (view, hourOfDay, minute1) -> {
                    String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
                    timeInput.setText(formattedTime);
                }, hour, minute, true);
        tpd.show();
    }

    private void saveFollowUp() {
        String date = dateInput.getText().toString().trim();
        String time = timeInput.getText().toString().trim();
        String note = noteInput.getText().toString().trim();

        if (date.isEmpty() || time.isEmpty() || note.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> followUp = new HashMap<>();
        followUp.put("date", date);
        followUp.put("time", time);
        followUp.put("note", note);
        followUp.put("timestamp", Timestamp.now());

        db.collection("leads").document(userId)
                .collection("items").document(leadId)
                .collection("followups")
                .add(followUp)
                .addOnSuccessListener(docRef -> {
                    Toast.makeText(this, "Follow-up scheduled", Toast.LENGTH_SHORT).show();
                    dateInput.setText("");
                    timeInput.setText("");
                    noteInput.setText("");
                    loadFollowUps();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void loadFollowUps() {
        db.collection("leads").document(userId)
                .collection("items").document(leadId)
                .collection("followups")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    list.clear();
                    for (DocumentSnapshot doc : querySnapshot) {
                        FollowUp followUp = doc.toObject(FollowUp.class);
                        list.add(followUp);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error loading follow-ups", Toast.LENGTH_SHORT).show());
    }
}
