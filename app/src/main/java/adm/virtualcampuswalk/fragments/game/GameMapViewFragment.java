package adm.virtualcampuswalk.fragments.game;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import adm.virtualcampuswalk.utli.arrow.ArrowUpdater;
import adm.virtualcampuswalk.utli.arrow.SimpleArrowUpdater;
import adm.virtualcampuswalk.utli.gps.LocationService;
import adm.virtualcampuswalk.utli.rotation.RotationReader;
import adm.virtualcampuswalk.utli.rotation.SimpleRotationReader;

/**
 * Created by Adam Piech on 2016-10-21.
 */

public class GameMapViewFragment extends GamePositionServiceFragment implements LocationListener, OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private LocationService locationService;
    private Location lastKnownLocation;
    private Marker marker;
    private RotationReader rotationReader;
    private ArrowUpdater arrowUpdater;
    private ImageView arrow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.map_view_activity, container, false);
        initGoogleMap(savedInstanceState, view);
        initLocationService();
        initArrowUtils(view);
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
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
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
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude())));
        this.googleMap.setMinZoomPreference(18.0f);
        if (googleMap != null) {
            marker = this.googleMap.addMarker(new MarkerOptions().position(map).title("You"));
        }
        return map;
    }

    @Override
    protected void initUpdateUI() {
        super.initUpdateUI();
        scheduleNewTimerTask(new Runnable() {
            @Override
            public void run() {
                PhoneRotation phoneRotation = positionSensorService.getPhoneRotation();
                arrowUpdater.setArrowDirection(arrow, (float) phoneRotation.getAzimuth());
            }
        }, 1000, 100);
    }

    private void initArrowUtils(View view) {
        rotationReader = new SimpleRotationReader(getContext());
        arrowUpdater = new SimpleArrowUpdater(rotationReader);
        arrow = (ImageView) view.findViewById(R.id.arrowMapTV);
    }

    private void initLocationService() {
        locationService = new LocationService(getContext(), this);
    }

    private void initGoogleMap(Bundle savedInstanceState, View view) {
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

}
