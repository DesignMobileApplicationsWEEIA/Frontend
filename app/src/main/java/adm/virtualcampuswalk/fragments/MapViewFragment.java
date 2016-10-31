package adm.virtualcampuswalk.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;
import java.util.TimerTask;

import adm.virtualcampuswalk.R;
import adm.virtualcampuswalk.models.PhoneRotation;
import adm.virtualcampuswalk.utli.Util;
import adm.virtualcampuswalk.utli.gps.LocationService;
import adm.virtualcampuswalk.utli.gyroscope.PositionSensorService;

import static adm.virtualcampuswalk.utli.Util.TAG;

/**
 * Created by Adam Piech on 2016-10-21.
 */

public class MapViewFragment extends Fragment implements LocationListener, OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private LocationService locationService;
    private Location lastKnownLocation;
    private PositionSensorService positionSensorService;
    boolean positionBounded = false;
    private Marker marker;
    private Timer timer;
    private double currentArrowDegree = 0f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_view_activity, container, false);
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        locationService = new LocationService(getContext(), this);
        bindPositionSensorService();
        setArrowDegreeDependsOfOrientation(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        initUpdateUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        stopUpdateUI();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        locationService.stopLocationRequest();
        unbindPositionSensorService();
    }

    @Override
    public void onLocationChanged(Location location) {
        this.lastKnownLocation = location;
        if (marker != null) {
            marker.remove();
        }
        setMarkerOnMap(location);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        try {
            googleMap.setMyLocationEnabled(true);
        } catch (SecurityException ex) {
            Log.e(Util.TAG, ex.getMessage());
        }
        if (lastKnownLocation != null) {
            LatLng latLng = setMarkerOnMap(lastKnownLocation);
            this.googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    @NonNull
    private LatLng setMarkerOnMap(Location location) {
        LatLng map = new LatLng(location.getLatitude(), location.getLongitude());
        if (googleMap != null) {
            marker = this.googleMap.addMarker(new MarkerOptions().position(map).title("You"));
        }
        return map;
    }

    private void bindPositionSensorService() {
        if (!positionBounded) {
            Intent positionSensorIntent = new Intent(getActivity().getBaseContext(), PositionSensorService.class);
            getActivity().bindService(positionSensorIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private void unbindPositionSensorService() {
        if (positionBounded) {
            getActivity().unbindService(serviceConnection);
            positionBounded = false;
        }
    }

    private void setArrowDegreeDependsOfOrientation(View inflate) {
        final int rotation = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        View arrow = inflate.findViewById(R.id.arrowMapTV);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT && rotation == Surface.ROTATION_0) {
            Log.i(TAG, "PORT ROT 0");
            arrow.setRotation(270);
        }
        if (orientation == Configuration.ORIENTATION_PORTRAIT && rotation == Surface.ROTATION_180) {
            Log.i(TAG, "PORT ROT 180");
            arrow.setRotation(90);
        }
        if (orientation == Configuration.ORIENTATION_LANDSCAPE && rotation == Surface.ROTATION_270) {
            Log.i(TAG, "PORT ROT 270");
            arrow.setRotation(0);
        }
        if (orientation == Configuration.ORIENTATION_LANDSCAPE && rotation == Surface.ROTATION_90) {
            Log.i(TAG, "LAND ROT 90");
            arrow.setRotation(180);
        }
    }

    private void initUpdateUI() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (positionBounded) {
                            PhoneRotation phoneRotation = positionSensorService.getPhoneRotation();
                            updateArrowDirection(phoneRotation);
                        }
                    }
                });
            }
        }, 1000, 100);
    }

    private void updateArrowDirection(PhoneRotation phoneRotation) {
        RotateAnimation rotateAnimation = new RotateAnimation((float) currentArrowDegree, (float) phoneRotation.getAzimuth() * -1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(210);
        rotateAnimation.setFillAfter(true);
        View arrow = getActivity().findViewById(R.id.arrowMapTV);
        arrow.startAnimation(rotateAnimation);
        currentArrowDegree = -phoneRotation.getAzimuth();
    }

    private void stopUpdateUI() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Log.d(TAG, "Connested to " + componentName.getShortClassName());
            PositionSensorService.LocalBinder binder = (PositionSensorService.LocalBinder) service;
            MapViewFragment.this.positionSensorService = binder.getService();
            positionBounded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            positionBounded = false;
        }
    };
}
