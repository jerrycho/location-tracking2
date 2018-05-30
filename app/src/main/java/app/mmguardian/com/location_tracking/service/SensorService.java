package app.mmguardian.com.location_tracking.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import app.mmguardian.com.location_tracking.LocationTrackingApplication;
import app.mmguardian.com.location_tracking.bus.NewLocationTrackingRecordEvent;
import app.mmguardian.com.location_tracking.db.model.LocationRecord;

public class SensorService extends Service {

    public final static String TAG = SensorService.class.getSimpleName();

    private Timer mTimer;
    private TimerTask mTimerTask;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStartCommand");
        doStartTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        Intent broadcastIntent = new Intent("app.mmguardian.com.location_tracking.RestartSensor");
        sendBroadcast(broadcastIntent);
        doStopTimer();
    }

    public void doStartTimer() {
        mTimer = new Timer();
        initTimerTask();
        mTimer.schedule(mTimerTask, 0, app.mmguardian.com.Constants.SCHEDULER_TIME);
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
                        LocationTrackingApplication.getInstance().getLocationDatabase().locationRecordDao.insertLocationRecord(mLocationRecord);
                        EventBus.getDefault().post(new NewLocationTrackingRecordEvent(mLocationRecord));
                    }
                });
    }
}
