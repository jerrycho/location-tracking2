package app.mmguardian.com.location_tracking;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;
import java.util.List;


import app.mmguardian.com.location_tracking.receiver.AlarmReceiver;
import app.mmguardian.com.location_tracking.utils.AlarmUtil;
import app.mmguardian.com.location_tracking.utils.Util;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by apple on 17/6/2018.
 */

public class MainActivity2 extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    private static final int REQUEST_PERMISSION_RESULT = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* work
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        boolean ignoringOptimizations = powerManager.isIgnoringBatteryOptimizations(packageName);

        if (!ignoringOptimizations) {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
            startActivity(intent);
        }


        AlarmUtil.setAlarmTime4(MainActivity2.this); //work
        */
        if (haveAllPermission()){
            AlarmUtil.setAlarmTime4(MainActivity2.this);
        }
        else {
            EasyPermissions.requestPermissions(this,
                    "Please approved : ",
                    R.string.yes,
                    R.string.no,
                    REQUEST_PERMISSION_RESULT,
                    new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                    }
            );
        }

    }

    public boolean haveAllPermission(){
        return EasyPermissions.hasPermissions(this, new String[] {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
        });
    }

    @Override
    public void onPermissionsGranted(int i, List<String> list) {
        AlarmUtil.setAlarmTime4(MainActivity2.this);
    }

    @Override
    public void onPermissionsDenied(int i, List<String> list) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}
