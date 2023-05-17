package com.example.homepage;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class child_location extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_location);
        ListView listView = findViewById(R.id.listview);

        String childIds = SPUMaster.getChildId(getApplicationContext());
        String parentPassword = SPUMaster.getParentPassword(getApplicationContext());
        String childID=childIds+parentPassword;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("child_location")
                .whereEqualTo("childID", childID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();

                            List<String> longitudeList = new ArrayList<>();
                            List<String> latitudeList = new ArrayList<>();

                            for (DocumentSnapshot document : documents) {
                                String longitude = String.valueOf(document.getDouble("longitude"));
                                String latitude = String.valueOf(document.getDouble("latitude"));

                                longitudeList.add(longitude);
                                latitudeList.add(latitude);

                                // ... process other fields
                                Log.d(longitude + " " + latitude, "onComplete: ");
                            }

                            // Set up the adapter for the ListView
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(child_location.this,
                                    android.R.layout.simple_list_item_1, combineLists(longitudeList, latitudeList));
                            listView.setAdapter(adapter);
                        } else {
                            Log.e(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private List<String> combineLists(List<String> list1, List<String> list2) {
        List<String> combinedList = new ArrayList<>();
        for (int i = 0; i < list1.size(); i++) {
            String item = "Latitude: " + list2.get(i) +"  , Longitude: " + list1.get(i);
            combinedList.add(item);
        }
        return combinedList;
    }
}