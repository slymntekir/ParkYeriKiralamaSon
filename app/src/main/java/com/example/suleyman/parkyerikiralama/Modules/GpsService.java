package com.example.suleyman.parkyerikiralama.Modules;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

/**
 * Created by suleyman on 5/6/2017.
 */

public class GpsService extends Service {

    private LocationManager locationManager;
    private LocationListener locationListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(location != null) {
                    Intent ı = new Intent("location_update");
                    ı.putExtra("coordinates", location.getLatitude() + "," + location.getLongitude());
                    sendBroadcast(ı);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent ı = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                ı.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(ı);
            }
        };
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,0,locationListener);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {}

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null)
        {
            locationManager.removeUpdates(locationListener);
        }
    }
}