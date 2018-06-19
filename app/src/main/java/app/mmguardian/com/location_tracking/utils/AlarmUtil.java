package app.mmguardian.com.location_tracking.utils;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import app.mmguardian.com.Constants;
import app.mmguardian.com.location_tracking.MainActivity;
import app.mmguardian.com.location_tracking.MainActivity2;
import app.mmguardian.com.location_tracking.log.AppLog;
import app.mmguardian.com.location_tracking.receiver.AlarmReceiver;

public class AlarmUtil {

//    public static void setAlarmTime4(Context context){
//        AppLog.d("AlarmUtil.setAlarmTime4(context);");
//
//        //Util.getCurrentLocaiton(context);
//
//        Intent _intent = new Intent(context, AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, _intent, 0);
//        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//        // Remove any previous pending intent.
//        try {
//            alarmManager.cancel(pendingIntent);
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//
//        Calendar c = Calendar.getInstance();
//        c.add(Calendar.SECOND, 10);
//        long after = c.getTimeInMillis();
//
//        alarmManager.set(AlarmManager.RTC_WAKEUP, after , pendingIntent);
//    }

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

//        Calendar c = Calendar.getInstance();
//        int diffSec = Util.getLastInsertWithCurrentDiffSec(c, context);
//
//        if (diffSec > 0){
//            c.add(Calendar.SECOND, diffSec);
//        }

        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lastInsertDate = new PreferenceManager(context).getLongPref("LAST_INSERT_DATE");
        Calendar lastDate = Calendar.getInstance();
        lastDate.setTimeInMillis(lastInsertDate);
        Calendar c = Calendar.getInstance();


        AppLog.d("lastDate >>> "+ sdFormat.format(lastDate.getTime()) + "| todate is "+sdFormat.format(c.getTime()));

        long after = 0;
        if (lastInsertDate == 0){
            c.add(Calendar.SECOND, 5);
            after = c.getTimeInMillis();
        }
        else {
            int diffSec = (int) ((c.getTimeInMillis() - lastInsertDate) / 1000);
            diffSec = ((int) Constants.SCHEDULER_TIME_SEC) - diffSec;
            if (diffSec > 0){
                c.add(Calendar.SECOND, diffSec);
            }else {
                c.add(Calendar.SECOND, 5);
            }
            after = c.getTimeInMillis();
        }

        AppLog.d("lastDate >>> next time should : "+ sdFormat.format(c.getTime()) );

        alarmManager.set(AlarmManager.RTC_WAKEUP, after , pendingIntent);
    }


}
