package adm.virtualcampuswalk.utli.gps;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

/**
 * Created by mariusz on 21.10.16.
 */

public class GPSPrompt implements ResultCallback<LocationSettingsResult> {
    final static int REQUEST_LOCATION = 199;
    private final Context context;

    public GPSPrompt(Context context) {
        this.context = context;
    }

    @Override
    public void onResult(LocationSettingsResult result) {
        final Status status = result.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    status.startResolutionForResult(
                            (Activity) context, REQUEST_LOCATION);
                } catch (IntentSender.SendIntentException e) {
                }
                break;
        }
    }
}
