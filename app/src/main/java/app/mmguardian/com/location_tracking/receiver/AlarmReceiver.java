package app.mmguardian.com.location_tracking.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import app.mmguardian.com.location_tracking.log.AppLog;
import app.mmguardian.com.location_tracking.utils.AlarmUtil;
import app.mmguardian.com.location_tracking.utils.PreferenceManager;
import app.mmguardian.com.location_tracking.utils.Util;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    public static final String EXTRA_CALENDAR = "EXTRA_CALENDAR";

    @Override
    public void onReceive(Context context, Intent intent) {

        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MILLISECOND, 0);

        AppLog.d("AlarmReceiver do something >> "+ sdFormat.format(c.getTime()));

        long currentDatetime = c.getTimeInMillis();
        PreferenceManager mPreferenceManager = new PreferenceManager(context);
        mPreferenceManager.setLongPref("LAST_INSERT_DATE", currentDatetime);

        //at this time, event bus to mainactivty start the count down counter!

        AlarmUtil.setAlarmTime4(context);

        Util.getCurrentLocaiton(c, context);

    }

//    @Override
//    public void onReceive(Context context, Intent intent) {
//        AppLog.d("AlarmReceiver do something");
//
//        //AlarmUtil.setAlarmTime(context); //work
//        AlarmUtil.setAlarmTime4(context);
//    }
}
