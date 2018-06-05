package app.mmguardian.com.location_tracking;


import android.Manifest;
import android.content.Intent;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import app.mmguardian.com.Constants;
import app.mmguardian.com.location_tracking.service.LocationJobIntentService;

@RunWith(MockitoJUnitRunner.class)
public class MainActivityTest2 {

    @Mock
    MainActivity mainActivity;

    TextView tvCountDownTime;

    @Before
    public void setup(){
        tvCountDownTime = mainActivity.findViewById(R.id.tvCountDownTime);
    }

    @Test
    public void testHavePermission(){
        boolean havePermission = mainActivity.havePermission();
        Assert.assertEquals(false, havePermission);
    }

    @Test
    public void testAfterStartService(){
        mainActivity.doStartService();
        TextView tvCountDownTime = mainActivity.findViewById(R.id.tvCountDownTime);
        Assert.assertEquals(mainActivity.tvCountDownTime.getText(), "00:00:05");

//        cbHaveRight.setChecked(Boolean.TRUE);
//        LocationJobIntentService.enqueueWork(MainActivity.this, new Intent());
//
//        Assert.assertEquals(false, havePermission);
    }
}
