package app.mmguardian.com.location_tracking.task;

import android.content.Context;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.mmguardian.com.Constants;
import app.mmguardian.com.location_tracking.LocationTrackingApplication;
import app.mmguardian.com.location_tracking.bus.NewLocationTrackingRecordEvent;
import app.mmguardian.com.location_tracking.db.model.LocationRecord;

import app.mmguardian.com.location_tracking.log.AppLog;
import app.mmguardian.com.location_tracking.utils.PreferenceManager;

public class AsyncInsertDBTaskRunner extends AsyncTask<Location, Void, Void> {

    Geocoder mGeocoder;

    private Context mContext;
    private PreferenceManager mPreferenceManager;

    public AsyncInsertDBTaskRunner(Context context) {
        this.mContext = context;
    }

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
                mGeocoder = new Geocoder(mContext);
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
            mPreferenceManager = new PreferenceManager(mContext);
        }
        mPreferenceManager.setLongPref("LAST_INSERT_DATE", currentDatetime);
        long beforeDate = currentDatetime - Constants.RECORD_KEEP;
        LocationTrackingApplication.getInstance().getLocationDatabase().locationRecordDao().deleteBeforeDate(beforeDate - Constants.RECORD_KEEP);
        LocationRecord mLocationRecord = new LocationRecord(currentDatetime, location, address);
        AppLog.d("getted Location >>" + mLocationRecord.toString());

        LocationTrackingApplication.getInstance().getLocationDatabase().locationRecordDao().insertLocationRecord(mLocationRecord);
        EventBus.getDefault().post(new NewLocationTrackingRecordEvent(mLocationRecord, beforeDate));

        return null;
    }
}
