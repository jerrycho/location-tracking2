package app.mmguardian.com.location_tracking.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import android.location.Location;


import java.util.Calendar;

/**
 * Room DB class of location, stored a locaiton information
 *
 * @author  Jerry Cho
 * @version 1.0
 */
@Entity(indices = {@Index(value = "id", unique = true)})
public class LocationRecord {


    @PrimaryKey(autoGenerate = true)
    public long id;

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

    public LocationRecord(Long currentDatetime, Location location, String address) {
        this.date = currentDatetime;
        this.address = address;
        this.mLocation = location;
        if (location != null) {
            this.isNull =false;
            this.altitude = location.getAltitude();
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
        }
    }

    public LocationRecord(boolean isNull, double altitude, double latitude,
                          double longitude, String address) {
        this.date = Calendar.getInstance().getTimeInMillis();
        this.address = address;
        this.mLocation = null;

        this.isNull = false;
        this.altitude = altitude;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    @Override
    public String toString() {
        return "LocationRecord{" +
                "id=" + id +
                ", mLocation=" + mLocation +
                ", isNull=" + isNull +
                ", date=" + date +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                ", address='" + address + '\'' +
                '}';
    }
}
