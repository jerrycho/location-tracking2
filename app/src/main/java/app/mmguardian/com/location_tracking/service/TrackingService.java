package app.mmguardian.com.location_tracking.service;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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

/**
 * The Android service, when started service, service will auto loop by period to get the current
 * location
 *
 * @author  Jerry Cho
 * @version 1.0
 */
public class TrackingService extends Service {

    public final static String TAG = "location_tracking";

    private boolean isTimerRunning = false;
    PreferenceManager mPreferenceManager;

    CountDownTimer mMainCountDownByConstantTimer;
    CountDownTimer mOneTimeOnlyCountDownTimer;
    Geocoder mGeocoder;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    app.mmguardian.com.location_tracking.utils.CountDownTimer mRXTimer;
    app.mmguardian.com.location_tracking.utils.CountDownTimer mdiffRXTimer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(1,new Notification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        AppLog.d("[TrackingService] onStartCommand");
        //checkLastRecordWithRxTimer();
//        Intent i = new Intent(this, MyIntentService.class);
//        startService(i);
        return START_NOT_STICKY;
    }

    public void startRXTimer(){
        doGetCurrentLocaiton();
        mRXTimer =  new app.mmguardian.com.location_tracking.utils.CountDownTimer(Long.valueOf(Constants.SCHEDULER_TIME_SEC), TimeUnit.SECONDS) {

            @Override
            public void onTick(long tickValue) {
                AppLog.d("RX Timer >> on Remain "+ tickValue);
                EventBus.getDefault().post(new RemainTimeEvent( (int)tickValue ));
            }

            @Override
            public void onFinish() {
                startRXTimer();
                AppLog.d("RX Timer >> on Finish");
            }

            @Override
            public void onError(Throwable e) {
                AppLog.d("RX Timer >> on Error>>"+ e.toString());
            }
        };
        mRXTimer.start();
    }

    public void checkLastRecordWithRxTimer(){
        if (mRXTimer==null || !mRXTimer.isRunning()){
            if (mPreferenceManager == null)
                mPreferenceManager = new PreferenceManager(this);

            long lastInsertDate = mPreferenceManager.getLongPref("LAST_INSERT_DATE");
            AppLog.d("lastInsertDate>>>" + new Date(lastInsertDate));
            if (lastInsertDate == 0) {
                AppLog.d("lastInsertDate is == 0");
                startRXTimer();
            } else {
                if (mdiffRXTimer!=null){
                    mdiffRXTimer.cancel();
                }
                int diffSec = (int) ((java.util.Calendar.getInstance().getTime().getTime() - lastInsertDate) / 1000);
                AppLog.d("now :" + java.util.Calendar.getInstance().getTime());
                AppLog.d("diffSec before :" +diffSec);
                diffSec = ((int)Constants.SCHEDULER_TIME_SEC) - diffSec;
                AppLog.d("diffSec after :" +diffSec);
                if (diffSec > 0) {
                    mdiffRXTimer =  new app.mmguardian.com.location_tracking.utils.CountDownTimer( (long)diffSec, TimeUnit.SECONDS) {

                        @Override
                        public void onTick(long tickValue) {
                            AppLog.d("RX Timer >> on Remain "+ tickValue);
                            EventBus.getDefault().post(new RemainTimeEvent( (int)tickValue ));
                        }

                        @Override
                        public void onFinish() {
                            startRXTimer();
                            AppLog.d("RX Timer >> on Finish");
                        }

                        @Override
                        public void onError(Throwable e) {
                            AppLog.d("RX Timer >> on Error>>"+ e.toString());
                        }
                    };
                    mdiffRXTimer.start();
                } else {
                    startRXTimer();
                }
            }

        }
    }

    public void checkLastRecord(){
        if (!isTimerRunning) {

            if (mPreferenceManager == null)
                mPreferenceManager = new PreferenceManager(this);

            long lastInsertDate = mPreferenceManager.getLongPref("LAST_INSERT_DATE");
            AppLog.d("lastInsertDate>>>" + new Date(lastInsertDate));
            if (lastInsertDate == 0) {
                AppLog.d("lastInsertDate is == 0");
                doStartMainCountDownTimer();
            } else {

                int diffSec = (int) ((java.util.Calendar.getInstance().getTime().getTime() - lastInsertDate) / 1000);
                AppLog.d("now :" + java.util.Calendar.getInstance().getTime());
                AppLog.d("diffSec before :" +diffSec);
                diffSec = ((int)Constants.SCHEDULER_TIME_SEC) - diffSec;
                AppLog.d("diffSec after :" +diffSec);
                if (diffSec > 0) {
                    mOneTimeOnlyCountDownTimer = new CountDownTimer(diffSec * 1000 , 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            AppLog.d("[mOneTimeOnlyCountDownTimer] onTick >>" + Math.round(millisUntilFinished * 0.001f));
                            EventBus.getDefault().post(new RemainTimeEvent( Math.round(millisUntilFinished * 0.001f) ));
                        }

                        @Override
                        public void onFinish() {
                            doStartMainCountDownTimer();
                        }
                    }.start();
                } else {
                    doStartMainCountDownTimer();
                }
            }
        }
    }

    public void doStartMainCountDownTimer() {
        doGetCurrentLocaiton();
        mMainCountDownByConstantTimer = new CountDownTimer(Constants.SCHEDULER_TIME_SEC * 1000 , 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                isTimerRunning = true;
                AppLog.d("[mMainCountDownByConstantTimer] onTick >>" + Math.round(millisUntilFinished * 0.001f));
                EventBus.getDefault().post(new RemainTimeEvent( Math.round(millisUntilFinished * 0.001f) ));
            }

            @Override
            public void onFinish() {
                doGetCurrentLocaiton();
                isTimerRunning = false;
                mMainCountDownByConstantTimer.start();
            }
        }.start();

    }

    public void doStopMainCountDownTimer(){
        if (mMainCountDownByConstantTimer!=null){
            mMainCountDownByConstantTimer.cancel();
            mMainCountDownByConstantTimer = null;
            isTimerRunning = false;
        }
    }

    @Override
    public void onDestroy() {
        AppLog.d( "onDestroy >>>");
        if (mRXTimer!=null){
            mRXTimer.cancel();
        }
        doStopMainCountDownTimer();
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
        sendBroadcast(broadcastIntent);
        super.onDestroy();
    }

    /**
     * This method is used to get current location information
     */
    public void doGetCurrentLocaiton() {
        AppLog.d( "doGetCurrentLocaiton0 :" );
        if (mFusedLocationProviderClient == null) {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (Util.isNetworkingConnected()) {
            mFusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            new AsyncInsertDBTaskRunner().execute(location);
                        }
                    });
        }
        else {
            new AsyncInsertDBTaskRunner().execute((Location) null);
        }
    }

    /**
     * This method is used to a insert a new location record into DB
     */
    private class AsyncInsertDBTaskRunner extends AsyncTask<Location, Void, Void> {
        @Override
        protected Void doInBackground(Location... params) {

            Location location = params[0];

            String address = "";
            List<Address> addresses = null;


            if (location==null){
                address = "Network service not available";
            }
            else {
                if (mGeocoder == null) {
                    mGeocoder = new Geocoder(TrackingService.this);
                }

                try {
                    addresses = mGeocoder.getFromLocation(
                            location.getLatitude(),
                            location.getLongitude(),
                            // In this sample, get just a single address.
                            1);
                } catch (IOException ioException) {
                    address = "Network service not available";
                } catch (IllegalArgumentException illegalArgumentException) {
                    address = "Invalid Latitude & Longitude";
                } catch (Exception exception) {
                    address = "Null Latitude & Longitude";
                }
            }

            if (addresses == null || addresses.size() == 0) {
                if (address.isEmpty()) {
                    address = "No address found";
                }
            } else {
                Address address1 = addresses.get(0);
                ArrayList<String> alAddress = new ArrayList<String>();
                for (int i = 0; i <= address1.getMaxAddressLineIndex(); i++) {
                    alAddress.add(address1.getAddressLine(i));
                }
                address = TextUtils.join(System.getProperty("line.separator"), alAddress);
            }

            long currentDatetime = Calendar.getInstance().getTimeInMillis();
            if (mPreferenceManager == null) {
                mPreferenceManager = new PreferenceManager(TrackingService.this);
            }
            mPreferenceManager.setLongPref("LAST_INSERT_DATE", currentDatetime);
            long beforeDate = currentDatetime - Constants.RECORD_KEEP;
            LocationTrackingApplication.getInstance().getLocationDatabase().locationRecordDao().deleteBeforeDate(beforeDate - Constants.RECORD_KEEP);
            LocationRecord mLocationRecord = new LocationRecord(currentDatetime, location, address);
            LocationTrackingApplication.getInstance().getLocationDatabase().locationRecordDao().insertLocationRecord(mLocationRecord);
            EventBus.getDefault().post(new NewLocationTrackingRecordEvent(mLocationRecord, beforeDate));

            return null;
        }

    }

}