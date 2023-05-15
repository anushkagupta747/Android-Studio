package com.example.homepage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

public class home extends AppCompatActivity {

    private CardView general;
    private CardView call_history;
    private CardView app_usage;
    private CardView app_dwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");

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


    }



}