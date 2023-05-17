package com.example.homepage;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ChildLogin extends AppCompatActivity {

    Button login;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_login);
        EditText passwordEditText = findViewById(R.id.pcode_edit);
        EditText emailEditText = findViewById(R.id.chEmail_edit);
        db = FirebaseFirestore.getInstance();

        login=findViewById(R.id.child_login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the password and email match with the database
                String password = passwordEditText.getText().toString();
                String email = emailEditText.getText().toString();
                performLogin(email, password);
            }
        });
    }

    private void performLogin(String email, String password) {
        // Query to check if the provided email and password match in the 'parent_details' collection
        db.collection("child_details")
                .whereEqualTo("parent_id", email)
                .whereEqualTo("child_password", password)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                // Login successful, user credentials are valid
                                Toast.makeText(ChildLogin.this, "Login successful", Toast.LENGTH_SHORT).show();
                                // Redirect to the home or main activity
                                SPUMaster.saveMode(getApplicationContext(), "childmode");
                                SPUMaster.saveChildPassword(getApplicationContext(), password);
                                SPUMaster.saveChildId(getApplicationContext(), email);
                                SPUMaster.saveParentId(getApplicationContext(), email);
                                                Intent intent = new Intent(ChildLogin.this, child_home.class);
                                                startActivity(intent);

                            } else {
                                // No matching user found
                                Toast.makeText(ChildLogin.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Error occurred while querying the database
                            Toast.makeText(ChildLogin.this, "Error occurred while logging in", Toast.LENGTH_SHORT).show();
                            Log.d("login error", "Failed to query parent collection: " + task.getException());
                        }
                    }
                });
    }


}