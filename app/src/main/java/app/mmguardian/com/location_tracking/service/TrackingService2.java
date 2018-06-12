package app.mmguardian.com.location_tracking.service;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import app.mmguardian.com.Constants;
import app.mmguardian.com.location_tracking.bus.ServiceEventConnectedEvent;
import app.mmguardian.com.location_tracking.log.AppLog;
import app.mmguardian.com.location_tracking.utils.LocationTracking;


public class TrackingService2 extends Service {

    public final static String TAG = "location_tracking";
    public static final String ACTION="app.mmguardian.com.location_tracking.StartTimer";

    ScheduledExecutorService mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private Timer mTimer;
    private TimerTask mTimerTask;

    private boolean isStartedTimer = false;

    BroadcastReceiver startTimerReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        startForeground(1,new Notification());

        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction(ACTION);
        this.startTimerReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                doStartTimer();
            }
        };
        // Registers the receiver so that your service will listen for
        // broadcasts
        this.registerReceiver(this.startTimerReceiver, theFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        AppLog.d("[TrackingService] onStartCommand");

        if (mScheduledExecutorService==null){
            mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        }

        doStartTimer();

        return START_STICKY;
    }

    public void doStartTimer() {
        if (!isStartedTimer) {
            LocationTracking mLocationTracking = new LocationTracking(TrackingService2.this);
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
                LocationTracking mLocationTracking = new LocationTracking(TrackingService2.this);
                mLocationTracking.doStartTimer();
            }
        };
    }


    @Override
    public void onDestroy() {

        AppLog.d( "onDestroy >>>");
        doStopTimer();
        this.unregisterReceiver(this.startTimerReceiver);
        Intent broadcastIntent = new Intent();
        Context c = null;
        try {
            c = createPackageContext("app.mmguardian.com.location_tracking.debug", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "onDestroy >>>" + e.toString());
        }

        broadcastIntent.setClassName(c, "app.mmguardian.com.location_tracking.receiver.SensorRestarterBroadcastReceiver");
        broadcastIntent.setAction("app.mmguardian.com.location_tracking.RestartSensor");
        broadcastIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(broadcastIntent);





        super.onDestroy();
    }
}
