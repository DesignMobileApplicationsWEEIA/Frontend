package adm.virtualcampuswalk.fragments.game;

import android.location.Location;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import adm.virtualcampuswalk.R;
import adm.virtualcampuswalk.fragments.PositionServiceFragment;
import adm.virtualcampuswalk.models.Achievement;
import adm.virtualcampuswalk.models.PhoneRotation;
import adm.virtualcampuswalk.models.Result;
import adm.virtualcampuswalk.utli.api.VirtualCampusWalk;
import adm.virtualcampuswalk.utli.arrow.ArrowUpdater;
import adm.virtualcampuswalk.utli.arrow.SimpleArrowUpdater;
import adm.virtualcampuswalk.utli.gps.LocationService;
import adm.virtualcampuswalk.utli.network.MacReader;
import adm.virtualcampuswalk.utli.network.SimpleMacReader;
import adm.virtualcampuswalk.utli.rotation.RotationReader;
import adm.virtualcampuswalk.utli.rotation.SimpleRotationReader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static adm.virtualcampuswalk.utli.Util.BASE_URL;
import static adm.virtualcampuswalk.utli.Util.TAG;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_BLUE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker;

/**
 * Created by Adam Piech on 2016-10-21.
 */

public class GameMapViewFragment extends PositionServiceFragment implements LocationListener, OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap googleMap;
    private LocationService locationService;
    private Location lastKnownLocation;
    private RotationReader rotationReader;
    private ArrowUpdater arrowUpdater;
    private ImageView arrow;
    private VirtualCampusWalk virtualCampusWalk;
    private MacReader macReader = new SimpleMacReader();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.map_view_activity, container, false);
        initGoogleMap(savedInstanceState, view);
        initLocationService();
        initArrowUtils(view);
        initVirtualCampusWalk();
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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            googleMap.setMyLocationEnabled(true);
        } catch (SecurityException ex) {
            Log.e(TAG, ex.getMessage());
        }
        if (lastKnownLocation != null) {
            LatLng latLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            this.googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
        setMarkersOnMap(googleMap);
    }

    private void setMarkersOnMap(final GoogleMap googleMap) {
        Call<Result<List<Achievement>>> achievements = virtualCampusWalk.getAchievements(macReader.getMacAddress());
        achievements.enqueue(
                new Callback<Result<List<Achievement>>>() {
                    @Override
                    public void onResponse(Call<Result<List<Achievement>>> call, Response<Result<List<Achievement>>> response) {

                        if (response.isSuccessful() && response.body().isSuccess()) {
                            for (Achievement achievement : response.body().getValue()) {
                                googleMap.addMarker(createMarker(achievement));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Result<List<Achievement>>> call, Throwable throwable) {
                        Log.e(TAG, "getAchievements: ", throwable);
                    }
                }
        );
    }

    private MarkerOptions createMarker(Achievement achievement) {
        LatLng latLng = new LatLng(achievement.getLatitude(), achievement.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(achievement.getName());
        markerOptions.icon(defaultMarker(achievement.isCompleted() ? HUE_BLUE : HUE_RED));
        return markerOptions;
    }

    private void initVirtualCampusWalk() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        virtualCampusWalk = retrofit.create(VirtualCampusWalk.class);
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
