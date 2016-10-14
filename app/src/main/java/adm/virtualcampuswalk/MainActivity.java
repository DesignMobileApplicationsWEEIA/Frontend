package adm.virtualcampuswalk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Camera;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import adm.virtualcampuswalk.models.PhoneRotation;
import adm.virtualcampuswalk.utli.Util;
import adm.virtualcampuswalk.utli.camera.CameraPreview;
import adm.virtualcampuswalk.utli.camera.CameraService;
import adm.virtualcampuswalk.utli.gps.LocationService;
import adm.virtualcampuswalk.utli.gyroscope.PositionSensorService;

import static adm.virtualcampuswalk.utli.camera.CameraService.*;
import static android.hardware.Camera.Parameters.FOCUS_MODE_AUTO;


public class MainActivity extends AppCompatActivity {

    private Camera camera;
    private CameraPreview preview;
    Camera.Parameters parameters;

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

        initCamera();
    }

    private void initCamera() {
        camera = getCameraInstance();
        preview = new CameraPreview(this, camera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(this.preview);
        setPosition(camera, getResources().getConfiguration());

        Camera.Parameters params = camera.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        camera.setParameters(params);
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
            azimuthTV.setText(String.format("Azimuth: %.2f", phoneRotation.getAzimuth()));
            pitchTV.setText(String.format("Pitch: %.2f", phoneRotation.getPitch()));
            rollTV.setText(String.format("Roll: %.2f", phoneRotation.getRoll()));
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
