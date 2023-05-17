package com.example.homepage;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class app_usage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_usage);

        String childId = SPUMaster.getChildId(getApplicationContext());
        String parentPassword = SPUMaster.getParentPassword(getApplicationContext());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDate = sdf.format(new Date());
        String documentID=childId+parentPassword+currentDate;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ListView listView = findViewById(R.id.listview);

        db.collection("child_app_usage")
                .document(documentID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Map<String, Long> finalResult = (Map<String, Long>) document.get("finalResult");

                            List<String> usageData = new ArrayList<>();

                            // Add TotalUsage entry with formatting
                            if (finalResult.containsKey("TotalUsage")) {
                                String totalUsage = "TotalUsage in MINUTES : " + finalResult.get("TotalUsage");
                                SpannableStringBuilder str = new SpannableStringBuilder(totalUsage.toUpperCase());
                                str.setSpan(new StyleSpan(Typeface.BOLD), 0, totalUsage.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                TextView headerView = new TextView(app_usage.this);
                                headerView.setText(str);
                                headerView.setGravity(Gravity.CENTER);
                                usageData.add(0, headerView.getText().toString());
                                finalResult.remove("TotalUsage");
                            }

                            // Sort remaining entries in descending order
                            List<Map.Entry<String, Long>> sortedEntries = new ArrayList<>(finalResult.entrySet());
                            Collections.sort(sortedEntries, new Comparator<Map.Entry<String, Long>>() {
                                @Override
                                public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                                    return o2.getValue().compareTo(o1.getValue());
                                }
                            });

                            // Add sorted entries to usageData list
                            for (Map.Entry<String, Long> entry : sortedEntries) {
                                usageData.add(entry.getKey() + ": " + entry.getValue());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(app_usage.this, android.R.layout.simple_list_item_1, usageData);
                            listView.setAdapter(adapter);
                        } else {
                            // Handle the error
                        }
                    }
                });
    }
}