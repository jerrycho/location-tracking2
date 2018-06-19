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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Calendar;

import app.mmguardian.com.Constants;
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

    public static int getLastInsertWithCurrentDiffSec(Calendar calendar, Context context){
        long lastInsertDate = new PreferenceManager(context).getLongPref("LAST_INSERT_DATE");
        int diffSec = 0;
        if (lastInsertDate>0) {
            diffSec = (int) ((calendar.getTime().getTime() - lastInsertDate) / 1000);
            diffSec = ((int) Constants.SCHEDULER_TIME_SEC) - diffSec;
        }

        return diffSec;

//        long after = 0;
//        Calendar c = Calendar.getInstance();
//        if (lastInsertDate == 0){
//            after = c.getTimeInMillis();
//        }
//        else {
//            int diffSec = (int) ((c.getTime().getTime() - lastInsertDate) / 1000);
//            diffSec = ((int) Constants.SCHEDULER_TIME_SEC) - diffSec;
//            if (diffSec > 0){
//                c.add(Calendar.SECOND, diffSec);
//            }
//            after = c.getTimeInMillis();
//        }

    }

    public static boolean isNetworkingConnected(){
        return true;
    }


    public static boolean isInternetAvailable(String address, int port, int timeoutMs) {
        try {
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress(address, port);

            sock.connect(sockaddr, timeoutMs); // This will block no more than timeoutMs
            sock.close();

            return true;

        } catch (IOException e) { return false; }
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


        if (Util.isNetworkingConnected()) {
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
