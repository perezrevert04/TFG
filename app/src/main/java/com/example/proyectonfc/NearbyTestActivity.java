package com.example.proyectonfc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class NearbyTestActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_test);

        Button advertising = findViewById(R.id.buttonStartAdvertising);
        advertising.setOnClickListener( (elem) -> {
            Intent intent = new Intent(this, AdvertisingActivity.class);
            startActivity(intent);
        });

        Button discovery = findViewById(R.id.buttonStartDiscovery);
        discovery.setOnClickListener( (elem) -> {
            Intent intent = new Intent(this, DiscoveryActivity.class);
            startActivity(intent);
        });
    }

}