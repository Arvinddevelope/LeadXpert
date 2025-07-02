package com.arvind.leadxpert.utils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;

public class ExportUtil {

    public static void exportToCSV(Context context, List<Map<String, Object>> leads) {
        try {
            File folder = new File(Environment.getExternalStorageDirectory(), "LeadXpert");
            if (!folder.exists()) folder.mkdirs();

            File file = new File(folder, "leads_export.csv");
            FileWriter writer = new FileWriter(file);

            // Write header
            writer.append("Name,Phone,Email,Status,Notes,Timestamp\n");

            for (Map<String, Object> lead : leads) {
                writer.append(lead.get("name") + ",");
                writer.append(lead.get("phone") + ",");
                writer.append((lead.get("email") != null ? lead.get("email") : "") + ",");
                writer.append(lead.get("status") + ",");
                writer.append((lead.get("notes") != null ? lead.get("notes") : "") + ",");
                writer.append(lead.get("timestamp").toString());
                writer.append("\n");
            }

            writer.flush();
            writer.close();

            Toast.makeText(context, "Leads exported to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, "Export Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
