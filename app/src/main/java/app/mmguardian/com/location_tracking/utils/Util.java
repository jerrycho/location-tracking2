package app.mmguardian.com.location_tracking.utils;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;

import app.mmguardian.com.Constants;
import app.mmguardian.com.location_tracking.log.AppLog;

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

    public static int getLastInsertWithCurrentDiffSec(Calendar calendar, Context context){
        long lastInsertDate = new PreferenceManager(context).getLongPref("LAST_INSERT_DATE");
        int diffSec = 0;
        if (lastInsertDate>0) {
            diffSec = (int) ((calendar.getTime().getTime() - lastInsertDate) / 1000);
            diffSec = ((int) Constants.SCHEDULER_TIME_SEC) - diffSec;
        }

        return diffSec;

    }

    public static boolean isNetworkingConnected(Context context){
//        Runtime runtime = Runtime.getRuntime();
//        try {
//            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
//            int exitValue = ipProcess.waitFor();
//
//            return (exitValue == 0);
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return false;
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
        return connected;
    }


    /**
     * This method is used to get current location information
     */
    public static void getCurrentLocaiton(Calendar calendar, Context context) {
        AppLog.d( "getCurrentLocaiton :" );

        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        if (Util.isNetworkingConnected(context)) {
            mFusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            new AsyncInsertDBTaskRunner(calendar, context).execute(location);
                        }
                    });
        }
        else {
            new AsyncInsertDBTaskRunner(calendar, context).execute((Location) null);
        }

    }
}
