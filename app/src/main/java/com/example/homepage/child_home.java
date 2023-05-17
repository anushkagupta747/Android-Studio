package com.example.homepage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.TimeUnit;


public class child_home extends AppCompatActivity implements SensorEventListener {

    private CardView dnd, ec, sos,ws,ch;
    private static final int MY_PERMISSIONS_REQUEST_READ_CALL_LOG = 123;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private static final int SHAKE_THRESHOLD = 8000; // Threshold value for detecting an accident
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    FirebaseFirestore db;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_home);

        //FOR PUSH NOTIFICATION DELIVERY
        setParentToken();
        //FOR PUSH NOTIFICATION DELIVERY
        setChildPass();
        setParentPass();

        //FOR BACKGROUND SERVICE
        Log.d("MAIN ACTIVITY WORK RUN", "Running work...");
        OneTimeWorkRequest initialRequest = new OneTimeWorkRequest.Builder(MyWorker2.class)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .build();

        WorkManager.getInstance(this).enqueue(initialRequest);

        //FOR ACCIDENT DETECTION
        Uri imagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ImageContentObserver observer = new ImageContentObserver(this, imagesUri);
        getContentResolver().registerContentObserver(imagesUri, true, observer);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

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

    private void setChildPass() {
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
                                String childpassword = documentSnapshot.getString("child_password");
                                // Use the parentToken as needed
                                SPUMaster.saveChildPassword(getApplicationContext(), childpassword);
                                Log.d("Child Password", childpassword);
                            } else {
                                // No matching child with the given parent ID found
                                Log.d("Child Password", "No matching child found");
                            }
                        } else {
                            // Error occurred while querying the database
                            Log.e("Child Password", "Error occurred: " + task.getException());
                        }
                    }
                });
    }

    private void setParentPass() {
        String parentid=SPUMaster.getParentId(getApplicationContext());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("parent_details")
                .whereEqualTo("parent_id", parentid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                String parentpassword = documentSnapshot.getString("password");
                                // Use the parentToken as needed
                                SPUMaster.saveParentPassword(getApplicationContext(), parentpassword);
                                Log.d("Parent Password", parentpassword);
                            } else {
                                // No matching child with the given parent ID found
                                Log.d("Parent Password", "No matching child found");
                            }
                        } else {
                            // Error occurred while querying the database
                            Log.e("Parent Password", "Error occurred: " + task.getException());
                        }
                    }
                });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float[] values = event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastUpdate) > 100) {
                long diffTime = (currentTime - lastUpdate);
                lastUpdate = currentTime;
                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
                if (speed > SHAKE_THRESHOLD) {
                    // Accident detected, trigger alarm
                    Toast.makeText(this, "dont fall", Toast.LENGTH_SHORT).show();
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing
    }
}