package adm.virtualcampuswalk.utli.gps;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import static adm.virtualcampuswalk.utli.Util.TAG;

/**
 * Created by mariusz on 10.10.16.
 */

public class SimpleLocationListener implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "CURRENT LOCATION: LAT " + location.getLatitude() + " LON " + location.getLongitude());

    }

    @Override
    public void onStatusChanged(String provider, int statusCode, Bundle bundle) {
        Log.i(TAG, "Provider " + provider + " status " + statusCode);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i(TAG, "User has enabled provider: " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i(TAG, "User has disabled provider: " + provider);
    }
}
