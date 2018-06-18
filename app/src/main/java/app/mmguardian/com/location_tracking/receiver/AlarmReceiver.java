package app.mmguardian.com.location_tracking.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import app.mmguardian.com.location_tracking.log.AppLog;
import app.mmguardian.com.location_tracking.utils.AlarmUtil;
import app.mmguardian.com.location_tracking.utils.Util;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AppLog.d("AlarmReceiver do something");
        Util.getCurrentLocaiton(context);
        //AlarmUtil.setAlarmTime(context); //work
        AlarmUtil.setAlarmTime4(context);
    }


}
