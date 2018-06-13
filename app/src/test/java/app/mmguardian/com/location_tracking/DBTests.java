package app.mmguardian.com.location_tracking;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import app.mmguardian.com.location_tracking.db.LocationDatabase;

@RunWith(AndroidJUnit4.class)
public class DBTests {
    LocationDatabase db;

    @Before
    public void setUp() {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getTargetContext(), LocationDatabase.class)
                .allowMainThreadQueries().build();
    }

    @Test
    public void basics() {
        Assert.assertEquals(0, db.locationRecordDao().getAll().size());
    }

    @After
    public void tearDown() {
        db.close();
    }


}
