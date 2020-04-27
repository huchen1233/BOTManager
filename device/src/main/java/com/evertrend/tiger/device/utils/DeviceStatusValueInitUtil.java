package com.evertrend.tiger.device.utils;

import android.content.Context;

import com.evertrend.tiger.device.R;
import com.evertrend.tiger.device.bean.Device;

import java.util.ArrayList;
import java.util.List;

public class DeviceStatusValueInitUtil {

    public static List<String> initDetailStatus(Context context, Device device) {
        List<String> deviceDetailInfoValues = new ArrayList<>();
        switch (device.getDevice_type()) {
            case Constants.DEVICE_TYPE_EVBOT_SL:
                initDeviceStatus(context, deviceDetailInfoValues, device);
                deviceDetailInfoValues.add(device.getBattery_level());
                initRechangeStatus(context, deviceDetailInfoValues, device);
                initDeviceRunningStatus(context, deviceDetailInfoValues, device);
                break;
            case Constants.DEVICE_TYPE_SWBOT_SL:
                initDeviceStatus(context, deviceDetailInfoValues, device);
                deviceDetailInfoValues.add(device.getBattery_level());
                initRechangeStatus(context, deviceDetailInfoValues, device);
                deviceDetailInfoValues.add(device.getWater_level());
                initAddWaterStatus(context, deviceDetailInfoValues, device);
                initEmptyTrashStatus(context, deviceDetailInfoValues, device);
                initDeviceRunningStatus(context, deviceDetailInfoValues, device);
                break;
            case Constants.DEVICE_TYPE_MFBOT_SL:
                initDeviceStatus(context, deviceDetailInfoValues, device);
                deviceDetailInfoValues.add(device.getBattery_level());
                initRechangeStatus(context, deviceDetailInfoValues, device);
                deviceDetailInfoValues.add(device.getWater_level());
                initAddWaterStatus(context, deviceDetailInfoValues, device);
                initDeviceRunningStatus(context, deviceDetailInfoValues, device);
                break;
            case Constants.DEVICE_TYPE_SWBOT_AP:
                initDeviceStatus(context, deviceDetailInfoValues, device);
                deviceDetailInfoValues.add(device.getBattery_level());
                initRechangeStatus(context, deviceDetailInfoValues, device);
                deviceDetailInfoValues.add(device.getWater_level());
                initAddWaterStatus(context, deviceDetailInfoValues, device);
                initEmptyTrashStatus(context, deviceDetailInfoValues, device);
                initDeviceRunningStatus(context, deviceDetailInfoValues, device);
                break;
            case Constants.DEVICE_TYPE_SWBOT_MINI:
                initDeviceStatus(context, deviceDetailInfoValues, device);
                deviceDetailInfoValues.add(device.getBattery_level());
                initRechangeStatus(context, deviceDetailInfoValues, device);
                initEmptyTrashStatus(context, deviceDetailInfoValues, device);
                initDeviceRunningStatus(context, deviceDetailInfoValues, device);
                break;
            default:
                initDeviceStatus(context, deviceDetailInfoValues, device);
                deviceDetailInfoValues.add(device.getBattery_level());
                initRechangeStatus(context, deviceDetailInfoValues, device);
                deviceDetailInfoValues.add(device.getWater_level());
                initAddWaterStatus(context, deviceDetailInfoValues, device);
                initEmptyTrashStatus(context, deviceDetailInfoValues, device);
                initDeviceRunningStatus(context, deviceDetailInfoValues, device);
                break;
        }
        return deviceDetailInfoValues;
    }

    private static void initDeviceRunningStatus(Context context, List<String> deviceDetailInfoValues, Device device) {
        switch (device.getIs_running()) {
            case 0:
                deviceDetailInfoValues.add(context.getResources().getString(R.string.yl_device_device_running_status_no));
                break;
            case 1:
                deviceDetailInfoValues.add(context.getResources().getString(R.string.yl_device_device_running_status_running));
                break;
            default:
                deviceDetailInfoValues.add(context.getResources().getString(R.string.yl_device_device_running_status_no));
                break;
        }
    }

    private static void initEmptyTrashStatus(Context context, List<String> deviceDetailInfoValues, Device device) {
        switch (device.getIs_emptying_trash()) {
            case 0:
                deviceDetailInfoValues.add(context.getResources().getString(R.string.yl_device_device_empty_trash_status_no));
                break;
            case 1:
                deviceDetailInfoValues.add(context.getResources().getString(R.string.yl_device_device_empty_trash_status_doing));
                break;
            default:
                deviceDetailInfoValues.add(context.getResources().getString(R.string.yl_device_device_empty_trash_status_no));
                break;
        }
    }

    private static void initAddWaterStatus(Context context, List<String> deviceDetailInfoValues, Device device) {
        switch (device.getIs_adding_water()) {
            case 0:
                deviceDetailInfoValues.add(context.getResources().getString(R.string.yl_device_device_add_water_status_no));
                break;
            case 1:
                deviceDetailInfoValues.add(context.getResources().getString(R.string.yl_device_device_add_water_status_manual));
                break;
            case 2:
                deviceDetailInfoValues.add(context.getResources().getString(R.string.yl_device_device_add_water_status_auto));
                break;
            default:
                deviceDetailInfoValues.add(context.getResources().getString(R.string.yl_device_device_add_water_status_no));
                break;
        }
    }

    private static void initRechangeStatus(Context context, List<String> deviceDetailInfoValues, Device device) {
        switch (device.getIs_recharge()) {
            case 0:
                deviceDetailInfoValues.add(context.getResources().getString(R.string.yl_device_device_charging_status_uncharged));
                break;
            case 1:
                deviceDetailInfoValues.add(context.getResources().getString(R.string.yl_device_device_charging_status_manual_line_charging));
                break;
            case 2:
                deviceDetailInfoValues.add(context.getResources().getString(R.string.yl_device_device_charging_status_auto_wireless_charging));
                break;
            case 3:
                deviceDetailInfoValues.add(context.getResources().getString(R.string.yl_device_device_charging_status_no_charg_detected));
                break;
            default:
                deviceDetailInfoValues.add(context.getResources().getString(R.string.yl_device_device_charging_status_uncharged));
                break;
        }
    }

    private static void initDeviceStatus(Context context, List<String> deviceDetailInfoValues, Device device) {
        switch (device.getStatus()) {
            case 2:
                deviceDetailInfoValues.add(context.getResources().getString(R.string.yl_device_device_status_fault));
                break;
            case 1:
                deviceDetailInfoValues.add(context.getResources().getString(R.string.yl_device_device_status_normal));
                break;
            default:
                deviceDetailInfoValues.add(context.getResources().getString(R.string.yl_device_device_status_undetected));
                break;
        }
    }

    public static List<String> initRunningStatus(Context context, Device device) {
        List<String> deviceRunningInfoValues = new ArrayList<>();
        switch (device.getDevice_type()) {
            case Constants.DEVICE_TYPE_EVBOT_SL:
                break;
            case Constants.DEVICE_TYPE_SWBOT_SL:
                initMainSweepStatus(context, deviceRunningInfoValues, device);
                initSideSweepStatus(context, deviceRunningInfoValues, device);
                initSprinklingWaterStatus(context, deviceRunningInfoValues, device);
                initAlermLightStatus(context, deviceRunningInfoValues, device);
                initFrontLightStatus(context, deviceRunningInfoValues, device);
                initLeftTailLightStatus(context, deviceRunningInfoValues, device);
                initRightTailLightStatus(context, deviceRunningInfoValues, device);
                break;
            case Constants.DEVICE_TYPE_MFBOT_SL:
                initMainSweepStatus(context, deviceRunningInfoValues, device);
                initSideSweepStatus(context, deviceRunningInfoValues, device);
                initSprinklingWaterStatus(context, deviceRunningInfoValues, device);
                break;
            case Constants.DEVICE_TYPE_SWBOT_AP:
                initMainSweepStatus(context, deviceRunningInfoValues, device);
                initSideSweepStatus(context, deviceRunningInfoValues, device);
                initSprinklingWaterStatus(context, deviceRunningInfoValues, device);
                initAlermLightStatus(context, deviceRunningInfoValues, device);
                initFrontLightStatus(context, deviceRunningInfoValues, device);
                initLeftTailLightStatus(context, deviceRunningInfoValues, device);
                initRightTailLightStatus(context, deviceRunningInfoValues, device);
                break;
            case Constants.DEVICE_TYPE_SWBOT_MINI:
                initMainSweepStatus(context, deviceRunningInfoValues, device);
                initSideSweepStatus(context, deviceRunningInfoValues, device);
                break;
            default:
                break;
        }
        return deviceRunningInfoValues;
    }

    private static void initRightTailLightStatus(Context context, List<String> deviceRunningInfoValues, Device device) {
        if (device.getRight_tail_light_status() != 0) {
            deviceRunningInfoValues.add(context.getResources().getString(R.string.yl_device_device_status_turn_on));
        } else {
            deviceRunningInfoValues.add(context.getResources().getString(R.string.yl_device_device_status_turn_off));
        }
    }

    private static void initLeftTailLightStatus(Context context, List<String> deviceRunningInfoValues, Device device) {
        if (device.getLeft_tail_light_status() != 0) {
            deviceRunningInfoValues.add(context.getResources().getString(R.string.yl_device_device_status_turn_on));
        } else {
            deviceRunningInfoValues.add(context.getResources().getString(R.string.yl_device_device_status_turn_off));
        }
    }

    private static void initFrontLightStatus(Context context, List<String> deviceRunningInfoValues, Device device) {
        if (device.getFront_light_status() != 0) {
            deviceRunningInfoValues.add(context.getResources().getString(R.string.yl_device_device_status_turn_on));
        } else {
            deviceRunningInfoValues.add(context.getResources().getString(R.string.yl_device_device_status_turn_off));
        }
    }

    private static void initAlermLightStatus(Context context, List<String> deviceRunningInfoValues, Device device) {
        if (device.getAlarm_light_status() != 0) {
            deviceRunningInfoValues.add(context.getResources().getString(R.string.yl_device_device_status_turn_on));
        } else {
            deviceRunningInfoValues.add(context.getResources().getString(R.string.yl_device_device_status_turn_off));
        }
    }

    private static void initSprinklingWaterStatus(Context context, List<String> deviceRunningInfoValues, Device device) {
        if (device.getSprinkling_water_status() != 0) {
            deviceRunningInfoValues.add(context.getResources().getString(R.string.yl_device_device_status_turn_on));
        } else {
            deviceRunningInfoValues.add(context.getResources().getString(R.string.yl_device_device_status_turn_off));
        }
    }

    private static void initSideSweepStatus(Context context, List<String> deviceRunningInfoValues, Device device) {
        if (device.getSide_sweep_status() != 0) {
            deviceRunningInfoValues.add(context.getResources().getString(R.string.yl_device_device_status_turn_on));
        } else {
            deviceRunningInfoValues.add(context.getResources().getString(R.string.yl_device_device_status_turn_off));
        }
    }

    private static void initMainSweepStatus(Context context, List<String> deviceRunningInfoValues, Device device) {
        if (device.getMain_sweep_status() != 0) {
            deviceRunningInfoValues.add(context.getResources().getString(R.string.yl_device_device_status_turn_on));
        } else {
            deviceRunningInfoValues.add(context.getResources().getString(R.string.yl_device_device_status_turn_off));
        }
    }
}
