package com.example.user.location;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.user.location.Utils.NotificationUtils;

public class MainActivity extends AppCompatActivity {

    public static final double DISTANCE = 50.0;
    public static final double CENTER_LATITUDE = 21.048234;
    public static final double CENTER_LONGITUDE = -89.644263;
    public static double CURRENT_LATITUDE = 0;
    public static double CURRENT_LONGITUDE = 0, CURRENT_DISTANCE = 0;
    private boolean hasBeenNotify = false;
    private Location center = new Location("");
    private TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        center.setLatitude(CENTER_LATITUDE);
        center.setLongitude(CENTER_LONGITUDE);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv_location);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                CURRENT_LATITUDE = location.getLatitude();
                CURRENT_LONGITUDE = location.getLongitude();
                double distance = center.distanceTo(location);
                CURRENT_DISTANCE = distance;

                if (distance <= DISTANCE) {
                    Log.i("Entró if", "Dentro de distancia");
                    if (!hasBeenNotify) {
                        NotificationUtils.showNotification("Hi everyone", "Estas en FMAT, ", BeaconActivity.class, getApplicationContext());
                        hasBeenNotify = true;

                        /*We put in the shared preferences, that you are in the location allowed*/
                        SharedPreferences preferences = getSharedPreferences("app", MODE_PRIVATE);
                        preferences.edit().putBoolean("Location", true).apply();
                    }

                } else {
                    hasBeenNotify = false;
                     /*We put in the shared preferences, that you are not in the location allowed*/
                    SharedPreferences preferences = getSharedPreferences("app", MODE_PRIVATE);
                    preferences.edit().putBoolean("Location", false).apply();
                }
                tv.setText("Latitude : " + location.getLatitude() +
                        " Longitude: " + location.getLongitude() +
                        " Distance: " + Double.toString(distance));
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }
}
