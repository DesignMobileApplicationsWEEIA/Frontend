package adm.virtualcampuswalk.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import adm.virtualcampuswalk.R;
import adm.virtualcampuswalk.utli.Util;
import adm.virtualcampuswalk.utli.gps.LocationService;

/**
 * Created by Adam Piech on 2016-10-21.
 */

public class MapViewFragment extends Fragment implements LocationListener, OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private LocationService locationService;
    private Location lastKnownLocation;
    private Marker marker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_view_activity, container, false);
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        locationService = new LocationService(getContext(), this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
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
}
