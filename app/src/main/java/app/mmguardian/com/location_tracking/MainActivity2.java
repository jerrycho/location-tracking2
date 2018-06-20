package app.mmguardian.com.location_tracking;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;


import app.mmguardian.com.Constants;
import app.mmguardian.com.location_tracking.adapter.LocationAdatper;
import app.mmguardian.com.location_tracking.bus.NewLocationTrackingRecordEvent;
import app.mmguardian.com.location_tracking.bus.RemainTimeEvent;
import app.mmguardian.com.location_tracking.bus.StartDownTimerEvent;
import app.mmguardian.com.location_tracking.db.model.LocationRecord;
import app.mmguardian.com.location_tracking.fragment.GoogleMapFragment;
import app.mmguardian.com.location_tracking.log.AppLog;
import app.mmguardian.com.location_tracking.receiver.SensorRestarterBroadcastReceiver;
import app.mmguardian.com.location_tracking.utils.AlarmUtil;
import app.mmguardian.com.location_tracking.utils.FragmentUtils;
import app.mmguardian.com.location_tracking.utils.PreferenceManager;
import io.reactivex.functions.Consumer;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by apple on 17/6/2018.
 */

public class MainActivity2 extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    private static final int REQUEST_PERMISSION_RESULT = 1001;
    private static final int ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS = 1003;

    RecyclerView rcvLocationRecord;
    LocationAdatper mAdapter;

    public TextView tvCountDownTime;
    CheckBox cbHaveRight;

    app.mmguardian.com.location_tracking.utils.CountDownTimer mRXTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init View
        EventBus.getDefault().register(this);

        rcvLocationRecord = findViewById(R.id.rcvLocationRecord);
        cbHaveRight = findViewById(R.id.cbHaveRight);
        tvCountDownTime = findViewById(R.id.tvCountDownTime);

        //setup UI
        cbHaveRight.setChecked(Boolean.FALSE);
        tvCountDownTime.setText("--:--:--");

        //setup RecyclerView view
        LinearLayoutManager verticalLinearLayoutManager = new LinearLayoutManager(this);
        verticalLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvLocationRecord.setLayoutManager(verticalLinearLayoutManager);
        rcvLocationRecord.setHasFixedSize(true);

        new AsyncGetRecordFromDBTaskRunner().execute();

        if (haveAllPermission()){
            sendToWhiteList();
        }
        else {
            EasyPermissions.requestPermissions(this,
                    "Please approved : ",
                    R.string.yes,
                    R.string.no,
                    REQUEST_PERMISSION_RESULT,
                    new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                    }
            );
        }

    }

    public boolean haveAllPermission(){
        return EasyPermissions.hasPermissions(this, new String[] {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
        });
    }

    @Override
    public void onPermissionsGranted(int i, List<String> list) {
        sendToWhiteList();
    }

    @Override
    public void onPermissionsDenied(int i, List<String> list) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS){
            if (resultCode==RESULT_OK){
                AlarmUtil.setAlarmTime4(MainActivity2.this);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        app.mmguardian.com.location_tracking.log.AppLog.d("MainActivity onDestory");
        EventBus.getDefault().unregister(this);
        Intent intent = new Intent(this, SensorRestarterBroadcastReceiver.class);
        sendBroadcast(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkTimer();
    }

    private void checkTimer(){
        long lastInsertDate = new PreferenceManager(MainActivity2.this).getLongPref("LAST_INSERT_DATE");
        AppLog.d("lastInsertDate>>> " + lastInsertDate);
        Calendar lastDate = Calendar.getInstance();
        lastDate.setTimeInMillis(lastInsertDate);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MILLISECOND, 0);
        if (lastInsertDate == 0){

        }
        else {
            int diffSec = (int) ((c.getTimeInMillis() - lastInsertDate) / 1000);
            diffSec = ((int) Constants.SCHEDULER_TIME_SEC) - diffSec;
            EventBus.getDefault().post(new StartDownTimerEvent(diffSec));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewLocationTrackingRecordEvent(NewLocationTrackingRecordEvent event) {
        new AsyncGetRecordFromDBTaskRunner().execute();
    }

    private class AsyncGetRecordFromDBTaskRunner extends AsyncTask<Void, Void, List<LocationRecord>> {

        @Override
        protected List<LocationRecord> doInBackground(Void... params) {
            List<LocationRecord> mLocationRecords = LocationTrackingApplication.getInstance().getLocationDatabase().locationRecordDao().getAll();
            app.mmguardian.com.location_tracking.log.AppLog.d("SIZE>>> " + String.valueOf(mLocationRecords.size()));
            return mLocationRecords;
        }

        @Override
        protected void onPostExecute(List<LocationRecord> locationRecords) {

            checkTimer();

            mAdapter = new LocationAdatper(locationRecords);
            mAdapter.setOnMapClick(new Consumer<LocationRecord>() {
                @Override
                public void accept(@io.reactivex.annotations.NonNull LocationRecord locationRecordRecord) throws Exception {
                    FragmentUtils.addFragment(getSupportFragmentManager(),
                            GoogleMapFragment.newInstance(locationRecordRecord.latitude, locationRecordRecord.longitude),
                            R.id.fragmentContainer,
                            Boolean.TRUE, FragmentUtils.TRANSITION_LEFT_RIGHT);
                }
            });
            rcvLocationRecord.setAdapter(mAdapter);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRemainTimeEvent(RemainTimeEvent event) {
        tvCountDownTime.setText(displayHHMMSSBySec(event.getmSecond()));
    }

    private String displayHHMMSSBySec(long secs) {
        return "Update remain : " + String.format("%02d:%02d:%02d", (secs % 86400) / 3600, (secs % 3600) / 60, secs % 60);
    }

    private void sendToWhiteList(){
        if (!ignoringOptimizations()) {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
            startActivityForResult(intent, ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        }
        else {
            AlarmUtil.setAlarmTime4(MainActivity2.this);
        }
    }

    private boolean ignoringOptimizations(){
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        return powerManager.isIgnoringBatteryOptimizations(packageName);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStartDownTimerEvent(StartDownTimerEvent event){
        if (mRXTimer!=null)
            mRXTimer.cancel();
        mRXTimer =  new app.mmguardian.com.location_tracking.utils.CountDownTimer(Long.valueOf(event.getFrom()), TimeUnit.SECONDS) {

            @Override
            public void onTick(long tickValue) {
                AppLog.d("RX My Intent Service Timer >> on Remain "+ tickValue);
                EventBus.getDefault().post(new RemainTimeEvent( (int)tickValue ));
            }

            @Override
            public void onFinish() {
                AppLog.d("RX Timer >> on Finish");
            }

            @Override
            public void onError(Throwable e) {
                AppLog.d("RX Timer >> on Error>>"+ e.toString());
            }
        };
        mRXTimer.start();
    }



}
