package adm.virtualcampuswalk.utli.gyroscope;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import static adm.virtualcampuswalk.utli.Util.TAG;

/**
 * Created by mariusz on 12.10.16.
 */

public class PositionSensorService extends Service {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;

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
                    float azimuth = orientation[0];
                    float pitch = orientation[1];
                    float roll = orientation[2];
                    Log.i(TAG, "AZIMUTH: " + Math.toDegrees(azimuth));
                    Log.i(TAG, "PITCH: " + Math.toDegrees(pitch));
                    Log.i(TAG, "ROLL: " + Math.toDegrees(roll));

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
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        SimpleSensorListener listener = new SimpleSensorListener();
        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(listener, magnetometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
