package com.evertrend.tiger.device.utils;

import com.alibaba.fastjson.JSONArray;
import com.evertrend.tiger.device.bean.Device;

import java.util.ArrayList;
import java.util.List;

public class JsonAnalysisUtil {
    public static List<Device> loadAllDevice(JSONArray devices) {
        List<Device> deviceLists = new ArrayList<>();
        for (int i = 0; i < devices.size(); i++) {
            Device device = new Device();
            device.setId(devices.getJSONObject(i).getIntValue(Constants.ID));
            device.setDescription(devices.getJSONObject(i).getString(Constants.DESCRIPTION));
            device.setStatus(devices.getJSONObject(i).getIntValue(Constants.STATUS));
            device.setDevice_type(devices.getJSONObject(i).getString(Constants.DEVICE_TYPE));
            device.setDevice_id(devices.getJSONObject(i).getString(Constants.DEVICE_ID));
            device.setCreate_time(devices.getJSONObject(i).getLongValue(Constants.CREATE_TIME));
            device.setDevice_ip(devices.getJSONObject(i).getString(Constants.DEVICE_IP));
            device.setBattery_level(devices.getJSONObject(i).getString(Constants.BATTERY_LEVEL));
            device.setIs_recharge(devices.getJSONObject(i).getIntValue(Constants.IS_RECHARGE));
            device.setIs_adding_water(devices.getJSONObject(i).getIntValue(Constants.IS_ADDING_WATER));
            device.setIs_emptying_trash(devices.getJSONObject(i).getIntValue(Constants.IS_EMPTYING_TRASH));
            device.setIs_running(devices.getJSONObject(i).getIntValue(Constants.DEVICE_STATUS));
            device.setMain_sweep_status(devices.getJSONObject(i).getIntValue(Constants.MAIN_SWEEP_STATUS));
            device.setSide_sweep_status(devices.getJSONObject(i).getIntValue(Constants.SIDE_SWEEP_STATUS));
            device.setSprinkling_water_status(devices.getJSONObject(i).getIntValue(Constants.SPRINKLING_WATER_STATUS));
            device.setAlarm_light_status(devices.getJSONObject(i).getIntValue(Constants.ALARM_LIGHT_STATUS));
            device.setFront_light_status(devices.getJSONObject(i).getIntValue(Constants.FRONT_LIGHT_STATUS));
            device.setTail_light_status(devices.getJSONObject(i).getIntValue(Constants.TAIL_LIGHT_STATUS));
            device.setLeft_tail_light_status(devices.getJSONObject(i).getIntValue(Constants.LEFT_TAIL_LIGHT_STATUS));
            device.setRight_tail_light_status(devices.getJSONObject(i).getIntValue(Constants.RIGHT_TAIL_LIGHT_STATUS));
            device.setWater_level(devices.getJSONObject(i).getString(Constants.WATER_LEVEL));
            device.setLatitude(devices.getJSONObject(i).getDoubleValue(Constants.LATITUDE));
            device.setLongitude(devices.getJSONObject(i).getDoubleValue(Constants.LONGITUDE));
            device.setClean_mode(devices.getJSONObject(i).getString(Constants.CLEAN_MODE));
            device.setWork_mode(devices.getJSONObject(i).getString(Constants.WORK_MODE));
            device.setCurrent_trace_path_id(devices.getJSONObject(i).getIntValue(Constants.CURRENT_TRACE_PATH_ID));
            device.setDelete_trace_path(devices.getJSONObject(i).getString(Constants.DELETE_TRACE_PATH));
            device.setLocal_ins_ip(devices.getJSONObject(i).getString(Constants.LOCAL_INS_IP));
            device.setLocal_chassis_ip(devices.getJSONObject(i).getString(Constants.LOCAL_CHASSIS_IP));
            device.setLocal_camera_ip(devices.getJSONObject(i).getString(Constants.LOCAL_CAMERA_IP));
            device.setIs_created_map(devices.getJSONObject(i).getIntValue(Constants.IS_CREATED_MAP));
            device.setSave_map(devices.getJSONObject(i).getIntValue(Constants.SAVE_MAP));
            device.setCurrent_map_page(devices.getJSONObject(i).getIntValue(Constants.CURRENT_MAP_PAGE));
            device.setBattery_low_limit(devices.getJSONObject(i).getIntValue(Constants.BATTERY_LOW_LIMIT));
            device.setWater_low_limit(devices.getJSONObject(i).getIntValue(Constants.WATER_LOW_LIMIT));
            device.setEmpty_trash_interval(devices.getJSONObject(i).getIntValue(Constants.EMPTY_TRASH_INTERVAL));
            device.setIs_timing_path(devices.getJSONObject(i).getIntValue(Constants.IS_TIMING_PATH));
            device.setTiming_start_1(devices.getJSONObject(i).getIntValue(Constants.TIMING_START_1));
            device.setTiming_start_2(devices.getJSONObject(i).getIntValue(Constants.TIMING_START_2));
            device.setTiming_start_3(devices.getJSONObject(i).getIntValue(Constants.TIMING_START_3));
            device.setTiming_end_1(devices.getJSONObject(i).getIntValue(Constants.TIMING_END_1));
            device.setTiming_end_2(devices.getJSONObject(i).getIntValue(Constants.TIMING_END_2));
            device.setTiming_end_3(devices.getJSONObject(i).getIntValue(Constants.TIMING_END_3));
            device.setHorn_status(devices.getJSONObject(i).getIntValue(Constants.HORN_STATUS));
            device.setSuck_fan_status(devices.getJSONObject(i).getIntValue(Constants.SUCK_FAN_STATUS));
            device.setVibrating_dust_status(devices.getJSONObject(i).getIntValue(Constants.VIBRATING_DUST_STATUS));
            device.setMotor_release_status(devices.getJSONObject(i).getIntValue(Constants.MOTOR_RELEASE_STATUS));
            device.setEmergency_stop_status(devices.getJSONObject(i).getIntValue(Constants.EMERGENCY_STOP_STATUS));
            deviceLists.add(device);
        }
        return deviceLists;
    }
}
