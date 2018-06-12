package app.mmguardian.com.location_tracking.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import app.mmguardian.com.location_tracking.log.AppLog;
import app.mmguardian.com.location_tracking.service.TrackingService;

import static android.os.Build.*;

public class SensorRestarterBroadcastReceiver extends BroadcastReceiver{

    public final static String TAG = "location_tracking";

    @Override
    public void onReceive(Context context, Intent intent) {
        //start the service
        AppLog.d("onReceive SensorRestarterBroadcastReceiver");
        Intent i = new Intent(context,TrackingService.class);

        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            context.startForegroundService(i);
        } else {
            context.startService(i);
        }

    }
}
