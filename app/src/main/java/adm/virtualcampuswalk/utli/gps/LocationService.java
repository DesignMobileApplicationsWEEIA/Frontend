package adm.virtualcampuswalk.utli.gps;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import static adm.virtualcampuswalk.utli.Util.TAG;
import static com.google.android.gms.common.api.GoogleApiClient.Builder;
import static com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import static com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

public class LocationService implements ConnectionCallbacks, OnConnectionFailedListener {

    public static final int GPS_INTERVAL = 5000;
    public static final int GPS_FASTEST_INTERVAL = 2500;
    public static final long GPS_SMALLEST_DISPLACEMENT = 1L;
    private Context context;
    private LocationListener locationListener;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;

    public LocationService() {
        Log.d(TAG, "LocationService: initialize");
    }

    public LocationService(Context context, LocationListener locationListener) {
        this();
        this.context = context;
        this.locationListener = locationListener;
        this.googleApiClient = initGoogleApiClient();
        this.locationRequest = initLocationRequest();
        this.googleApiClient.connect();
    }

    private LocationRequest initLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(GPS_INTERVAL);
        locationRequest.setFastestInterval(GPS_FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(GPS_SMALLEST_DISPLACEMENT);
        return locationRequest;
    }

    private GoogleApiClient initGoogleApiClient() {
        return new Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void stopLocationRequest() {
        if (googleApiClient.isConnected()) {
            Log.d(TAG, "LocationService - stopLocationRequest: disconnecting");
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, locationListener);
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            Log.d(TAG, "LocationService - onConnected: connected");
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, locationListener);
        } catch (SecurityException ex) {
            Log.e(TAG, "LocationService - onConnected: " + ex.getMessage());
        }
    }

    @Override
    public void onConnectionSuspended(int flag) {
        Log.d(TAG, "LocationService - onConnectionSuspended: " + String.valueOf(flag));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "LocationService - onConnectionFailed: " + connectionResult.toString());
    }

}