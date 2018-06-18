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


public class MyIntentService extends IntentService {

    Geocoder mGeocoder;

    app.mmguardian.com.location_tracking.utils.CountDownTimer mRXTimer;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    PreferenceManager mPreferenceManager;

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppLog.d("MyIntentService >>> onHandleIntent" );

        doGetCurrentLocaiton();
        mRXTimer =  new app.mmguardian.com.location_tracking.utils.CountDownTimer(Constants.SCHEDULER_TIME_SEC, TimeUnit.SECONDS) {

            @Override
            public void onTick(long tickValue) {
                AppLog.d("RX My Intent Service Timer >> on Remain "+ tickValue);
                EventBus.getDefault().post(new RemainTimeEvent( (int)tickValue ));
            }

            @Override
            public void onFinish() {
                doGetCurrentLocaiton();
                AppLog.d("RX Timer >> on Finish");
            }

            @Override
            public void onError(Throwable e) {
                AppLog.d("RX Timer >> on Error>>"+ e.toString());
            }
        };
        mRXTimer.start();

        try {
            Thread.sleep(Constants.SCHEDULER_TIME_SEC * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onDestroy() {
        AppLog.d("MyIntentService >>> onDestroy" );

        super.onDestroy();
        Intent i = new Intent(this, MyIntentService.class);
        startService(i);
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
                    mGeocoder = new Geocoder(MyIntentService.this);
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
                mPreferenceManager = new PreferenceManager(MyIntentService.this);
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
