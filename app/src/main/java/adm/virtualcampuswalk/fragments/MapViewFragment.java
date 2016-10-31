package adm.virtualcampuswalk.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import adm.virtualcampuswalk.R;
import adm.virtualcampuswalk.models.PhoneRotation;
import adm.virtualcampuswalk.utli.Util;
import adm.virtualcampuswalk.utli.gps.LocationService;
import adm.virtualcampuswalk.utli.rotation.RotationReader;
import adm.virtualcampuswalk.utli.rotation.SimpleRotationReader;

import static adm.virtualcampuswalk.utli.Util.TAG;

/**
 * Created by Adam Piech on 2016-10-21.
 */

public class MapViewFragment extends PositionServiceFragment implements LocationListener, OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private LocationService locationService;
    private Location lastKnownLocation;
    private Marker marker;
    private double currentArrowDegree = 0f;
    private RotationReader rotationReader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.map_view_activity, container, false);
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        locationService = new LocationService(getContext(), this);
        rotationReader = new SimpleRotationReader(getContext());
        setArrowDegreeDependsOfOrientation(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
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

    private void setArrowDegreeDependsOfOrientation(View inflate) {
        View arrow = inflate.findViewById(R.id.arrowMapTV);
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
    protected void initUpdateUI() {
        super.initUpdateUI();
        scheduleNewTimerTask(new Runnable() {
            @Override
            public void run() {
                PhoneRotation phoneRotation = positionSensorService.getPhoneRotation();
                updateArrowDirection(phoneRotation);
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
}
