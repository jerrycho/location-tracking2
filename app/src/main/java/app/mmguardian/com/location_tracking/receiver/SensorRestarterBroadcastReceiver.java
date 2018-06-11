package app.mmguardian.com.location_tracking.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import app.mmguardian.com.location_tracking.service.TrackingService;
import app.mmguardian.com.location_tracking.service.TrackingService2;

public class SensorRestarterBroadcastReceiver extends BroadcastReceiver{

    public final static String TAG = "location_tracking";

    @Override
    public void onReceive(Context context, Intent intent) {
        //start the service
        Log.d(TAG,"onReceive SensorRestarterBroadcastReceiver");
        context.startService(new Intent(context, TrackingService2.class));
    }
}
