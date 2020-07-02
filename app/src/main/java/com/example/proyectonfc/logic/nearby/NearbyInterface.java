package com.example.proyectonfc.logic.nearby;

interface NearbyInterface {

    String LOG = "AppNearby";

    void start();
    void stop();
    void sendPayload(String endpointId, String msg);

}
