package app.mmguardian.com.location_tracking.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import app.mmguardian.com.location_tracking.service.LocationJobIntentService;

public class BootReceiver extends BroadcastReceiver{

    public static final String TAG = "location_tracking";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onBootReceived >>>> ");
        LocationJobIntentService.enqueueWork(context, new Intent());
    }

}
