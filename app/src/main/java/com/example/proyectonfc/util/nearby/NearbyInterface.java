package com.example.proyectonfc.util.nearby;

interface NearbyInterface {

    String LOG = "NearbyLog";

    void start();
    void stop();
    void sendPayload(String endpointId, String msg);

}
