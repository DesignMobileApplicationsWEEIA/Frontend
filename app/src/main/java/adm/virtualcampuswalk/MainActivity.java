package adm.virtualcampuswalk;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import adm.virtualcampuswalk.utli.Util;
import adm.virtualcampuswalk.utli.gps.MyService;

public class MainActivity extends AppCompatActivity {

    private long lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(getBaseContext(), MyService.class);
        startService(intent);
        Log.i(Util.TAG, "started");
    }
}
