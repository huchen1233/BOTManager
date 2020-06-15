package com.evertrend.tiger.common.utils.general;

import java.text.DecimalFormat;

public class CommonConstants {

    public static final String TYPE_EXTRA_NAME = "name";
    public static final String TYPE_EXTRA_PASS = "pass";

    public static final int GET_CODE_COUNT = 60;

    public static final int TYPE_SUCCESS_EVENT_LOGIN = 1;
    public static final int TYPE_SUCCESS_EVENT_LOGOUT = 2;
    public static final int TYPE_SUCCESS_EVENT_DELETE_CLEANTASK = 3;

    public static final int TYPE_DELETE_ALL_VIRTUAL_WALL = 0;
    public static final int TYPE_DELETE_ALL_VIRTUAL_TRACK = 1;

    public static final int TYPE_ADD_VIRTUAL_WALL = 0;
    public static final int TYPE_DELETE_VIRTUAL_WALL = 1;
    public static final int TYPE_MOVE_VIRTUAL_WALL = 2;
    public static final int TYPE_MOVE_VIRTUAL_WALL_START = 0;
    public static final int TYPE_MOVE_VIRTUAL_WALL_END = 1;

    public static final int TYPE_ADD_VIRTUAL_TRACK = 0;
    public static final int TYPE_DELETE_VIRTUAL_TRACK = 1;
    public static final int TYPE_MOVE_VIRTUAL_TRACK = 2;
    public static final int TYPE_MOVE_VIRTUAL_TRACK_START = 0;
    public static final int TYPE_MOVE_VIRTUAL_TRACK_END = 1;

    public static final int TYPE_MAPPAGE_OPERATION_OPEN = 0;
    public static final int TYPE_MAPPAGE_OPERATION_DELETE = 1;
    public static final int TYPE_MAPPAGE_OPERATION_EDIT = 2;
    public static final int TYPE_MAPPAGE_OPERATION_TRACE_PATH = 3;
    public static final int TYPE_MAPPAGE_OPERATION_VIRTUAL_TRACK = 4;
    public static final int TYPE_MAPPAGE_OPERATION_CREATE_TASK_CHOICE = 5;
    public static final int TYPE_MAPPAGE_OPERATION_ADD_COMMON_SPOT_CHOICE = 6;

    public static final int LIST_OPERATION_UPDATE = 1;
    public static final int LIST_OPERATION_CREATE = 2;
    public static final int LIST_OPERATION_DELETE = 3;

    public static final int TYPE_TRACE_PATH_OPERATION_SAVE_SPOT = 0;
    public static final int TYPE_TRACE_PATH_OPERATION_SHOW_PATH = 1;

    public static final int CREATE_TRACE_SPOT_REQUEST_CODE = 104;
    public static final int CREATE_TRACE_PATH_REQUEST_CODE = 105;

    public static final DecimalFormat df = new DecimalFormat("######0.00");
}
