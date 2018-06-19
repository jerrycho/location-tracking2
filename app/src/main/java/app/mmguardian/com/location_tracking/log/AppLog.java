package app.mmguardian.com.location_tracking.log;


import android.os.Build;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import app.mmguardian.com.location_tracking.BuildConfig;

/**
 * The Application Log, special Sony F3116 is not support Log.d
 *
 * @author  Jerry Cho
 * @version 1.0
 */
public class AppLog {

    public static final String TAG = "location_tracking";

    public static void d(String msg) {
        if(BuildConfig.DEBUG) {
            if ("F3116".equalsIgnoreCase(Build.MODEL))
                System.out.println("["+TAG+"] "+ msg);
            else
                Log.d(TAG, msg);
        }
    }

    public static void d(String m , long timeInMillis) {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeInMillis);

        if(BuildConfig.DEBUG) {
            if ("F3116".equalsIgnoreCase(Build.MODEL))
                System.out.println("["+TAG+"] "+ "Time of "+ m +" : "+ sdFormat.format(c.getTime()));
            else
                Log.d(TAG, "Time of "+ m +" : "+ sdFormat.format(c.getTime()));
        }
    }

}
