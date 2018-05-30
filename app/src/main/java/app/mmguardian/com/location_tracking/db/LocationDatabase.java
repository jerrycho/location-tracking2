package app.mmguardian.com.location_tracking.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.location.Location;

import app.mmguardian.com.location_tracking.db.dao.LocationRecordDao;

//http://androidkt.com/room-persistence-library/
@Database(entities = {Location.class}, version = 1)
public abstract class LocationDatabase extends RoomDatabase {
    public LocationRecordDao locationRecordDao;
}
