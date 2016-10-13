package adm.virtualcampuswalk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import adm.virtualcampuswalk.models.PhoneRotation;
import adm.virtualcampuswalk.utli.Util;
import adm.virtualcampuswalk.utli.gps.LocationService;
import adm.virtualcampuswalk.utli.gyroscope.PositionSensorService;


public class MainActivity extends AppCompatActivity {

    private LocationService locationService;
    private PositionSensorService positionSensorService;
    boolean locationBounded = false;
    boolean positionBounded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(getBaseContext(), LocationService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        Intent positionSensorIntent = new Intent(getBaseContext(), PositionSensorService.class);
        bindService(positionSensorIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void btnClicked(View view) {
        if (locationBounded) {
            Location myLastLocation = locationService.getMyLastLocation();
            if (myLastLocation != null) {
                Toast.makeText(this, "LON: " + myLastLocation.getLongitude() + " LAT: " + myLastLocation.getLatitude(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Location unavailable! Try later!", Toast.LENGTH_SHORT).show();
            }
        }
        if (positionBounded) {
            TextView azimuthTV = (TextView) findViewById(R.id.azimuthTV);
            TextView pitchTV = (TextView) findViewById(R.id.pitchTV);
            TextView rollTV = (TextView) findViewById(R.id.rollTV);
            PhoneRotation phoneRotation = positionSensorService.getPhoneRotation();
            Log.i(Util.TAG, phoneRotation.toString());
            azimuthTV.setText(String.format("Azimuth %f", phoneRotation.getAzimuth()));
            pitchTV.setText(String.format("Pitch %f", phoneRotation.getPitch()));
            rollTV.setText(String.format("Roll %f", phoneRotation.getRoll()));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationBounded) {
            unbindService(serviceConnection);
            locationBounded = false;
            positionBounded = false;
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            String className = componentName.getClassName();
            if (className.equals(LocationService.class.getName())) {
                Log.d(Util.TAG, "Connested to " + componentName.getShortClassName());
                LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
                MainActivity.this.locationService = binder.getService();
                locationBounded = true;
            } else if (className.equals(PositionSensorService.class.getName())) {
                Log.d(Util.TAG, "Connested to " + componentName.getShortClassName());
                PositionSensorService.LocalBinder binder = (PositionSensorService.LocalBinder) service;
                MainActivity.this.positionSensorService = binder.getService();
                positionBounded = true;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            if (className.getClass().isInstance(LocationService.class)) {
                locationBounded = false;
            } else if (className.getClass().isInstance(PositionSensorService.class)) {
                positionBounded = false;
            }

        }
    };
}
