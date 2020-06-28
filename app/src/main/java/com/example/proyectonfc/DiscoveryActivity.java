package com.example.proyectonfc;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DiscoveryActivity extends AppCompatActivity {

    private final String TAG = "AppLog";
    private String SERVICE_ID;
    private String DEVICE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);

        SERVICE_ID = getApplicationContext().getPackageName();
        DEVICE_NAME = android.os.Build.MODEL;
        startDiscovery();

        setTitle("Cliente");
    }

    private void startDiscovery() {
        DiscoveryOptions discoveryOptions = new DiscoveryOptions.Builder().setStrategy( Strategy.P2P_STAR ).build();
        Nearby.getConnectionsClient( this )
                .startDiscovery(SERVICE_ID, endpointDiscoveryCallback, discoveryOptions)
                .addOnSuccessListener( (Void unused) -> Log.d(TAG, "Buscador iniciado...") )
                .addOnFailureListener( (Exception e) -> Log.d(TAG, "Se ha producido un error...") );
    }

    private final EndpointDiscoveryCallback endpointDiscoveryCallback = new EndpointDiscoveryCallback() {
        @Override
        public void onEndpointFound(@NonNull String endpointId, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
            Log.d(TAG, "Se ha encontrado un endpoint: " + endpointId);
            // An endpoint was found. We request a connection to it.
            Nearby.getConnectionsClient( getApplicationContext() )
                    .requestConnection(DEVICE_NAME, endpointId, connectionLifecycleCallback)
                    .addOnSuccessListener(
                            (Void unused) -> {
                                // We successfully requested a connection. Now both sides
                                // must accept before the connection is established.
                            })
                    .addOnFailureListener(
                            (Exception e) -> {
                                // Nearby Connections failed to request the connection.
                            });
        }

        @Override
        public void onEndpointLost(@NonNull String endpointId) {
            // A previously discovered endpoint has gone away.
            Log.d(TAG, "Se ha perdido la conexión con: " + endpointId);
        }
    };

    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                    // Automatically accept the connection on both sides.
                    Nearby.getConnectionsClient( getApplicationContext() ).acceptConnection(endpointId, payloadCallback);
                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
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
                public void onDisconnected(String endpointId) {
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



//                String student = "20458644 - Carles Pérez Revert";
//                Log.d(TAG, "Enviando datos..." + " (" + student + ")");
//                InputStream stream = new ByteArrayInputStream(student.getBytes(StandardCharsets.UTF_8));
//                Payload data = Payload.fromStream(stream);
//                Nearby.getConnectionsClient(getApplicationContext()).sendPayload(endpointId, data);