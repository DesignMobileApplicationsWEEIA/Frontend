package adm.virtualcampuswalk.utli.arrow;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import adm.virtualcampuswalk.utli.rotation.RotationReader;

import static adm.virtualcampuswalk.utli.Util.TAG;

/**
 * Created by mariusz on 31.10.16.
 */

public class SimpleArrowUpdater implements ArrowUpdater {

    private boolean fixedArrowDirection;
    private float currentArrowDegree;
    private RotationReader rotationReader;
    private int offset;

    public SimpleArrowUpdater() {
        this.offset = 0;
        this.fixedArrowDirection = false;
        this.currentArrowDegree = 0f;
    }

    public SimpleArrowUpdater(RotationReader rotationReader) {
        this();
        this.rotationReader = rotationReader;
    }

    public SimpleArrowUpdater(RotationReader rotationReader, int offset) {
        this(rotationReader);
        this.offset = offset;
    }

    @Override
    public void setArrowDirection(View arrow, float direction) {
        checkArrowStartingPosition(arrow);
        rotateArrow(arrow, direction);
    }

    private void checkArrowStartingPosition(View arrow) {
        if (!fixedArrowDirection) {
            setArrowStaringPosition(arrow);
        } else {
            fixedArrowDirection = true;
        }
    }

    private void rotateArrow(View arrow, float direction) {
        RotateAnimation rotateAnimation = new RotateAnimation(currentArrowDegree, direction * -1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(210);
        rotateAnimation.setFillAfter(true);
        arrow.startAnimation(rotateAnimation);
        currentArrowDegree = -direction;
    }

    private void setArrowStaringPosition(View arrow) {
        if (rotationReader.isPortrait()) {
            Log.i(TAG, "PORT ROT 0");
            arrow.setRotation(270 + offset);
        }
        if (rotationReader.isPortraitUpsideDown()) {
            Log.i(TAG, "PORT ROT 180");
            arrow.setRotation(90 + offset);
        }
        if (rotationReader.isLandscapeRight()) {
            Log.i(TAG, "PORT ROT 270");
            arrow.setRotation(offset);
        }
        if (rotationReader.isLandscapeLeft()) {
            Log.i(TAG, "LAND ROT 90");
            arrow.setRotation(180 + offset);
        }
    }
}
