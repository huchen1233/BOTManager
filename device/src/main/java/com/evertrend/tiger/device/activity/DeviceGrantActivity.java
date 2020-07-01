package com.evertrend.tiger.device.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evertrend.tiger.common.adapter.DeviceGrantAdapter;
import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.bean.DeviceGrant;
import com.evertrend.tiger.common.bean.event.ChoiceDeviceGrantEvent;
import com.evertrend.tiger.common.bean.event.CreateNewDeviceGrantFailEvent;
import com.evertrend.tiger.common.bean.event.CreateNewDeviceGrantSuccessEvent;
import com.evertrend.tiger.common.bean.event.DeleteDeviceGrantEvent;
import com.evertrend.tiger.common.bean.event.DialogChoiceEvent;
import com.evertrend.tiger.common.bean.event.GetDeviceAllGrantsSuccessEvent;
import com.evertrend.tiger.common.bean.event.UpdateDeviceGrantSuccessEvent;
import com.evertrend.tiger.common.utils.general.CommonConstants;
import com.evertrend.tiger.common.utils.general.DialogUtil;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.common.utils.network.CommTaskUtils;
import com.evertrend.tiger.common.utils.network.CommonNetReq;
import com.evertrend.tiger.common.widget.GrantBottomPopupView;
import com.lxj.xpopup.XPopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DeviceGrantActivity extends AppCompatActivity {
    private static final String TAG = DeviceGrantActivity.class.getSimpleName();

    private RecyclerView rlv_all_grant;
    private ImageButton ibtn_create_grant;
    private GrantBottomPopupView grantBottomPopupView;

    private Device device;
    private List<DeviceGrant> deviceGrantList;
    private DeviceGrantAdapter deviceGrantAdapter;
    private DeviceGrant deviceGrant;

    private ScheduledThreadPoolExecutor scheduledThreadGetDeviceAllGrants;
    private ScheduledThreadPoolExecutor scheduledThreadDelete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(com.evertrend.tiger.common.R.layout.yl_common_activity_device_grant);
        device = (Device) getIntent().getSerializableExtra("device");
        deviceGrantList = new ArrayList<>();
        scheduledThreadGetDeviceAllGrants = new ScheduledThreadPoolExecutor(3);
        scheduledThreadGetDeviceAllGrants.scheduleAtFixedRate(new CommTaskUtils.TaskGetDeviceAllGrants(device),
                0, 6, TimeUnit.SECONDS);
        initView();
        EventBus.getDefault().register(this);
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


    @Override
    protected void onDestroy() {
        exit();
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(GetDeviceAllGrantsSuccessEvent event) {
        stopGetDeviceAllGrantsTimer();
        LogUtil.i(this, TAG, "size : "+event.getDeviceGrantList().size());
        deviceGrantList = event.getDeviceGrantList();
        showGrants();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CreateNewDeviceGrantSuccessEvent event) {
        refreshTracePathList(event.getDeviceGrant(), CommonConstants.LIST_OPERATION_CREATE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UpdateDeviceGrantSuccessEvent event) {
        refreshTracePathList(event.getDeviceGrant(), CommonConstants.LIST_OPERATION_UPDATE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DialogChoiceEvent event) {
        scheduledThreadDelete = new ScheduledThreadPoolExecutor(3);
        scheduledThreadDelete.scheduleAtFixedRate(new CommTaskUtils.TaskDeleteDeviceGrant(deviceGrant),
                0, 8, TimeUnit.SECONDS);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DeleteDeviceGrantEvent event) {
        stopDeleteBaseTraceTimer();
        DialogUtil.hideProgressDialog();
        refreshTracePathList(event.getDeviceGrant(), CommonConstants.LIST_OPERATION_DELETE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ChoiceDeviceGrantEvent event) {
        deviceGrant = event.getDeviceGrant();
        if (event.getType() == CommonConstants.LIST_OPERATION_DELETE) {
            String deleteConfirm = getResources().getString(com.evertrend.tiger.common.R.string.yl_common_delete_grant_confirm);
            DialogUtil.showChoiceDialog(this, String.format(deleteConfirm, deviceGrant.getUser()), CommonConstants.TYPE_MAPPAGE_OPERATION_DELETE);
        } else if (event.getType() == CommonConstants.LIST_OPERATION_UPDATE) {
            grantBottomPopupView = new GrantBottomPopupView(DeviceGrantActivity.this, device, deviceGrant);
            showBottomPopup(grantBottomPopupView);
        }
    }

    private void refreshTracePathList(DeviceGrant deviceGrant, int operation) {
        if (CommonConstants.LIST_OPERATION_CREATE == operation) {
            deviceGrantList.add(deviceGrant);
            deviceGrantAdapter.notifyItemInserted(deviceGrantList.size()-1);
        } else {
            for (int i = 0; i < deviceGrantList.size(); i++) {
                if (deviceGrant.getId() == deviceGrantList.get(i).getId()) {
                    if (CommonConstants.LIST_OPERATION_DELETE == operation) {
                        deviceGrantList.remove(i);
                        deviceGrantAdapter.notifyItemRemoved(i);
                    } else if (CommonConstants.LIST_OPERATION_UPDATE == operation) {
                        deviceGrantList.set(i, deviceGrant);
                        deviceGrantAdapter.notifyItemChanged(i);
                    }
                }
            }
        }
    }

    private void exit() {
        stopDeleteBaseTraceTimer();
        stopGetDeviceAllGrantsTimer();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void stopGetDeviceAllGrantsTimer() {
        if (scheduledThreadGetDeviceAllGrants != null) {
            scheduledThreadGetDeviceAllGrants.shutdownNow();
            scheduledThreadGetDeviceAllGrants = null;
        }
    }

    private void stopDeleteBaseTraceTimer() {
        if (scheduledThreadDelete != null) {
            scheduledThreadDelete.shutdownNow();
            scheduledThreadDelete = null;
        }
    }

    private void showGrants() {
        rlv_all_grant.setHasFixedSize(true);
        rlv_all_grant.setItemAnimator(new DefaultItemAnimator());
        rlv_all_grant.setLayoutManager(new LinearLayoutManager(this));
        deviceGrantAdapter = new DeviceGrantAdapter(this, deviceGrantList);
        rlv_all_grant.setAdapter(deviceGrantAdapter);
    }

    private void initView() {
        rlv_all_grant = findViewById(com.evertrend.tiger.common.R.id.rlv_all_grant);
        ibtn_create_grant = findViewById(com.evertrend.tiger.common.R.id.ibtn_create_grant);
        ibtn_create_grant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grantBottomPopupView = new GrantBottomPopupView(DeviceGrantActivity.this, device);
                showBottomPopup(grantBottomPopupView);
            }
        });
    }

    private void showBottomPopup(GrantBottomPopupView grantBottomPopupView) {
        new XPopup.Builder(this)
                .autoOpenSoftInput(true)
                .asCustom(grantBottomPopupView)
                .show();
    }
}