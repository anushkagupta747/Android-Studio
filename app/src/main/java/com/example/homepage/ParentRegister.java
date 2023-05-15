package com.example.homepage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ParentRegister extends AppCompatActivity {

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
                // Perform sign up logic
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