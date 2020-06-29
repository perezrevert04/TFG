package com.example.proyectonfc.logic.nearby;

import android.content.Context;

import androidx.annotation.NonNull;

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

public class Discovery {
    
    Context context;
    String serviceId, nickname;
    String log;
    
    public Discovery(Context context, String nickname) {
        this.context = context;
        this.nickname = nickname;
        log = "";
    }

    public void startDiscovery() {
        DiscoveryOptions discoveryOptions = new DiscoveryOptions.Builder().setStrategy( Strategy.P2P_STAR ).build();
        Nearby.getConnectionsClient( context )
                .startDiscovery(serviceId, endpointDiscoveryCallback, discoveryOptions)
                .addOnSuccessListener( (Void unused) -> log = "Buscador iniciado..." )
                .addOnFailureListener( (Exception e) -> log = "Se ha producido un error..." );
    }

    private final EndpointDiscoveryCallback endpointDiscoveryCallback = new EndpointDiscoveryCallback() {
        @Override
        public void onEndpointFound(@NonNull String endpointId, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
            log += "\nSe ha encontrado un endpoint: " + endpointId;
            // An endpoint was found. We request a connection to it.
            Nearby.getConnectionsClient( context ).requestConnection(nickname, endpointId, connectionLifecycleCallback)
                    .addOnSuccessListener(
                            (Void unused) -> {
                                // We successfully requested a connection. Now both sides must accept before the connection is established.
                                log += "\nSe ha enviado una petición de conexión...";
                            })
                    .addOnFailureListener(
                            (Exception e) -> {
                                log += "\nSe ha producido un error...\n" + e.getMessage();

//                                Payload bytesPayload = Payload.fromBytes(new byte[] {0xa, 0xb, 0xc, 0xd});
//                                Nearby.getConnectionsClient( context ).sendPayload(endpointId, bytesPayload);
                                // Nearby Connections failed to request the connection.
                            });
        }

        @Override
        public void onEndpointLost(@NonNull String endpointId) {
            // A previously discovered endpoint has gone away.
            log += "\nSe ha perdido la conexión con: " + endpointId;
        }
    };

    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                    // Automatically accept the connection on both sides.
                    log += "\nAceptando conexión con el servidor...";
                    Nearby.getConnectionsClient( context ).acceptConnection(endpointId, payloadCallback);
                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    switch (result.getStatus().getStatusCode()) {
                        case ConnectionsStatusCodes.STATUS_OK:
                            log += "\n¡SE HA CONECTADO!";
                            break;
                        case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                            log += "\nThe connection was rejected by one or both sides.";
                            break;
                        case ConnectionsStatusCodes.STATUS_ERROR:
                            log += "\nThe connection broke before it was able to be accepted.";
                            break;
                        default:
                            log += "\nUnknown status code";
                    }
                }

                @Override
                public void onDisconnected(String endpointId) {
                    log += "\nDesconectado...";
                }
            };


    private PayloadCallback payloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String endpointId, @NonNull Payload payload) {

        }

        @Override
        public void onPayloadTransferUpdate(@NonNull String endpointId, @NonNull PayloadTransferUpdate payloadTransferUpdate) {

        }
    };
}
