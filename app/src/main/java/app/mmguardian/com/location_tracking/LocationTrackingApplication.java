package app.mmguardian.com.location_tracking;

import android.app.Application;
import android.arch.persistence.room.Room;

import app.mmguardian.com.location_tracking.db.LocationDatabase;

public class LocationTrackingApplication extends Application {

    private static LocationTrackingApplication instance;
    LocationDatabase mLocationDatabase;

    public static LocationTrackingApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mLocationDatabase =
                Room.databaseBuilder(getApplicationContext(), LocationDatabase.class, "location.db").build();

    }

    public LocationDatabase getLocationDatabase() {
        return mLocationDatabase;
    }
}
