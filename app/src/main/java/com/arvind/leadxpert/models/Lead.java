package com.arvind.leadxpert.models;

import com.google.firebase.Timestamp;

public class Lead {
    private String id;
    private String name;
    private String phone;
    private String email;
    private String source;
    private String status;
    private String notes;
    private Timestamp timestamp;

    public Lead() {
        // Required for Firestore
    }

    public Lead(String id, String name, String phone, String email, String source, String status, String notes, Timestamp timestamp) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.source = source;
        this.status = status;
        this.notes = notes;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getSource() { return source; }

    public void setSource(String source) { this.source = source; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }

    public void setNotes(String notes) { this.notes = notes; }

    public Timestamp getTimestamp() { return timestamp; }

    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
}
