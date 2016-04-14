package com.example.user.location;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BeaconActivity extends AppCompatActivity {

    //private BeaconManageranager beaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);


        //beaconManager = new BeaconManager(getApplicationContext());
    }
}
