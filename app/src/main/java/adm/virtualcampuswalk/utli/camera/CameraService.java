package adm.virtualcampuswalk.utli.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;

import adm.virtualcampuswalk.utli.Util;

/**
 * Created by Adam Piech on 2016-10-13.
 */

public class CameraService {

    public boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            Log.d(Util.TAG, e.getMessage());
        }
        return camera;
    }

    public static void setPosition(Camera camera, Configuration conf, WindowManager windowManager) {
        int rotation = windowManager.getDefaultDisplay().getRotation();
        if (conf.orientation == Configuration.ORIENTATION_LANDSCAPE && rotation == Surface.ROTATION_270) {
            Log.i(Util.TAG, "LAND ROT 270");
            camera.setDisplayOrientation(180);
        }
        if (conf.orientation == Configuration.ORIENTATION_PORTRAIT && rotation == Surface.ROTATION_0) {
            Log.i(Util.TAG, "PORT ROT 90");
            camera.setDisplayOrientation(90);
        }
        if (conf.orientation == Configuration.ORIENTATION_PORTRAIT && rotation == Surface.ROTATION_180) {
            Log.i(Util.TAG, "PORT ROT 180");
            camera.setDisplayOrientation(270);
        }
    }

    public static void setFocus(Camera camera, String focusMode) {
        Camera.Parameters params = camera.getParameters();
        params.setFocusMode(focusMode);
        camera.setParameters(params);
    }
}
