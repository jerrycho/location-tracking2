package app.mmguardian.com.location_tracking.utils;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import app.mmguardian.com.location_tracking.service.SensorService;

public class Util {

    public static final String TAG = "location_tracking";

    public static void startService(Context context){
        Log.d(TAG, "startService >>>> " + (isServiceRunning(SensorService.class, context) ? "True" : "False"));
        if (!isServiceRunning(SensorService.class, context)) {
            context.startService(new Intent(context, SensorService.class));
        }
    }

    private static boolean isServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
