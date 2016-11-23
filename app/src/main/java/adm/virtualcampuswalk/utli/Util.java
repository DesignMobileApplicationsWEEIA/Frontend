package adm.virtualcampuswalk.utli;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by mariusz on 10.10.16.
 */

public class Util {
    public static final String TAG = "VCW";
    public static final String BASE_URL = "http://13.91.248.143/api/";

    public static void exitToastMessage(Context context, String message, int duration, int delay) {
        Toast.makeText(context, message, duration).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                System.exit(-1);
            }
        }, delay);
    }
}
