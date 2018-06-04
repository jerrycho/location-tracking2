package app.mmguardian.com.location_tracking.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SensorRestarterBroadcastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        //start the service
        //context.startService(new Intent(context, SensorService.class));
    }
}
