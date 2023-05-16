package com.example.homepage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.TimeUnit;


public class child_home extends AppCompatActivity {

    private CardView dnd, ec, sos,ws,ch;
    private static final int MY_PERMISSIONS_REQUEST_READ_CALL_LOG = 123;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    FirebaseFirestore db;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_home);
        setParentToken();
        Log.d("MAIN ACTIVITY WORK RUN", "Running work...");
        OneTimeWorkRequest initialRequest = new OneTimeWorkRequest.Builder(MyWorker2.class)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .build();

        WorkManager.getInstance(this).enqueue(initialRequest);


        dnd=(CardView) findViewById(R.id.card_dnd);
        dnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(child_home.this, dnd.class);
                startActivity(intent);
            }
        });

        ec=(CardView) findViewById(R.id.card_ec);
        ec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(child_home.this, emergency_contacts.class);
                startActivity(intent);
            }
        });

        sos=(CardView) findViewById(R.id.card_sos);
        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(child_home.this, sos.class);
                startActivity(intent);
            }
        });

        ws=(CardView) findViewById(R.id.card_ws);
        ws.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(child_home.this, web_search2.class);
                startActivity(intent);
            }
        });

        ch=(CardView) findViewById(R.id.card_ch);
        ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(child_home.this, child_helpline2.class);
                startActivity(intent);
            }
        });

    }

    private void setParentToken() {
        String parentid=SPUMaster.getParentId(getApplicationContext());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("child_details")
                .whereEqualTo("parent_id", parentid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                String parentToken = documentSnapshot.getString("parent_token");
                                // Use the parentToken as needed
                                SPUChildSupport.saveToken(getApplicationContext(), parentToken);
                                Log.d("Parent Token", parentToken);
                            } else {
                                // No matching child with the given parent ID found
                                Log.d("Parent Token", "No matching child found");
                            }
                        } else {
                            // Error occurred while querying the database
                            Log.e("Parent Token", "Error occurred: " + task.getException());
                        }
                    }
                });
    }
}