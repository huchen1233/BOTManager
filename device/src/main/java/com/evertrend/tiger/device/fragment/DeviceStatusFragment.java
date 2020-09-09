package com.evertrend.tiger.device.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evertrend.tiger.common.fragment.BaseFragment;
import com.evertrend.tiger.common.utils.general.Utils;
import com.evertrend.tiger.device.R;
import com.evertrend.tiger.device.adapter.AlarmInfoAdapter;
import com.evertrend.tiger.device.adapter.StatusShowAdapter;
import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.bean.event.DeviceMessageEvent;
import com.evertrend.tiger.device.bean.event.SpinnerChoiceDeviceMessageEvent;
import com.evertrend.tiger.device.utils.Constants;
import com.evertrend.tiger.device.utils.DeviceStatusValueInitUtil;
import com.evertrend.tiger.device.widget.NestListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class DeviceStatusFragment extends BaseFragment {
    public static final String TAG = DeviceStatusFragment.class.getSimpleName();

    private View root;
    private NestListView lv_device_detail_info;
    private NestListView lv_device_running_info;
    private RecyclerView rlv_device_fault_info;
    private LinearLayout ll_device_detail_info;
    private LinearLayout ll_device_running_info;
    private LinearLayout ll_device_fault_info;

    private Device device = null;
    private String[] deviceDetailInfoNames = new String[]{};
    private String[] deviceRunningInfoNames = new String[]{};
    private List<String> deviceDetailInfoValues;
    private List<String> deviceRunningInfoValues;
    private StatusShowAdapter detailInfoAdapter;
    private StatusShowAdapter runningInfoAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.yl_device_fragment_status, container, false);
        initView();
        EventBus.getDefault().register(this);
        return root;
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    private void initView() {
        ll_device_detail_info = root.findViewById(R.id.ll_device_detail_info);
        ll_device_running_info = root.findViewById(R.id.ll_device_running_info);
        ll_device_fault_info = root.findViewById(R.id.ll_device_fault_info);
        lv_device_detail_info = root.findViewById(R.id.lv_device_detail_info);
        lv_device_running_info = root.findViewById(R.id.lv_device_running_info);
        rlv_device_fault_info = root.findViewById(R.id.rlv_device_fault_info);
        deviceDetailInfoValues = new ArrayList<>();
        deviceRunningInfoValues = new ArrayList<>();
    }

    private void deviceTypeCheckAndInit(Device device) {
        switch (device.getDevice_type()) {
            case Constants.DEVICE_TYPE_EVBOT_SL:
                deviceDetailInfoNames = getResources().getStringArray(R.array.yl_device_type_evbot_sl_detail_info_name);
                deviceRunningInfoNames = getResources().getStringArray(R.array.yl_device_type_evbot_sl_running_info_name);
                break;
            case Constants.DEVICE_TYPE_SWBOT_SL:
                deviceDetailInfoNames = getResources().getStringArray(R.array.yl_device_type_swbot_sl_detail_info_name);
                deviceRunningInfoNames = getResources().getStringArray(R.array.yl_device_type_swbot_sl_running_info_name);
                break;
            case Constants.DEVICE_TYPE_MFBOT_SL:
                deviceDetailInfoNames = getResources().getStringArray(R.array.yl_device_type_mfbot_sl_detail_info_name);
                deviceRunningInfoNames = getResources().getStringArray(R.array.yl_device_type_mfbot_sl_running_info_name);
                break;
            case Constants.DEVICE_TYPE_SWBOT_AP:
                deviceDetailInfoNames = getResources().getStringArray(R.array.yl_device_type_swbot_ap_detail_info_name);
                deviceRunningInfoNames = getResources().getStringArray(R.array.yl_device_type_swbot_ap_running_info_name);
                break;
            case Constants.DEVICE_TYPE_SWBOT_MINI:
                deviceDetailInfoNames = getResources().getStringArray(R.array.yl_device_type_swbot_mini_detail_info_name);
                deviceRunningInfoNames = getResources().getStringArray(R.array.yl_device_type_swbot_mini_running_info_name);
                break;
            default:
                deviceDetailInfoNames = getResources().getStringArray(R.array.yl_device_type_evbot_sl_detail_info_name);
                deviceRunningInfoNames = getResources().getStringArray(R.array.yl_device_type_evbot_sl_running_info_name);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(DeviceMessageEvent messageEvent) {
        device = messageEvent.getMessage();
        reloadStatus(device);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(SpinnerChoiceDeviceMessageEvent messageEvent) {
        device = messageEvent.getDevice();
        reloadStatus(device);
    }

    private void reloadStatus(Device device) {
        if (device.getIs_running() != 0) {
            ll_device_running_info.setVisibility(View.VISIBLE);
        } else {
            ll_device_running_info.setVisibility(View.GONE);
        }
        List<String> alarmInfoList = Utils.parseAlarmInfo(device.getAlarm_info());
        List<String> alarmInfoDescList = Utils.getAlarmInfoDescription(device.getAlarm_info());
        if (alarmInfoList.size() > 0) {
            ll_device_fault_info.setVisibility(View.VISIBLE);
        } else {
            ll_device_fault_info.setVisibility(View.GONE);
        }
        deviceTypeCheckAndInit(device);
        deviceDetailInfoValues = DeviceStatusValueInitUtil.initDetailStatus(getActivity(), device);
        deviceRunningInfoValues = DeviceStatusValueInitUtil.initRunningStatus(getActivity(), device);
        detailInfoAdapter = new StatusShowAdapter(deviceDetailInfoValues, deviceDetailInfoNames, device, getActivity());
        runningInfoAdapter = new StatusShowAdapter(deviceRunningInfoValues, deviceRunningInfoNames, null, getActivity());
        lv_device_detail_info.setAdapter(detailInfoAdapter);
        lv_device_running_info.setAdapter(runningInfoAdapter);

        rlv_device_fault_info.setHasFixedSize(true);
        rlv_device_fault_info.setItemAnimator(new DefaultItemAnimator());
        rlv_device_fault_info.setLayoutManager(new LinearLayoutManager(getActivity()));
        //添加Android自带的分割线
        rlv_device_fault_info.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rlv_device_fault_info.setAdapter(new AlarmInfoAdapter(getActivity(), alarmInfoList, alarmInfoDescList));
    }
}
