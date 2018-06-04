package app.mmguardian.com.location_tracking.service;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import app.mmguardian.com.Constants;
import app.mmguardian.com.location_tracking.bus.ServiceEventConnectedEvent;
import app.mmguardian.com.location_tracking.utils.LocationTracking;
import app.mmguardian.com.location_tracking.utils.LocationTracking2;
import app.mmguardian.com.location_tracking.utils.Util;

public class LocationJobIntentService extends JobIntentService {

    public static final String TAG = "location_tracking";
    public static final int JOB_ID = 0x01;

    public static void enqueueWork(Context context, Intent work) {
        Log.d(TAG, "enqueueWork");
        enqueueWork(context, LocationJobIntentService.class, JOB_ID, work);
    }


    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "onHandleWork>>>");
        EventBus.getDefault().post(new ServiceEventConnectedEvent());
        LocationTracking2 mLocationTracking2 = new LocationTracking2(this);
        mLocationTracking2.doStartTimer();

        try {
            Thread.sleep(Constants.SCHEDULER_TIME_SEC * 1000 + 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "on LocationJobIntentService Destory");
        LocationJobIntentService.enqueueWork(this, new Intent());
        super.onDestroy();
    }

}