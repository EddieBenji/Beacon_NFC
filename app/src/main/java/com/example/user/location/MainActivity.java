package com.example.user.location;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final double DISTANCE = 50.0;
    private boolean isRange;
    private boolean hasBeenNotifyRange = false;
    private boolean hasBeenNotifyNotRange = false;
    Location centro = new Location("");
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        centro.setLatitude(21.048234);
        centro.setLongitude(-89.644263);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.tv_location);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                double distance = centro.distanceTo(location);
                Log.d("Tas chao", Double.toString(distance));
                if (distance <= DISTANCE){
                    isRange = true;
                    if(hasBeenNotifyRange){
                        showNotification("Hi everyone", "Estas en FMAT");
                        hasBeenNotifyRange = true;
                        hasBeenNotifyNotRange = false;
                    }

                }else{
                    isRange = false;
                    if(hasBeenNotifyNotRange){
                        showNotification("Tas Chao", "No estas en FMAT");
                        hasBeenNotifyNotRange = true;
                        hasBeenNotifyRange = false;
                    }
                }

                if(isRange )

                tv.setText("Latitud : " + location.getLatitude() + " Longitud: " + location.getLongitude() + " Distancia: "+ Double.toString(distance));
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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);


    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, BeaconActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
