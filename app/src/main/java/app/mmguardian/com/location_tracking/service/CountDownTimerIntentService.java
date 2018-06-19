package app.mmguardian.com.location_tracking.service;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import app.mmguardian.com.Constants;
import app.mmguardian.com.location_tracking.LocationTrackingApplication;
import app.mmguardian.com.location_tracking.bus.NewLocationTrackingRecordEvent;
import app.mmguardian.com.location_tracking.bus.RemainTimeEvent;
import app.mmguardian.com.location_tracking.db.model.LocationRecord;
import app.mmguardian.com.location_tracking.log.AppLog;
import app.mmguardian.com.location_tracking.utils.PreferenceManager;
import app.mmguardian.com.location_tracking.utils.Util;


public class CountDownTimerIntentService extends IntentService {

    public static final String EXTRA_REMAIN = "EXTRA_REMAIN";

    app.mmguardian.com.location_tracking.utils.CountDownTimer mRXTimer;

    public CountDownTimerIntentService() {
        super("CountDownTimerIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppLog.d("CountDownTimerIntentService >>> onHandleIntent" );
        int remain = intent.getExtras().getInt(EXTRA_REMAIN);
        mRXTimer =  new app.mmguardian.com.location_tracking.utils.CountDownTimer(Long.valueOf(remain), TimeUnit.SECONDS) {

            @Override
            public void onTick(long tickValue) {
                AppLog.d("RX My Intent Service Timer >> on Remain "+ tickValue);
                EventBus.getDefault().post(new RemainTimeEvent( (int)tickValue ));
            }

            @Override
            public void onFinish() {
                AppLog.d("RX Timer >> on Finish");
            }

            @Override
            public void onError(Throwable e) {
                AppLog.d("RX Timer >> on Error>>"+ e.toString());
            }
        };
        mRXTimer.start();

        try {
            Thread.sleep(remain * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
