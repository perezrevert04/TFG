package com.example.proyectonfc.util.nearby;

import com.google.android.gms.nearby.connection.Strategy;

interface NearbyInterface {

    Strategy STRATEGY = Strategy.P2P_CLUSTER;
    String LOG = "NearbyLog";

    void start();
    void stop();
    void sendPayload(String endpointId, byte[] msg);

}
