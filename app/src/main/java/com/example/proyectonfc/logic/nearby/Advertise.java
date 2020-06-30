package com.example.proyectonfc.logic.nearby;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.Strategy;

public class Advertise {

    private String nickname, serviceId;
    private String log;

    private ConnectionsClient mConnectionsClient;
    private PayloadCallback payloadCallback;

    public Advertise(Context context, String nickname, String serviceId, PayloadCallback payloadCallback) {
        this.nickname = nickname;
        this.serviceId = serviceId;
        this.payloadCallback = payloadCallback;

        log = "";
        mConnectionsClient = Nearby.getConnectionsClient(context);
    }

    public String getLog() { return log; }

    public void setLog(String log) { this.log = log; }

    public void start() {
        AdvertisingOptions advertisingOptions = new AdvertisingOptions.Builder().setStrategy( Strategy.P2P_STAR ).build();

        mConnectionsClient
                .startAdvertising(nickname, serviceId, connectionLifecycleCallback, advertisingOptions)
                .addOnSuccessListener( (Void unused) -> log = "Anunciante iniciado..." )
                .addOnFailureListener( (Exception e) -> log = "Se ha producido un error...");
    }

    private final ConnectionLifecycleCallback connectionLifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(@NonNull String endpointId, @NonNull ConnectionInfo connectionInfo) {
            // Automatically accept the connection on both sides.
            log += "\nAceptando conexión con el cliente...";
            mConnectionsClient.acceptConnection(endpointId, payloadCallback);
        }

        @Override
        public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution result) {
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
        public void onDisconnected(@NonNull String s) {
            log += "\nDesconectado...";
        }
    };

    public void stop() {
        mConnectionsClient.stopAdvertising();
    }
}
