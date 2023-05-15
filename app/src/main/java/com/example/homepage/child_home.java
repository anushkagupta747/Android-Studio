package com.example.homepage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class child_home extends AppCompatActivity {

    private CardView dnd, ec, sos;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_home);

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