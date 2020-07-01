package com.example.proyectonfc.logic.nearby;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.Strategy;

import java.nio.charset.StandardCharsets;

public class Advertise {

    Context context;
    private String nickname, serviceId;
    private ConnectionsClient mConnectionsClient;
    private PayloadCallback payloadCallback;

    public Advertise(Context context, String nickname, String serviceId, PayloadCallback payloadCallback) {
        this.context = context;
        this.nickname = nickname;
        this.serviceId = serviceId;
        this.payloadCallback = payloadCallback;
    }


    public void start() {
        mConnectionsClient = Nearby.getConnectionsClient(context);

        AdvertisingOptions advertisingOptions = new AdvertisingOptions.Builder().setStrategy( Strategy.P2P_STAR ).build();

        mConnectionsClient
                .startAdvertising(nickname, serviceId, connectionLifecycleCallback, advertisingOptions)
                .addOnSuccessListener( (Void unused) -> Log.d("NearbyLog", "Anunciante iniciado..." ))
                .addOnFailureListener( (Exception e) -> Log.d("NearbyLog", "Se ha producido un error...\n" + e.getMessage()));
    }

    private final ConnectionLifecycleCallback connectionLifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(@NonNull String endpointId, @NonNull ConnectionInfo connectionInfo) {
            // Automatically accept the connection on both sides.
            Log.d("NearbyLog", "Aceptando conexión con el cliente...");
            mConnectionsClient.acceptConnection(endpointId, payloadCallback);
        }

        @Override
        public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution result) {
            switch (result.getStatus().getStatusCode()) {
                case ConnectionsStatusCodes.STATUS_OK:
                    Log.d("NearbyLog", "¡SE HA CONECTADO!");
                    break;
                case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                    Log.d("NearbyLog", "he connection was rejected by one or both sides.");
                    break;
                case ConnectionsStatusCodes.STATUS_ERROR:
                    Log.d("NearbyLog", "he connection broke before it was able to be accepted.");
                    break;
                default:
                    Log.d("NearbyLog", "nknown status code");
            }
        }

        @Override
        public void onDisconnected(@NonNull String s) {
            Log.d("NearbyLog", "Desconectado...");
        }
    };

    public void stop() {
        mConnectionsClient.stopAllEndpoints();
        mConnectionsClient.stopAdvertising();
    }

    public void sendPayload(String endpointId, String msg) {
        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        mConnectionsClient.sendPayload(endpointId, Payload.fromBytes(bytes));
    }
}
