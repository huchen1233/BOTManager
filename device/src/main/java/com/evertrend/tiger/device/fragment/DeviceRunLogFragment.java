package com.evertrend.tiger.device.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.bean.RunLog;
import com.evertrend.tiger.common.fragment.BaseFragment;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.device.R;
import com.evertrend.tiger.device.adapter.RunLogAdapter;
import com.evertrend.tiger.common.bean.event.GetRunLogsSuccessEvent;
import com.evertrend.tiger.device.bean.event.SpinnerChoiceDeviceMessageEvent;
import com.evertrend.tiger.common.utils.general.ScheduledThreadUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class DeviceRunLogFragment extends BaseFragment {
    public static final String TAG = DeviceStatusFragment.class.getSimpleName();

    private View root;
    private RecyclerView rlv_run_log;
    private SmartRefreshLayout srl_run_log;

    private Device device;
    private List<RunLog> runLogList;
    private RunLogAdapter runLogAdapter;
    private int page = 1;
    private boolean loadMoreType = true;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.yl_device_fragment_run_log, container, false);
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(SpinnerChoiceDeviceMessageEvent messageEvent) {
        device = messageEvent.getDevice();
//        DialogUtil.showProgressDialog(getContext(), getResources().getString(R.string.yl_common_getting), true, true);
        ScheduledThreadUtils.ThreadGetRunLogs(device, page);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GetRunLogsSuccessEvent messageEvent) {
        ScheduledThreadUtils.stopGetRunLogsTimer();
//        DialogUtil.hideProgressDialog();
        List<RunLog> runLogs = messageEvent.getRunLogList();
        LogUtil.d(TAG, "runLogs size:"+runLogs.size());
        runLogList.addAll(runLogs);
        runLogAdapter.notifyDataSetChanged();
        if (runLogs.size() < 20) {
            loadMoreType = false;
        }
        EventBus.getDefault().removeStickyEvent(messageEvent);
    }

    private void initView() {
        srl_run_log = root.findViewById(R.id.srl_run_log);
        rlv_run_log = root.findViewById(R.id.rlv_run_log);

        rlv_run_log.setHasFixedSize(true);
        rlv_run_log.setItemAnimator(new DefaultItemAnimator());
        rlv_run_log.setLayoutManager(new LinearLayoutManager(getContext()));
        //添加Android自带的分割线
        rlv_run_log.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        runLogList = new ArrayList<>();
//        test();
        runLogAdapter = new RunLogAdapter(getContext(), runLogList);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.yl_device_view_run_log_item_header, null);
        runLogAdapter.setHeaderView(view);
        rlv_run_log.setAdapter(runLogAdapter);

        srl_run_log.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                srl_run_log.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreType = true;
                        page = 1;
                        runLogList.removeAll(runLogList);
                        runLogAdapter.notifyDataSetChanged();
                        ScheduledThreadUtils.ThreadGetRunLogs(device, page);
                        srl_run_log.finishRefresh();
                    }
                }, 2000);
            }
        });

        srl_run_log.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                srl_run_log.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!loadMoreType) {
                            srl_run_log.finishLoadMoreWithNoMoreData();
                            return;
                        }
                        page++;
                        ScheduledThreadUtils.ThreadGetRunLogs(device, page);
                        srl_run_log.setEnableLoadMore(true);
                        srl_run_log.finishLoadMore();
                    }
                }, 2000);
            }
        });
    }
}
