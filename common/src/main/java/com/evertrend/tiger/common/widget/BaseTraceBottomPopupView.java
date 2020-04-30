package com.evertrend.tiger.common.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.evertrend.tiger.common.R;
import com.evertrend.tiger.common.bean.BaseTrace;
import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.bean.MapPages;
import com.evertrend.tiger.common.bean.event.CreateNewBaseTraceSuccessEvent;
import com.evertrend.tiger.common.bean.event.UpdateBaseTraceSuccessEvent;
import com.evertrend.tiger.common.utils.general.DialogUtil;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.common.utils.network.CommTaskUtils;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BaseTraceBottomPopupView extends BottomPopupView {
    private static  final String TAG = BaseTraceBottomPopupView.class.getSimpleName();

    private Device device;
    private MapPages mapPages;
    private Context context;
    private int type;
    private BaseTrace baseTrace;
    private BaseTrace newBaseTrace;

    private TextView tv_map_name;
    private EditText et_name, et_desc;
    private Button btn_submit;

    private ScheduledThreadPoolExecutor scheduledThreadSaveBaseTrace;

    public BaseTraceBottomPopupView(Context context, Device device, MapPages mapPages, int type) {
        super(context);
        this.device = device;
        this.mapPages = mapPages;
        this.context = context;
        this.type = type;
        newBaseTrace = new BaseTrace();
    }

    public BaseTraceBottomPopupView(Context context, Device device, MapPages mapPages, int type, BaseTrace baseTrace) {
        super(context);
        this.device = device;
        this.mapPages = mapPages;
        this.context = context;
        this.type = type;
        this.baseTrace = baseTrace;
        newBaseTrace = new BaseTrace();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.yl_common_dialog_base_trace;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
        if (baseTrace != null) {
            newBaseTrace.setBaseTrace(baseTrace);
            initViewData();
        }
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        exit();
        super.onDetachedFromWindow();
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(context)*.5f);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CreateNewBaseTraceSuccessEvent event) {
        Toast.makeText(context, "create success", Toast.LENGTH_SHORT).show();
        DialogUtil.hideProgressDialog();
        stopSaveBaseTraceTimer();
        dismiss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UpdateBaseTraceSuccessEvent event) {
        Toast.makeText(context, "update success", Toast.LENGTH_SHORT).show();
        DialogUtil.hideProgressDialog();
        stopSaveBaseTraceTimer();
        dismiss();
    }

    private void exit() {
        stopSaveBaseTraceTimer();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void initView() {
        tv_map_name = findViewById(R.id.tv_map_name);
        tv_map_name.setText(mapPages.getName());
        et_name = findViewById(R.id.et_name);
        et_desc = findViewById(R.id.et_desc);
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                newBaseTrace.setName(et_name.getText().toString().trim());
                newBaseTrace.setDesc(et_desc.getText().toString().trim());
                newBaseTrace.setDeviceId(device.getId());
                newBaseTrace.setMapPage(mapPages.getId());
                if (TextUtils.isEmpty(newBaseTrace.getName())) {
                    Toast.makeText(context, "请输入名称", Toast.LENGTH_SHORT).show();
                } else {
                    if (baseTrace != null) {
                        if (baseTrace.getName().equals(newBaseTrace.getName()) && baseTrace.getDesc().equals(newBaseTrace.getDesc())) {
                            Toast.makeText(context, "请修改名称或描述", Toast.LENGTH_SHORT).show();
                        } else {
                            startUpdateBaseTrace();
                        }
                    } else {
                        startSaveBaseTrace();
                    }
                }
            }
        });
    }

    private void initViewData() {
        LogUtil.i(context, TAG, "clean tack : " + newBaseTrace.toString());
        et_name.setText(newBaseTrace.getName());
        et_name.setSelection(newBaseTrace.getName().length());
        et_desc.setText(newBaseTrace.getDesc());
        et_desc.setSelection(newBaseTrace.getDesc().length());
    }

    private void startSaveBaseTrace() {
        DialogUtil.showProgressDialog(context, getResources().getString(R.string.yl_common_saving), false, false);
        scheduledThreadSaveBaseTrace = new ScheduledThreadPoolExecutor(5);
        scheduledThreadSaveBaseTrace.scheduleAtFixedRate(new CommTaskUtils.TaskSaveBaseTrace(newBaseTrace, type),
                0, 8, TimeUnit.SECONDS);
    }

    private void startUpdateBaseTrace() {
        DialogUtil.showProgressDialog(context, getResources().getString(R.string.yl_common_saving), false, false);
        scheduledThreadSaveBaseTrace = new ScheduledThreadPoolExecutor(5);
        scheduledThreadSaveBaseTrace.scheduleAtFixedRate(new CommTaskUtils.TaskUpdateBaseTrace(newBaseTrace, type),
                0, 8, TimeUnit.SECONDS);
    }

    private void stopSaveBaseTraceTimer() {
        if (scheduledThreadSaveBaseTrace != null) {
            scheduledThreadSaveBaseTrace.shutdownNow();
            scheduledThreadSaveBaseTrace = null;
        }
    }
}
