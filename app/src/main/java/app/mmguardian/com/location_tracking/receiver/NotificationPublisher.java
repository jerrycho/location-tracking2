package app.mmguardian.com.location_tracking.receiver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import app.mmguardian.com.location_tracking.log.AppLog;

public class NotificationPublisher extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AppLog.d("NotificationPublisher>>>>");
    }
}
