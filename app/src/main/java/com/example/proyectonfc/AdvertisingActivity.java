package com.example.proyectonfc;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;

import java.io.InputStream;
import java.util.Objects;

public class AdvertisingActivity extends AppCompatActivity {

    private final String TAG = "AppLog";
    private String SERVICE_ID;
    private String DEVICE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertising);

        SERVICE_ID = getApplicationContext().getPackageName();
        DEVICE_NAME = android.os.Build.MODEL;
        startAdvertising();

        setTitle("Servidor");
    }

    private void startAdvertising() {
        AdvertisingOptions advertisingOptions = new AdvertisingOptions.Builder().setStrategy( Strategy.P2P_STAR ).build();

        Nearby.getConnectionsClient( this )
                .startAdvertising(DEVICE_NAME, SERVICE_ID, connectionLifecycleCallback, advertisingOptions)
                .addOnSuccessListener( (Void unused) -> Log.d(TAG, "Anunciante iniciado...") )
                .addOnFailureListener( (Exception e) -> Log.d(TAG, "Se ha producido un error...") );
    }

    private final ConnectionLifecycleCallback connectionLifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(@NonNull String endpointId, @NonNull ConnectionInfo connectionInfo) {
            // Automatically accept the connection on both sides.
            Nearby.getConnectionsClient( getApplicationContext() ).acceptConnection(endpointId, payloadCallback);
        }

        @Override
        public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution result) {
            switch (result.getStatus().getStatusCode()) {
                case ConnectionsStatusCodes.STATUS_OK:
                    Log.d(TAG, "We're connected! Can now start sending and receiving data.");
                    break;
                case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                    Log.d(TAG, "The connection was rejected by one or both sides.");
                    break;
                case ConnectionsStatusCodes.STATUS_ERROR:
                    Log.d(TAG, "The connection broke before it was able to be accepted.");
                    break;
                default:
                    Log.d(TAG, "Unknown status code");
            }
        }

        @Override
        public void onDisconnected(@NonNull String s) {
            // We've been disconnected from this endpoint. No more data can be
            // sent or received.
        }
    };

    private PayloadCallback payloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {

        }

        @Override
        public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {

        }
    };
}