package com.example.homepage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class home extends AppCompatActivity implements View.OnClickListener {

    private CardView general;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        general = (CardView) findViewById(R.id.card_general);
        general.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;

        //implement switch case instead
        i = new Intent(home.this, GeneralActivity.class);
        startActivity(i);
    }
}