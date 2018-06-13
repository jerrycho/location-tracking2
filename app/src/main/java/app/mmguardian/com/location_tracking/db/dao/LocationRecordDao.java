package app.mmguardian.com.location_tracking.db.dao;

import android.arch.persistence.room.Dao;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;


import app.mmguardian.com.location_tracking.db.model.LocationRecord;


/**
 * Get, insert , delete record from DB
 *
 * @author  Jerry Cho
 * @version 1.0
 */

@Dao
public interface LocationRecordDao {

    @Insert
    void insertLocationRecord(LocationRecord locationRecord);

    @Query("select * from LocationRecord order by date desc")
    List<LocationRecord> getAll();

    @Query("select * from LocationRecord where id = :id")
    LocationRecord getById(int id);

    @Query("delete from LocationRecord where date < :date")
    void deleteBeforeDate(long date);

}
