package app.mmguardian.com.location_tracking.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import app.mmguardian.com.location_tracking.MainActivity;
import app.mmguardian.com.location_tracking.service.LocationJobIntentService;
import app.mmguardian.com.location_tracking.utils.Util;

public class BootReceiver extends BroadcastReceiver{

    public static final String TAG = "location_tracking";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onBootReceived >>>> ");

        //Util.startService(context);
        LocationJobIntentService.enqueueWork(context, new Intent());
    }
}
