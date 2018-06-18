package app.mmguardian.com.location_tracking.utils;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import app.mmguardian.com.location_tracking.log.AppLog;
import app.mmguardian.com.location_tracking.service.MyIntentService;
import app.mmguardian.com.location_tracking.task.AsyncInsertDBTaskRunner;

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

    public static boolean isNetworkingConnected(){
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        }catch (Exception e){
            return false;
        }
    }

    /**
     * This method is used to get current location information
     */
    public static void getCurrentLocaiton(Context context) {
        AppLog.d( "getCurrentLocaiton :" );

        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
//        if (Util.isNetworkingConnected()) {
//            mFusedLocationProviderClient.getLastLocation()
//                    .addOnSuccessListener(new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            new AsyncInsertDBTaskRunner(context).execute(location);
//                        }
//                    });
//        }
//        else {
//            new AsyncInsertDBTaskRunner(context).execute((Location) null);
//        }
        //if (Util.isNetworkingConnected()) {
            mFusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            new AsyncInsertDBTaskRunner(context).execute(location);
                        }
                    });
//        }
//        else {
//            new AsyncInsertDBTaskRunner(context).execute((Location) null);
//        }
    }
}
