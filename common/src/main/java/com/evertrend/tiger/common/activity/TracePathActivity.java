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
import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.bean.MapPages;
import com.evertrend.tiger.common.bean.TracePath;
import com.evertrend.tiger.common.bean.event.ChoiceBaseTraceEvent;
import com.evertrend.tiger.common.bean.event.CreateNewBaseTraceSuccessEvent;
import com.evertrend.tiger.common.bean.event.DeleteBaseTraceEvent;
import com.evertrend.tiger.common.bean.event.UpdateBaseTraceSuccessEvent;
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

public class TracePathActivity extends BaseActivity {
    public static final String TAG = TracePathActivity.class.getSimpleName();

    private RecyclerView rlv_all_trace;
    private ImageButton ibtn_create_trace;
    private BaseTraceBottomPopupView traceBottomPopupView;

    private Device device;
    private MapPages mapPages;
    private List<TracePath> tracePathList;
    private BaseTraceAdapter baseTraceAdapter;

    private ScheduledThreadPoolExecutor scheduledThreadGetMapPagesAllPath;
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
        tracePathList = new ArrayList<>();
        scheduledThreadGetMapPagesAllPath = new ScheduledThreadPoolExecutor(3);
        scheduledThreadGetMapPagesAllPath.scheduleAtFixedRate(new CommTaskUtils.TaskGetMapPagesAllPath(device, mapPages),
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
    public void onMainEvent(GetMapPagesAllPathSuccessEvent event) {
        stopGetMapPagesAllPathTimer();
        LogUtil.i(this, TAG, "size : "+event.getTracePathList().size());
        tracePathList = event.getTracePathList();
        showTracePath();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CreateNewBaseTraceSuccessEvent event) {
        refreshTracePathList((TracePath) event.getBaseTrace(), CommonConstants.LIST_OPERATION_CREATE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UpdateBaseTraceSuccessEvent event) {
        TracePath tracePath = (TracePath) event.getBaseTrace();
        refreshTracePathList(tracePath, CommonConstants.LIST_OPERATION_UPDATE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DeleteBaseTraceEvent event) {
        stopDeleteBaseTraceTimer();
        DialogUtil.hideProgressDialog();
        refreshTracePathList((TracePath) event.getBaseTrace(), CommonConstants.LIST_OPERATION_DELETE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ChoiceBaseTraceEvent event) {
        if (event.getType() == CommonConstants.LIST_OPERATION_DELETE) {
            DialogUtil.showProgressDialog(this, getResources().getString(R.string.yl_common_deleting), false, false);
            scheduledThreadDelete = new ScheduledThreadPoolExecutor(3);
            scheduledThreadDelete.scheduleAtFixedRate(new CommTaskUtils.TaskDeleteBaseTrace(device, event.getBaseTrace(), CommonConstants.TYPE_MAPPAGE_OPERATION_TRACE_PATH),
                    0, 8, TimeUnit.SECONDS);
        } else if (event.getType() == CommonConstants.LIST_OPERATION_UPDATE) {
            traceBottomPopupView = new BaseTraceBottomPopupView(TracePathActivity.this, device, mapPages,
                    CommonConstants.TYPE_MAPPAGE_OPERATION_TRACE_PATH, event.getBaseTrace());
            showBottomPopup(traceBottomPopupView);
        }
    }

    private void refreshTracePathList(TracePath tracePath, int operation) {
        if (CommonConstants.LIST_OPERATION_CREATE == operation) {
            tracePathList.add(tracePath);
            baseTraceAdapter.notifyItemInserted(tracePathList.size()-1);
        } else {
            for (int i = 0; i < tracePathList.size(); i++) {
                if (tracePath.getId() == tracePathList.get(i).getId()) {
                    if (CommonConstants.LIST_OPERATION_DELETE == operation) {
                        tracePathList.remove(i);
                        baseTraceAdapter.notifyItemRemoved(i);
                    } else if (CommonConstants.LIST_OPERATION_UPDATE == operation) {
                        tracePathList.set(i, tracePath);
                        baseTraceAdapter.notifyItemChanged(i);
                    }
                }
            }
        }
    }

    private void exit() {
        stopDeleteBaseTraceTimer();
        stopGetMapPagesAllPathTimer();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void stopGetMapPagesAllPathTimer() {
        if (scheduledThreadGetMapPagesAllPath != null) {
            scheduledThreadGetMapPagesAllPath.shutdownNow();
            scheduledThreadGetMapPagesAllPath = null;
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
        baseTraceAdapter = new BaseTraceAdapter(this, tracePathList);
        rlv_all_trace.setAdapter(baseTraceAdapter);
    }

    private void initView() {
        rlv_all_trace = findViewById(R.id.rlv_all_trace);
        ibtn_create_trace = findViewById(R.id.ibtn_create_trace);
        ibtn_create_trace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                traceBottomPopupView = new BaseTraceBottomPopupView(TracePathActivity.this, device, mapPages, CommonConstants.TYPE_MAPPAGE_OPERATION_TRACE_PATH);
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
