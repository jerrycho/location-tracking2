package app.mmguardian.com.location_tracking.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import app.mmguardian.com.location_tracking.log.AppLog;
import app.mmguardian.com.location_tracking.utils.AlarmUtil;

public class SensorRestarterBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AppLog.d("SensorRestarterBroadcastReceiver>>>");
        AlarmUtil.setAlarmTime4(context);
    }
}
