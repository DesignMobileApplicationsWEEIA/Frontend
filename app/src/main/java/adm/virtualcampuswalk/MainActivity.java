package adm.virtualcampuswalk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import adm.virtualcampuswalk.utli.gps.LocationService;
import adm.virtualcampuswalk.utli.gyroscope.PositionSensorService;


public class MainActivity extends AppCompatActivity {

    private LocationService locationService;
    boolean bounded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(getBaseContext(), LocationService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        Intent positionSensorIntent = new Intent(getBaseContext(), PositionSensorService.class);
        startService(positionSensorIntent);
    }

    public void btnClicked(View view) {
        if (bounded) {
            Location myLastLocation = locationService.getMyLastLocation();
            Toast.makeText(this, "LON: " + myLastLocation.getLongitude() + " LAT: " + myLastLocation.getLatitude(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bounded) {
            unbindService(serviceConnection);
            bounded = false;
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
            MainActivity.this.locationService = binder.getService();
            bounded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bounded = false;
        }
    };
}
