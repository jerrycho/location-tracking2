package app.mmguardian.com.location_tracking;

import android.app.Application;
import android.arch.persistence.room.Room;


import com.simplymadeapps.quickperiodicjobscheduler.PeriodicJob;
import com.simplymadeapps.quickperiodicjobscheduler.QuickJobFinishedCallback;
import com.simplymadeapps.quickperiodicjobscheduler.QuickPeriodicJob;
import com.simplymadeapps.quickperiodicjobscheduler.QuickPeriodicJobCollection;
import com.simplymadeapps.quickperiodicjobscheduler.QuickPeriodicJobScheduler;

import app.mmguardian.com.location_tracking.db.LocationDatabase;
import app.mmguardian.com.location_tracking.log.AppLog;


/**
 * The Main application class
 *
 * @author  Jerry Cho
 * @version 1.0
 */
public class LocationTrackingApplication extends Application {

    private static LocationTrackingApplication instance;
    LocationDatabase mLocationDatabase;

    public static LocationTrackingApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mLocationDatabase =
                Room.databaseBuilder(getApplicationContext(), LocationDatabase.class, "location.db").build();

        //initJobs();
    }

    public LocationDatabase getLocationDatabase() {
        return mLocationDatabase;
    }
//
//    public void initJobs() {
//        int jobId = 1;
//        QuickPeriodicJob job = new QuickPeriodicJob(jobId, new PeriodicJob() {
//            @Override
//            public void execute(QuickJobFinishedCallback callback) {
//                SomeJobClass.performJob();
//
//                // When you have done all your work in the job, call jobFinished to release the resources
//                callback.jobFinished();
//            }
//        });
//
//        QuickPeriodicJobCollection.addJob(job);
//
//        QuickPeriodicJobScheduler jobScheduler = new QuickPeriodicJobScheduler(this);
//        jobScheduler.start(1, 10 * 1000);
//    }
//
//    public static class SomeJobClass {
//        public static void performJob() {
//            AppLog.d("Job Fired");
//        }
//    }

}
