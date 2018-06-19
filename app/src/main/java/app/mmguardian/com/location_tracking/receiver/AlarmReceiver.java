package app.mmguardian.com.location_tracking.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import app.mmguardian.com.location_tracking.BuildConfig;
import app.mmguardian.com.location_tracking.log.AppLog;
import app.mmguardian.com.location_tracking.task.AsyncInsertDBTaskRunner;
import app.mmguardian.com.location_tracking.utils.AlarmUtil;
import app.mmguardian.com.location_tracking.utils.PreferenceManager;
import app.mmguardian.com.location_tracking.utils.Util;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String EXTRA_CALENDAR = "EXTRA_CALENDAR";

    @Override
    public void onReceive(Context context, Intent intent) {

        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();

        AppLog.d("AlarmReceiver do something >> "+ sdFormat.format(c.getTime()));

        long currentDatetime = c.getTimeInMillis();
        PreferenceManager mPreferenceManager = new PreferenceManager(context);
        mPreferenceManager.setLongPref("LAST_INSERT_DATE", currentDatetime);

        //at this time, event bus to mainactivty start the count down counter!

        AlarmUtil.setAlarmTime4(context);

        Util.getCurrentLocaiton(Calendar.getInstance(), context);
    }

//    @Override
//    public void onReceive(Context context, Intent intent) {
//        AppLog.d("AlarmReceiver do something");
//
//        //AlarmUtil.setAlarmTime(context); //work
//        AlarmUtil.setAlarmTime4(context);
//    }
}
