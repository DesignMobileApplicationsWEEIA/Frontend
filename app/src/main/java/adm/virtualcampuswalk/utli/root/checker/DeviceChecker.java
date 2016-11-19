package adm.virtualcampuswalk.utli.root.checker;

import java.io.File;

/**
 * Created by mariusz on 19.11.16.
 */

public class DeviceChecker {

    public static final String SYSTEM_APP_SUPERUSER_APK = "/system/app/Superuser.apk";
    public static final String TEST_KEYS = "test-keys";

    public static boolean isRooted() {
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains(TEST_KEYS)) {
            return true;
        }
        try {
            File file = new File(SYSTEM_APP_SUPERUSER_APK);
            if (file.exists()) {
                return true;
            }
        } catch (Throwable e1) {
        }
        return false;
    }


}
