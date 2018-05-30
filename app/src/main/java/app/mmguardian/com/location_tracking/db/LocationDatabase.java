package app.mmguardian.com.location_tracking.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import app.mmguardian.com.location_tracking.db.dao.LocationRecordDao;
import app.mmguardian.com.location_tracking.db.dao.UserDao;
import app.mmguardian.com.location_tracking.db.model.LocationRecord;
import app.mmguardian.com.location_tracking.db.model.User;

@Database(entities = {User.class, LocationRecord.class}, version = 1)
public abstract class LocationDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract LocationRecordDao locationRecordDao();
}
