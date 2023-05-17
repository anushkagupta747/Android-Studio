package com.example.homepage;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class app_download extends AppCompatActivity {

    private static final String TAG = "app_download";
    private ListView listView;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_download);


        String childId = SPUMaster.getChildId(getApplicationContext());
        String parentPassword = SPUMaster.getParentPassword(getApplicationContext());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String currentDate = sdf.format(new Date());
        String documentID=childId+parentPassword+currentDate;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ListView listView = findViewById(R.id.listview);

        db.collection("child_app_list")
                .document(documentID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            List<String> installedApps = (List<String>) document.get("InstalledApps");

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(app_download.this, android.R.layout.simple_list_item_1, installedApps);
                            listView.setAdapter(adapter);
                        } else {
                            // Handle the error
                        }
                    }
                });
    }
}