package com.arvind.leadxpert.models;

public class Report {
    private String month;
    private int totalLeads;
    private int convertedLeads;

    public Report() {}

    public Report(String month, int totalLeads, int convertedLeads) {
        this.month = month;
        this.totalLeads = totalLeads;
        this.convertedLeads = convertedLeads;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getTotalLeads() {
        return totalLeads;
    }

    public void setTotalLeads(int totalLeads) {
        this.totalLeads = totalLeads;
    }

    public int getConvertedLeads() {
        return convertedLeads;
    }

    public void setConvertedLeads(int convertedLeads) {
        this.convertedLeads = convertedLeads;
    }

    public int getConversionRate() {
        return totalLeads == 0 ? 0 : (convertedLeads * 100 / totalLeads);
    }
}
