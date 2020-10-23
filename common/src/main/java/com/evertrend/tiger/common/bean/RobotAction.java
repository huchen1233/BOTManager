package com.evertrend.tiger.common.bean;

public class RobotAction {

    public interface CMD {
        int FORWARD = 0x21;
        int BACKWARD = 0x21;
        int TURN_LEFT = 0x22;
        int TURN_RIGHT = 0x22;
        int STOP = 0x23;
        int GET_MAP = 0x28;
        int GET_MAP_CONDENSE = 0x29;
        int GET_MAP_CON_BIN = 0x2A;
        int GET_LASER_SCAN = 0x30;
        int GET_ROBOT_POSE = 0x31;
        int SET_ROBOT_POSE = 0x32;
        int GET_VIRTUAL_WALL = 0x33;
        int SET_VIRTUAL_WALL = 0x34;
        int CLEAR_ONE_VIRTUAL_WALL = 0x35;
        int CLEAR_ALL_VIRTUAL_WALL = 0x36;
        int GET_VIRTUAL_TRACK = 0x37;
        int SET_VIRTUAL_TRACK = 0x38;
        int CLEAR_ONE_VIRTUAL_TRACK = 0x39;
        int CLEAR_ALL_VIRTUAL_TRACK = 0x3A;
        int MOVE_TO_POSE = 0x40;
        int GET_NAVIGATION_PATH_PLANNING = 0x41;
    }

    public static final String DEVICE_ID = "device_id";
    public static final String KEY = "key";
    public static final String TIME_STAMP = "time_stamp";
    public static final String CMD_CODE = "cmd_code";
    public static final String DATA = "data";
    public static final String RESULT_CODE = "result_code";
    public static final String RESOLUTION = "resolution";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String ORIGIN_X = "origin_x";
    public static final String ORIGIN_Y = "origin_y";
    public static final String ORIGIN_YAW = "origin_yaw";
    public static final String POSE_X = "pose_x";
    public static final String POSE_Y = "pose_y";
    public static final String POSE_YAW = "pose_yaw";
    public static final String ANGLE_INCREMENT = "angle_increment";
    public static final String ANGLE_MAX = "angle_max";
    public static final String ANGLE_MIN = "angle_min";
}
