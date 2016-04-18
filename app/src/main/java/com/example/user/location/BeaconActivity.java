package com.example.user.location;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.example.user.location.Utils.NotificationUtils;

import java.util.List;
import java.util.UUID;

public class BeaconActivity extends AppCompatActivity {

    private BeaconManager beaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);

        beaconManager = new BeaconManager(getApplicationContext());

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                NotificationUtils.showNotification(
                        "BIENVENIDO AL CC1",
                        "Listo para descargar la información de la aisgnatura?", DownloadActivity.class, getApplicationContext());
            }
            @Override
            public void onExitedRegion(Region region) {
                NotificationUtils.showNotification("SALIDA","Nos vemos pronto", MainActivity.class, getApplicationContext());
            }
        });

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region(
                        "monitored region",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                        63463, 21120));
            }
        });
    }
}
