package com.example.user.location;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.example.user.location.Utils.NotificationUtils;

import java.util.List;
import java.util.UUID;

public class BeaconActivity extends AppCompatActivity {

    private BeaconManager beaconManager;
    private final static int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter mBluetoothAdapter;

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
                        "Listo para descargar la información de la aisgnatura?",
                        DownloadActivity.class, getApplicationContext());

                /*We put in the shared preferences, that you are near to the beacon*/
                SharedPreferences preferences = getSharedPreferences("app", MODE_PRIVATE);
                preferences.edit().putBoolean("Beacon", true).apply();
            }

            @Override
            public void onExitedRegion(Region region) {
                NotificationUtils.showNotification("SALIDA", "Nos vemos pronto",
                        MainActivity.class, getApplicationContext());

                /*We put in the shared preferences, that you are far from the beacon*/
                SharedPreferences preferences = getSharedPreferences("app", MODE_PRIVATE);
                preferences.edit().putBoolean("Beacon", false).apply();
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

        //Validations for the bluetooth:
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Tu dispositivo no soporta bluetooth",
                    Toast.LENGTH_LONG).show();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }
}
