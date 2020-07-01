package com.evertrend.tiger.common.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.evertrend.tiger.common.R;
import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.bean.DeviceGrant;
import com.evertrend.tiger.common.bean.event.CreateNewBaseTraceSuccessEvent;
import com.evertrend.tiger.common.bean.event.CreateNewDeviceGrantFailEvent;
import com.evertrend.tiger.common.bean.event.CreateNewDeviceGrantSuccessEvent;
import com.evertrend.tiger.common.bean.event.UpdateBaseTraceSuccessEvent;
import com.evertrend.tiger.common.bean.event.UpdateDeviceGrantSuccessEvent;
import com.evertrend.tiger.common.utils.general.DialogUtil;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.common.utils.network.CommTaskUtils;
import com.evertrend.tiger.common.utils.network.CommonNetReq;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashSet;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GrantBottomPopupView extends BottomPopupView implements CompoundButton.OnCheckedChangeListener {
    private static  final String TAG = GrantBottomPopupView.class.getSimpleName();

    private Device device;
    private Context context;
    private DeviceGrant deviceGrant;
    private DeviceGrant newDeviceGrant;

    private CheckBox cb_create_map, cb_edit_path;
    private EditText et_authorized_user;
    private Button btn_submit;
    private HashSet<Integer> authItems;

    private ScheduledThreadPoolExecutor scheduledThreadSaveDeviceGrant;

    public GrantBottomPopupView(Context context, Device device) {
        super(context);
        this.device = device;
        this.context = context;
        newDeviceGrant = new DeviceGrant();
    }

    public GrantBottomPopupView(Context context, Device device, DeviceGrant deviceGrant) {
        super(context);
        this.device = device;
        this.context = context;
        this.deviceGrant = deviceGrant;
        newDeviceGrant = new DeviceGrant();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.yl_common_device_grant;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
        if (deviceGrant != null) {
            newDeviceGrant.setDeviceGrant(deviceGrant);
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
    public void onEventMainThread(CreateNewDeviceGrantSuccessEvent event) {
        Toast.makeText(context, "create success", Toast.LENGTH_SHORT).show();
        DialogUtil.hideProgressDialog();
        stopSaveDeviceGrantTimer();
        dismiss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CreateNewDeviceGrantFailEvent event) {
        DialogUtil.hideProgressDialog();
        stopSaveDeviceGrantTimer();
        showFail(event.getFailCode());
        dismiss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UpdateDeviceGrantSuccessEvent event) {
        Toast.makeText(context, "update success", Toast.LENGTH_SHORT).show();
        DialogUtil.hideProgressDialog();
        stopSaveDeviceGrantTimer();
        dismiss();
    }

    private void exit() {
        stopSaveDeviceGrantTimer();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void initView() {
        authItems = new HashSet<>();
        cb_create_map = findViewById(R.id.cb_create_map);
        cb_edit_path = findViewById(R.id.cb_edit_path);
        cb_create_map.setOnCheckedChangeListener(this);
        cb_edit_path.setOnCheckedChangeListener(this);
        et_authorized_user = findViewById(R.id.et_authorized_user);
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                newDeviceGrant.setUser(et_authorized_user.getText().toString().trim());
                newDeviceGrant.setDevice(device.getId());
                newDeviceGrant.setUser_flag(1);
                if (!authItems.isEmpty()) {
                    String tmp = authItems.toString();
                    LogUtil.d(TAG, "item : "+authItems.toString().substring(1, tmp.length() - 1));
                    newDeviceGrant.setAuthorization_item(authItems.toString().substring(1, tmp.length() - 1));
                } else {
                    newDeviceGrant.setAuthorization_item("");
                }
                if (TextUtils.isEmpty(newDeviceGrant.getUser())) {
                    Toast.makeText(context, "请输入被授权用户", Toast.LENGTH_SHORT).show();
                } else {
                    if (deviceGrant != null) {
                        if (deviceGrant.equals(newDeviceGrant)) {
                            Toast.makeText(context, "授权信息未改变", Toast.LENGTH_SHORT).show();
                        } else {
                            startUpdateDeviceGrant();
                        }
                    } else {
                        startSaveDeviceGrant();
                    }
                }
            }
        });
    }

    private void initViewData() {
        LogUtil.i(context, TAG, "grant : " + newDeviceGrant.toString());
        et_authorized_user.setText(newDeviceGrant.getUser());
        et_authorized_user.setEnabled(false);
//        et_authorized_user.setSelection(newDeviceGrant.getUser().length());
        if (!TextUtils.isEmpty(newDeviceGrant.getAuthorization_item())) {
            String[] items = newDeviceGrant.getAuthorization_item().split(",");
            if (items.length > 0) {
                for (int i = 0; i < items.length; i++) {
                    if (Integer.parseInt(items[i].trim()) == 1) {
                        cb_create_map.setChecked(true);
                    } else if (Integer.parseInt(items[i].trim()) == 2) {
                        cb_edit_path.setChecked(true);
                    }
                }
            }
        }
    }

    private void startSaveDeviceGrant() {
        DialogUtil.showProgressDialog(context, getResources().getString(R.string.yl_common_saving), false, false);
        scheduledThreadSaveDeviceGrant = new ScheduledThreadPoolExecutor(5);
        scheduledThreadSaveDeviceGrant.scheduleAtFixedRate(new CommTaskUtils.TaskSaveDeviceGrant(newDeviceGrant),
                0, 8, TimeUnit.SECONDS);
    }

    private void startUpdateDeviceGrant() {
        DialogUtil.showProgressDialog(context, getResources().getString(R.string.yl_common_saving), false, false);
        scheduledThreadSaveDeviceGrant = new ScheduledThreadPoolExecutor(5);
        scheduledThreadSaveDeviceGrant.scheduleAtFixedRate(new CommTaskUtils.TaskUpdateDeviceGrant(newDeviceGrant),
                0, 8, TimeUnit.SECONDS);
    }

    private void stopSaveDeviceGrantTimer() {
        if (scheduledThreadSaveDeviceGrant != null) {
            scheduledThreadSaveDeviceGrant.shutdownNow();
            scheduledThreadSaveDeviceGrant = null;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.cb_create_map) {
            if (isChecked) {
                authItems.add(1);
            } else {
                authItems.remove(1);
            }
        } else if (buttonView.getId() == R.id.cb_edit_path) {
            if (isChecked) {
                authItems.add(2);
            } else {
                authItems.remove(2);
            }
        }
    }

    private void showFail(int failCode) {
        switch (failCode) {
            case CommonNetReq.ERR_CODE_GRANTED_USER_NON_EXISTENT:
                DialogUtil.showToast(context, R.string.yl_common_granted_user_non_existent, Toast.LENGTH_SHORT);
                break;
            case CommonNetReq.ERR_CODE_DUPLICATE_AUTHORIZATION:
                DialogUtil.showToast(context, R.string.yl_common_duplicate_authorization, Toast.LENGTH_SHORT);
                break;
            case CommonNetReq.ERR_CODE_CANNOT_AUTHORIZE_TOYOURSELF:
                DialogUtil.showToast(context, R.string.yl_common_cannot_authorize_to_yourself, Toast.LENGTH_SHORT);
                break;
            case CommonNetReq.ERR_CODE_SAVE_FAIL:
                DialogUtil.showToast(context, R.string.yl_common_authorization_fail, Toast.LENGTH_SHORT);
                break;
        }
    }
}
