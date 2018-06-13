package app.mmguardian.com.location_tracking;

import android.arch.persistence.room.Room;
import android.icu.util.Calendar;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import app.mmguardian.com.location_tracking.db.LocationDatabase;
import app.mmguardian.com.location_tracking.db.model.LocationRecord;

@RunWith(AndroidJUnit4.class)
public class DBTest {
    LocationDatabase db;

    @Before
    public void setUp() {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getTargetContext(), LocationDatabase.class)
                .allowMainThreadQueries().build();
    }

    @Test
    public void testZeroRecord() {
        Assert.assertEquals(0, db.locationRecordDao().getAll().size());
    }

    @Test
    public void testInsertRecord() {
        LocationRecord record = new LocationRecord(false, 1.0f, 1.0f, 1.0f, "this is test address");
        db.locationRecordDao().insertLocationRecord(record);
        Assert.assertEquals(1, db.locationRecordDao().getAll().size());
    }

    @Test
    public void testDeleteRecord() {
        db.locationRecordDao().deleteBeforeDate(Calendar.getInstance().getTimeInMillis() + 10000);
        Assert.assertEquals(0, db.locationRecordDao().getAll().size());
    }

    @After
    public void tearDown() {
        db.close();
    }

}
