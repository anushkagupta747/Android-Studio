package com.example.homepage;

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

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;


public class child_home extends AppCompatActivity {

    private CardView dnd, ec, sos;
    private static final int MY_PERMISSIONS_REQUEST_READ_CALL_LOG = 123;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    FirebaseFirestore db;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_home);

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

    }
}