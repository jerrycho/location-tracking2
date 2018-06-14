package app.mmguardian.com.location_tracking.service;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import app.mmguardian.com.Constants;
import app.mmguardian.com.location_tracking.log.AppLog;
import app.mmguardian.com.location_tracking.utils.LocationTracking;

/**
 * The Android service, when started service, service will auto loop by period to get the current
 * location
 *
 * @author  Jerry Cho
 * @version 1.0
 */
public class TrackingService extends Service {

    public final static String TAG = "location_tracking";

    private Timer mTimer;
    private TimerTask mTimerTask;

    private boolean isStartedTimer = false;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(1,new Notification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        AppLog.d("[TrackingService] onStartCommand");
        doStartTimer();
        return START_STICKY;
    }

    public void doStartTimer() {
        if (!isStartedTimer) {
            LocationTracking mLocationTracking = new LocationTracking(TrackingService.this);
            mLocationTracking.doStartTimer();

            isStartedTimer = true;
            mTimer = new Timer();
            initTimerTask();
            mTimer.schedule(mTimerTask, Constants.SCHEDULER_TIME_SEC * 1000, Constants.SCHEDULER_TIME_SEC * 1000);
        }
    }

    public void doStopTimer() {
        if (mTimer != null) {
            isStartedTimer = false;
            mTimer.cancel();
            mTimer = null;
        }

    }

    private void initTimerTask() {
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                LocationTracking mLocationTracking = new LocationTracking(TrackingService.this);
                mLocationTracking.doStartTimer();
            }
        };
    }


    @Override
    public void onDestroy() {
        AppLog.d( "onDestroy >>>");
        doStopTimer();
        Intent broadcastIntent = new Intent();
        Context c = null;
        try {
            c = createPackageContext("app.mmguardian.com.location_tracking", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "onDestroy >>>" + e.toString());
        }

        broadcastIntent.setClassName(c, "app.mmguardian.com.location_tracking.receiver.ServiceRequestRestartBroadcastReceiver");
        broadcastIntent.setAction("app.mmguardian.com.location_tracking.ServiceRequestRestart");
        broadcastIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(broadcastIntent);
        super.onDestroy();
    }
}
