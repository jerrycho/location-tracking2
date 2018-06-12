package app.mmguardian.com.location_tracking.log;


import android.os.Build;
import android.util.Log;

import app.mmguardian.com.location_tracking.BuildConfig;

public class AppLog {

    private static final String TAG = "location_tracking";

    public static void d(String msg) {
        if(BuildConfig.DEBUG) {
            if ("F3116".equalsIgnoreCase(Build.MODEL))
                System.out.println("["+TAG+"] "+ msg);
            else
                Log.d(TAG, msg);
        }
    }


}
