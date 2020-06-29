package com.example.proyectonfc;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class DiscoveryActivity extends AppCompatActivity {

    private final String TAG = "AppLog";
    private String SERVICE_ID;
    private String DEVICE_NAME;

    TextView ring;

    String myEndpoint;

    private ConnectionsClient mConnectionsClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);

        ring = findViewById(R.id.textViewDiscovery);

        SERVICE_ID = getApplicationContext().getPackageName();
        DEVICE_NAME = android.os.Build.MODEL;
        startDiscovery();

        setTitle("Cliente");
    }

    private void startDiscovery() {
        DiscoveryOptions discoveryOptions = new DiscoveryOptions.Builder().setStrategy( Strategy.P2P_STAR ).build();
        Nearby.getConnectionsClient( getApplicationContext() )
                .startDiscovery(SERVICE_ID, endpointDiscoveryCallback, discoveryOptions)
                .addOnSuccessListener( (Void unused) -> ring.setText("Buscador iniciado...") )
                .addOnFailureListener( (Exception e) -> ring.setText("Se ha producido un error...") );
    }

    private final EndpointDiscoveryCallback endpointDiscoveryCallback = new EndpointDiscoveryCallback() {
        @Override
        public void onEndpointFound(@NonNull String endpointId, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
            ring.setText(ring.getText().toString() + "\nSe ha encontrado un endpoint: " + endpointId);
            // An endpoint was found. We request a connection to it.
            myEndpoint = endpointId;
            Nearby.getConnectionsClient( getApplicationContext() )
                    .requestConnection(DEVICE_NAME, endpointId, connectionLifecycleCallback)
                    .addOnSuccessListener(
                            (Void unused) -> {
                                // We successfully requested a connection. Now both sides
                                // must accept before the connection is established.
                                ring.setText(ring.getText().toString() + "\nSe ha enviado una petición de conexión...");
                            })
                    .addOnFailureListener(
                            (Exception e) -> {
                                ring.setText(ring.getText().toString() + "\nSe ha producido un error...\n" + e.getMessage());

                                Payload bytesPayload = Payload.fromBytes(new byte[] {0xa, 0xb, 0xc, 0xd});
                                Nearby.getConnectionsClient( getApplicationContext() ).sendPayload(myEndpoint, bytesPayload);
                                // Nearby Connections failed to request the connection.
                            });
        }

        @Override
        public void onEndpointLost(@NonNull String endpointId) {
            // A previously discovered endpoint has gone away.
            ring.setText(ring.getText().toString() + "\nSe ha perdido la conexión con: " + endpointId);
        }
    };

    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                    // Automatically accept the connection on both sides.
                    ring.setText(ring.getText().toString() + "\nAceptando conexión con el servidor...");
                    Nearby.getConnectionsClient( getApplicationContext() ).acceptConnection(endpointId, payloadCallback);
                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    switch (result.getStatus().getStatusCode()) {
                        case ConnectionsStatusCodes.STATUS_OK:
                            ring.setText(ring.getText().toString() + "\n¡SE HA CONECTADO!");

                            String student = "3967203186";
                            ring.setText(ring.getText().toString() + "\nEnviando datos..." + " (" + student + ")");
                            byte[] bytes = student.getBytes(Charset.forName("UTF-8"));
                            Payload data = Payload.fromBytes(bytes);
                            Nearby.getConnectionsClient(getApplicationContext()).sendPayload(endpointId, data);

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
                public void onDisconnected(String endpointId) {
                    ring.setText(ring.getText().toString() + "\nDesconectado...");
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
