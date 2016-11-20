package adm.virtualcampuswalk.utli.gps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import adm.virtualcampuswalk.utli.Util;

/**
 * Created by mariusz on 20.11.16.
 */

public class GpsChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isConnected(context)) {
            Log.d(Util.TAG, this.getClass().getSimpleName() + "-onReceive: no gps connection. Exiting");
            Util.exitToastMessage(context, "No GPS connection. Exiting...", Toast.LENGTH_LONG, 3000);
        }
    }

    public static boolean isConnected(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
