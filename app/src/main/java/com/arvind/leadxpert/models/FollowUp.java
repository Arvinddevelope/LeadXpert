package com.arvind.leadxpert.models;

public class FollowUp {
    private String date, time, note;

    public FollowUp() {} // required for Firestore

    public FollowUp(String date, String time, String note) {
        this.date = date;
        this.time = time;
        this.note = note;
    }

    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getNote() { return note; }
}
