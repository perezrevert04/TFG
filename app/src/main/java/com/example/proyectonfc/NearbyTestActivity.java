package com.example.proyectonfc;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Strategy;

public class NearbyTestActivity extends AppCompatActivity {

    private final String TAG = "AppLog";
    private final String SERVICE_ID = getApplicationContext().getPackageName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_test);

        Button advertising = findViewById(R.id.buttonStartAdvertising);
        advertising.setOnClickListener( (elem) -> {
            startAdvertising();
        });

        Button discovery = findViewById(R.id.buttonStartDiscovery);
        discovery.setOnClickListener( (elem) -> {
            startDiscovery();
        });
    }

    private void startAdvertising() {
        AdvertisingOptions advertisingOptions =
                new AdvertisingOptions.Builder().setStrategy( Strategy.P2P_STAR ).build();
        Nearby.getConnectionsClient( this )
                .startAdvertising(
                        "Carles Pérez Revert",
                        SERVICE_ID,
                        new ConnectionLifecycleCallback() {
                            @Override
                            public void onConnectionInitiated(@NonNull String s, @NonNull ConnectionInfo connectionInfo) {
                                Log.d(TAG, "onConnectionInitiated: " + s + " | " + connectionInfo.getEndpointName());

                            }

                            @Override
                            public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution connectionResolution) {
                                Log.d(TAG, "onConnectionResult: " + s);
                            }

                            @Override
                            public void onDisconnected(@NonNull String s) {
                                Log.d(TAG, "onDisconnected: " + s);
                            }
                        },
                        advertisingOptions
                ).addOnSuccessListener(
                        (Void unused) -> {
                            Log.d(TAG, "We're advertising!");
                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            Log.d(TAG, "We were unable to start advertising.");
                        });
    }

    private void startDiscovery() {
        DiscoveryOptions discoveryOptions =
                new DiscoveryOptions.Builder().setStrategy( Strategy.P2P_STAR ).build();
        Nearby.getConnectionsClient( this )
                .startDiscovery(
                        SERVICE_ID,

                        new EndpointDiscoveryCallback() {
                            @Override
                            public void onEndpointFound(@NonNull String s, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
                                Log.d("AppLog", "onEndpointFound: " + s + " | " + discoveredEndpointInfo.getEndpointName());
                            }

                            @Override
                            public void onEndpointLost(@NonNull String s) {
                                Log.d("AppLog", "onEndpointLost: " + s);
                            }
                        },
                        discoveryOptions
                ).addOnSuccessListener(
                        (Void unused) -> {
                            Log.d(TAG, "We're discovering!");
                        })
                .addOnFailureListener(
                        (Exception e) -> {
                            Log.d(TAG, "We're unable to start discovering.");
                        });
    }
}