package adm.virtualcampuswalk.fragments.game;

import android.content.Context;
import android.hardware.Camera;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.gson.Gson;

import adm.virtualcampuswalk.R;
import adm.virtualcampuswalk.fragments.PositionServiceFragment;
import adm.virtualcampuswalk.models.Achievement;
import adm.virtualcampuswalk.models.PhoneData;
import adm.virtualcampuswalk.models.PhoneLocation;
import adm.virtualcampuswalk.models.PhoneRotation;
import adm.virtualcampuswalk.models.Result;
import adm.virtualcampuswalk.utli.api.VirtualCampusWalk;
import adm.virtualcampuswalk.utli.arrow.ArrowUpdater;
import adm.virtualcampuswalk.utli.arrow.SimpleArrowUpdater;
import adm.virtualcampuswalk.utli.camera.CameraPreview;
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
import static adm.virtualcampuswalk.utli.camera.CameraService.getCameraInstance;
import static adm.virtualcampuswalk.utli.camera.CameraService.setFocus;
import static adm.virtualcampuswalk.utli.camera.CameraService.setPosition;

/**
 * Created by Adam Piech on 2016-10-20.
 */
public class GameCameraViewFragment extends PositionServiceFragment {
    public static final String EARNED_ACHIEVEMENT_MESSAGE = "Wykonałeś zadanie. Odblokowałeś nową część achievementu!";

    private static int DELAY = 300;
    private Camera camera;
    private CameraPreview preview;

    private LocationService locationService;
    private LocationListener locationListener;
    private RotationReader rotationReader;
    private ArrowUpdater arrowUpdater;
    private ImageView arrow;
    private VirtualCampusWalk virtualCampusWalk;
    private MacReader macReader = new SimpleMacReader();
    private Location lastLocation;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            initBuildingCalls();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View inflate = inflater.inflate(R.layout.camera_view_activity, container, false);
        initLocationRequests();
        initArrowUtils(inflate);
        initVirtualCampusWalk();
        return inflate;
    }

    private void initBuildingCalls() {
        if (lastLocation != null) {
            double azimuth = positionSensorService.getPhoneRotation().getAzimuth();
            PhoneData phoneData = new PhoneData(azimuth, new PhoneLocation(lastLocation.getLongitude(), lastLocation.getLatitude()), macReader.getMacAddress());
//            PhoneData phoneData = new PhoneData(azimuth, new PhoneLocation(19.455894, 51.747071), macReader.getMacAddress());
            buildingCall(phoneData);
        } else {
            Log.w(TAG, getClassName() + " initBuildingCalls lastLocation is null");
        }
        handler.postDelayed(runnable, DELAY);
    }


    private void buildingCall(PhoneData phoneData) {
        Call<Result<Achievement>> call = virtualCampusWalk.postBuildingForAchievement(phoneData);
        call.enqueue(new Callback<Result<Achievement>>() {
            @Override
            public void onResponse(Call<Result<Achievement>> call, Response<Result<Achievement>> response) {
                if (response.isSuccessful() && response.body().isSuccess()) {
                    Achievement achievement = response.body().getValue();
                    if (!achievement.isCompleted()) {
                        Toast.makeText(getContext(), EARNED_ACHIEVEMENT_MESSAGE + " ( " + achievement.getName() + " )", Toast.LENGTH_LONG).show();
                        Log.i(TAG, getClassName() + " buildingCall achievement found! ( " + achievement.getName() + " )");
                    }
                }
            }

            @Override
            public void onFailure(Call<Result<Achievement>> call, Throwable throwable) {
                Log.e(TAG, getClassName() + " " + throwable.getMessage(), throwable);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initCamera();
        runnable.run();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopCamera();
        handler.removeCallbacks(runnable);
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
                lastLocation = location;
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

    private void initVirtualCampusWalk() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
        virtualCampusWalk = retrofit.create(VirtualCampusWalk.class);
    }

    private void stopCamera() {
        FrameLayout frameLayout = (FrameLayout) getActivity().findViewById(R.id.camera_preview);
        frameLayout.removeView(preview);
        preview.stopPreviewAndFreeCamera();
    }

    private String getClassName() {
        return GameCameraViewFragment.class.getClass().getSimpleName();
    }

}