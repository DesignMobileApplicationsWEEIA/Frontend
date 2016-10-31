package adm.virtualcampuswalk.utli.rotation;

import android.content.Context;
import android.content.res.Configuration;
import android.view.Surface;
import android.view.WindowManager;

/**
 * Created by mariusz on 31.10.16.
 */

public class SimpleRotationReader implements RotationReader {

    private Context context;
    private int rotation;
    private int orientation;

    public SimpleRotationReader(Context context) {
        this.context = context;
        this.rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        this.orientation = context.getResources().getConfiguration().orientation;
    }

    @Override
    public boolean isPortrait() {
        return orientation == Configuration.ORIENTATION_PORTRAIT && rotation == Surface.ROTATION_0;
    }

    @Override
    public boolean isPortraitUpsideDown() {
        return orientation == Configuration.ORIENTATION_PORTRAIT && rotation == Surface.ROTATION_180;
    }

    @Override
    public boolean isLandscapeRight() {
        return orientation == Configuration.ORIENTATION_LANDSCAPE && rotation == Surface.ROTATION_270;
    }

    @Override
    public boolean isLandscapeLeft() {
        return orientation == Configuration.ORIENTATION_LANDSCAPE && rotation == Surface.ROTATION_90;
    }
}
