package app.mmguardian.com.location_tracking.utils;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import app.mmguardian.com.Constants;
import app.mmguardian.com.location_tracking.LocationTrackingApplication;
import app.mmguardian.com.location_tracking.bus.NewLocationTrackingRecordEvent;
import app.mmguardian.com.location_tracking.bus.RemainTimeEvent;
import app.mmguardian.com.location_tracking.db.model.LocationRecord;


public class LocationTracking {

    public final static String TAG = "location_tracking";

    Context context;

    Timer mTimer;

    int mInterval;

    Geocoder mGeocoder;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    public LocationTracking(Context context) {
        this.context = context;
        mTimer = new Timer();
        mInterval = Constants.SCHEDULER_TIME_SEC;
        mGeocoder = new Geocoder(context);
    }

    public void doStartTimer(){
        new AsyncGetCurrentLocationTaskRunner().execute("");
        mTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                EventBus.getDefault().post(new RemainTimeEvent(setInterval()));
            }
        }, 1000, 1000);
    }


    private int setInterval() {
        if (mInterval == 1) {
            //doGetCurrentLocaiton();
            if (mTimer!=null) {
                mTimer.cancel();
                mTimer = null;
            }
        }
        return mInterval--;
    }

    public void doGetCurrentLocaiton() {
        Log.d(TAG, "doGetCurrentLocaiton0 :" );
        if (mFusedLocationProviderClient == null) {
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        new AsyncInsertDBTaskRunner().execute(location);
                    }
                });
    }

    private class AsyncInsertDBTaskRunner extends AsyncTask<Location, Void, Void> {
        @Override
        protected Void doInBackground(Location... params) {
            Location location = params[0];
            String address = "";
            List<Address> addresses = null;

            try {
                addresses = mGeocoder.getFromLocation(
                        location.getLatitude(),
                        location.getLongitude(),
                        // In this sample, get just a single address.
                        1);
            } catch (IOException ioException){
                address = "Network service not available";
            } catch (IllegalArgumentException illegalArgumentException) {
                address = "Invalid Latitude & Longitude";
            }

            if (addresses == null || addresses.size() ==0){
                if (address.isEmpty()){
                    address = "No address found";
                }
            } else {
                Address address1 = addresses.get(0);
                ArrayList<String> alAddress = new ArrayList<String>();
                for(int i = 0; i <= address1.getMaxAddressLineIndex(); i++) {
                    alAddress.add(address1.getAddressLine(i));
                }
                address = TextUtils.join(System.getProperty("line.separator"), alAddress);
            }

            long beforeDate = Calendar.getInstance().getTimeInMillis();
            beforeDate -= Constants.RECORD_KEEP;
            LocationTrackingApplication.getInstance().getLocationDatabase().locationRecordDao().deleteBeforeDate(beforeDate - Constants.RECORD_KEEP);
            LocationRecord mLocationRecord = new LocationRecord(location, address);
            LocationTrackingApplication.getInstance().getLocationDatabase().locationRecordDao().insertLocationRecord(mLocationRecord);
            EventBus.getDefault().post(new NewLocationTrackingRecordEvent(mLocationRecord, beforeDate));
            return null;
        }

    }

    private class AsyncGetCurrentLocationTaskRunner extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            return 0;
        }

        @Override
        protected void onPostExecute(Integer size) {
            doGetCurrentLocaiton();
        }
    }
}