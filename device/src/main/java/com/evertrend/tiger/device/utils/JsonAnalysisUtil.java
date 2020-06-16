package com.evertrend.tiger.device.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.evertrend.tiger.device.bean.CleanTask;
import com.evertrend.tiger.common.bean.Device;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class JsonAnalysisUtil {

    public static CleanTask getCleanTask(JSONObject jsonObject) {
        CleanTask cleanTask = new CleanTask();
        cleanTask.setId(jsonObject.getIntValue(Constants.ID));
        cleanTask.setName(jsonObject.getString(Constants.NAME));
        cleanTask.setDevice(jsonObject.getIntValue(Constants.DEVICE));
        cleanTask.setMapPage(jsonObject.getIntValue(Constants.MAP_PAGE));
        cleanTask.setTaskPriority(jsonObject.getIntValue(Constants.PRIORITY));
        cleanTask.setDesc(jsonObject.getString(Constants.DESCRIPTION));
        cleanTask.setTracePaths(jsonObject.getString(Constants.TRACE_PATH));
        cleanTask.setTaskPriority(jsonObject.getIntValue(Constants.TRACE_PATH_PRIORITY));
        cleanTask.setVirtualTrackGroups(jsonObject.getString(Constants.VIRTUAL_TRACK));
        cleanTask.setVirtualTrackGroupsPriority(jsonObject.getIntValue(Constants.VIRTUAL_TRACK_PRIORITY));
        cleanTask.setSpecialWorks(jsonObject.getString(Constants.SPE_WROK));
        cleanTask.setSpecialWorksPriority(jsonObject.getIntValue(Constants.SPE_WROK_PRIORITY));
        cleanTask.setStartTime(formatStartTime(jsonObject.getLongValue(Constants.START_TIME)));
        cleanTask.setTaskOption(jsonObject.getIntValue(Constants.EXEC_FLAG));
        cleanTask.setEst_end_time(jsonObject.getString(Constants.EST_END_TIME));
        cleanTask.setEst_consume_time(jsonObject.getString(Constants.EST_CONSUME_TIME));
        cleanTask.setRun_once(jsonObject.getIntValue(Constants.RUN_ONCE));
        cleanTask.setTaskType(jsonObject.getIntValue(Constants.TASK_TYPE));
        return cleanTask;
    }

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
            device.setSet_current_task(devices.getJSONObject(i).getIntValue(Constants.SET_CURRENT_TASK));
            device.setCurrent_virtaul_track_group(devices.getJSONObject(i).getIntValue(Constants.CURRENT_VIRTAUL_TRACK_GROUP));
            device.setEnable_auto_recharge(devices.getJSONObject(i).getIntValue(Constants.ENABLE_AUTO_RECHARGE));
            device.setEnable_auto_add_water(devices.getJSONObject(i).getIntValue(Constants.ENABLE_AUTO_ADD_WATER));
            device.setEnable_auto_empty_trash(devices.getJSONObject(i).getIntValue(Constants.ENABLE_AUTO_EMPTY_TRASH));
            deviceLists.add(device);
        }
        return deviceLists;
    }

    public static Device getDevice(JSONObject jsonObject) {
        Device device = new Device();
        device.setId(jsonObject.getIntValue(Constants.ID));
        device.setDescription(jsonObject.getString(Constants.DESCRIPTION));
        device.setStatus(jsonObject.getIntValue(Constants.STATUS));
        device.setDevice_type(jsonObject.getString(Constants.DEVICE_TYPE));
        device.setDevice_id(jsonObject.getString(Constants.DEVICE_ID));
        device.setCreate_time(jsonObject.getLongValue(Constants.CREATE_TIME));
        device.setDevice_ip(jsonObject.getString(Constants.DEVICE_IP));
        device.setBattery_level(jsonObject.getString(Constants.BATTERY_LEVEL));
        device.setIs_recharge(jsonObject.getIntValue(Constants.IS_RECHARGE));
        device.setWater_level(jsonObject.getString(Constants.WATER_LEVEL));
        device.setIs_adding_water(jsonObject.getIntValue(Constants.IS_ADDING_WATER));
        device.setIs_emptying_trash(jsonObject.getIntValue(Constants.IS_EMPTYING_TRASH));
        device.setIs_running(jsonObject.getIntValue(Constants.DEVICE_STATUS));
        device.setMain_sweep_status(jsonObject.getIntValue(Constants.MAIN_SWEEP_STATUS));
        device.setSide_sweep_status(jsonObject.getIntValue(Constants.SIDE_SWEEP_STATUS));
        device.setSprinkling_water_status(jsonObject.getIntValue(Constants.SPRINKLING_WATER_STATUS));
        device.setAlarm_light_status(jsonObject.getIntValue(Constants.ALARM_LIGHT_STATUS));
        device.setFront_light_status(jsonObject.getIntValue(Constants.FRONT_LIGHT_STATUS));
        device.setTail_light_status(jsonObject.getIntValue(Constants.TAIL_LIGHT_STATUS));
        device.setLeft_tail_light_status(jsonObject.getIntValue(Constants.LEFT_TAIL_LIGHT_STATUS));
        device.setRight_tail_light_status(jsonObject.getIntValue(Constants.RIGHT_TAIL_LIGHT_STATUS));
        device.setLatitude(jsonObject.getDoubleValue(Constants.LATITUDE));
        device.setLongitude(jsonObject.getDoubleValue(Constants.LONGITUDE));
        device.setClean_mode(jsonObject.getString(Constants.CLEAN_MODE));
        device.setWork_mode(jsonObject.getString(Constants.WORK_MODE));
        device.setCurrent_trace_path_id(jsonObject.getIntValue(Constants.CURRENT_TRACE_PATH_ID));
        device.setDelete_trace_path(jsonObject.getString(Constants.DELETE_TRACE_PATH));
        device.setLocal_ins_ip(jsonObject.getString(Constants.LOCAL_INS_IP));
        device.setLocal_chassis_ip(jsonObject.getString(Constants.LOCAL_CHASSIS_IP));
        device.setLocal_camera_ip(jsonObject.getString(Constants.LOCAL_CAMERA_IP));
        device.setIs_created_map(jsonObject.getIntValue(Constants.IS_CREATED_MAP));
        device.setSave_map(jsonObject.getIntValue(Constants.SAVE_MAP));
        device.setCurrent_map_page(jsonObject.getIntValue(Constants.CURRENT_MAP_PAGE));
        device.setBattery_low_limit(jsonObject.getIntValue(Constants.BATTERY_LOW_LIMIT));
        device.setWater_low_limit(jsonObject.getIntValue(Constants.WATER_LOW_LIMIT));
        device.setEmpty_trash_interval(jsonObject.getIntValue(Constants.EMPTY_TRASH_INTERVAL));
        device.setIs_timing_path(jsonObject.getIntValue(Constants.IS_TIMING_PATH));
        device.setTiming_start_1(jsonObject.getIntValue(Constants.TIMING_START_1));
        device.setTiming_start_2(jsonObject.getIntValue(Constants.TIMING_START_2));
        device.setTiming_start_3(jsonObject.getIntValue(Constants.TIMING_START_3));
        device.setTiming_end_1(jsonObject.getIntValue(Constants.TIMING_END_1));
        device.setTiming_end_2(jsonObject.getIntValue(Constants.TIMING_END_2));
        device.setTiming_end_3(jsonObject.getIntValue(Constants.TIMING_END_3));
        device.setHorn_status(jsonObject.getIntValue(Constants.HORN_STATUS));
        device.setSuck_fan_status(jsonObject.getIntValue(Constants.SUCK_FAN_STATUS));
        device.setVibrating_dust_status(jsonObject.getIntValue(Constants.VIBRATING_DUST_STATUS));
        device.setMotor_release_status(jsonObject.getIntValue(Constants.MOTOR_RELEASE_STATUS));
        device.setEmergency_stop_status(jsonObject.getIntValue(Constants.EMERGENCY_STOP_STATUS));
        device.setSet_current_task(jsonObject.getIntValue(Constants.SET_CURRENT_TASK));
        device.setCurrent_virtaul_track_group(jsonObject.getIntValue(Constants.CURRENT_VIRTAUL_TRACK_GROUP));
        device.setEnable_auto_recharge(jsonObject.getIntValue(Constants.ENABLE_AUTO_RECHARGE));
        device.setEnable_auto_add_water(jsonObject.getIntValue(Constants.ENABLE_AUTO_ADD_WATER));
        device.setEnable_auto_empty_trash(jsonObject.getIntValue(Constants.ENABLE_AUTO_EMPTY_TRASH));
        return device;
    }

    public static List<CleanTask> loadAllCLeanTask(JSONArray jsonArray) {
        List<CleanTask> cleanTaskList = new ArrayList<>();
        if (jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                CleanTask cleanTask = new CleanTask();
                cleanTask.setId(jsonArray.getJSONObject(i).getIntValue(Constants.ID));
                cleanTask.setName(jsonArray.getJSONObject(i).getString(Constants.NAME));
                cleanTask.setDevice(jsonArray.getJSONObject(i).getIntValue(Constants.DEVICE));
                cleanTask.setMapPage(jsonArray.getJSONObject(i).getIntValue(Constants.MAP_PAGE));
                cleanTask.setTaskPriority(jsonArray.getJSONObject(i).getIntValue(Constants.PRIORITY));
                cleanTask.setDesc(jsonArray.getJSONObject(i).getString(Constants.DESCRIPTION));
                cleanTask.setTracePaths(jsonArray.getJSONObject(i).getString(Constants.TRACE_PATH));
                cleanTask.setTaskPriority(jsonArray.getJSONObject(i).getIntValue(Constants.TRACE_PATH_PRIORITY));
                cleanTask.setVirtualTrackGroups(jsonArray.getJSONObject(i).getString(Constants.VIRTUAL_TRACK));
                cleanTask.setVirtualTrackGroupsPriority(jsonArray.getJSONObject(i).getIntValue(Constants.VIRTUAL_TRACK_PRIORITY));
                cleanTask.setSpecialWorks(jsonArray.getJSONObject(i).getString(Constants.SPE_WROK));
                cleanTask.setSpecialWorksPriority(jsonArray.getJSONObject(i).getIntValue(Constants.SPE_WROK_PRIORITY));
                cleanTask.setStartTime(formatStartTime(jsonArray.getJSONObject(i).getLongValue(Constants.START_TIME)));
                cleanTask.setTaskOption(jsonArray.getJSONObject(i).getIntValue(Constants.EXEC_FLAG));
                cleanTask.setEst_end_time(jsonArray.getJSONObject(i).getString(Constants.EST_END_TIME));
                cleanTask.setEst_consume_time(jsonArray.getJSONObject(i).getString(Constants.EST_CONSUME_TIME));
                cleanTask.setRun_once(jsonArray.getJSONObject(i).getIntValue(Constants.RUN_ONCE));
                cleanTask.setTaskType(jsonArray.getJSONObject(i).getIntValue(Constants.TASK_TYPE));
                cleanTask.setRunStatus(jsonArray.getJSONObject(i).getIntValue(Constants.RUN_STATUS));
                cleanTaskList.add(cleanTask);
            }
        }
        return cleanTaskList;
    }

    private static String formatStartTime(long intValue) {
        Calendar beginC = Calendar.getInstance();
        beginC.setTimeInMillis(intValue*1000);
        int hourB = beginC.get(Calendar.HOUR_OF_DAY);
        int minuteB = beginC.get(Calendar.MINUTE);
        String strStartTime;
        if (minuteB < 10) {
            strStartTime = hourB + ":0" + minuteB;
        } else {
            strStartTime = hourB + ":" + minuteB;
        }
        return strStartTime;
    }
}
