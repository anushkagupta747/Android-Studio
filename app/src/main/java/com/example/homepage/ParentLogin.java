package com.example.homepage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.os.Bundle;

public class ParentLogin extends AppCompatActivity {

    Button register_button;
    Button mainLogin_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);

        //Toast.makeText(getApplicationContext(), "Parent Login Page", Toast.LENGTH_LONG).show();

        mainLogin_button=findViewById(R.id.parent_login_button);
        mainLogin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParentLogin.this, home.class);
                startActivity(intent);
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
}