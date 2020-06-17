package com.evertrend.tiger.device.utils;

import com.evertrend.tiger.common.utils.network.CommonNetReq;

public class NetReq {

    public static final String NET_LOGIN_PASS = CommonNetReq.REQ_HOST + "api/user/login-pass";
    public static final String NET_LOGIN_PASS_EMAIL = CommonNetReq.REQ_HOST + "api/user/login-pass-email";

    public static final String NET_ASSOCIATED_DEVICE = CommonNetReq.REQ_HOST + "api/remote/associated-device";
    public static final String NET_REGISTER_DEVICE = CommonNetReq.REQ_HOST + "api/remote/register-device";
    public static final String NET_GET_DEVICE = CommonNetReq.REQ_HOST + "api/remote/get-device";

    public static final String NET_GET_RUN_LOGS = CommonNetReq.REQ_HOST + "api/remote/get-run-logs";
    public static final String NET_GET_ALL_MAP_PAGES = CommonNetReq.REQ_HOST + "api/remote/get-map-pages";
    public static final String NET_GET_CLEAN_TASK = CommonNetReq.REQ_HOST + "api/remote/get-clean-task";
    public static final String NET_SAVE_BASIC_CONFIG = CommonNetReq.REQ_HOST + "api/remote/save-basic-config";
    public static final String NET_GET_CURRENT_MAP_PAGE_ALL_PATH = CommonNetReq.REQ_HOST + "api/remote/get-map-page-all-path";
    public static final String NET_GET_CURRENT_MAP_PAGE_ALL_VIRTUAL_TRACK_GROUP = CommonNetReq.REQ_HOST + "api/remote/get-map-page-all-virtual-track-group";
    public static final String NET_GET_CURRENT_MAP_PAGE_ALL_SPECIAL_TASK_SPOT = CommonNetReq.REQ_HOST + "api/remote/get-map-page-all-special-task-spot";

    public static final String NET_ADD_CLEAN_TASK = CommonNetReq.REQ_HOST + "api/remote/add-clean-task";
    public static final String NET_UPDATE_CLEAN_TASK = CommonNetReq.REQ_HOST + "api/remote/update-clean-task";
    public static final String NET_DELETE_CLEAN_TASK = CommonNetReq.REQ_HOST + "api/remote/delete-clean-task";

    public static final String NET_UPDATE_MAP_PAGE = CommonNetReq.REQ_HOST + "api/remote/update-map-page";
    public static final String NET_DELETE_MAP_PAGE = CommonNetReq.REQ_HOST + "api/remote/delete-map-page";
    public static final String NET_CREATE_NEW_MAP_APGE = CommonNetReq.REQ_HOST + "api/remote/create-new-map-page";

    public static final String NET_READ_CONTROL_STATUS = CommonNetReq.REQ_HOST + "api/remote/read-control-status";
    public static final String NET_SET_DEVICE_STATUS = CommonNetReq.REQ_HOST + "api/remote/set-device-status";
    public static final String NET_SET_MAIN_SWEEP = CommonNetReq.REQ_HOST + "api/remote/set-main-sweep";
    public static final String NET_SET_SIDE_SWEEP = CommonNetReq.REQ_HOST + "api/remote/set-side-sweep";
    public static final String NET_SET_SPRINKLING_WATER = CommonNetReq.REQ_HOST + "api/remote/set-sprinkling-water";
    public static final String NET_SET_ALARM_LIGHT = CommonNetReq.REQ_HOST + "api/remote/set-alarm-light";
    public static final String NET_SET_FRONT_LIGHT = CommonNetReq.REQ_HOST + "api/remote/set-front-light";
    public static final String NET_SET_HORN = CommonNetReq.REQ_HOST + "api/remote/set-horn";
    public static final String NET_SET_SUCK_FUN = CommonNetReq.REQ_HOST + "api/remote/set-suck-fun";
    public static final String NET_SET_VIBRATING_DUST = CommonNetReq.REQ_HOST + "api/remote/set-vibrating-dust";
    public static final String NET_SET_MOTOR = CommonNetReq.REQ_HOST + "api/remote/set-motor";
    public static final String NET_SET_EMERGENCY_STOP = CommonNetReq.REQ_HOST + "api/remote/set-emergency-stop";
    public static final String NET_SET_TAIL_LIGHT = CommonNetReq.REQ_HOST + "api/remote/set-tail-light";
    public static final String NET_SET_LEFT_TAIL_LIGHT = CommonNetReq.REQ_HOST + "api/remote/set-left-tail-light";
    public static final String NET_SET_RIGHT_TAIL_LIGHT = CommonNetReq.REQ_HOST + "api/remote/set-right-tail-light";
    public static final String NET_GO_TO_RECHARGE = CommonNetReq.REQ_HOST + "api/remote/go-to-recharge";
    public static final String NET_GO_TO_EMPTY_TRASH = CommonNetReq.REQ_HOST + "api/remote/go-to-empty-trash";
    public static final String NET_GO_TO_GARAGE = CommonNetReq.REQ_HOST + "api/remote/go-to-garage";
    public static final String NET_GO_TO_WORK = CommonNetReq.REQ_HOST + "api/remote/go-to-work";
    public static final String NET_GO_TO_IDEL = CommonNetReq.REQ_HOST + "api/remote/go-to-idel";
    public static final String NET_GO_TO_ADD_WATER = CommonNetReq.REQ_HOST + "api/remote/go-to-add-water";

    public static final String MOBILE = "mobile";
    public static final String EMAIL = "email";
    public static final String FLAG = "flag";
    public static final String VERI_CODE = "code";
    public static final String PASSWORD = "password";
    public static final String RESULT_TOKEN = "token";

    public static final String REG_CODE = "reg_code";
    public static final String DEVICE_ID = "device_id";
    public static final String PAGE = "page";
    public static final String PASS = "pass";
    public static final String DEVICE_TOKEN = "device_token";
    public static final String BEGIN_TIME = "begin_time";
    public static final String STOP_TIME = "stop_time";
    public static final String IS_TIMING_PATH = "is_timing_path";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String WORK_MODE = "work_mode";
    public static final String CLEAN_MODE = "clean_mode";
    public static final String ID = "id";
    public static final String VALUE = "value";
    public static final String STATUS = "status";
    public static final String PATH_ID = "path_id";
    public static final String WHAT_STATUS = "what_status";
    public static final String PRIORITY = "priority";
    public static final String TRACE_PATH = "trace_path";
    public static final String TRACE_PATH_PRIORITY = "trace_path_priority";
    public static final String VIRTUAL_TRACK = "virtual_track";
    public static final String VIRTUAL_TRACK_PRIORITY = "virtual_track_priority";
    public static final String SPE_WROK = "spe_wrok";
    public static final String SPE_WROK_PRIORITY = "spe_wrok_priority";
    public static final String START_TIME = "start_time";
    public static final String EXEC_FLAG = "exec_flag";
    public static final String TASK_TYPE = "task_type";
    public static final String BATTERY_LOW_LIMIT = "battery_low_limit";
    public static final String WATER_LOW_LIMIT = "water_low_limit";
    public static final String EMPTY_TRASH_INTERVAL = "empty_trash_interval";
    public static final String ENABLE_AUTO_RECHARGE = "enable_auto_recharge";
    public static final String ENABLE_AUTO_ADD_WATER = "enable_auto_add_water";
    public static final String ENABLE_AUTO_EMPTY_TRASH = "enable_auto_empty_trash";

    public static final String TRACE_PATH_ID = "trace_path_id";
    public static final String MAP_PAGE = "map_page";
    public static final String PAGE_NAME = "page_name";
    public static final String PAGE_DESC = "page_desc";
    public static final String PAGE_DO_SAVE_MAP = "do_save_map";
    public static final String SPOT_FLAG = "spot_flag";
    public static final String SPOT_FLAG_DATA = "spot_flag_data";
    public static final String TRACK_ID = "track_id";

    public static final int CODE_FAIL_USER_NOT_EXIST = 5100;             //用户不存在
    public static final int ERR_CODE_NOT_FOUND_DEVICE = 5200;         //未找到设备
    public static final int ERR_CODE_DEVICE_HAS_BEEN_REGISTERED = 5204;      //设备已经被其他用户注册
    public static final int ERR_CODE_REGISTER_DEVICE_FAIL = 5205;     //注册设备失败
}
