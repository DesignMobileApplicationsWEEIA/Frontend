package adm.virtualcampuswalk.utli;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Base64;
import android.widget.Toast;

/**
 * Created by mariusz on 10.10.16.
 */

public class Util {
    public static final String TAG = "VCW";
    public static final String BASE_URL = "http:///138.91.187.124/api/";

    public static void exitToastMessage(Context context, String message, int duration, int delay) {
        Toast.makeText(context, message, duration).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                System.exit(-1);
            }
        }, delay);
    }

    public static Bitmap convertStringByteToBitmap(String bytes){
        byte[] decode = Base64.decode(bytes, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decode, 0, decode.length);
    }
}
