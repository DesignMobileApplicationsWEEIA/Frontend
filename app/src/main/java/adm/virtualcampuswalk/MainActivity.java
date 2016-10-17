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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;

import java.util.Timer;
import java.util.TimerTask;

import adm.virtualcampuswalk.models.PhoneRotation;
import adm.virtualcampuswalk.utli.Util;
import adm.virtualcampuswalk.utli.camera.CameraPreview;
import adm.virtualcampuswalk.utli.gps.LocationService;
import adm.virtualcampuswalk.utli.gyroscope.PositionSensorService;

import static adm.virtualcampuswalk.utli.camera.CameraService.getCameraInstance;
import static adm.virtualcampuswalk.utli.camera.CameraService.setPosition;


public class MainActivity extends AppCompatActivity {

    private Camera camera;
    private CameraPreview preview;
    Camera.Parameters parameters;

    private LocationService locationService;
    private LocationListener locationListener;
    private PositionSensorService positionSensorService;
    boolean positionBounded = false;
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent positionSensorIntent = new Intent(getBaseContext(), PositionSensorService.class);
        bindService(positionSensorIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        initCamera();
        initUpdateUI();
        initLocationListener();
        locationService = new LocationService(getBaseContext(), locationListener);
    }

    private void initLocationListener() {
        this.locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i(Util.TAG, "NEW LOCATION " + "LAT: " + location.getLatitude() + " LON: " + location.getLongitude());
                setTextViewText(R.id.locationTV, "LAT: " + location.getLatitude() + " LON: " + location.getLongitude());
            }
        };
    }

    private void initUpdateUI() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (positionBounded) {
                            PhoneRotation phoneRotation = positionSensorService.getPhoneRotation();
                            setTextViewText(R.id.azimuthTV, String.format("Azimuth: %.2f", phoneRotation.getAzimuth()));
                            setTextViewText(R.id.pitchTV, String.format("Pitch: %.2f", phoneRotation.getPitch()));
                            setTextViewText(R.id.rollTV, String.format("Roll: %.2f", phoneRotation.getRoll()));
                        }
                    }
                });
            }
        }, 1000, 100);
    }

    private void setTextViewText(int id, String text) {
        try {
            TextView textView = (TextView) findViewById(id);
            textView.setText(text);
        } catch (Exception ex) {
            Log.e(Util.TAG, "MainActivity: setTextViewText: " + ex.getMessage());
        }
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

    @Override
    protected void onRestart() {
        super.onRestart();
        initUpdateUI();
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
        locationService.stopLocationRequest();
        if (positionBounded) {
            unbindService(serviceConnection);
            positionBounded = false;
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Log.d(Util.TAG, "Connested to " + componentName.getShortClassName());
            PositionSensorService.LocalBinder binder = (PositionSensorService.LocalBinder) service;
            MainActivity.this.positionSensorService = binder.getService();
            positionBounded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            positionBounded = false;
        }
    };

}
