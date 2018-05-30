package app.mmguardian.com.location_tracking.db.dao;

import android.arch.persistence.room.Dao;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.ArrayList;


import app.mmguardian.com.location_tracking.db.model.LocationRecord;

@Dao
public interface LocationRecordDao {

    @Query("SELECT * FROM LocationRecord")
    ArrayList<LocationRecord> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocationRecord(LocationRecord locationRecord);
}
