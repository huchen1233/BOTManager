package com.evertrend.tiger.device.activity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.evertrend.tiger.common.fragment.BaseFragment;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.device.R;
import com.evertrend.tiger.device.adapter.DeviceSpinnerAdapter;
import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.device.bean.event.DeviceListEvent;
import com.evertrend.tiger.device.bean.event.SpinnerChoiceDeviceMessageEvent;
import com.evertrend.tiger.device.fragment.DeviceMapFragment;
import com.evertrend.tiger.device.fragment.DeviceOperationFragment;
import com.evertrend.tiger.device.fragment.DeviceStatusFragment;
import com.evertrend.tiger.device.fragment.DeviceTaskFragment;
import com.evertrend.tiger.device.utils.Constants;
import com.evertrend.tiger.device.utils.TaskUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DeviceMainActivity extends AppCompatActivity {
    private static final String TAG = DeviceMainActivity.class.getSimpleName();

    private int deviceStatusIndex = 0;
    private int deviceTaskIndex = 1;
    private int deviceMapIndex = 2;
    private int deviceOperationIndex = 3;
    private ViewPager2 viewPager2;
    private List<BaseFragment> fragments;
    private Toolbar tbMain;
    private ImageView ibtn_back;
    private Spinner sp_device_choice;

    private Device device;
    private List<Device> deviceList;
    private DeviceSpinnerAdapter adapter;
    private BottomNavigationView bottomNavigationView;
    private ScheduledThreadPoolExecutor scheduledThreadGetDeviceInfoPoolExecutor;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.yl_device_activity_main);
        this.getSupportActionBar().hide();
        device = (Device) getIntent().getSerializableExtra("device");
        fragments = getFragments();
        initView();
        initSpinner();
        if (device != null) {
            startGetDeviceInfoTimer();
        }
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        exit();
        super.onDestroy();
//        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(mReveiver);
    }

    @Override
    public void onBackPressed() {
        LogUtil.i(this, TAG, "===onBackPressed===");
        exit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(DeviceListEvent messageEvent) {
        LogUtil.i(this, TAG, "===DeviceListEvent===");
        deviceList.addAll(messageEvent.getDeviceList());
        if (deviceList.size() > 0) {
            for (int i = 0; i < deviceList.size(); i++) {
                if (device.getId() == deviceList.get(i).getId()) {
                    sp_device_choice.setSelection(i);
                }
            }
            adapter.notifyDataSetChanged();
        }
        EventBus.getDefault().removeStickyEvent(messageEvent);
    }

    private void exit() {
        stopGetDeviceInfoTimer();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        finish();
    }

    private void startGetDeviceInfoTimer() {
        scheduledThreadGetDeviceInfoPoolExecutor = new ScheduledThreadPoolExecutor(2);
        scheduledThreadGetDeviceInfoPoolExecutor.scheduleAtFixedRate(new TaskUtils.TaskGetDevice(String.valueOf(device.getId())),
                0, Constants.GET_DEVICE_TIME_INTERVAL, TimeUnit.SECONDS);
        //0表示首次执行任务的延迟时间，10 表示每次执行任务的间隔时间，TimeUnit.SECONDS 执行的时间间隔数值单位
    }

    private void stopGetDeviceInfoTimer() {
        if (scheduledThreadGetDeviceInfoPoolExecutor != null) {
            scheduledThreadGetDeviceInfoPoolExecutor.shutdownNow();
            scheduledThreadGetDeviceInfoPoolExecutor = null;
        }
    }

    private void initSpinner() {
        deviceList = new ArrayList<>();
        adapter = new DeviceSpinnerAdapter(deviceList, this);
        sp_device_choice.setAdapter(adapter);
        sp_device_choice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                device = deviceList.get(position);
                stopGetDeviceInfoTimer();
                EventBus.getDefault().postSticky(new SpinnerChoiceDeviceMessageEvent(device));
                startGetDeviceInfoTimer();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initView() {
        tbMain = findViewById(R.id.tb_main);
        sp_device_choice = findViewById(R.id.sp_device_choice);
        ibtn_back = findViewById(R.id.ibtn_back);
        ibtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
        viewPager2 = findViewById(R.id.viewPager2);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        viewPager2.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return fragments.size();
            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
//                LogUtil.i(TAG, "onPageScrolled: "+position+"--->"+positionOffset+"--->"+positionOffsetPixels );
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
//                LogUtil.i(TAG, "onPageSelected: "+position );
                bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(position).getItemId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
//                LogUtil.i(TAG, "onPageScrollStateChanged: "+state );
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_devices_status) {
                    viewPager2.setCurrentItem(deviceStatusIndex, false);
                } else if (menuItem.getItemId() == R.id.action_devices_task) {
                    viewPager2.setCurrentItem(deviceTaskIndex, false);
                } else if (menuItem.getItemId() == R.id.action_devices_mappage) {
                    viewPager2.setCurrentItem(deviceMapIndex, false);
                } else if (menuItem.getItemId() == R.id.action_devices_operation) {
                    viewPager2.setCurrentItem(deviceOperationIndex, false);
                }
                return true;
            }
        });
    }

    private List<BaseFragment> getFragments() {
        List<BaseFragment> fragments = new ArrayList<BaseFragment>(2);
        DeviceStatusFragment statusFragment = new DeviceStatusFragment();
        fragments.add(statusFragment);
        DeviceTaskFragment taskFragment = new DeviceTaskFragment();
        fragments.add(taskFragment);
        DeviceMapFragment mapFragment = new DeviceMapFragment();
        fragments.add(mapFragment);
        DeviceOperationFragment operationFragment = new DeviceOperationFragment();
        fragments.add(operationFragment);
        return fragments;
    }
}
