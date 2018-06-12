package app.mmguardian.com.location_tracking.service;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import app.mmguardian.com.Constants;
import app.mmguardian.com.location_tracking.bus.ServiceEventConnectedEvent;
import app.mmguardian.com.location_tracking.utils.LocationTracking;

public class LocationJobIntentService extends JobIntentService {

    public static final String TAG = "location_tracking";
    public static final int JOB_ID = 0x01;

    public static void enqueueWork(Context context, Intent work) {
        app.mmguardian.com.location_tracking.log.AppLog.d("enqueueWork");
        enqueueWork(context, LocationJobIntentService.class, JOB_ID, work);
    }


    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        app.mmguardian.com.location_tracking.log.AppLog.d("onHandleWork>>>");
        EventBus.getDefault().post(new ServiceEventConnectedEvent());
        LocationTracking mLocationTracking = new LocationTracking(this);
        mLocationTracking.doStartTimer();

        try {
            Thread.sleep(Constants.SCHEDULER_TIME_SEC * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        app.mmguardian.com.location_tracking.log.AppLog.d("on LocationJobIntentService Destory");
        LocationJobIntentService.enqueueWork(this, new Intent());
        super.onDestroy();
    }

}