package app.mmguardian.com.location_tracking;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import app.mmguardian.com.location_tracking.adapter.LocationAdatper;
import app.mmguardian.com.location_tracking.bus.NewLocationTrackingRecordEvent;
import app.mmguardian.com.location_tracking.db.model.LocationRecord;
import app.mmguardian.com.location_tracking.service.LocationJobIntentService;
import app.mmguardian.com.location_tracking.utils.Util;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    public final static String TAG = "location_tracking";

    private static final int PERMISSIONS_REQUEST_ACCESS_LOCATION = 100;

    RecyclerView rcvLocationRecord;
    LocationAdatper mAdapter;

    TextView tvPermissionStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);

        rcvLocationRecord = (RecyclerView) findViewById(R.id.rcvLocationRecord);
        tvPermissionStatus = findViewById(R.id.tvPermissionStatus);
        tvPermissionStatus.setText("No Permission to access location!");
        LinearLayoutManager verticalLinearLayoutManager = new LinearLayoutManager(this);
        verticalLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvLocationRecord.setLayoutManager(verticalLinearLayoutManager);
        rcvLocationRecord.setHasFixedSize(true);
        new AsyncGetRecordFromDBTaskRunner().execute();
        doRequestPermission();
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
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void doStartService(){
//        Util.startService(MainActivity.this);
        tvPermissionStatus.setText("Have Permission to access location!");
        LocationJobIntentService.enqueueWork(MainActivity.this, new Intent());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewLocationTrackingRecordEvent(NewLocationTrackingRecordEvent event) {
        mAdapter.add(0, event.getmLocationRecord());

        Log.d(TAG, "onStartCommand >>" +mAdapter.getItemCount());
        mAdapter.notifyDataSetChanged();
    }

    private class AsyncGetRecordFromDBTaskRunner extends AsyncTask<Void, Void, List<LocationRecord>> {

        @Override
        protected List<LocationRecord> doInBackground(Void... params) {
            Log.d(TAG, "SIZE>>> " + String.valueOf(LocationTrackingApplication.getInstance().getLocationDatabase().locationRecordDao().getAll().size()));
            return LocationTrackingApplication.getInstance().getLocationDatabase().locationRecordDao().getAll();
        }

        @Override
        protected void onPostExecute(List<LocationRecord> locationRecords) {
            mAdapter = new LocationAdatper(locationRecords);
            rcvLocationRecord.setAdapter(mAdapter);
        }
    }
}
