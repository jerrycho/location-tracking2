package app.mmguardian.com.location_tracking.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import app.mmguardian.com.location_tracking.log.AppLog;
import app.mmguardian.com.location_tracking.service.TrackingService;

import static android.os.Build.*;

/**
 * The Receiver, when receive message, to restart the TrackingService
 *
 * @author  Jerry Cho
 * @version 1.0
 */
public class ServiceRequestRestartBroadcastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        //start the service
        AppLog.d("onReceive ServiceRequestRestartBroadcastReceiver");
        Intent i = new Intent(context,TrackingService.class);

        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            context.startForegroundService(i);
        } else {
            context.startService(i);
        }

    }
}
