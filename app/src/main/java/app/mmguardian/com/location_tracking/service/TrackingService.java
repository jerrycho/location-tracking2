package app.mmguardian.com.location_tracking.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import app.mmguardian.com.Constants;
import app.mmguardian.com.location_tracking.bus.ServiceEventConnectedEvent;
import app.mmguardian.com.location_tracking.utils.LocationTracking;


public class TrackingService extends Service {

    public final static String TAG = "location_tracking";

    ScheduledExecutorService mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private Timer mTimer;
    private TimerTask mTimerTask;

    private boolean isStartedTimer = false;

    IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public TrackingService getServerInstance() {
            return TrackingService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "[TrackingService] onStartCommand");

        if (mScheduledExecutorService==null){
            mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        }

        return START_STICKY;
    }

    public void doStartTimer() {
        if (!isStartedTimer) {
            EventBus.getDefault().post(new ServiceEventConnectedEvent());
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
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        Intent broadcastIntent = new Intent("app.mmguardian.com.location_tracking.RestartSensor");
        sendBroadcast(broadcastIntent);
        doStopTimer();
    }
}
