package adm.virtualcampuswalk;

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
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import adm.virtualcampuswalk.models.PhoneRotation;
import adm.virtualcampuswalk.utli.Util;
import adm.virtualcampuswalk.utli.camera.CameraPreview;
import adm.virtualcampuswalk.utli.gps.LocationService;
import adm.virtualcampuswalk.utli.gyroscope.PositionSensorService;

import static adm.virtualcampuswalk.utli.camera.CameraService.getCameraInstance;

/**
 * Created by Adam Piech on 2016-10-14.
 */

public class CameraViewFragment extends Fragment {

    private Camera camera;
    private CameraPreview preview;

    private LocationService locationService;
    private PositionSensorService positionSensorService;
    boolean locationBounded = false;
    boolean positionBounded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initCamera();

        Intent intent = new Intent(getActivity().getBaseContext(), LocationService.class);
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        Intent positionSensorIntent = new Intent(getActivity().getBaseContext(), PositionSensorService.class);
        getActivity().bindService(positionSensorIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        return inflater.inflate(R.layout.camera_view_activity, container, false);
    }

    private void initCamera() {
//        camera = getCameraInstance();
//        preview = new CameraPreview(getActivity().getApplicationContext(), camera);
//        FrameLayout preview = (FrameLayout) getActivity().findViewById(R.id.camera_preview);
//        preview.addView(this.preview);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    public void btnClicked(View view) {
        if (locationBounded) {
            Location myLastLocation = locationService.getMyLastLocation();
            if (myLastLocation != null) {
                Toast.makeText(getActivity().getApplicationContext(), "LON: " + myLastLocation.getLongitude() + " LAT: " + myLastLocation.getLatitude(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Location unavailable! Try later!", Toast.LENGTH_SHORT).show();
            }
        }
        if (positionBounded) {
            TextView azimuthTV = (TextView) getActivity().findViewById(R.id.azimuthTV);
            TextView pitchTV = (TextView) getActivity().findViewById(R.id.pitchTV);
            TextView rollTV = (TextView) getActivity().findViewById(R.id.rollTV);
            PhoneRotation phoneRotation = positionSensorService.getPhoneRotation();
            Log.i(Util.TAG, phoneRotation.toString());
            azimuthTV.setText(String.format("Azimuth: %.2f", phoneRotation.getAzimuth()));
            pitchTV.setText(String.format("Pitch: %.2f", phoneRotation.getPitch()));
            rollTV.setText(String.format("Roll: %.2f", phoneRotation.getRoll()));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (locationBounded) {
            getActivity().unbindService(serviceConnection);
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
                CameraViewFragment.this.locationService = binder.getService();
                locationBounded = true;
            } else if (className.equals(PositionSensorService.class.getName())) {
                Log.d(Util.TAG, "Connested to " + componentName.getShortClassName());
                PositionSensorService.LocalBinder binder = (PositionSensorService.LocalBinder) service;
                CameraViewFragment.this.positionSensorService = binder.getService();
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
