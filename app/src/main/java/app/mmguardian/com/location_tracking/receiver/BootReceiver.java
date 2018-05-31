package app.mmguardian.com.location_tracking.receiver;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import app.mmguardian.com.location_tracking.MainActivity;
import app.mmguardian.com.location_tracking.service.LocationJobIntentService;
import app.mmguardian.com.location_tracking.service.SensorService;
import app.mmguardian.com.location_tracking.utils.Util;

public class BootReceiver extends BroadcastReceiver{

    public static final String TAG = "location_tracking";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onBootReceived >>>> ");
        //startServiceByAlarm(context);
        //Util.startService(context);
        LocationJobIntentService.enqueueWork(context, new Intent());

//        //后边的xxx.class就是要启动的服务
//        Intent service = new Intent(context,SensorService.class);
//        context.startService(service);
//
//        //启动应用，参数为需要自动启动的应用的包名
//        Intent i = context.getPackageManager().getLaunchIntentForPackage("app.mmguardian.com.location_tracking.debug");
//        context.startActivity(i);
    }

    private void startServiceByAlarm(Context context)
    {
        // Get alarm manager.
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        // Create intent to invoke the background service.
        Intent intent = new Intent(context, SensorService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long startTime = System.currentTimeMillis();
        long intervalTime = 60*1000;

        String message = "Start service use repeat alarm. ";


        // Create repeat alarm.
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime, intervalTime, pendingIntent);
    }

}
