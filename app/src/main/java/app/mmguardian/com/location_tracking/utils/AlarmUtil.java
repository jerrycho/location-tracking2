package app.mmguardian.com.location_tracking.utils;


import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import app.mmguardian.com.Constants;


import app.mmguardian.com.location_tracking.bus.RemainTimeEvent;
import app.mmguardian.com.location_tracking.bus.StartDownTimerEvent;
import app.mmguardian.com.location_tracking.log.AppLog;
import app.mmguardian.com.location_tracking.receiver.AlarmReceiver;
import app.mmguardian.com.location_tracking.service.CountDownTimerIntentService;

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

        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lastInsertDate = new PreferenceManager(context).getLongPref("LAST_INSERT_DATE");
        Calendar lastDate = Calendar.getInstance();
        lastDate.setTimeInMillis(lastInsertDate);
        Calendar c = Calendar.getInstance();


        AppLog.d("lastDate >>> "+ sdFormat.format(lastDate.getTime()) + "| todate is "+sdFormat.format(c.getTime()));

        long after = 0;
        if (lastInsertDate == 0){
            sendToCountDownTimerIntentService(context, 5);
            c.add(Calendar.SECOND, 5);
            after = c.getTimeInMillis();
        }
        else {
            int diffSec = (int) ((c.getTimeInMillis() - lastInsertDate) / 1000);
            diffSec = ((int) Constants.SCHEDULER_TIME_SEC) - diffSec;
            if (diffSec > 0){
                sendToCountDownTimerIntentService(context, diffSec);
                c.add(Calendar.SECOND, diffSec);
            }else {
                sendToCountDownTimerIntentService(context, 5);
                c.add(Calendar.SECOND, 5);
            }
            after = c.getTimeInMillis();
        }

        AppLog.d("lastDate >>> next time should : "+ sdFormat.format(c.getTime()) );

        //alarmManager.set(AlarmManager.RTC_WAKEUP, after , pendingIntent);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, after, pendingIntent);
    }

    private static void sendToCountDownTimerIntentService(Context context, int remainSec){
//        Intent i = new Intent(context, CountDownTimerIntentService.class);
//        i.putExtra(CountDownTimerIntentService.EXTRA_REMAIN, remainSec);
//        context.startService(i);
        EventBus.getDefault().post(new StartDownTimerEvent(remainSec));
    }


}
