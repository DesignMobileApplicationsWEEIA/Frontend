package adm.virtualcampuswalk.fragments;

import android.content.Context;
import android.hardware.Camera;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;

import adm.virtualcampuswalk.R;
import adm.virtualcampuswalk.models.PhoneRotation;
import adm.virtualcampuswalk.utli.camera.CameraPreview;
import adm.virtualcampuswalk.utli.gps.LocationService;
import adm.virtualcampuswalk.utli.rotation.RotationReader;
import adm.virtualcampuswalk.utli.rotation.SimpleRotationReader;

import static adm.virtualcampuswalk.utli.Util.TAG;
import static adm.virtualcampuswalk.utli.camera.CameraService.getCameraInstance;
import static adm.virtualcampuswalk.utli.camera.CameraService.setFocus;
import static adm.virtualcampuswalk.utli.camera.CameraService.setPosition;

/**
 * Created by Adam Piech on 2016-10-20.
 */
public class CameraViewFragment extends PositionServiceFragment {

    private Camera camera;
    private CameraPreview preview;
    private Camera.Parameters parameters;

    private LocationService locationService;
    private LocationListener locationListener;
    private double currentArrowDegree = 0f;
    private RotationReader rotationReader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View inflate = inflater.inflate(R.layout.camera_view_activity, container, false);
        initLocationRequests();
        rotationReader = new SimpleRotationReader(getContext());
        setArrowDegreeDependsOfOrientation(inflate);
        return inflate;
    }

    private void setArrowDegreeDependsOfOrientation(View inflate) {
        View arrow = inflate.findViewById(R.id.arrowTV);
        if (rotationReader.isPortrait()) {
            Log.i(TAG, "PORT ROT 0");
            arrow.setRotation(270);
        }
        if (rotationReader.isPortraitUpsideDown()) {
            Log.i(TAG, "PORT ROT 180");
            arrow.setRotation(90);
        }
        if (rotationReader.isLandscapeRight()) {
            Log.i(TAG, "PORT ROT 270");
            arrow.setRotation(0);
        }
        if (rotationReader.isLandscapeLeft()) {
            Log.i(TAG, "LAND ROT 90");
            arrow.setRotation(180);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationRequests();
    }

    @Override
    protected void initUpdateUI() {
        super.initUpdateUI();
        scheduleNewTimerTask(new Runnable() {
            @Override
            public void run() {
                PhoneRotation phoneRotation = positionSensorService.getPhoneRotation();
                setTextViewText(R.id.azimuthTV, String.format("Azimuth: %.2f", phoneRotation.getAzimuth()));
                setTextViewText(R.id.pitchTV, String.format("Pitch: %.2f", phoneRotation.getPitch()));
                setTextViewText(R.id.rollTV, String.format("Roll: %.2f", phoneRotation.getRoll()));
                updateArrowDirection(phoneRotation);
            }
        }, 1000, 100);
    }

    private void initLocationRequests() {
        initLocationListener();
        locationService = new LocationService(getActivity(), locationListener);
    }

    private void initLocationListener() {
        this.locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i(TAG, "NEW LOCATION " + "LAT: " + location.getLatitude() + " LON: " + location.getLongitude());
                setTextViewText(R.id.locationTV, "LAT: " + location.getLatitude() + " LON: " + location.getLongitude());
            }
        };
    }

    private void stopLocationRequests() {
        locationService.stopLocationRequest();
    }

    private void updateArrowDirection(PhoneRotation phoneRotation) {
        RotateAnimation rotateAnimation = new RotateAnimation((float) currentArrowDegree, (float) phoneRotation.getAzimuth() * -1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(210);
        rotateAnimation.setFillAfter(true);
        View arrow = getActivity().findViewById(R.id.arrowTV);
        arrow.startAnimation(rotateAnimation);
        currentArrowDegree = -phoneRotation.getAzimuth();
    }

    private void setTextViewText(int id, String text) {
        try {
            TextView textView = (TextView) getActivity().findViewById(id);
            textView.setText(text);
        } catch (Exception ex) {
            Log.e(TAG, "CameraViewFragment: setTextViewText: " + ex.getMessage());
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

    private void stopCamera() {
        FrameLayout frameLayout = (FrameLayout) getActivity().findViewById(R.id.camera_preview);
        frameLayout.removeView(preview);
        preview.stopPreviewAndFreeCamera();
    }

}