package com.example.proyectonfc.logic.nearby;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Discover {
    
    private String nickname, serviceId;
    private String log;
    private ConnectionsClient mConnectionsClient;

    private List<String> list;

    Map<String, String> map;

    public interface Observer {
        void update();
    }

    private final List<Observer> observers = new ArrayList<>();

    /* TODO: Revisar si este String no es necesario */
    private void notifyObservers() {
        observers.forEach(observer -> observer.update());
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void scanSystemIn() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            notifyObservers();
        }
    }

    public Discover(Context context, String nickname, String serviceId) {
        this.nickname = nickname;
        this.serviceId = serviceId;

        mConnectionsClient = Nearby.getConnectionsClient( context );
        log = "";
        list = new ArrayList<>();
        map = new HashMap<>();
    }

    public List<String> getList() {
        return list;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public String getLog() { return log; }

    public void setLog(String log) { this.log = log; }

    public void start() {
        DiscoveryOptions discoveryOptions = new DiscoveryOptions.Builder().setStrategy( Strategy.P2P_STAR ).build();
        mConnectionsClient
                .startDiscovery(serviceId, endpointDiscoveryCallback, discoveryOptions)
                .addOnSuccessListener( (Void unused) -> log = "Buscador iniciado..." )
                .addOnFailureListener( (Exception e) -> log = "Se ha producido un error..." );
    }

    private void requestConnection(String endpointId) {
        mConnectionsClient.requestConnection(nickname, endpointId, connectionLifecycleCallback)
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

    private final EndpointDiscoveryCallback endpointDiscoveryCallback = new EndpointDiscoveryCallback() {
        @Override
        public void onEndpointFound(@NonNull String endpointId, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
            log += "\nSe ha encontrado un endpoint: " + endpointId;

            String name = discoveredEndpointInfo.getEndpointName();
            map.put(endpointId, name);
            list.add(name);
            notifyObservers();

            // An endpoint was found. We request a connection to it.
//            requestConnection(endpointId);
        }

        @Override
        public void onEndpointLost(@NonNull String endpointId) {
            // A previously discovered endpoint has gone away.
            map.remove(endpointId);
            Log.d("AppLog", "Se ha perdido la conexión " + map.size());
            notifyObservers();
            log += "\nSe ha perdido la conexión con: " + endpointId;
        }
    };

    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                    // Automatically accept the connection on both sides.
                    log += "\nAceptando conexión con el servidor...";
                    mConnectionsClient.acceptConnection(endpointId, payloadCallback);
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

    public void stop() {
        mConnectionsClient.stopAdvertising();
    }
}
