package app.mmguardian.com.location_tracking.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import app.mmguardian.com.location_tracking.db.dao.LocationRecordDao;
import app.mmguardian.com.location_tracking.db.model.LocationRecord;

/**
 * DB, store Location information
 *
 * @author  Jerry Cho
 * @version 1.0
 */
@Database(entities = {LocationRecord.class}, version = 1)
public abstract class LocationDatabase extends RoomDatabase {
    public abstract LocationRecordDao locationRecordDao();
}
