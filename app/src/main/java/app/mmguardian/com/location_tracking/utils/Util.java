package app.mmguardian.com.location_tracking.utils;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Common function on app
 *
 * @author  Jerry Cho
 * @version 1.0
 */
public class Util {

    /**
     * This method is used to checking the service is running or not.
     *
     * @param context This is the first paramter to isServiceRunning method
     * @param serviceClass  This is the second parameter to isServiceRunning method
     * @return boolean This returns result of checking
     */
    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
