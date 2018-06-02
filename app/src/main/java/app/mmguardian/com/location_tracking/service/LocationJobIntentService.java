package app.mmguardian.com.location_tracking.service;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import app.mmguardian.com.location_tracking.utils.LocationTracking;
import app.mmguardian.com.location_tracking.utils.Util;

public class LocationJobIntentService extends JobIntentService {

    public static final String TAG = "location_tracking";

    public static final int JOB_ID = 0x01;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, LocationJobIntentService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "onHandleWork>>>");
        //Util.startService(this);
        LocationTracking mLocationTracking = new LocationTracking(this);
        mLocationTracking.doStartTimer();

        try {
            Thread.sleep(60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mLocationTracking.doStopTimer();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "on LocationJobIntentService Destory");
        LocationJobIntentService.enqueueWork(this, new Intent());
        super.onDestroy();
    }
}