package adm.virtualcampuswalk.utli.gps;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import adm.virtualcampuswalk.utli.Util;

import static adm.virtualcampuswalk.utli.Util.TAG;
import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;

public class LocationService extends Service {

    private Location myLastLocation;
    private static final long MIN_TIME_INTERVAL = 10000;
    private static final float DISTANCE_UPDATE = 3;
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }

    public class SimpleLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            myLastLocation = location;
            Log.d(TAG, "CURRENT LOCATION: LAT " + location.getLatitude() + " LON " + location.getLongitude());

        }

        @Override
        public void onStatusChanged(String provider, int statusCode, Bundle bundle) {
            Log.d(TAG, "Provider " + provider + " status " + statusCode);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "User has enabled provider: " + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "User has disabled provider: " + provider);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(Util.TAG, "START LOCATION LISTENER");
        LocationManager locationManager = initLocationManager();
        try {
            SimpleLocationListener listener = new SimpleLocationListener();
            locationManager.requestLocationUpdates(NETWORK_PROVIDER, MIN_TIME_INTERVAL, DISTANCE_UPDATE, listener);
            locationManager.requestLocationUpdates(GPS_PROVIDER, MIN_TIME_INTERVAL, DISTANCE_UPDATE, listener);
        } catch (SecurityException ex) {
            Log.e(Util.TAG, ex.getMessage());
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public Location getMyLastLocation() {
        return myLastLocation;
    }

    private LocationManager initLocationManager() {
        return (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }
}