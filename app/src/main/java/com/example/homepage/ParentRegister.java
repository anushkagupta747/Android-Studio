package com.example.homepage;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;

public class ParentRegister extends AppCompatActivity {

    FirebaseFirestore db;
    private EditText fullNameEditText;
    private EditText emailAddressEditText;
    private EditText phoneEditText;
    private EditText passwordEditText;
    private EditText retypePasswordEditText;
    private Button signUpButton;
    Button login_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_register);
        db = FirebaseFirestore.getInstance();

        // Initialize EditText fields
        fullNameEditText = findViewById(R.id.parent_fullname_edit);
        emailAddressEditText = findViewById(R.id.parent_emailaddress_edit);
        phoneEditText = findViewById(R.id.parent_number_edit);
        passwordEditText = findViewById(R.id.parent_password_edit);
        retypePasswordEditText = findViewById(R.id.parent_repassword_edit);

        // Initialize Button
        signUpButton = findViewById(R.id.parent_signup_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("111111", "onClick: ");
                // Retrieve the values from EditText fields and store them in strings
                String fullName = fullNameEditText.getText().toString();
                String emailAddress = emailAddressEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String retypePassword = retypePasswordEditText.getText().toString();
                // Perform sign up logic
                // Query to check if the email address already exists in the 'parent_details' collection
                db.collection("parent_details")
                        .whereEqualTo("email", emailAddress)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                        // Email address already exists in the database
                                        Log.d("parent add error", "Email address already exists");
                                        Toast.makeText(ParentRegister.this, "Email address already exists", Toast.LENGTH_SHORT).show();
                                        // Handle the error or display a message to the user
                                    } else {
                                        // Email address doesn't exist, proceed with adding the entry to the database
                                        MCFParentRegister data = new MCFParentRegister(fullName, emailAddress, phone, password);
                                        db.collection("parent_details")
                                                .add(data)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Log.d("new parent added", "Parent added successfully");
                                                        Toast.makeText(ParentRegister.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(ParentRegister.this, ParentLogin.class);
                                                        startActivity(intent);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d("parent add error", "Failed to add parent: " + e.getMessage());
                                                        // Handle the error or display a message to the user
                                                    }
                                                });
                                    }
                                } else {
                                    Log.d("parent add error", "Failed to query parent collection: " + task.getException());
                                    // Handle the error or display a message to the user
                                }
                            }
                        });
            }
        });

        login_button=findViewById(R.id.parent_signin_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParentRegister.this, ParentLogin.class);
                startActivity(intent);
            }
        });
    }
}