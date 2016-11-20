package adm.virtualcampuswalk.utli.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import adm.virtualcampuswalk.utli.Util;

/**
 * Created by mariusz on 19.11.16.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isConnected(context)) {
            Log.d(Util.TAG, this.getClass().getSimpleName() + "-onReceive: no internet connection. Exiting");
            Util.exitToastMessage(context, "No internet connection. Exiting...", Toast.LENGTH_LONG, 3000);
        }
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnected();
    }

}
