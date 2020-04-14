package com.evertrend.tiger.device.utils;

import android.os.Environment;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class Constants {

    public static final String ID = "id";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String DEVICE_ID = "device_id";
    public static final String DEVICE_IP = "device_ip";
    public static final String BATTERY_LEVEL = "battery_level";
    public static final String WATER_LEVEL = "water_level";
    public static final String IS_RECHARGE = "is_recharge";
    public static final String IS_ADDING_WATER = "is_adding_water";
    public static final String IS_EMPTYING_TRASH = "is_emptying_trash";
    public static final String DEVICE_STATUS = "device_status";
    public static final String MAIN_SWEEP_STATUS = "main_sweep_status";
    public static final String SIDE_SWEEP_STATUS = "side_sweep_status";
    public static final String SPRINKLING_WATER_STATUS = "sprinkling_water_status";
    public static final String ALARM_LIGHT_STATUS = "alarm_light_status";
    public static final String FRONT_LIGHT_STATUS = "front_light_status";
    public static final String TAIL_LIGHT_STATUS = "tail_light_status";
    public static final String LEFT_TAIL_LIGHT_STATUS = "left_tail_light_status";
    public static final String RIGHT_TAIL_LIGHT_STATUS = "right_tail_light_status";
    public static final String HORN_STATUS = "horn_status";
    public static final String SUCK_FAN_STATUS = "suck_fan_status";
    public static final String VIBRATING_DUST_STATUS = "vibrating_dust_status";
    public static final String MOTOR_RELEASE_STATUS = "motor_release_status";
    public static final String EMERGENCY_STOP_STATUS = "emergency_stop_status";
    public static final String DESCRIPTION = "description";
    public static final String STATUS = "status";
    public static final String DEVICE_TYPE = "device_type";
    public static final String CREATE_TIME = "create_time";
    public static final String WORK_MODE = "work_mode";
    public static final String CLEAN_MODE = "clean_mode";
    public static final String CURRENT_TRACE_PATH_ID = "current_trace_path_id";
    public static final String DELETE_TRACE_PATH = "delete_trace_path";
    public static final String LOCAL_INS_IP = "local_ins_ip";
    public static final String LOCAL_CHASSIS_IP = "local_chassis_ip";
    public static final String LOCAL_CAMERA_IP = "local_camera_ip";
    public static final String IS_CREATED_MAP = "is_created_map";
    public static final String SAVE_MAP = "save_map";
    public static final String CURRENT_MAP_PAGE = "current_map_page";
    public static final String BATTERY_LOW_LIMIT = "battery_low_limit";
    public static final String WATER_LOW_LIMIT = "water_low_limit";
    public static final String EMPTY_TRASH_INTERVAL = "empty_trash_interval";
    public static final String IS_TIMING_PATH = "is_timing_path";
    public static final String TIMING_START_1 = "timing_start_1";
    public static final String TIMING_START_2 = "timing_start_2";
    public static final String TIMING_START_3 = "timing_start_3";
    public static final String TIMING_END_1 = "timing_end_1";
    public static final String TIMING_END_2 = "timing_end_2";
    public static final String TIMING_END_3 = "timing_end_3";

    public static final String DEVICE_TYPE_EVBOT_SL = "1";//EVBOT-SL(服务机器人-SL)
    public static final String DEVICE_TYPE_SWBOT_SL = "2";//SWBOT-SL(扫地机器人-SL)
    public static final String DEVICE_TYPE_SWBOT_AP = "3";//SWBOT-SL(扫地机器人-AP)
    public static final String DEVICE_TYPE_SWBOT_MINI = "4";//SWBOT-SL(扫地机器人-MINI)
    public static final String DEVICE_TYPE_MFBOT_SL = "5";//MFBOT-SL(拖地机器人-SL)
    public static final int SCANNER_QR_CODE_REQUEST_CODE = 100;
}
