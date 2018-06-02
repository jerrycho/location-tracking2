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

    public Long date;
    public double mLatitude = 0.0f;
    public double mLongitude = 0.0f;
    public double mAltitude = 0.0f;

    public LocationRecord() {

    }

    public LocationRecord(Location location) {
        this.date = Calendar.getInstance().getTimeInMillis();
        this.mAltitude = location.getAltitude();
        this.mLatitude = location.getLatitude();
        this.mLongitude = location.getLongitude();
    }
}

//import android.arch.persistence.room.ColumnInfo;
//import android.arch.persistence.room.Entity;
//import android.arch.persistence.room.PrimaryKey;
//import android.location.Location;
//
//import java.util.Date;
//
//@Entity
//public class LocationRecord {
//
//    @PrimaryKey(autoGenerate = true)
//    public int uid;
//
//    @ColumnInfo
//    public Date date;
//
//    @ColumnInfo
//    public double mLatitude = 0.0;
//
//    @ColumnInfo
//    public double mLongitude = 0.0;
//
//    @ColumnInfo
//    public double mAltitude = 0.0f;
//
//    public LocationRecord(){}
//
//    public LocationRecord(Location location) {
//        this.date = new Date();
//        this.mAltitude = location.getAltitude();
//        this.mLatitude = location.getLatitude();
//        this.mLongitude = location.getLongitude();
//    }
//
//    public Date getDate() {
//        return date;
//    }
//
//    public void setDate(Date date) {
//        this.date = date;
//    }
//
//    public double getmLatitude() {
//        return mLatitude;
//    }
//
//    public void setmLatitude(double mLatitude) {
//        this.mLatitude = mLatitude;
//    }
//
//    public double getmLongitude() {
//        return mLongitude;
//    }
//
//    public void setmLongitude(double mLongitude) {
//        this.mLongitude = mLongitude;
//    }
//
//    public double getmAltitude() {
//        return mAltitude;
//    }
//
//    public void setmAltitude(double mAltitude) {
//        this.mAltitude = mAltitude;
//    }
//}
