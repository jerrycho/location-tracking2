//package app.mmguardian.com.location_tracking.service;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.os.AsyncTask;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.JobIntentService;
//import android.util.Log;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.tasks.OnSuccessListener;
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.util.Random;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import app.mmguardian.com.location_tracking.LocationTrackingApplication;
//import app.mmguardian.com.location_tracking.bus.NewLocationTrackingRecordEvent;
//import app.mmguardian.com.location_tracking.db.model.LocationRecord;
//
///*https://blog.klinkerapps.com/android-o-background-services/*/
//public class LocationJobIntentService extends JobIntentService {
//
//    public final static String TAG = "location_tracking";
//
//    public static final int JOB_ID = 10;
//
//    private Timer mTimer;
//    private TimerTask mTimerTask;
//
//    private FusedLocationProviderClient mFusedLocationProviderClient;
//
//    public static void enqueueWork(Context context, Intent work) {
//        Log.d(TAG, "enqueueWork >>>>>");
//        enqueueWork(context, LocationJobIntentService.class, JOB_ID, work);
//    }
//
//    @Override
//    protected void onHandleWork(@NonNull Intent intent) {
//        Log.d(TAG, "onHandleWork >>>>>");
//        // We have received work to do.  The system or framework is already
//        // holding a wake lock for us at this point, so we can just go.
//        doStartTimer();
//        while (true){}
//
//    }
//
//    @Override
//    public void onTaskRemoved(Intent rootIntent) {
//        super.onTaskRemoved(rootIntent);
//    }
//
//    @Override
//    public void onDestroy() {
//        Log.d(TAG, "onDestroy");
//        Intent broadcastIntent = new Intent("app.mmguardian.com.location_tracking.RestartSensor");
//        sendBroadcast(broadcastIntent);
//        doStopTimer();
//        super.onDestroy();
//    }
//
//    public void doStartTimer() {
//        mTimer = new Timer();
//        initTimerTask();
//        mTimer.schedule(mTimerTask, app.mmguardian.com.Constants.SCHEDULER_TIME, app.mmguardian.com.Constants.SCHEDULER_TIME);
//    }
//
//    public void doStopTimer() {
//        if (mTimer != null) {
//            mTimer.cancel();
//            mTimer = null;
//        }
//    }
//
//    private void initTimerTask() {
//        mTimerTask = new TimerTask() {
//            @Override
//            public void run() {
//                doGetCurrentLocaiton();
//            }
//        };
//    }
//
//    private void doGetCurrentLocaiton() {
//        Log.d(TAG, "doGetCurrentLocaiton0 :" );
//        if (mFusedLocationProviderClient == null) {
//            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        }
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        mFusedLocationProviderClient.getLastLocation()
//                .addOnSuccessListener(new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        LocationRecord mLocationRecord = new LocationRecord(location);
//                        new AsyncTaskRunner().execute(mLocationRecord);
//                        EventBus.getDefault().post(new NewLocationTrackingRecordEvent(mLocationRecord));
//                    }
//                });
//    }
//
//    private class AsyncTaskRunner extends AsyncTask<LocationRecord, Void, Void> {
//        @Override
//        protected Void doInBackground(LocationRecord... params) {
//            LocationTrackingApplication.getInstance().getLocationDatabase().locationRecordDao().insertLocationRecord(params[0]);
//            Log.d(TAG, "inserted>>"+params[0].date);
//            return null;
//        }
//    }
//}

package app.mmguardian.com.location_tracking.service;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.greenrobot.eventbus.EventBus;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import app.mmguardian.com.location_tracking.LocationTrackingApplication;
import app.mmguardian.com.location_tracking.bus.NewLocationTrackingRecordEvent;
import app.mmguardian.com.location_tracking.db.model.LocationRecord;
import app.mmguardian.com.location_tracking.utils.Util;

/*https://blog.klinkerapps.com/android-o-background-services/*/
public class LocationJobIntentService extends JobIntentService {

    public final static String TAG = "location_tracking";

    public static final int JOB_ID = 10;

    private Timer mTimer;
    private TimerTask mTimerTask;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public static void enqueueWork(Context context, Intent work) {
        Log.d(TAG, "enqueueWork >>>>>");
        enqueueWork(context, LocationJobIntentService.class, JOB_ID, work);
        Util.startService(context);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "onHandleWork >>>>>");
        // We have received work to do.  The system or framework is already
        // holding a wake lock for us at this point, so we can just go.
//        doStartTimer();
//        while (true){}

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        Intent broadcastIntent = new Intent("app.mmguardian.com.location_tracking.RestartSensor");
        sendBroadcast(broadcastIntent);
        doStopTimer();
        super.onDestroy();
    }

    public void doStartTimer() {
        mTimer = new Timer();
        initTimerTask();
        mTimer.schedule(mTimerTask, app.mmguardian.com.Constants.SCHEDULER_TIME, app.mmguardian.com.Constants.SCHEDULER_TIME);
    }

    public void doStopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    private void initTimerTask() {
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                doGetCurrentLocaiton();
            }
        };
    }

    private void doGetCurrentLocaiton() {
        Log.d(TAG, "doGetCurrentLocaiton0 :" );
        if (mFusedLocationProviderClient == null) {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        LocationRecord mLocationRecord = new LocationRecord(location);
                        new AsyncTaskRunner().execute(mLocationRecord);
                        EventBus.getDefault().post(new NewLocationTrackingRecordEvent(mLocationRecord));
                    }
                });
    }

    private class AsyncTaskRunner extends AsyncTask<LocationRecord, Void, Void> {
        @Override
        protected Void doInBackground(LocationRecord... params) {
            LocationTrackingApplication.getInstance().getLocationDatabase().locationRecordDao().insertLocationRecord(params[0]);
            Log.d(TAG, "inserted>>"+params[0].date);
            return null;
        }
    }

}
