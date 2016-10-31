package adm.virtualcampuswalk.utli.rotation;

/**
 * Created by mariusz on 31.10.16.
 */

public interface RotationReader {
    boolean isPortrait();

    boolean isPortraitUpsideDown();

    boolean isLandscapeRight();

    boolean isLandscapeLeft();

}
