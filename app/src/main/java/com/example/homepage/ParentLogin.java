package com.example.homepage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ParentLogin extends AppCompatActivity {

    EditText emailEditText;
    EditText passwordEditText;
    FirebaseFirestore db;
    Button register_button;
    Button mainLogin_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);
        emailEditText = findViewById(R.id.parent_email_edit);
        passwordEditText = findViewById(R.id.parent_pw_edit);
        db = FirebaseFirestore.getInstance();
        //Toast.makeText(getApplicationContext(), "Parent Login Page", Toast.LENGTH_LONG).show();

        mainLogin_button=findViewById(R.id.parent_login_button);
        mainLogin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(ParentLogin.this, home.class);
//                startActivity(intent);
                // Retrieve the values from the EditText fields
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                // Perform login operation with the entered credentials
                performLogin(email, password);
            }
        });

        register_button=findViewById(R.id.register1_button);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParentLogin.this, ParentRegister.class);
                startActivity(intent);
            }
        });
    }

    private void performLogin(String email, String password) {
        // Query to check if the provided email and password match in the 'parent_details' collection
        db.collection("parent_details")
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                // Login successful, user credentials are valid
                                Toast.makeText(ParentLogin.this, "Login successful", Toast.LENGTH_SHORT).show();
                                // Redirect to the home or main activity
                                Intent intent = new Intent(ParentLogin.this, home.class);
                                intent.putExtra("email", email);
                                intent.putExtra("password", password);
                                startActivity(intent);
                            } else {
                                // No matching user found
                                Toast.makeText(ParentLogin.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Error occurred while querying the database
                            Toast.makeText(ParentLogin.this, "Error occurred while logging in", Toast.LENGTH_SHORT).show();
                            Log.d("login error", "Failed to query parent collection: " + task.getException());
                        }
                    }
                });
    }

}