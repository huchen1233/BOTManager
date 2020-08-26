package com.evertrend.tiger.common.bean;

public class RobotAction {

    public interface CMD {
        int FORWARD = 0x21;
        int BACKWARD = 0x21;
        int TURN_LEFT = 0x22;
        int TURN_RIGHT = 0x22;
        int STOP = 0x23;
        int GET_MAP = 0x28;
        int GET_LASER_SCAN = 0x30;
        int GET_ROBOT_POSE = 0x31;
        int SET_ROBOT_POSE = 0x32;
    }

    public static final String DEVICE_ID = "device_id";
    public static final String KEY = "key";
    public static final String TIME_STAMP = "time_stamp";
    public static final String CMD_CODE = "cmd_code";
    public static final String DATA = "data";
    public static final String RESULT_CODE = "result_code";
}
