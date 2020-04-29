package com.evertrend.tiger.device.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.fragment.BaseFragment;
import com.evertrend.tiger.common.utils.general.DialogUtil;
import com.evertrend.tiger.device.R;
import com.evertrend.tiger.device.bean.event.DeviceMessageEvent;
import com.evertrend.tiger.device.bean.event.OperationStopEvent;
import com.evertrend.tiger.device.bean.event.SetStatusCompleteEvent;
import com.evertrend.tiger.device.bean.event.SetStatusSuccessEvent;
import com.evertrend.tiger.device.utils.TaskUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DeviceOperationFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {
    public static final String TAG = DeviceOperationFragment.class.getCanonicalName();

    private View root;
    private Switch sw_device_run;
    private RadioGroup rg_device_work_control;
    private RadioButton rb_go_to_idle, rb_go_to_garage, rb_go_to_recharge, rb_go_to_add_water, rb_go_to_empty_trash, rb_go_to_work;
    private Switch sw_main_sweep, sw_side_sweep, sw_sprinkling_water;
    private Switch sw_left_tail_light, sw_alarm_light, sw_front_light;
    private Switch sw_right_tail_light, sw_horn, sw_suck_fan, sw_vibrating_dust;
    private Switch sw_motor, sw_emergency_stop;

    private Device mDevice;
    private ProgressDialog mDialogOperation;

    private ScheduledThreadPoolExecutor scheduledThreadControl;
    private ScheduledThreadPoolExecutor scheduledThreadReadControlStatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.yl_device_fragment_device_operation, container, false);
        initView(root);
        EventBus.getDefault().register(this);
        setListener();
        return root;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventMainThread(DeviceMessageEvent event) {
        mDevice = event.getMessage();
        updateStatus(mDevice);
    }

    private void updateStatus(Device device) {
        if (device.getIs_running() == 1) {
            sw_device_run.setChecked(true);
        } else {
            sw_device_run.setChecked(false);
        }
        if (device.getMain_sweep_status() == 1) {
            sw_main_sweep.setChecked(true);
        } else {
            sw_main_sweep.setChecked(false);
        }
        if (device.getSide_sweep_status() == 1) {
            sw_side_sweep.setChecked(true);
        } else {
            sw_side_sweep.setChecked(false);
        }
        if (device.getSprinkling_water_status() == 1) {
            sw_sprinkling_water.setChecked(true);
        } else {
            sw_sprinkling_water.setChecked(false);
        }
        if (device.getFront_light_status() == 1) {
            sw_front_light.setChecked(true);
        } else {
            sw_front_light.setChecked(false);
        }
        if (device.getLeft_tail_light_status() == 1) {
            sw_left_tail_light.setChecked(true);
        } else {
            sw_left_tail_light.setChecked(false);
        }
        if (device.getRight_tail_light_status() == 1) {
            sw_right_tail_light.setChecked(true);
        } else {
            sw_right_tail_light.setChecked(false);
        }
        if (device.getAlarm_light_status() == 1) {
            sw_alarm_light.setChecked(true);
        } else {
            sw_alarm_light.setChecked(false);
        }
        if (device.getHorn_status() == 1) {
            sw_horn.setChecked(true);
        } else {
            sw_horn.setChecked(false);
        }
        if (device.getSuck_fan_status() == 1) {
            sw_suck_fan.setChecked(true);
        } else {
            sw_suck_fan.setChecked(false);
        }
        if (device.getVibrating_dust_status() == 1) {
            sw_vibrating_dust.setChecked(true);
        } else {
            sw_vibrating_dust.setChecked(false);
        }
        if (device.getMotor_release_status() == 1) {
            sw_motor.setChecked(true);
        } else {
            sw_motor.setChecked(false);
        }
        if (device.getEmergency_stop_status() == 1) {
            sw_emergency_stop.setChecked(true);
        } else {
            sw_emergency_stop.setChecked(false);
        }
        switch (device.getSet_current_task()) {
            case 0:
                rb_go_to_idle.setChecked(true);
                break;
            case 1:
                rb_go_to_garage.setChecked(true);
                break;
            case 2:
                rb_go_to_recharge.setChecked(true);
                break;
            case 3:
                rb_go_to_add_water.setChecked(true);
                break;
            case 4:
                rb_go_to_empty_trash.setChecked(true);
                break;
            case 5:
                rb_go_to_work.setChecked(true);
                break;
            default:
                rb_go_to_idle.setChecked(true);
                break;
        }
    }

    private void setListener() {
        sw_device_run.setOnCheckedChangeListener(this);
        rg_device_work_control.setOnCheckedChangeListener(this);
        sw_main_sweep.setOnCheckedChangeListener(this);
        sw_side_sweep.setOnCheckedChangeListener(this);
        sw_sprinkling_water.setOnCheckedChangeListener(this);
        sw_left_tail_light.setOnCheckedChangeListener(this);
        sw_right_tail_light.setOnCheckedChangeListener(this);
        sw_alarm_light.setOnCheckedChangeListener(this);
        sw_front_light.setOnCheckedChangeListener(this);
        sw_horn.setOnCheckedChangeListener(this);
        sw_suck_fan.setOnCheckedChangeListener(this);
        sw_vibrating_dust.setOnCheckedChangeListener(this);
        sw_motor.setOnCheckedChangeListener(this);
        sw_emergency_stop.setOnCheckedChangeListener(this);
    }

    private void initView(View root) {
        sw_device_run = root.findViewById(R.id.sw_device_run);
        rg_device_work_control = root.findViewById(R.id.rg_device_work_control);
        rb_go_to_idle = root.findViewById(R.id.rb_go_to_idle);
        rb_go_to_garage = root.findViewById(R.id.rb_go_to_garage);
        rb_go_to_recharge = root.findViewById(R.id.rb_go_to_recharge);
        rb_go_to_add_water = root.findViewById(R.id.rb_go_to_add_water);
        rb_go_to_empty_trash = root.findViewById(R.id.rb_go_to_empty_trash);
        rb_go_to_work = root.findViewById(R.id.rb_go_to_work);
        sw_main_sweep = root.findViewById(R.id.sw_main_sweep);
        sw_side_sweep = root.findViewById(R.id.sw_side_sweep);
        sw_sprinkling_water = root.findViewById(R.id.sw_sprinkling_water);
        sw_left_tail_light = root.findViewById(R.id.sw_left_tail_light);
        sw_right_tail_light = root.findViewById(R.id.sw_right_tail_light);
        sw_alarm_light = root.findViewById(R.id.sw_alarm_light);
        sw_front_light = root.findViewById(R.id.sw_front_light);
        sw_horn = root.findViewById(R.id.sw_horn);
        sw_suck_fan = root.findViewById(R.id.sw_suck_fan);
        sw_vibrating_dust = root.findViewById(R.id.sw_vibrating_dust);
        sw_motor = root.findViewById(R.id.sw_motor);
        sw_emergency_stop = root.findViewById(R.id.sw_emergency_stop);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.getCheckedRadioButtonId() == R.id.rb_go_to_idle) {
            startControlTimer(0, "rb_go_to_idle");
        } else if (group.getCheckedRadioButtonId() == R.id.rb_go_to_garage) {
            startControlTimer(1, "rb_go_to_garage");
        } else if (group.getCheckedRadioButtonId() == R.id.rb_go_to_recharge) {
            startControlTimer(2, "rb_go_to_recharge");
        } else if (group.getCheckedRadioButtonId() == R.id.rb_go_to_add_water) {
            startControlTimer(3, "rb_go_to_add_water");
        } else if (group.getCheckedRadioButtonId() == R.id.rb_go_to_empty_trash) {
            startControlTimer(4, "rb_go_to_empty_trash");
        } else if (group.getCheckedRadioButtonId() == R.id.rb_go_to_work) {
            startControlTimer(5, "rb_go_to_work");
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (buttonView.getId() == R.id.sw_device_run) {
                startControlTimer(1, "sw_device_run");
            } else if (buttonView.getId() == R.id.sw_main_sweep) {
                startControlTimer(1, "sw_main_sweep");
            } else if (buttonView.getId() == R.id.sw_side_sweep) {
                startControlTimer(1, "sw_side_sweep");
            } else if (buttonView.getId() == R.id.sw_sprinkling_water) {
                startControlTimer(1, "sw_sprinkling_water");
            } else if (buttonView.getId() == R.id.sw_left_tail_light) {
                startControlTimer(1, "sw_left_tail_light");
            } else if (buttonView.getId() == R.id.sw_right_tail_light) {
                startControlTimer(1, "sw_right_tail_light");
            } else if (buttonView.getId() == R.id.sw_alarm_light) {
                startControlTimer(1, "sw_alarm_light");
            } else if (buttonView.getId() == R.id.sw_front_light) {
                startControlTimer(1, "sw_front_light");
            } else if (buttonView.getId() == R.id.sw_horn) {
                startControlTimer(1, "sw_horn");
            } else if (buttonView.getId() == R.id.sw_suck_fan) {
                startControlTimer(1, "sw_suck_fan");
            } else if (buttonView.getId() == R.id.sw_vibrating_dust) {
                startControlTimer(1, "sw_vibrating_dust");
            } else if (buttonView.getId() == R.id.sw_motor) {
                startControlTimer(1, "sw_motor");
            } else if (buttonView.getId() == R.id.sw_emergency_stop) {
                startControlTimer(1, "sw_emergency_stop");
            }
        } else {
            if (buttonView.getId() == R.id.sw_device_run) {
                startControlTimer(0, "sw_device_run");
            } else if (buttonView.getId() == R.id.sw_main_sweep) {
                startControlTimer(0, "sw_main_sweep");
            } else if (buttonView.getId() == R.id.sw_side_sweep) {
                startControlTimer(0, "sw_side_sweep");
            } else if (buttonView.getId() == R.id.sw_sprinkling_water) {
                startControlTimer(0, "sw_sprinkling_water");
            } else if (buttonView.getId() == R.id.sw_left_tail_light) {
                startControlTimer(0, "sw_left_tail_light");
            } else if (buttonView.getId() == R.id.sw_right_tail_light) {
                startControlTimer(0, "sw_right_tail_light");
            } else if (buttonView.getId() == R.id.sw_alarm_light) {
                startControlTimer(0, "sw_alarm_light");
            } else if (buttonView.getId() == R.id.sw_front_light) {
                startControlTimer(0, "sw_front_light");
            } else if (buttonView.getId() == R.id.sw_horn) {
                startControlTimer(0, "sw_horn");
            } else if (buttonView.getId() == R.id.sw_suck_fan) {
                startControlTimer(0, "sw_suck_fan");
            } else if (buttonView.getId() == R.id.sw_vibrating_dust) {
                startControlTimer(0, "sw_vibrating_dust");
            } else if (buttonView.getId() == R.id.sw_motor) {
                startControlTimer(0, "sw_motor");
            } else if (buttonView.getId() == R.id.sw_emergency_stop) {
                startControlTimer(0, "sw_emergency_stop");
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SetStatusSuccessEvent messageEvent) {
        stopReadControlStatusTimer();
        DialogUtil.hideProgressDialog();
        String mark = messageEvent.getMark();
        int status = messageEvent.getStatus();
        if ("rb_go_to_recharge".equals(mark)) {
//            if (status == 3) {
//                Toast.makeText(this, getResources().getText(R.string.str_add_rechange_point), Toast.LENGTH_LONG).show();
//            }
            Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
        } else if ("rb_go_to_add_water".equals(mark)) {
            Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
        } else if ("rb_go_to_empty_trash".equals(mark)) {
            Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
        } else if ("rb_go_to_garage".equals(mark)) {
            Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
        } else if ("rb_go_to_work".equals(mark)) {
            Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
        } else if ("rb_go_to_idle".equals(mark)) {
            Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OperationStopEvent messageEvent) {
        stopControlTimer();
        stopReadControlStatusTimer();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SetStatusCompleteEvent messageEvent) {
        stopControlTimer();
        startReadControlStatusTimer(messageEvent.getStatus(), messageEvent.getMark());
    }

    private void startReadControlStatusTimer(int status, String mark) {
        scheduledThreadReadControlStatus = new ScheduledThreadPoolExecutor(6);
        scheduledThreadReadControlStatus.scheduleAtFixedRate(new TaskUtils.TaskReadControlStatus(mDevice, status, mark),
                0, 2, TimeUnit.SECONDS);
    }

    private void startControlTimer(int status, String mark) {
        DialogUtil.showProgressDialog(getActivity(), getResources().getString(R.string.yl_common_saving), false, false);
        scheduledThreadControl = new ScheduledThreadPoolExecutor(6);
        scheduledThreadControl.scheduleAtFixedRate(new TaskUtils.TaskControlStatus(mDevice, status, mark),
                0, 5, TimeUnit.SECONDS);
    }

    private void stopReadControlStatusTimer() {
        if (scheduledThreadReadControlStatus != null) {
            scheduledThreadReadControlStatus.shutdownNow();
            scheduledThreadReadControlStatus = null;
        }
    }

    private void stopControlTimer() {
        if (scheduledThreadControl != null) {
            scheduledThreadControl.shutdownNow();
            scheduledThreadControl = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        stopControlTimer();
        stopReadControlStatusTimer();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
