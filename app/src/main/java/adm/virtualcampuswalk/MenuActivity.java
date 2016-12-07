package adm.virtualcampuswalk;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import adm.virtualcampuswalk.utli.Util;
import adm.virtualcampuswalk.utli.gps.GpsChangeReceiver;
import adm.virtualcampuswalk.utli.network.NetworkChangeReceiver;
import adm.virtualcampuswalk.utli.root.checker.DeviceChecker;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
//        if (DeviceChecker.isRooted()) {
//            Log.e(Util.TAG, "APP IS ROOTED! Exiting...");
//            finish();
//        }
        checkInternetConnection();
        checkGPSConnection();
    }

    public void goToGameView(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void goToWalkView(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void checkInternetConnection() {
        if (!NetworkChangeReceiver.isConnected(this)) {
            String alertTitle = "No Internet connection.";
            String alertMessage = "You have no internet connection. Please turn it on!";
            showResourceNotEnabledExitAlert(alertTitle, alertMessage);
        }
    }

    private void checkGPSConnection() {
        if (!GpsChangeReceiver.isConnected(this)) {
            String alertTitle = "No GPS connection.";
            String alertMessage = "You have no GPS connection. Please turn it on!";
            showResourceNotEnabledExitAlert(alertTitle, alertMessage);
        }
    }

    private void showResourceNotEnabledExitAlert(String alertTitle, String alertMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(alertTitle);
        builder.setMessage(alertMessage);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }
}
