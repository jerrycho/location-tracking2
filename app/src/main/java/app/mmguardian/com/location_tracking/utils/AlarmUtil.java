package app.mmguardian.com.location_tracking.utils;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import app.mmguardian.com.Constants;


import app.mmguardian.com.location_tracking.bus.StartDownTimerEvent;
import app.mmguardian.com.location_tracking.log.AppLog;
import app.mmguardian.com.location_tracking.receiver.AlarmReceiver;
import app.mmguardian.com.location_tracking.receiver.SensorRestarterBroadcastReceiver;

public class AlarmUtil {

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


        long lastInsertDate = new PreferenceManager(context).getLongPref("LAST_INSERT_DATE");
        if (lastInsertDate == 0){
            Intent intent = new Intent(context, AlarmReceiver.class);
            context.sendBroadcast(intent);
        }
        //sendToCountDownTimerIntentService(context, diffSec);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, after , pendingIntent);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + (Constants.SCHEDULER_TIME_SEC * 1000), pendingIntent);
    }

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
//        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
//        long lastInsertDate = new PreferenceManager(context).getLongPref("LAST_INSERT_DATE");
//        Calendar lastDate = Calendar.getInstance();
//        lastDate.setTimeInMillis(lastInsertDate);
//        Calendar c = Calendar.getInstance();
//        c.set(Calendar.MILLISECOND, 0);
//
//
//        AppLog.d("lastDate >>> "+ sdFormat.format(lastDate.getTime()) + "| todate is "+sdFormat.format(c.getTime()));
//
//        long after = 0;
//        if (lastInsertDate == 0){
//            sendToCountDownTimerIntentService(context, 5);
//            c.add(Calendar.SECOND, 5);
//            after = c.getTimeInMillis();
//        }
//        else {
//            int diffSec = (int) ((c.getTimeInMillis() - lastInsertDate) / 1000);
//            diffSec = ((int) Constants.SCHEDULER_TIME_SEC) - diffSec;
//            if (diffSec > 0){
//                sendToCountDownTimerIntentService(context, diffSec);
//                c.add(Calendar.SECOND, diffSec);
//            }else {
//                sendToCountDownTimerIntentService(context, 5);
//                c.add(Calendar.SECOND, 5);
//            }
//            after = c.getTimeInMillis();
//        }
//
//        AppLog.d("lastDate >>> next time should : "+ sdFormat.format(c.getTime()) );
//
////        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, after , pendingIntent);
//        if (Build.VERSION.SDK_INT == 24 )
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, after, pendingIntent);
//        else
//            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, after, pendingIntent);
//    }

    private static void sendToCountDownTimerIntentService(Context context, int remainSec){
//        Intent i = new Intent(context, CountDownTimerIntentService.class);
//        i.putExtra(CountDownTimerIntentService.EXTRA_REMAIN, remainSec);
//        context.startService(i);
        EventBus.getDefault().post(new StartDownTimerEvent(remainSec));
    }


}
