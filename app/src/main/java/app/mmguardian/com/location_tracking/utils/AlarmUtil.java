package app.mmguardian.com.location_tracking.utils;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import java.util.Calendar;

import app.mmguardian.com.location_tracking.MainActivity;
import app.mmguardian.com.location_tracking.MainActivity2;
import app.mmguardian.com.location_tracking.log.AppLog;
import app.mmguardian.com.location_tracking.receiver.AlarmReceiver;

public class AlarmUtil {
/*
        Intent broadcastIntent = new Intent();
        Context c = null;
        try {
            c = createPackageContext("app.mmguardian.com.location_tracking", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "onDestroy >>>" + e.toString());
        }

        broadcastIntent.setClassName(c, "app.mmguardian.com.location_tracking.receiver.ServiceRequestRestartBroadcastReceiver");
        broadcastIntent.setAction("app.mmguardian.com.location_tracking.ServiceRequestRestart");
        broadcastIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);


 */
    public static void setAlarmTime4(Context context){
        AppLog.d("AlarmUtil.setAlarmTime4(context);");

        //Util.getCurrentLocaiton(context);

        Intent _intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, _intent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        // Remove any previous pending intent.
        try {
            alarmManager.cancel(pendingIntent);
        } catch (Exception e){
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, 10);
        long after = c.getTimeInMillis();

        alarmManager.set(AlarmManager.RTC_WAKEUP, after , pendingIntent);
    }


}
