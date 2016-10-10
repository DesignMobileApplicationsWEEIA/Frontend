package adm.virtualcampuswalk.utli.gps;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import adm.virtualcampuswalk.utli.Util;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;

public class MyService extends Service {

    private LocationManager locationManager;
    private static final long MIN_TIME_INTERVAL = 10000;
    private static final float DISTANCE_UPDATE = 3;
    private SimpleLocationListener listener;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(Util.TAG, "START");
        locationManager = initLocationManager();
        try {
            listener = new SimpleLocationListener();
            locationManager.requestLocationUpdates(NETWORK_PROVIDER, MIN_TIME_INTERVAL, DISTANCE_UPDATE, listener);
            locationManager.requestLocationUpdates(GPS_PROVIDER, MIN_TIME_INTERVAL, DISTANCE_UPDATE, listener);
        } catch (SecurityException ex) {
            Log.e(Util.TAG, ex.getMessage());
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private LocationManager initLocationManager() {
        return (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }
}