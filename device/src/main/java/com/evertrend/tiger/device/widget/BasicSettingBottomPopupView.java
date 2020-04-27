package com.evertrend.tiger.device.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.evertrend.tiger.common.utils.general.DialogUtil;
import com.evertrend.tiger.device.R;
import com.evertrend.tiger.device.bean.Device;
import com.evertrend.tiger.device.bean.event.SaveBasicConfigEvent;
import com.evertrend.tiger.device.utils.Constants;
import com.evertrend.tiger.device.utils.TaskUtils;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BasicSettingBottomPopupView extends BottomPopupView implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = BasicSettingBottomPopupView.class.getSimpleName();

    private CheckBox cb_enable_auto_recharge;
    private CheckBox cb_enable_auto_add_water;
    private CheckBox cb_enable_auto_empty_trash;
    private EditText et_basic_battery_low_limit;
    private EditText et_basic_water_low_limit;
    private EditText et_basic_empty_trash_interval;
    private Button btn_submit;
    private LinearLayout ll_config_battery_low_limit;
    private LinearLayout ll_config_water_low_limit;
    private LinearLayout ll_config_empty_trash_interval;

    private Context context;
    private Device device;
    private int batteryLimit = -1;
    private int waterLimit = -1;
    private int emptyTrash = -1;
    private int enableRecharge = -1;
    private int enableAddWater = -1;
    private int enableEmptyTrash = -1;

    private ScheduledThreadPoolExecutor scheduledThreadSaveBasicConfig;

    public BasicSettingBottomPopupView(@NonNull Context context) {
        super(context);
    }

    public BasicSettingBottomPopupView(Context context, Device device) {
        super(context);
        this.context = context;
        this.device = device;
        batteryLimit = device.getBattery_low_limit();
        waterLimit = device.getWater_low_limit();
        emptyTrash = device.getEmpty_trash_interval();
        enableRecharge = device.getEnable_auto_recharge();
        enableAddWater = device.getEnable_auto_add_water();
        enableEmptyTrash = device.getEnable_auto_empty_trash();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.yl_device_dialog_basic_setting;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        exit();
        super.onDetachedFromWindow();
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext()) * .6f);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (buttonView.getId() == R.id.cb_enable_auto_recharge) {
                device.setEnable_auto_recharge(1);
            } else if (buttonView.getId() == R.id.cb_enable_auto_add_water) {
                device.setEnable_auto_add_water(1);
            } else if (buttonView.getId() == R.id.cb_enable_auto_empty_trash) {
                device.setEnable_auto_empty_trash(1);
            }
        } else {
            if (buttonView.getId() == R.id.cb_enable_auto_recharge) {
                device.setEnable_auto_recharge(0);
            } else if (buttonView.getId() == R.id.cb_enable_auto_add_water) {
                device.setEnable_auto_add_water(0);
            } else if (buttonView.getId() == R.id.cb_enable_auto_empty_trash) {
                device.setEnable_auto_empty_trash(0);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SaveBasicConfigEvent event) {
        stopSaveBasicConfigTimer();
        DialogUtil.hideProgressDialog();
        dismiss();
    }

    private void exit() {
        stopSaveBasicConfigTimer();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void stopSaveBasicConfigTimer() {
        if (scheduledThreadSaveBasicConfig != null) {
            scheduledThreadSaveBasicConfig.shutdownNow();
            scheduledThreadSaveBasicConfig = null;
        }
    }

    private void initView() {
        cb_enable_auto_recharge = findViewById(R.id.cb_enable_auto_recharge);
        cb_enable_auto_add_water = findViewById(R.id.cb_enable_auto_add_water);
        cb_enable_auto_empty_trash = findViewById(R.id.cb_enable_auto_empty_trash);
        et_basic_battery_low_limit = findViewById(R.id.et_basic_battery_low_limit);
        et_basic_water_low_limit = findViewById(R.id.et_basic_water_low_limit);
        et_basic_empty_trash_interval = findViewById(R.id.et_basic_empty_trash_interval);
        ll_config_battery_low_limit = findViewById(R.id.ll_config_battery_low_limit);
        ll_config_water_low_limit = findViewById(R.id.ll_config_water_low_limit);
        ll_config_empty_trash_interval = findViewById(R.id.ll_config_empty_trash_interval);
        btn_submit = findViewById(R.id.btn_submit);
        cb_enable_auto_recharge.setOnCheckedChangeListener(this);
        cb_enable_auto_add_water.setOnCheckedChangeListener(this);
        cb_enable_auto_empty_trash.setOnCheckedChangeListener(this);
        btn_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkInputIsNull()) {
                    batteryLimit = Integer.parseInt(et_basic_battery_low_limit.getText().toString().trim());
                    waterLimit = Integer.parseInt(et_basic_water_low_limit.getText().toString().trim());
                    emptyTrash = Integer.parseInt(et_basic_empty_trash_interval.getText().toString().trim());
                    saveBasicConfig();
                }
            }
        });
        if (device.getEnable_auto_recharge() == 1) {
            cb_enable_auto_recharge.setChecked(true);
        } else {
            cb_enable_auto_recharge.setChecked(false);
        }
        if (device.getEnable_auto_add_water() == 1) {
            cb_enable_auto_add_water.setChecked(true);
        } else {
            cb_enable_auto_add_water.setChecked(false);
        }
        if (device.getEnable_auto_empty_trash() == 1) {
            cb_enable_auto_empty_trash.setChecked(true);
        } else {
            cb_enable_auto_empty_trash.setChecked(false);
        }
        et_basic_battery_low_limit.setText(device.getBattery_low_limit() + "");
        et_basic_battery_low_limit.setSelection(String.valueOf(device.getBattery_low_limit()).length());
        et_basic_water_low_limit.setText(device.getWater_low_limit() + "");
        et_basic_water_low_limit.setSelection(String.valueOf(device.getWater_low_limit()).length());
        et_basic_empty_trash_interval.setText(device.getEmpty_trash_interval() + "");
        et_basic_empty_trash_interval.setSelection(String.valueOf(device.getEmpty_trash_interval()).length());
        initShow();
    }

    private void initShow() {
        switch (device.getDevice_type()) {
            case Constants.DEVICE_TYPE_EVBOT_SL:
                ll_config_water_low_limit.setVisibility(GONE);
                ll_config_empty_trash_interval.setVisibility(GONE);
                cb_enable_auto_add_water.setVisibility(GONE);
                cb_enable_auto_empty_trash.setVisibility(GONE);
                break;
            case Constants.DEVICE_TYPE_SWBOT_SL:
                break;
            case Constants.DEVICE_TYPE_MFBOT_SL:
                ll_config_empty_trash_interval.setVisibility(GONE);
                cb_enable_auto_empty_trash.setVisibility(GONE);
                break;
            case Constants.DEVICE_TYPE_SWBOT_AP:
                break;
            case Constants.DEVICE_TYPE_SWBOT_MINI:
                ll_config_water_low_limit.setVisibility(GONE);
                cb_enable_auto_add_water.setVisibility(GONE);
                break;
            default:
                ll_config_battery_low_limit.setVisibility(GONE);
                ll_config_water_low_limit.setVisibility(GONE);
                ll_config_empty_trash_interval.setVisibility(GONE);
                cb_enable_auto_recharge.setVisibility(GONE);
                cb_enable_auto_add_water.setVisibility(GONE);
                cb_enable_auto_empty_trash.setVisibility(GONE);
                break;
        }
    }

    private void saveBasicConfig() {
        if (checkBasicConfigValue()) {
            device.setBattery_low_limit(batteryLimit);
            device.setWater_low_limit(waterLimit);
            device.setEmpty_trash_interval(emptyTrash);
            DialogUtil.showProgressDialog(context, getResources().getString(R.string.yl_common_saving), false, false);
            scheduledThreadSaveBasicConfig = new ScheduledThreadPoolExecutor(4);
            scheduledThreadSaveBasicConfig.scheduleAtFixedRate(new TaskUtils.TaskSaveBasicConfigValue(device),
                    0, 6, TimeUnit.SECONDS);
        }
    }

    private boolean checkBasicConfigValue() {
        switch (device.getDevice_type()) {
            case Constants.DEVICE_TYPE_EVBOT_SL:
                return checkBatteryValue();
            case Constants.DEVICE_TYPE_SWBOT_SL:
                return checkBatteryValue() && checkWaterValue() && checkEmptyTrashValue();
            case Constants.DEVICE_TYPE_MFBOT_SL:
                return checkBatteryValue() && checkWaterValue();
            case Constants.DEVICE_TYPE_SWBOT_AP:
                return checkBatteryValue() && checkWaterValue() && checkEmptyTrashValue();
            case Constants.DEVICE_TYPE_SWBOT_MINI:
                return checkBatteryValue() && checkEmptyTrashValue();
        }
        return false;
    }

    private boolean checkInputIsNull() {
        if (TextUtils.isEmpty(et_basic_battery_low_limit.getText().toString().trim())
                || TextUtils.isEmpty(et_basic_water_low_limit.getText().toString().trim())
                || TextUtils.isEmpty(et_basic_empty_trash_interval.getText().toString().trim())) {
            showInputEmptyToast();
            return true;
        }
        return false;
    }

    private boolean checkBatteryValue() {
        if (batteryLimit < 5 || batteryLimit > 20) {
            Toast.makeText(context, "低电量自动回充输入不在范围内", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }

    private boolean checkWaterValue() {
        if (waterLimit < 1 || waterLimit > 5) {
            Toast.makeText(context, "低水位自动加水输入不在范围内", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }

    private boolean checkEmptyTrashValue() {
        if (emptyTrash < 60 || emptyTrash > 240) {
            Toast.makeText(context, "倾倒垃圾时间间隔输入不在范围内", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }

    private void showInputEmptyToast() {
        Toast.makeText(context, "输入为空", Toast.LENGTH_SHORT).show();
    }

}
