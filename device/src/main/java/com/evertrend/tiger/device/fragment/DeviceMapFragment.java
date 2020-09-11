package com.evertrend.tiger.device.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evertrend.tiger.common.bean.event.DialogChoiceEvent;
import com.evertrend.tiger.common.fragment.BaseFragment;
import com.evertrend.tiger.common.utils.general.CommonConstants;
import com.evertrend.tiger.common.utils.general.DialogUtil;
import com.evertrend.tiger.device.R;
import com.evertrend.tiger.common.adapter.MapPagesChoiceAdapter;
import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.bean.MapPages;
import com.evertrend.tiger.common.bean.event.ChoiceMapPagesEvent;
import com.evertrend.tiger.device.bean.event.DeleteMapPageEvent;
import com.evertrend.tiger.common.bean.event.SaveMapPageEvent;
import com.evertrend.tiger.common.bean.event.DeviceMessageEvent;
import com.evertrend.tiger.common.bean.event.GetAllMapPagesSuccessEvent;
import com.evertrend.tiger.device.bean.event.SpinnerChoiceDeviceMessageEvent;
import com.evertrend.tiger.common.utils.general.ScheduledThreadUtils;
import com.evertrend.tiger.device.utils.TaskUtils;
import com.evertrend.tiger.common.widget.MapBottomPopupView;
import com.lxj.xpopup.XPopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DeviceMapFragment extends BaseFragment {
    public static final String TAG = DeviceMapFragment.class.getSimpleName();

    private View root;
    private RecyclerView rlv_all_map_page;
    private ImageButton ibtn_create_map_page;
    private MapBottomPopupView mapBottomPopupView;

    private MapPagesChoiceAdapter mapPagesChoiceAdapter;
    private Device mDevice;
    private List<MapPages> mapPagesList;
    private MapPages choiceMapPages;
    private ScheduledThreadPoolExecutor scheduledThreadDeleteMapPages;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.yl_device_fragment_device_map, container, false);
        initView();
        showMapPages();
        return root;
    }

    @Override
    public void onStart() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        super.onStart();
    }

    @Override
    public void onStop() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        exit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(DeviceMessageEvent messageEvent) {
        mDevice = messageEvent.getMessage();
        ScheduledThreadUtils.ThreadGetAllMapPages(mDevice);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(SpinnerChoiceDeviceMessageEvent messageEvent) {
        mDevice = messageEvent.getDevice();
//        DialogUtil.showProgressDialog(getContext(), getResources().getString(R.string.yl_common_getting), true, true);
        ScheduledThreadUtils.ThreadGetAllMapPages(mDevice);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventMainThread(GetAllMapPagesSuccessEvent event) {
        DialogUtil.hideProgressDialog();
        ScheduledThreadUtils.stopGetAllMapPagesTimer();
        mapPagesList.removeAll(mapPagesList);
        mapPagesChoiceAdapter.notifyItemRangeRemoved(0, mapPagesList.size());
        mapPagesChoiceAdapter.notifyDataSetChanged();
        mapPagesList.addAll(event.getMapPagesList());
        mapPagesChoiceAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SaveMapPageEvent event) {
        MapPages mapPages = event.getMapPages();
        DialogUtil.showSuccessToast(getContext());
        if (event.isUpdate()) {
            refreshMapPageList(mapPages, CommonConstants.LIST_OPERATION_UPDATE);
        } else {
            refreshMapPageList(mapPages, CommonConstants.LIST_OPERATION_CREATE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ChoiceMapPagesEvent event) {
        choiceMapPages = event.getMapPages();
        Intent intent = new Intent();
        intent.putExtra("device", mDevice);
        intent.putExtra("mappage", choiceMapPages);
        if (CommonConstants.TYPE_MAPPAGE_OPERATION_OPEN == event.getType()) {
            intent.setAction("android.intent.action.OperationAreaMapActivity");
            startActivity(intent);
        } else if (CommonConstants.TYPE_MAPPAGE_OPERATION_DELETE == event.getType()) {
            String deleteConfirm = getResources().getString(R.string.yl_device_delete_mappage_confirm);
            DialogUtil.showChoiceDialog(getActivity(), String.format(deleteConfirm, choiceMapPages.getName()), CommonConstants.TYPE_MAPPAGE_OPERATION_DELETE);
        } else if (CommonConstants.TYPE_MAPPAGE_OPERATION_EDIT == event.getType()) {
            mapBottomPopupView = new MapBottomPopupView(getActivity(), mDevice, choiceMapPages);
            new XPopup.Builder(getActivity())
                    .autoOpenSoftInput(true)
                    .asCustom(mapBottomPopupView)
                    .show();
        } else if (CommonConstants.TYPE_MAPPAGE_OPERATION_TRACE_PATH == event.getType()) {
            intent.setAction("android.intent.action.TracePathOperationActivity");
            startActivity(intent);
        } else if (CommonConstants.TYPE_MAPPAGE_OPERATION_VIRTUAL_TRACK == event.getType()) {
            intent.setAction("android.intent.action.VirtualTrackGroupOperationActivity");
            startActivity(intent);
        } else if (CommonConstants.TYPE_MAPPAGE_OPERATION_GPS_FENCE == event.getType()) {
            intent.setAction("android.intent.action.GpsFenceActivity");
            startActivity(intent);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DialogChoiceEvent event) {
        if (event.getType() == CommonConstants.TYPE_MAPPAGE_OPERATION_DELETE) {
            DialogUtil.showProgressDialog(getActivity(), getResources().getString(R.string.yl_common_deleting), false, false);
            scheduledThreadDeleteMapPages = new ScheduledThreadPoolExecutor(4);
            scheduledThreadDeleteMapPages.scheduleAtFixedRate(new TaskUtils.TaskDeleteMapPages(mDevice, choiceMapPages),
                    0, 5, TimeUnit.SECONDS);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DeleteMapPageEvent event) {
        MapPages mapPages = event.getMapPages();
        refreshMapPageList(mapPages, CommonConstants.LIST_OPERATION_DELETE);
        stopDeleteMapPagesTimer();
        DialogUtil.hideProgressDialog();
        DialogUtil.showSuccessToast(getContext());
    }

    private void refreshMapPageList(MapPages mapPages, int operation) {
        if (CommonConstants.LIST_OPERATION_CREATE == operation) {
            mapPagesList.add(mapPages);
            mapPagesChoiceAdapter.notifyItemInserted(mapPagesList.size()-1);
        } else {
            for (int i = 0; i < mapPagesList.size(); i++) {
                if (mapPages.getId() == mapPagesList.get(i).getId()) {
                    if (CommonConstants.LIST_OPERATION_DELETE == operation) {
                        mapPagesList.remove(i);
                        mapPagesChoiceAdapter.notifyItemRemoved(i);
                    } else if (CommonConstants.LIST_OPERATION_UPDATE == operation) {
                        mapPagesList.set(i, mapPages);
                        mapPagesChoiceAdapter.notifyItemChanged(i);
                    }
                }
            }
        }
    }

    private void initView() {
        rlv_all_map_page = root.findViewById(R.id.rlv_all_map_page);
        ibtn_create_map_page = root.findViewById(R.id.ibtn_create_map_page);
        ibtn_create_map_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapBottomPopupView = new MapBottomPopupView(getActivity(), mDevice);
                new XPopup.Builder(getActivity())
                        .autoOpenSoftInput(true)
                        .asCustom(mapBottomPopupView)
                        .show();
            }
        });
    }

    private void showMapPages() {
        mapPagesList = new ArrayList<>();
        rlv_all_map_page.setHasFixedSize(true);
        rlv_all_map_page.setItemAnimator(new DefaultItemAnimator());
        rlv_all_map_page.setLayoutManager(new LinearLayoutManager(getContext()));
        //添加Android自带的分割线
//        rlv_all_map_page.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mapPagesChoiceAdapter = new MapPagesChoiceAdapter(getContext(), mapPagesList);
        rlv_all_map_page.setAdapter(mapPagesChoiceAdapter);
    }

    private void stopDeleteMapPagesTimer() {
        if (scheduledThreadDeleteMapPages != null) {
            scheduledThreadDeleteMapPages.shutdownNow();
            scheduledThreadDeleteMapPages = null;
        }
    }

    private void exit() {
        ScheduledThreadUtils.stopGetAllMapPagesTimer();
        stopDeleteMapPagesTimer();
    }
}
