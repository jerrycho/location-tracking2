package app.mmguardian.com.location_tracking.utils;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import app.mmguardian.com.Constants;
import app.mmguardian.com.location_tracking.LocationTrackingApplication;
import app.mmguardian.com.location_tracking.bus.NewLocationTrackingRecordEvent;
import app.mmguardian.com.location_tracking.bus.RemainTimeEvent;
import app.mmguardian.com.location_tracking.db.model.LocationRecord;
import app.mmguardian.com.location_tracking.service.LocationJobIntentService;


public class LocationTracking2 {

    public final static String TAG = "location_tracking";

    Context context;

    Timer mTimer;

    int mInterval;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    public LocationTracking2(Context context) {
        this.context = context;
        mTimer = new Timer();
        mInterval = Constants.SCHEDULER_TIME_SEC;
    }

    public void doStartTimer(){
        mTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                EventBus.getDefault().post(new RemainTimeEvent(setInterval()));
            }
        }, 1000, 1000);
    }


    private int setInterval() {
        if (mInterval == 0) {
            doGetCurrentLocaiton();
            if (mTimer!=null) {
                mTimer.cancel();
                mTimer = null;
            }
        }
        return mInterval--;
    }

    private void doGetCurrentLocaiton() {
        Log.d(TAG, "doGetCurrentLocaiton0 :" );
        if (mFusedLocationProviderClient == null) {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
