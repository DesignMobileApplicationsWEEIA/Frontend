package adm.virtualcampuswalk.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Timer;
import java.util.TimerTask;

import adm.virtualcampuswalk.R;
import adm.virtualcampuswalk.utli.gyroscope.PositionSensorService;

import static adm.virtualcampuswalk.utli.Util.TAG;

/**
 * Created by mariusz on 31.10.16.
 */

public class PositionServiceFragment extends Fragment {
    protected PositionSensorService positionSensorService;
    private boolean positionBounded = false;
    private Timer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        bindPositionSensorService();
        return inflater.inflate(R.layout.camera_view_activity, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        initUpdateUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopUpdateUI();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindPositionSensorService();
    }

    protected void initUpdateUI() {
        timer = new Timer();
    }

    protected void stopUpdateUI() {
        if (timer != null) {
            timer.cancel();
        }
    }

    protected void scheduleNewTimerTask(final Runnable runnable, long delay, long period) {
        if (timer == null) {
            Log.e(TAG, "scheduleNewTimerTask: Timer is null!");
            return;
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (positionBounded) {
                            runnable.run();
                        }
                    }
                });
            }
        }, delay, period);
    }

    private void unbindPositionSensorService() {
        if (positionBounded) {
            getActivity().unbindService(serviceConnection);
            positionBounded = false;
        }
    }

    private void bindPositionSensorService() {
        if (!positionBounded) {
            Intent positionSensorIntent = new Intent(getActivity().getBaseContext(), PositionSensorService.class);
            getActivity().bindService(positionSensorIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Log.d(TAG, "Connested to " + componentName.getShortClassName());
            PositionSensorService.LocalBinder binder = (PositionSensorService.LocalBinder) service;
            PositionServiceFragment.this.positionSensorService = binder.getService();
            positionBounded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            positionBounded = false;
        }
    };
}
