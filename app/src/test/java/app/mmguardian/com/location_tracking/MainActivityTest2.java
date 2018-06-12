package app.mmguardian.com.location_tracking;


import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.location.Location;
import android.widget.TextView;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import app.mmguardian.com.location_tracking.db.LocationDatabase;
import app.mmguardian.com.location_tracking.db.model.LocationRecord;
import app.mmguardian.com.location_tracking.service.TrackingService;
import app.mmguardian.com.location_tracking.utils.Util;

@RunWith(MockitoJUnitRunner.class)
public class MainActivityTest2 {

    @Mock
    MainActivity mainActivity;

    @Mock
    Location location;

    @Mock
    android.database.sqlite.SQLiteOpenHelper mSQLiteOpenHelper;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();


    private LocationDatabase mLocationDatabase;
    //private TodoDao dao;

    TextView tvCountDownTime;

    @InjectMocks
    private TrackingService mTrackingService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        mLocationDatabase = Room.inMemoryDatabaseBuilder(mainActivity, LocationDatabase.class)
                .allowMainThreadQueries().build();

        tvCountDownTime = mainActivity.findViewById(R.id.tvCountDownTime);
    }

    @Test
    public void testHavePermission(){
        boolean havePermission = mainActivity.havePermission();
        Assert.assertEquals(false, havePermission);
    }

    @Test
    public void testInsert(){
        //Location location = new Location("testing");
        location.setAltitude(1.0);
        location.setLatitude(1.0);
        location.setLongitude(1.0);

        LocationRecord record = new LocationRecord(location, "testing address");
        mLocationDatabase.locationRecordDao().insertLocationRecord(record);

        int recordCount = mLocationDatabase.locationRecordDao().getAll().size();
        Assert.assertEquals(1, recordCount);
    }

    @After
    public void tearDown() throws Exception {
        mLocationDatabase.close();
    }


}
