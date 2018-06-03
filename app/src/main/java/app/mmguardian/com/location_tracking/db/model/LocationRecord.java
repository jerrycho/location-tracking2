package app.mmguardian.com.location_tracking.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.location.Location;


import java.util.Date;

@Entity(indices = {@Index(value = "id", unique = true)})
public class LocationRecord {


    @PrimaryKey(autoGenerate = true)
    public int id;

    @Ignore
    public Location mLocation;

    public boolean isNull = true;
    public Long date;
    public double latitude = 0.0f;
    public double longitude = 0.0f;
    public double altitude = 0.0f;
    public String address;

    public LocationRecord() {

    }

    public LocationRecord(Location location, String address) {
        this.date = Calendar.getInstance().getTimeInMillis();
        this.address = address;
        this.mLocation = location;
        if (location != null) {
            this.isNull =false;
            this.altitude = location.getAltitude();
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
        }
    }
}
