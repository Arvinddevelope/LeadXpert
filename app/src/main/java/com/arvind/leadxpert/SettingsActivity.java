package com.arvind.leadxpert;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchNotifications, switchDarkMode;
    private LinearLayout layoutChangePassword, layoutExport, layoutLogout;
    private SharedPreferences preferences;
    private static final String PREFS_NAME = "settings_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize views
        switchNotifications = findViewById(R.id.switchNotifications);
        switchDarkMode = findViewById(R.id.switchDarkMode);
        layoutChangePassword = findViewById(R.id.layoutChangePassword);
        layoutExport = findViewById(R.id.layoutExport);
        layoutLogout = findViewById(R.id.layoutLogout);

        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Load switch states
        switchNotifications.setChecked(preferences.getBoolean("notifications_enabled", true));
        switchDarkMode.setChecked(preferences.getBoolean("dark_mode", false));

        // Switch listeners
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("notifications_enabled", isChecked).apply();
            Toast.makeText(this, "Notifications " + (isChecked ? "Enabled" : "Disabled"), Toast.LENGTH_SHORT).show();
        });

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("dark_mode", isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(isChecked ?
                    AppCompatDelegate.MODE_NIGHT_YES :
                    AppCompatDelegate.MODE_NIGHT_NO);
        });

        layoutChangePassword.setOnClickListener(v -> {
            // Open ForgotPasswordActivity to reset password
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        });

        layoutExport.setOnClickListener(v -> {
            // Handle export logic (CSV export can be implemented here)
            Toast.makeText(this, "Exporting leads to CSV...", Toast.LENGTH_SHORT).show();
        });

        layoutLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
