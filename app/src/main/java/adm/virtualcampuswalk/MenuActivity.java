package adm.virtualcampuswalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import adm.virtualcampuswalk.utli.Util;
import adm.virtualcampuswalk.utli.root.checker.DeviceChecker;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        if (DeviceChecker.isRooted()) {
            Log.e(Util.TAG, "APP IS ROOTED! Exiting...");
            finish();
        }
    }


    public void goToGameView(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void goToWalkView(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
