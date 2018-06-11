package app.mmguardian.com.location_tracking.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import app.mmguardian.com.Constants;


public class TrackingService extends Service {

    public final static String TAG = "location_tracking";

    ScheduledExecutorService mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();


    private Timer mTimer;
    private TimerTask mTimerTask;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "[TrackingService] onStartCommand");

        if (mScheduledExecutorService==null){
            mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        }

        doStartTimer();

        return START_STICKY;
    }

    public void doStartTimer() {
        mTimer = new Timer();
        initTimerTask();
        mTimer.schedule(mTimerTask, Constants.SCHEDULER_TIME_SEC * 1000, Constants.SCHEDULER_TIME_SEC * 1000);
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
                Log.d(TAG, "[TrackingService] run()");
            }
        };
    }
}
