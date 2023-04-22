package com.example.homepage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FrontPage extends AppCompatActivity {

    Button p_button;
    Button c_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);

        p_button=findViewById(R.id.parent_button);
        p_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FrontPage.this, ParentLogin.class);
                startActivity(intent);
            }
        });

        c_button=findViewById(R.id.child_button);
        c_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FrontPage.this, ChildLogin.class);
                startActivity(intent);
            }
        });
    }
}