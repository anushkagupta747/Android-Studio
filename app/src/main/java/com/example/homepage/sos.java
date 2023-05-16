package com.example.homepage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class sos extends AppCompatActivity {

    private static final int REQUEST_CALL_PHONE_PERMISSION = 1;
    private Button sosButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        sosButton = findViewById(R.id.sos_button);
        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = SPUChildSupport.getPhone(getApplicationContext());
                makeSosCall(phoneNumber);
            }
        });

        String parentName = SPUMaster.getParentId(getApplicationContext()); // Replace with the actual parent name
        getParentPhone(parentName);
    }

    private void makeSosCall(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_PERMISSION);
        } else {
            startActivity(callIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CALL_PHONE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String phoneNumber = SPUChildSupport.getPhone(getApplicationContext());
                makeSosCall(phoneNumber);
            }
        }
    }

    private void getParentPhone(String parentName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("parent_details")
                .whereEqualTo("parent_name", parentName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                                String phoneNumber = documentSnapshot.getString("phone");
                                // Use the phoneNumber as needed
                                Log.d("Phone Number", phoneNumber);
                                SPUChildSupport.savePhone(getApplicationContext(), phoneNumber);
                            } else {
                                // No matching parent with the given name found
                                Log.d("Phone Number", "No matching parent found");
                            }
                        } else {
                            // Error occurred while querying the database
                            Log.e("Phone Number", "Error occurred: " + task.getException());
                        }
                    }
                });
    }
}
