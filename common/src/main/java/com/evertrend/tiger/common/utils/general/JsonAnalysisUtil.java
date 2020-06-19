package com.evertrend.tiger.common.utils.general;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.evertrend.tiger.common.bean.CleanTask;
import com.evertrend.tiger.common.bean.Device;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class JsonAnalysisUtil {

    public static CleanTask getCleanTask(JSONObject jsonObject) {
        CleanTask cleanTask = new CleanTask();
        cleanTask.setId(jsonObject.getIntValue(CommonConstants.ID));
        cleanTask.setName(jsonObject.getString(CommonConstants.NAME));
        cleanTask.setDevice(jsonObject.getIntValue(CommonConstants.DEVICE));
        cleanTask.setMapPage(jsonObject.getIntValue(CommonConstants.MAP_PAGE));
        cleanTask.setTaskPriority(jsonObject.getIntValue(CommonConstants.PRIORITY));
        cleanTask.setDesc(jsonObject.getString(CommonConstants.DESCRIPTION));
        cleanTask.setTracePaths(jsonObject.getString(CommonConstants.TRACE_PATH));
        cleanTask.setTaskPriority(jsonObject.getIntValue(CommonConstants.TRACE_PATH_PRIORITY));
        cleanTask.setVirtualTrackGroups(jsonObject.getString(CommonConstants.VIRTUAL_TRACK));
        cleanTask.setVirtualTrackGroupsPriority(jsonObject.getIntValue(CommonConstants.VIRTUAL_TRACK_PRIORITY));
        cleanTask.setSpecialWorks(jsonObject.getString(CommonConstants.SPE_WROK));
        cleanTask.setSpecialWorksPriority(jsonObject.getIntValue(CommonConstants.SPE_WROK_PRIORITY));
        cleanTask.setStartTime(formatStartTime(jsonObject.getLongValue(CommonConstants.START_TIME)));
        cleanTask.setTaskOption(jsonObject.getIntValue(CommonConstants.EXEC_FLAG));
        cleanTask.setEst_end_time(jsonObject.getString(CommonConstants.EST_END_TIME));
        cleanTask.setEst_consume_time(jsonObject.getString(CommonConstants.EST_CONSUME_TIME));
        cleanTask.setRun_once(jsonObject.getIntValue(CommonConstants.RUN_ONCE));
        cleanTask.setTaskType(jsonObject.getIntValue(CommonConstants.TASK_TYPE));
        return cleanTask;
    }

    public static List<Device> loadAllDevice(JSONArray devices) {
        List<Device> deviceLists = new ArrayList<>();
        for (int i = 0; i < devices.size(); i++) {
            Device device = new Device();
            device.setId(devices.getJSONObject(i).getIntValue(CommonConstants.ID));
            device.setDescription(devices.getJSONObject(i).getString(CommonConstants.DESCRIPTION));
            device.setStatus(devices.getJSONObject(i).getIntValue(CommonConstants.STATUS));
            device.setDevice_type(devices.getJSONObject(i).getString(CommonConstants.DEVICE_TYPE));
            device.setDevice_id(devices.getJSONObject(i).getString(CommonConstants.DEVICE_ID));
            device.setCreate_time(devices.getJSONObject(i).getLongValue(CommonConstants.CREATE_TIME));
            device.setDevice_ip(devices.getJSONObject(i).getString(CommonConstants.DEVICE_IP));
            device.setBattery_level(devices.getJSONObject(i).getString(CommonConstants.BATTERY_LEVEL));
            device.setIs_recharge(devices.getJSONObject(i).getIntValue(CommonConstants.IS_RECHARGE));
            device.setIs_adding_water(devices.getJSONObject(i).getIntValue(CommonConstants.IS_ADDING_WATER));
            device.setIs_emptying_trash(devices.getJSONObject(i).getIntValue(CommonConstants.IS_EMPTYING_TRASH));
            device.setIs_running(devices.getJSONObject(i).getIntValue(CommonConstants.DEVICE_STATUS));
            device.setMain_sweep_status(devices.getJSONObject(i).getIntValue(CommonConstants.MAIN_SWEEP_STATUS));
            device.setSide_sweep_status(devices.getJSONObject(i).getIntValue(CommonConstants.SIDE_SWEEP_STATUS));
            device.setSprinkling_water_status(devices.getJSONObject(i).getIntValue(CommonConstants.SPRINKLING_WATER_STATUS));
            device.setAlarm_light_status(devices.getJSONObject(i).getIntValue(CommonConstants.ALARM_LIGHT_STATUS));
            device.setFront_light_status(devices.getJSONObject(i).getIntValue(CommonConstants.FRONT_LIGHT_STATUS));
            device.setTail_light_status(devices.getJSONObject(i).getIntValue(CommonConstants.TAIL_LIGHT_STATUS));
            device.setLeft_tail_light_status(devices.getJSONObject(i).getIntValue(CommonConstants.LEFT_TAIL_LIGHT_STATUS));
            device.setRight_tail_light_status(devices.getJSONObject(i).getIntValue(CommonConstants.RIGHT_TAIL_LIGHT_STATUS));
            device.setWater_level(devices.getJSONObject(i).getString(CommonConstants.WATER_LEVEL));
            device.setLatitude(devices.getJSONObject(i).getDoubleValue(CommonConstants.LATITUDE));
            device.setLongitude(devices.getJSONObject(i).getDoubleValue(CommonConstants.LONGITUDE));
            device.setClean_mode(devices.getJSONObject(i).getString(CommonConstants.CLEAN_MODE));
            device.setWork_mode(devices.getJSONObject(i).getString(CommonConstants.WORK_MODE));
            device.setCurrent_trace_path_id(devices.getJSONObject(i).getIntValue(CommonConstants.CURRENT_TRACE_PATH_ID));
            device.setDelete_trace_path(devices.getJSONObject(i).getString(CommonConstants.DELETE_TRACE_PATH));
            device.setLocal_ins_ip(devices.getJSONObject(i).getString(CommonConstants.LOCAL_INS_IP));
            device.setLocal_chassis_ip(devices.getJSONObject(i).getString(CommonConstants.LOCAL_CHASSIS_IP));
            device.setLocal_camera_ip(devices.getJSONObject(i).getString(CommonConstants.LOCAL_CAMERA_IP));
            device.setIs_created_map(devices.getJSONObject(i).getIntValue(CommonConstants.IS_CREATED_MAP));
            device.setSave_map(devices.getJSONObject(i).getIntValue(CommonConstants.SAVE_MAP));
            device.setCurrent_map_page(devices.getJSONObject(i).getIntValue(CommonConstants.CURRENT_MAP_PAGE));
            device.setBattery_low_limit(devices.getJSONObject(i).getIntValue(CommonConstants.BATTERY_LOW_LIMIT));
            device.setWater_low_limit(devices.getJSONObject(i).getIntValue(CommonConstants.WATER_LOW_LIMIT));
            device.setEmpty_trash_interval(devices.getJSONObject(i).getIntValue(CommonConstants.EMPTY_TRASH_INTERVAL));
            device.setIs_timing_path(devices.getJSONObject(i).getIntValue(CommonConstants.IS_TIMING_PATH));
            device.setTiming_start_1(devices.getJSONObject(i).getIntValue(CommonConstants.TIMING_START_1));
            device.setTiming_start_2(devices.getJSONObject(i).getIntValue(CommonConstants.TIMING_START_2));
            device.setTiming_start_3(devices.getJSONObject(i).getIntValue(CommonConstants.TIMING_START_3));
            device.setTiming_end_1(devices.getJSONObject(i).getIntValue(CommonConstants.TIMING_END_1));
            device.setTiming_end_2(devices.getJSONObject(i).getIntValue(CommonConstants.TIMING_END_2));
            device.setTiming_end_3(devices.getJSONObject(i).getIntValue(CommonConstants.TIMING_END_3));
            device.setHorn_status(devices.getJSONObject(i).getIntValue(CommonConstants.HORN_STATUS));
            device.setSuck_fan_status(devices.getJSONObject(i).getIntValue(CommonConstants.SUCK_FAN_STATUS));
            device.setVibrating_dust_status(devices.getJSONObject(i).getIntValue(CommonConstants.VIBRATING_DUST_STATUS));
            device.setMotor_release_status(devices.getJSONObject(i).getIntValue(CommonConstants.MOTOR_RELEASE_STATUS));
            device.setEmergency_stop_status(devices.getJSONObject(i).getIntValue(CommonConstants.EMERGENCY_STOP_STATUS));
            device.setSet_current_task(devices.getJSONObject(i).getIntValue(CommonConstants.SET_CURRENT_TASK));
            device.setCurrent_virtaul_track_group(devices.getJSONObject(i).getIntValue(CommonConstants.CURRENT_VIRTAUL_TRACK_GROUP));
            device.setEnable_auto_recharge(devices.getJSONObject(i).getIntValue(CommonConstants.ENABLE_AUTO_RECHARGE));
            device.setEnable_auto_add_water(devices.getJSONObject(i).getIntValue(CommonConstants.ENABLE_AUTO_ADD_WATER));
            device.setEnable_auto_empty_trash(devices.getJSONObject(i).getIntValue(CommonConstants.ENABLE_AUTO_EMPTY_TRASH));
            device.setAlarm_info(devices.getJSONObject(i).getString(CommonConstants.ALARM_INFO));
            deviceLists.add(device);
        }
        return deviceLists;
    }

    public static Device getDevice(JSONObject jsonObject) {
        Device device = new Device();
        device.setId(jsonObject.getIntValue(CommonConstants.ID));
        device.setDescription(jsonObject.getString(CommonConstants.DESCRIPTION));
        device.setStatus(jsonObject.getIntValue(CommonConstants.STATUS));
        device.setDevice_type(jsonObject.getString(CommonConstants.DEVICE_TYPE));
        device.setDevice_id(jsonObject.getString(CommonConstants.DEVICE_ID));
        device.setCreate_time(jsonObject.getLongValue(CommonConstants.CREATE_TIME));
        device.setDevice_ip(jsonObject.getString(CommonConstants.DEVICE_IP));
        device.setBattery_level(jsonObject.getString(CommonConstants.BATTERY_LEVEL));
        device.setIs_recharge(jsonObject.getIntValue(CommonConstants.IS_RECHARGE));
        device.setWater_level(jsonObject.getString(CommonConstants.WATER_LEVEL));
        device.setIs_adding_water(jsonObject.getIntValue(CommonConstants.IS_ADDING_WATER));
        device.setIs_emptying_trash(jsonObject.getIntValue(CommonConstants.IS_EMPTYING_TRASH));
        device.setIs_running(jsonObject.getIntValue(CommonConstants.DEVICE_STATUS));
        device.setMain_sweep_status(jsonObject.getIntValue(CommonConstants.MAIN_SWEEP_STATUS));
        device.setSide_sweep_status(jsonObject.getIntValue(CommonConstants.SIDE_SWEEP_STATUS));
        device.setSprinkling_water_status(jsonObject.getIntValue(CommonConstants.SPRINKLING_WATER_STATUS));
        device.setAlarm_light_status(jsonObject.getIntValue(CommonConstants.ALARM_LIGHT_STATUS));
        device.setFront_light_status(jsonObject.getIntValue(CommonConstants.FRONT_LIGHT_STATUS));
        device.setTail_light_status(jsonObject.getIntValue(CommonConstants.TAIL_LIGHT_STATUS));
        device.setLeft_tail_light_status(jsonObject.getIntValue(CommonConstants.LEFT_TAIL_LIGHT_STATUS));
        device.setRight_tail_light_status(jsonObject.getIntValue(CommonConstants.RIGHT_TAIL_LIGHT_STATUS));
        device.setLatitude(jsonObject.getDoubleValue(CommonConstants.LATITUDE));
        device.setLongitude(jsonObject.getDoubleValue(CommonConstants.LONGITUDE));
        device.setClean_mode(jsonObject.getString(CommonConstants.CLEAN_MODE));
        device.setWork_mode(jsonObject.getString(CommonConstants.WORK_MODE));
        device.setCurrent_trace_path_id(jsonObject.getIntValue(CommonConstants.CURRENT_TRACE_PATH_ID));
        device.setDelete_trace_path(jsonObject.getString(CommonConstants.DELETE_TRACE_PATH));
        device.setLocal_ins_ip(jsonObject.getString(CommonConstants.LOCAL_INS_IP));
        device.setLocal_chassis_ip(jsonObject.getString(CommonConstants.LOCAL_CHASSIS_IP));
        device.setLocal_camera_ip(jsonObject.getString(CommonConstants.LOCAL_CAMERA_IP));
        device.setIs_created_map(jsonObject.getIntValue(CommonConstants.IS_CREATED_MAP));
        device.setSave_map(jsonObject.getIntValue(CommonConstants.SAVE_MAP));
        device.setCurrent_map_page(jsonObject.getIntValue(CommonConstants.CURRENT_MAP_PAGE));
        device.setBattery_low_limit(jsonObject.getIntValue(CommonConstants.BATTERY_LOW_LIMIT));
        device.setWater_low_limit(jsonObject.getIntValue(CommonConstants.WATER_LOW_LIMIT));
        device.setEmpty_trash_interval(jsonObject.getIntValue(CommonConstants.EMPTY_TRASH_INTERVAL));
        device.setIs_timing_path(jsonObject.getIntValue(CommonConstants.IS_TIMING_PATH));
        device.setTiming_start_1(jsonObject.getIntValue(CommonConstants.TIMING_START_1));
        device.setTiming_start_2(jsonObject.getIntValue(CommonConstants.TIMING_START_2));
        device.setTiming_start_3(jsonObject.getIntValue(CommonConstants.TIMING_START_3));
        device.setTiming_end_1(jsonObject.getIntValue(CommonConstants.TIMING_END_1));
        device.setTiming_end_2(jsonObject.getIntValue(CommonConstants.TIMING_END_2));
        device.setTiming_end_3(jsonObject.getIntValue(CommonConstants.TIMING_END_3));
        device.setHorn_status(jsonObject.getIntValue(CommonConstants.HORN_STATUS));
        device.setSuck_fan_status(jsonObject.getIntValue(CommonConstants.SUCK_FAN_STATUS));
        device.setVibrating_dust_status(jsonObject.getIntValue(CommonConstants.VIBRATING_DUST_STATUS));
        device.setMotor_release_status(jsonObject.getIntValue(CommonConstants.MOTOR_RELEASE_STATUS));
        device.setEmergency_stop_status(jsonObject.getIntValue(CommonConstants.EMERGENCY_STOP_STATUS));
        device.setSet_current_task(jsonObject.getIntValue(CommonConstants.SET_CURRENT_TASK));
        device.setCurrent_virtaul_track_group(jsonObject.getIntValue(CommonConstants.CURRENT_VIRTAUL_TRACK_GROUP));
        device.setEnable_auto_recharge(jsonObject.getIntValue(CommonConstants.ENABLE_AUTO_RECHARGE));
        device.setEnable_auto_add_water(jsonObject.getIntValue(CommonConstants.ENABLE_AUTO_ADD_WATER));
        device.setEnable_auto_empty_trash(jsonObject.getIntValue(CommonConstants.ENABLE_AUTO_EMPTY_TRASH));
        device.setAlarm_info(jsonObject.getString(CommonConstants.ALARM_INFO));
        return device;
    }

    public static List<CleanTask> loadAllCLeanTask(JSONArray jsonArray) {
        List<CleanTask> cleanTaskList = new ArrayList<>();
        if (jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                CleanTask cleanTask = new CleanTask();
                cleanTask.setId(jsonArray.getJSONObject(i).getIntValue(CommonConstants.ID));
                cleanTask.setName(jsonArray.getJSONObject(i).getString(CommonConstants.NAME));
                cleanTask.setDevice(jsonArray.getJSONObject(i).getIntValue(CommonConstants.DEVICE));
                cleanTask.setMapPage(jsonArray.getJSONObject(i).getIntValue(CommonConstants.MAP_PAGE));
                cleanTask.setTaskPriority(jsonArray.getJSONObject(i).getIntValue(CommonConstants.PRIORITY));
                cleanTask.setDesc(jsonArray.getJSONObject(i).getString(CommonConstants.DESCRIPTION));
                cleanTask.setTracePaths(jsonArray.getJSONObject(i).getString(CommonConstants.TRACE_PATH));
                cleanTask.setTaskPriority(jsonArray.getJSONObject(i).getIntValue(CommonConstants.TRACE_PATH_PRIORITY));
                cleanTask.setVirtualTrackGroups(jsonArray.getJSONObject(i).getString(CommonConstants.VIRTUAL_TRACK));
                cleanTask.setVirtualTrackGroupsPriority(jsonArray.getJSONObject(i).getIntValue(CommonConstants.VIRTUAL_TRACK_PRIORITY));
                cleanTask.setSpecialWorks(jsonArray.getJSONObject(i).getString(CommonConstants.SPE_WROK));
                cleanTask.setSpecialWorksPriority(jsonArray.getJSONObject(i).getIntValue(CommonConstants.SPE_WROK_PRIORITY));
                cleanTask.setStartTime(formatStartTime(jsonArray.getJSONObject(i).getLongValue(CommonConstants.START_TIME)));
                cleanTask.setTaskOption(jsonArray.getJSONObject(i).getIntValue(CommonConstants.EXEC_FLAG));
                cleanTask.setEst_end_time(jsonArray.getJSONObject(i).getString(CommonConstants.EST_END_TIME));
                cleanTask.setEst_consume_time(jsonArray.getJSONObject(i).getString(CommonConstants.EST_CONSUME_TIME));
                cleanTask.setRun_once(jsonArray.getJSONObject(i).getIntValue(CommonConstants.RUN_ONCE));
                cleanTask.setTaskType(jsonArray.getJSONObject(i).getIntValue(CommonConstants.TASK_TYPE));
                cleanTask.setRunStatus(jsonArray.getJSONObject(i).getIntValue(CommonConstants.RUN_STATUS));
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
