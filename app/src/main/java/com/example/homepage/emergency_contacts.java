package com.example.homepage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class emergency_contacts extends AppCompatActivity {

    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);

        rv=findViewById(R.id.ec);

        //data
        List<String> name= new ArrayList<>();
        name.add("National Emergency Number: 112");
        name.add("COVID-19 Helpline: 1075");
        name.add("Police: 100");
        name.add("Fire: 101");
        name.add("Ambulance: 102");
        name.add("Disaster Management Services: 108");
        name.add("Women Helpline: 1091");
        name.add("Domestic Abuse: 181");
        name.add("Air Ambulance: 9540161344");
        name.add("KIRAN MENTAL HEALTH Helpline: 18005990019");
        name.add("CYBER CRIME Helpline: 155620");

        rv.setLayoutManager(new LinearLayoutManager(this));
        ecAdapter adapter = new ecAdapter(this, name);
        rv.setAdapter(adapter);

    }
}