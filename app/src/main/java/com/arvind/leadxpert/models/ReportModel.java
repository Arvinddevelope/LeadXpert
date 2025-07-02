package com.arvind.leadxpert.models;

public class ReportModel {
    private String month;
    private int totalLeads;
    private int convertedLeads;

    public ReportModel() {}

    public ReportModel(String month, int totalLeads, int convertedLeads) {
        this.month = month;
        this.totalLeads = totalLeads;
        this.convertedLeads = convertedLeads;
    }

    public String getMonth() {
        return month;
    }

    public int getTotalLeads() {
        return totalLeads;
    }

    public int getConvertedLeads() {
        return convertedLeads;
    }

    public int getConversionRate() {
        return totalLeads == 0 ? 0 : (convertedLeads * 100 / totalLeads);
    }
}
