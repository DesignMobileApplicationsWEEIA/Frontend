package adm.virtualcampuswalk.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Camera;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;

import java.util.Timer;
import java.util.TimerTask;

import adm.virtualcampuswalk.R;
import adm.virtualcampuswalk.models.PhoneRotation;
import adm.virtualcampuswalk.utli.Util;
import adm.virtualcampuswalk.utli.camera.CameraPreview;
import adm.virtualcampuswalk.utli.gps.LocationService;
import adm.virtualcampuswalk.utli.gyroscope.PositionSensorService;

import static adm.virtualcampuswalk.utli.camera.CameraService.getCameraInstance;
import static adm.virtualcampuswalk.utli.camera.CameraService.setFocus;
import static adm.virtualcampuswalk.utli.camera.CameraService.setPosition;

/**
 * Created by Adam Piech on 2016-10-20.
 */
public class CameraViewFragment extends Fragment {

    private Camera camera;
    private CameraPreview preview;
    private Camera.Parameters parameters;

    private LocationService locationService;
    private LocationListener locationListener;
    private PositionSensorService positionSensorService;
    boolean positionBounded = false;
    private Timer timer = new Timer();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Intent positionSensorIntent = new Intent(getActivity().getBaseContext(), PositionSensorService.class);
        getActivity().bindService(positionSensorIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        initUpdateUI();
        initLocationListener();
        locationService = new LocationService(getActivity(), locationListener);

        return inflater.inflate(R.layout.camera_view_activity, container, false);
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
                getActivity().runOnUiThread(new Runnable() {
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
            TextView textView = (TextView) getActivity().findViewById(id);
            textView.setText(text);
        } catch (Exception ex) {
            Log.e(Util.TAG, "MainActivity: setTextViewText: " + ex.getMessage());
        }
    }

    private void initCamera() {
        camera = getCameraInstance();
        preview = new CameraPreview(getActivity().getBaseContext(), camera);
        FrameLayout preview = (FrameLayout) getActivity().findViewById(R.id.camera_preview);
        preview.addView(this.preview);
        setPosition(camera, getResources().getConfiguration(), (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE));
        setFocus(camera, Camera.Parameters.FOCUS_MODE_AUTO);
    }

    @Override
    public void onResume() {
        super.onResume();
        initCamera();
    }

    @Override
    public void onStop() {
        super.onStop();
        locationService.stopLocationRequest();
        if (positionBounded) {
            getActivity().unbindService(serviceConnection);
            positionBounded = false;
        }
        preview.stopPreviewAndFreeCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        preview.stopPreviewAndFreeCamera();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Log.d(Util.TAG, "Connested to " + componentName.getShortClassName());
            PositionSensorService.LocalBinder binder = (PositionSensorService.LocalBinder) service;
            CameraViewFragment.this.positionSensorService = binder.getService();
            positionBounded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            positionBounded = false;
        }
    };

}