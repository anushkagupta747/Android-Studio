package com.example.homepage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class call_history extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_history);
//        String childID = SPUMaster.getChildId(getApplicationContext()); // Add this line to retrieve the child ID


        String childIds = SPUMaster.getChildId(getApplicationContext());
        String parentPassword = SPUMaster.getParentPassword(getApplicationContext());
        String childID=childIds+parentPassword;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ListView listView = findViewById(R.id.call_log_list_view);

        db.collection("call_logs")
                .whereEqualTo("childid", childID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> callLogDataList = new ArrayList<>();

                            for (DocumentSnapshot document : task.getResult()) {
                                String callID = document.getString("callid");
                                String name = document.getString("name");
                                String number = document.getString("number");
                                String date = document.getString("date");
                                String duration = document.getString("duration");
                                String type = document.getString("type");

                                callLogDataList.add("\n Name: " + name + "\n Number: " + number + "\n Date: " + date + "\n Duration: " + duration + "\n Type: " + type+"\n");
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(call_history.this, android.R.layout.simple_list_item_1, callLogDataList);
                            listView.setAdapter(adapter);
                        } else {
                            // Handle the error
                        }
                    }
                });
    }
}