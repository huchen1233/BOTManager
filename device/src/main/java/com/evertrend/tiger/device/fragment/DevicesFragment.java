package com.evertrend.tiger.device.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evertrend.tiger.common.bean.event.ProgressStopEvent;
import com.evertrend.tiger.common.fragment.BaseFragment;
import com.evertrend.tiger.common.utils.general.DialogUtil;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.device.R;
import com.evertrend.tiger.device.bean.Device;
import com.evertrend.tiger.device.adapter.DevicesAdapter;
import com.evertrend.tiger.device.bean.event.LoadDevicesEvent;
import com.evertrend.tiger.device.utils.TaskUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DevicesFragment extends BaseFragment {
    private static final String TAG = DevicesFragment.class.getSimpleName();

    private RecyclerView rlv_devices;
    private ImageButton ibtn_add_device;

    private List<Device> deviceList;
    private DevicesAdapter devicesAdapter;

    private ScheduledThreadPoolExecutor scheduledThreadGetAssociatedDevice;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.i(TAG, "===onCreateView===");
        View root = inflater.inflate(R.layout.yl_device_fragment_devices, container, false);
        initView(root);
        deviceList = new ArrayList<>();
        initDeviceShow();
        EventBus.getDefault().register(this);
        loadDevice();
        return root;
    }

    @Override
    public void onDestroy() {
        exit();
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ProgressStopEvent event) {
        LogUtil.i(TAG, "===ProgressStopEvent===");
        stopGetAssociatedDeviceTimer();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoadDevicesEvent event) {
        LogUtil.i(TAG, "===LoadDevicesEvent===");
        stopGetAssociatedDeviceTimer();
        DialogUtil.hideProgressDialog();
        deviceList.clear();
        deviceList.addAll(event.getDeviceList());
        if (deviceList.size() > 0) {
            devicesAdapter.notifyDataSetChanged();
        }
    }

    private void loadDevice() {
        DialogUtil.showProgressDialog(getActivity(), getResources().getString(R.string.yl_common_devices), false, true);
        scheduledThreadGetAssociatedDevice = new ScheduledThreadPoolExecutor(4);
        scheduledThreadGetAssociatedDevice.scheduleAtFixedRate(new TaskUtils.TaskGetAssocitedDevice(),
                0, 6, TimeUnit.SECONDS);
    }

    private void exit() {
        stopGetAssociatedDeviceTimer();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void stopGetAssociatedDeviceTimer() {
        if (scheduledThreadGetAssociatedDevice != null) {
            scheduledThreadGetAssociatedDevice.shutdownNow();
            scheduledThreadGetAssociatedDevice = null;
        }
    }

    private void initDeviceShow() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rlv_devices.setLayoutManager(layoutManager);
        //添加Android自带的分割线
        rlv_devices.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        devicesAdapter = new DevicesAdapter(getActivity(), deviceList);
        rlv_devices.setAdapter(devicesAdapter);
    }

    private void initView(View root) {
        rlv_devices = root.findViewById(R.id.rlv_devices);
        ibtn_add_device = root.findViewById(R.id.ibtn_add_device);
    }
}
