package app.mmguardian.com.location_tracking.service;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class LocationJobService extends JobService {

    public final static String TAG = "location_tracking";

    @Override
    public boolean onStartJob(JobParameters params) {
        doService();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private void doService() {
        Log.d(TAG,"doService>>>");
        for (int i = 0; i<=4; i++){
            Log.d(TAG, "doService>>>>"+String.valueOf(i));
        }
    }

}
