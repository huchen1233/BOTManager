package com.evertrend.tiger.common.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evertrend.tiger.common.R;
import com.evertrend.tiger.common.adapter.BaseTraceAdapter;
import com.evertrend.tiger.common.bean.BaseTrace;
import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.bean.MapPages;
import com.evertrend.tiger.common.bean.TracePath;
import com.evertrend.tiger.common.bean.VirtualTrackGroup;
import com.evertrend.tiger.common.bean.event.ChoiceBaseTraceEvent;
import com.evertrend.tiger.common.bean.event.CreateNewBaseTraceSuccessEvent;
import com.evertrend.tiger.common.bean.event.DeleteBaseTraceEvent;
import com.evertrend.tiger.common.bean.event.DialogChoiceEvent;
import com.evertrend.tiger.common.bean.event.UpdateBaseTraceSuccessEvent;
import com.evertrend.tiger.common.bean.event.map.GetAllVirtualTrackGroupSuccessEvent;
import com.evertrend.tiger.common.bean.event.map.GetMapPagesAllPathSuccessEvent;
import com.evertrend.tiger.common.utils.general.CommonConstants;
import com.evertrend.tiger.common.utils.general.DialogUtil;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.common.utils.network.CommTaskUtils;
import com.evertrend.tiger.common.widget.BaseTraceBottomPopupView;
import com.lxj.xpopup.XPopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class VirtualTrackGroupActivity extends BaseActivity {
    public static final String TAG = VirtualTrackGroupActivity.class.getSimpleName();

    private RecyclerView rlv_all_trace;
    private ImageButton ibtn_create_trace;
    private BaseTraceBottomPopupView traceBottomPopupView;

    private Device device;
    private MapPages mapPages;
    private List<VirtualTrackGroup> virtualTrackGroupList;
    private BaseTraceAdapter baseTraceAdapter;
    private BaseTrace baseTrace;

    private ScheduledThreadPoolExecutor scheduledThreadGetMapPagesAllVirtualTrackGroup;
    private ScheduledThreadPoolExecutor scheduledThreadDelete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.yl_common_activity_base_trace);
        device = (Device) getIntent().getSerializableExtra("device");
        mapPages = (MapPages) getIntent().getSerializableExtra("mappage");
        virtualTrackGroupList = new ArrayList<>();
        scheduledThreadGetMapPagesAllVirtualTrackGroup = new ScheduledThreadPoolExecutor(3);
        scheduledThreadGetMapPagesAllVirtualTrackGroup.scheduleAtFixedRate(new CommTaskUtils.TaskGetMapPagesAllVirtualTrackGroup(device, mapPages),
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
    public void onMainEvent(GetAllVirtualTrackGroupSuccessEvent event) {
        stopGetMapPagesAllVirtualTrackTimer();
        LogUtil.i(this, TAG, "size : "+event.getVirtualTrackGroups().size());
        virtualTrackGroupList = event.getVirtualTrackGroups();
        showTracePath();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CreateNewBaseTraceSuccessEvent event) {
        refreshTracePathList((VirtualTrackGroup) event.getBaseTrace(), CommonConstants.LIST_OPERATION_CREATE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UpdateBaseTraceSuccessEvent event) {
        VirtualTrackGroup virtualTrackGroup = (VirtualTrackGroup) event.getBaseTrace();
        refreshTracePathList(virtualTrackGroup, CommonConstants.LIST_OPERATION_UPDATE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DialogChoiceEvent event) {
        scheduledThreadDelete = new ScheduledThreadPoolExecutor(3);
        scheduledThreadDelete.scheduleAtFixedRate(new CommTaskUtils.TaskDeleteBaseTrace(device, baseTrace, CommonConstants.TYPE_MAPPAGE_OPERATION_VIRTUAL_TRACK),
                0, 8, TimeUnit.SECONDS);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DeleteBaseTraceEvent event) {
        stopDeleteBaseTraceTimer();
        DialogUtil.hideProgressDialog();
        refreshTracePathList((VirtualTrackGroup) event.getBaseTrace(), CommonConstants.LIST_OPERATION_DELETE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ChoiceBaseTraceEvent event) {
        baseTrace = event.getBaseTrace();
        if (event.getType() == CommonConstants.LIST_OPERATION_DELETE) {
            String deleteConfirm = getResources().getString(R.string.yl_common_delete_virtual_track_group_confirm);
            DialogUtil.showChoiceDialog(this, String.format(deleteConfirm, baseTrace.getName()), CommonConstants.TYPE_MAPPAGE_OPERATION_DELETE);
        } else if (event.getType() == CommonConstants.LIST_OPERATION_UPDATE) {
            traceBottomPopupView = new BaseTraceBottomPopupView(VirtualTrackGroupActivity.this, device, mapPages,
                    CommonConstants.TYPE_MAPPAGE_OPERATION_VIRTUAL_TRACK, baseTrace);
            showBottomPopup(traceBottomPopupView);
        }
    }

    private void refreshTracePathList(VirtualTrackGroup virtualTrackGroup, int operation) {
        if (CommonConstants.LIST_OPERATION_CREATE == operation) {
            virtualTrackGroupList.add(virtualTrackGroup);
            baseTraceAdapter.notifyItemInserted(virtualTrackGroupList.size()-1);
        } else {
            for (int i = 0; i < virtualTrackGroupList.size(); i++) {
                if (virtualTrackGroup.getId() == virtualTrackGroupList.get(i).getId()) {
                    if (CommonConstants.LIST_OPERATION_DELETE == operation) {
                        virtualTrackGroupList.remove(i);
                        baseTraceAdapter.notifyItemRemoved(i);
                    } else if (CommonConstants.LIST_OPERATION_UPDATE == operation) {
                        virtualTrackGroupList.set(i, virtualTrackGroup);
                        baseTraceAdapter.notifyItemChanged(i);
                    }
                }
            }
        }
    }

    private void exit() {
        stopDeleteBaseTraceTimer();
        stopGetMapPagesAllVirtualTrackTimer();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void stopGetMapPagesAllVirtualTrackTimer() {
        if (scheduledThreadGetMapPagesAllVirtualTrackGroup != null) {
            scheduledThreadGetMapPagesAllVirtualTrackGroup.shutdownNow();
            scheduledThreadGetMapPagesAllVirtualTrackGroup = null;
        }
    }

    private void stopDeleteBaseTraceTimer() {
        if (scheduledThreadDelete != null) {
            scheduledThreadDelete.shutdownNow();
            scheduledThreadDelete = null;
        }
    }

    private void showTracePath() {
        rlv_all_trace.setHasFixedSize(true);
        rlv_all_trace.setItemAnimator(new DefaultItemAnimator());
        rlv_all_trace.setLayoutManager(new LinearLayoutManager(this));
        baseTraceAdapter = new BaseTraceAdapter(this, virtualTrackGroupList, CommonConstants.TYPE_MAPPAGE_OPERATION_VIRTUAL_TRACK);
        rlv_all_trace.setAdapter(baseTraceAdapter);
    }

    private void initView() {
        rlv_all_trace = findViewById(R.id.rlv_all_trace);
        ibtn_create_trace = findViewById(R.id.ibtn_create_trace);
        ibtn_create_trace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                traceBottomPopupView = new BaseTraceBottomPopupView(VirtualTrackGroupActivity.this, device, mapPages, CommonConstants.TYPE_MAPPAGE_OPERATION_VIRTUAL_TRACK);
                showBottomPopup(traceBottomPopupView);
            }
        });
    }

    private void showBottomPopup(BaseTraceBottomPopupView baseTraceBottomPopupView) {
        new XPopup.Builder(this)
                .autoOpenSoftInput(true)
                .asCustom(baseTraceBottomPopupView)
                .show();
    }
}
