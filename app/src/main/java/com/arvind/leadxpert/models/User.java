package com.arvind.leadxpert.models;

public class User {
    private String uid;
    private String name;
    private String email;
    private boolean notificationsEnabled;
    private boolean darkMode;

    public User() {}

    public User(String uid, String name, String email, boolean notificationsEnabled, boolean darkMode) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.notificationsEnabled = notificationsEnabled;
        this.darkMode = darkMode;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }
}
