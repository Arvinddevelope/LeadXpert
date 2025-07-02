package com.arvind.leadxpert;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileActivity extends AppCompatActivity {

    private EditText nameInput, emailView;
    private Button btnSave;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        nameInput = findViewById(R.id.nameInput);
        emailView = findViewById(R.id.emailView);
        btnSave = findViewById(R.id.btnSaveProfile);
        auth = FirebaseAuth.getInstance();

        // Get current user
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            nameInput.setText(user.getDisplayName());
            emailView.setText(user.getEmail());
            emailView.setEnabled(false);
        }

        // Save button listener
        btnSave.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update profile display name
            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();

            if (user != null) {
                user.updateProfile(request)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }
}
