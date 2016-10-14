package adm.virtualcampuswalk.utli.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by Adam Piech on 2016-10-13.
 */

public class CameraService {

    public boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        } else {
            return false;
        }
    }

    public static Camera getCameraInstance(){
        Camera camera = null;
        try {
            camera = Camera.open();
        }
        catch (Exception e){
            Log.d(TAG, e.getMessage());
        }
        return camera;
    }

    public static void setPosition(Camera camera, Configuration conf) {
        if (conf.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            camera.setDisplayOrientation(0);
        }
        if (conf.orientation == Configuration.ORIENTATION_PORTRAIT) {
            camera.setDisplayOrientation(90);
        }
    }
}
