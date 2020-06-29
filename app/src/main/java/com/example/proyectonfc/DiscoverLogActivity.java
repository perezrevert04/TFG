package com.example.proyectonfc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DiscoverLogActivity extends AppCompatActivity {

    public static final String EXTRA_LOG = "LOG";
    TextView log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery_log);

        log = findViewById(R.id.textViewDiscoveryLog);
        log.setText( getIntent().getStringExtra(EXTRA_LOG) );
    }
}