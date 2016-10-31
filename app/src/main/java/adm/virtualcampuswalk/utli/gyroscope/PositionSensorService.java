package adm.virtualcampuswalk.utli.gyroscope;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import adm.virtualcampuswalk.models.PhoneRotation;
import adm.virtualcampuswalk.utli.Util;

import static adm.virtualcampuswalk.utli.Util.TAG;

/**
 * Created by mariusz on 12.10.16.
 */

public class PositionSensorService extends Service {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private LocalBinder mBinder = new LocalBinder();
    private PhoneRotation phoneRotation = new PhoneRotation();
    private SimpleSensorListener sensorListener;

    public class LocalBinder extends Binder {
        public PositionSensorService getService() {
            return PositionSensorService.this;
        }
    }

    public class SimpleSensorListener implements SensorEventListener {

        private float[] mGravity;
        private float[] mGeomagnetic;
        private float R[] = new float[9];
        private float I[] = new float[9];


        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                mGravity = sensorEvent.values;
            if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                mGeomagnetic = sensorEvent.values;
            if (mGravity != null && mGeomagnetic != null) {
                boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
                if (success) {
                    float orientation[] = new float[3];
                    SensorManager.getOrientation(R, orientation);
                    double azimuth = (Math.toDegrees(orientation[0]) + 360) % 360;
                    phoneRotation.setAzimuth(azimuth);
                    phoneRotation.setPitch(Math.toDegrees(orientation[1]));
                    phoneRotation.setRoll(Math.toDegrees(orientation[2]));

                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(Util.TAG, "START POSITION LISTENER");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorListener = new SimpleSensorListener();
        sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(sensorListener, magnetometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        try {
            sensorManager.unregisterListener(sensorListener);

        } catch (SecurityException ex) {
            Log.e(TAG, ex.getMessage());
            ex.printStackTrace();
        }
        return super.onUnbind(intent);
    }

    public PhoneRotation getPhoneRotation() {
        return phoneRotation;
    }
}
