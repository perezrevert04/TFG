package com.example.proyectonfc;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

    TextView ring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertising);

        ring = findViewById(R.id.textViewRing);

        SERVICE_ID = getApplicationContext().getPackageName();
        DEVICE_NAME = android.os.Build.MODEL;
        startAdvertising();

        setTitle("Servidor");
    }

    private void startAdvertising() {
        AdvertisingOptions advertisingOptions = new AdvertisingOptions.Builder().setStrategy( Strategy.P2P_STAR ).build();

        Nearby.getConnectionsClient( this )
                .startAdvertising(DEVICE_NAME, SERVICE_ID, connectionLifecycleCallback, advertisingOptions)
                .addOnSuccessListener( (Void unused) -> ring.setText("Anunciante iniciado...") )
                .addOnFailureListener( (Exception e) -> ring.setText("Se ha producido un error...") );
    }

    private final ConnectionLifecycleCallback connectionLifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(@NonNull String endpointId, @NonNull ConnectionInfo connectionInfo) {
            // Automatically accept the connection on both sides.
            ring.setText(ring.getText().toString() + "\nAceptando conexión con el cliente...");
            Nearby.getConnectionsClient( getApplicationContext() ).acceptConnection(endpointId, payloadCallback);
        }

        @Override
        public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution result) {
            switch (result.getStatus().getStatusCode()) {
                case ConnectionsStatusCodes.STATUS_OK:
                    ring.setText(ring.getText().toString() + "\n¡SE HA CONECTADO!");
                    ring.setVisibility(View.VISIBLE);
                    break;
                case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                    ring.setText(ring.getText().toString() + "\nThe connection was rejected by one or both sides.");
                    break;
                case ConnectionsStatusCodes.STATUS_ERROR:
                    ring.setText(ring.getText().toString() + "\nThe connection broke before it was able to be accepted.");
                    break;
                default:
                    ring.setText(ring.getText().toString() + "\nUnknown status code");
            }
        }

        @Override
        public void onDisconnected(@NonNull String s) {
            ring.setText(ring.getText().toString() + "\nDesconectado...");
        }
    };

    private PayloadCallback payloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {

            ring.setText(ring.getText().toString() + "\nRecibiendo información...");
        }

        @Override
        public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {

            ring.setText(ring.getText().toString() + "\nRecibiendo información...");
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        Nearby.getConnectionsClient( getApplicationContext() ).stopAdvertising();
        Nearby.getConnectionsClient( getApplicationContext() ).stopAllEndpoints();

    }
}