package app.mmguardian.com.location_tracking;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import app.mmguardian.com.Constants;
import app.mmguardian.com.location_tracking.adapter.LocationAdatper;
import app.mmguardian.com.location_tracking.bus.NewLocationTrackingRecordEvent;
import app.mmguardian.com.location_tracking.bus.RemainTimeEvent;
import app.mmguardian.com.location_tracking.bus.ServiceEventConnectedEvent;
import app.mmguardian.com.location_tracking.db.model.LocationRecord;
import app.mmguardian.com.location_tracking.fragment.GoogleMapFragment;
import app.mmguardian.com.location_tracking.service.TrackingService;
import app.mmguardian.com.location_tracking.service.TrackingService2;
import app.mmguardian.com.location_tracking.utils.FragmentUtils;
import app.mmguardian.com.location_tracking.utils.Util;
import io.reactivex.functions.Consumer;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity3 extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    public final static String TAG = "location_tracking";

    private static final int PERMISSIONS_REQUEST_ACCESS_LOCATION = 100;
    private ProgressDialog pd;

    LinearLayout llLoading;

    RecyclerView rcvLocationRecord;
    LocationAdatper mAdapter;

    public TextView tvCountDownTime;
    CheckBox cbHaveRight;

    List<LocationRecord> mLocationRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);

        rcvLocationRecord = (RecyclerView) findViewById(R.id.rcvLocationRecord);
        cbHaveRight = (CheckBox) findViewById(R.id.cbHaveRight);
        llLoading = (LinearLayout) findViewById(R.id.llLoading);
        tvCountDownTime = findViewById(R.id.tvCountDownTime);

        llLoading.setVisibility(View.VISIBLE);
        cbHaveRight.setChecked(Boolean.FALSE);
        tvCountDownTime.setText(displayHHMMSSBySec(Constants.SCHEDULER_TIME_SEC));

        LinearLayoutManager verticalLinearLayoutManager = new LinearLayoutManager(this);
        verticalLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvLocationRecord.setLayoutManager(verticalLinearLayoutManager);
        rcvLocationRecord.setHasFixedSize(true);

        doRequestPermission();
        new AsyncGetRecordFromDBTaskRunner().execute();
    }

    public void doStartService(){
        tvCountDownTime.setText(displayHHMMSSBySec(Constants.SCHEDULER_TIME_SEC));
        cbHaveRight.setChecked(Boolean.TRUE);
        llLoading.setVisibility(View.GONE);
        if (!Util.isServiceRunning(this, TrackingService2.class)){
            startService(new Intent(this, TrackingService2.class));
        }
    }

    public boolean havePermission(){
        return EasyPermissions.hasPermissions(this, new String[] {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        });
    }

    @AfterPermissionGranted(PERMISSIONS_REQUEST_ACCESS_LOCATION)
    public void doRequestPermission(){
        if (EasyPermissions.hasPermissions(this, new String[] {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        })){
            doStartService();
        }
        else {
            EasyPermissions.requestPermissions(this,
                    "Please approved : ",
                    R.string.yes,
                    R.string.no,
                    PERMISSIONS_REQUEST_ACCESS_LOCATION,
                    new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }
            );
        }
    }

    @Override
    public void onPermissionsGranted(int i, @NonNull List<String> list) {
        doStartService();
    }

    @Override
    public void onPermissionsDenied(int i, @NonNull List<String> list) {
        Toast.makeText(this, "Please accept the permission request", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "MainActivity onDestory");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewLocationTrackingRecordEvent(NewLocationTrackingRecordEvent event) {

        if (mAdapter.getItemCount() > 0 &&
                mAdapter.getItemByPosition(mAdapter.getItemCount() - 1).date <event.getBeforeDate()){
            int position = mLocationRecords.size()-1;
            mAdapter.remove(position);
            mAdapter.notifyItemRemoved(position);
        }


        mAdapter.add(0, event.getmLocationRecord());

        Log.d(TAG, "onNewLocationTrackingRecordEvent >>" +mAdapter.getItemCount());
        mAdapter.notifyItemInserted(0);
        rcvLocationRecord.smoothScrollToPosition(0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRemainTimeEvent(RemainTimeEvent event) {
        tvCountDownTime.setText(displayHHMMSSBySec(event.getmSecond()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onServiceEventConnectedEvent(ServiceEventConnectedEvent event){
        llLoading.setVisibility(View.GONE);
    }

    private class AsyncGetRecordFromDBTaskRunner extends AsyncTask<Void, Void, List<LocationRecord>> {

        @Override
        protected List<LocationRecord> doInBackground(Void... params) {
            mLocationRecords = LocationTrackingApplication.getInstance().getLocationDatabase().locationRecordDao().getAll();
            Log.d(TAG, "SIZE>>> " + String.valueOf(mLocationRecords.size()));
            return mLocationRecords;
        }

        @Override
        protected void onPostExecute(List<LocationRecord> locationRecords) {
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


    private String displayHHMMSSBySec(long secs) {
        return "Update remain : " + String.format("%02d:%02d:%02d", (secs % 86400) / 3600, (secs % 3600) / 60, secs % 60);
    }


}