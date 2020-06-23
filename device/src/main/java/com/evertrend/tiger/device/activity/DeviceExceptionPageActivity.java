package com.evertrend.tiger.device.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;

import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.common.utils.general.Utils;
import com.evertrend.tiger.device.R;
import com.evertrend.tiger.device.adapter.AlarmInfoAdapter;

import java.util.List;

public class DeviceExceptionPageActivity extends AppCompatActivity {
    private static final String TAG = DeviceExceptionPageActivity.class.getSimpleName();

    private RecyclerView rlv_alarm_info_qAndA;

    private Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.yl_device_activity_device_exception_page);
        device = (Device) getIntent().getSerializableExtra("device");
        initView();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        rlv_alarm_info_qAndA = findViewById(R.id.rlv_alarm_info_qAndA);
        List<String> alarmInfoList = Utils.parseAlarmInfo(device.getAlarm_info());
        List<String> alarmInfoDescList = Utils.getAlarmInfoDescription(device.getAlarm_info());
        LogUtil.d(TAG, "alarmInfoList size:"+alarmInfoList.size());
        rlv_alarm_info_qAndA.setHasFixedSize(true);
        rlv_alarm_info_qAndA.setItemAnimator(new DefaultItemAnimator());
        rlv_alarm_info_qAndA.setLayoutManager(new LinearLayoutManager(this));
        //添加Android自带的分割线
        rlv_alarm_info_qAndA.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rlv_alarm_info_qAndA.setAdapter(new AlarmInfoAdapter(this, alarmInfoList, alarmInfoDescList));
    }
}