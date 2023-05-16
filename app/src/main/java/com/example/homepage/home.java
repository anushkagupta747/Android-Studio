package com.example.homepage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class home extends AppCompatActivity {

    private CardView general;
    private CardView call_history;
    private CardView app_usage;
    private CardView app_dwd;
    private CardView card_location;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");

//        setChildID();

        general = (CardView) findViewById(R.id.card_general);
        general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, GeneralActivity.class);
                startActivity(intent);
            }
        });


        call_history = (CardView) findViewById(R.id.card_call_history);
        call_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, call_history.class);
                startActivity(intent);
            }
        });

        app_usage = (CardView) findViewById(R.id.card_app_usage);
        app_usage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, app_usage.class);
                startActivity(intent);
            }
        });

        app_dwd = (CardView) findViewById(R.id.card_app_dwd);
        app_dwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, app_download.class);
                startActivity(intent);
            }
        });

        card_location = (CardView) findViewById(R.id.card_location);
        card_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home.this, child_location.class);
                startActivity(intent);
            }
        });

    }

//    public void setChildID(){
//        String parentID = SPUMaster.getParentId(getApplicationContext());
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        String parentId = "abc";
//
//        db.collection("child")
//                .whereEqualTo("parent_id", parentId)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (DocumentSnapshot document : task.getResult()) {
//                                String childId = document.getId();
//                                Log.d("MainActivity", "Child ID: " + childId);
//                            }
//                        } else {
//                            Log.d("MainActivity", "Error getting documents: ", task.getException());
//                        }
//                    }
//                });

    }



