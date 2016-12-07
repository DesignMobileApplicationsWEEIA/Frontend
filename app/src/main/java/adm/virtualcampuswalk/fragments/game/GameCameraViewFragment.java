package adm.virtualcampuswalk.fragments.game;

import android.content.Context;
import android.hardware.Camera;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;

import java.util.ArrayList;
import java.util.List;

import adm.virtualcampuswalk.R;
import adm.virtualcampuswalk.models.Building;
import adm.virtualcampuswalk.models.PhoneData;
import adm.virtualcampuswalk.models.PhoneLocation;
import adm.virtualcampuswalk.models.PhoneRotation;
import adm.virtualcampuswalk.models.Result;
import adm.virtualcampuswalk.utli.Util;
import adm.virtualcampuswalk.utli.api.VirtualCampusWalk;
import adm.virtualcampuswalk.utli.arrow.ArrowUpdater;
import adm.virtualcampuswalk.utli.arrow.SimpleArrowUpdater;
import adm.virtualcampuswalk.utli.camera.CameraPreview;
import adm.virtualcampuswalk.utli.gps.LocationService;
import adm.virtualcampuswalk.utli.rotation.RotationReader;
import adm.virtualcampuswalk.utli.rotation.SimpleRotationReader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static adm.virtualcampuswalk.utli.Util.BASE_URL;
import static adm.virtualcampuswalk.utli.Util.TAG;
import static adm.virtualcampuswalk.utli.camera.CameraService.getCameraInstance;
import static adm.virtualcampuswalk.utli.camera.CameraService.setFocus;
import static adm.virtualcampuswalk.utli.camera.CameraService.setPosition;

/**
 * Created by Adam Piech on 2016-10-20.
 */
public class GameCameraViewFragment extends GamePositionServiceFragment {

    private Camera camera;
    private CameraPreview preview;
    private Camera.Parameters parameters;

    private LocationService locationService;
    private LocationListener locationListener;
    private RotationReader rotationReader;
    private ArrowUpdater arrowUpdater;
    private ImageView arrow;
    private VirtualCampusWalk virtualCampusWalk;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View inflate = inflater.inflate(R.layout.camera_view_activity, container, false);
        initLocationRequests();
        initArrowUtils(inflate);
        initVirtualCampusGame();
        return inflate;
    }

    private void buildingCall(PhoneData phoneData) {
//        Call<Result<Building>> call = virtualCampusWalk.getBuilding(new PhoneData(positionSensorService.getPhoneRotation().getRoll(), phoneLocation));
        Call<Result<Building>> call = virtualCampusWalk.getBuilding(phoneData);
        call.enqueue(new Callback<Result<Building>>() {
            @Override
            public void onResponse(Call<Result<Building>> call, Response<Result<Building>> response) {
                Result<Building> body = response.body();
                Log.i(TAG, "RESPONSE: " + response.body().toString());
            }

            @Override
            public void onFailure(Call<Result<Building>> call, Throwable throwable) {
                Log.e(TAG, "ERROR: " + throwable.getMessage(), throwable);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        initCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
//        stopCamera();
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
                arrowUpdater.setArrowDirection(arrow, (float) phoneRotation.getAzimuth());
            }
        }, 1000, 100);
    }

    private void initLocationRequests() {
        initLocationListener();
        locationService = new LocationService(getActivity(), locationListener);
    }

    private void initArrowUtils(View inflate) {
        rotationReader = new SimpleRotationReader(getContext());
        arrowUpdater = new SimpleArrowUpdater(rotationReader);
        arrow = (ImageView) inflate.findViewById(R.id.arrowMapTV);
    }

    private void initLocationListener() {
        this.locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                PhoneRotation phoneRotation = positionSensorService.getPhoneRotation();
                Log.i(TAG, "PHONE ROTATION " + phoneRotation + " NEW LOCATION " + "LAT: " + location.getLatitude() + " LON: " + location.getLongitude());
            }
        };
    }

    private void stopLocationRequests() {
        locationService.stopLocationRequest();
    }

    private void initCamera() {
        camera = getCameraInstance();
        preview = new CameraPreview(getActivity().getBaseContext(), camera);
        FrameLayout preview = (FrameLayout) getActivity().findViewById(R.id.camera_preview);
        preview.addView(this.preview);
        setPosition(camera, getResources().getConfiguration(), (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE));
        setFocus(camera, Camera.Parameters.FOCUS_MODE_AUTO);
    }

    private void initVirtualCampusGame() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        virtualCampusWalk = retrofit.create(VirtualCampusWalk.class);
    }

    private void stopCamera() {
        FrameLayout frameLayout = (FrameLayout) getActivity().findViewById(R.id.camera_preview);
        frameLayout.removeView(preview);
        preview.stopPreviewAndFreeCamera();
    }

}