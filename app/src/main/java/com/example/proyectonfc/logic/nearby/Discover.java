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

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Discover {
    
    private String nickname, serviceId;
    private ConnectionsClient mConnectionsClient;

    private Map<String, String> map;

    public interface Observer {
        void update();
    }

    private final List<Observer> observers = new ArrayList<>();

    private void notifyObservers() {
        observers.forEach(Observer::update);
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void scanSystemIn() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) notifyObservers();
    }

    public Discover(Context context, String nickname, String serviceId) {
        this.nickname = nickname;
        this.serviceId = serviceId;

        mConnectionsClient = Nearby.getConnectionsClient( context );
        map = new HashMap<>();
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void start() {
        DiscoveryOptions discoveryOptions = new DiscoveryOptions.Builder().setStrategy( Strategy.P2P_STAR ).build();
        mConnectionsClient
                .startDiscovery(serviceId, endpointDiscoveryCallback, discoveryOptions)
                .addOnSuccessListener( (Void unused) -> Log.d("NearbyLog", "Buscador iniciado...") )
                .addOnFailureListener( (Exception e) -> Log.d("NearbyLog", "Se ha producido un error...") );
    }

    private final EndpointDiscoveryCallback endpointDiscoveryCallback = new EndpointDiscoveryCallback() {
        @Override
        public void onEndpointFound(@NonNull String endpointId, @NonNull DiscoveredEndpointInfo info) {
            Log.d("NearbyLog", "Se ha encontrado un endpoint: " + endpointId);

            // An endpoint was found. We request a connection to it.
            mConnectionsClient.requestConnection(nickname, endpointId, connectionLifecycleCallback)
                    .addOnSuccessListener(
                            (Void unused) -> {
                                // We successfully requested a connection. Now both sides must accept before the connection is established.
                                Log.d("NearbyLog", "Se ha enviado una petición de conexión...");
                            })
                    .addOnFailureListener(
                            (Exception e) -> {
                                Log.d("NearbyLog", "Se ha producido un error...\n" + e.getMessage() + " " + e.hashCode());

                                if (e.getMessage().equals("8003: STATUS_ALREADY_CONNECTED_TO_ENDPOINT") && !map.containsKey(endpointId)) {
                                    map.put(endpointId, info.getEndpointName());
                                    notifyObservers();
                                }

                            });
        }

        @Override
        public void onEndpointLost(@NonNull String endpointId) {
            // A previously discovered endpoint has gone away.
            Log.d("NearbyLog", "Se ha perdido la conexión con: " + endpointId);
        }
    };

    private final ConnectionLifecycleCallback connectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
        
                String endpointName;
                
                @Override
                public void onConnectionInitiated(@NotNull String endpointId, @NotNull ConnectionInfo info) {
                    // Automatically accept the connection on both sides.
                    Log.d("NearbyLog", "Aceptando conexión con el servidor...");
                    endpointName = info.getEndpointName();
                    mConnectionsClient.acceptConnection(endpointId, payloadCallback);
                }

                @Override
                public void onConnectionResult(@NotNull String endpointId, ConnectionResolution result) {
                    switch (result.getStatus().getStatusCode()) {
                        case ConnectionsStatusCodes.STATUS_OK:
                            Log.d("NearbyLog", "¡SE HA CONECTADO!");
                            map.put(endpointId, endpointName);
                            notifyObservers();
                            break;
                        case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                            Log.d("NearbyLog", "The connection was rejected by one or both sides.");
                            break;
                        case ConnectionsStatusCodes.STATUS_ERROR:
                            Log.d("NearbyLog", "The connection broke before it was able to be accepted.");
                            break;
                        default:
                            Log.d("NearbyLog", "Unknown status code");
                    }
                }

                @Override
                public void onDisconnected(@NotNull String endpointId) {
                    Log.d("NearbyLog", "Desconectado...");
                    map.remove(endpointId);
                    notifyObservers();
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
        mConnectionsClient.stopAllEndpoints();
        mConnectionsClient.stopDiscovery();
    }

    public void sendPayload(String endpointId, String identifier) {
        // "3967203186"
        Log.d("NearbyLog", "Sending payload");
        byte[] bytes = identifier.getBytes(StandardCharsets.UTF_8);
        mConnectionsClient.sendPayload(endpointId, Payload.fromBytes(bytes));
    }
}
