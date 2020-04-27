package com.evertrend.tiger.device.utils;

import com.evertrend.tiger.common.utils.network.CommonNetReq;

public class NetReq {

    public static final String NET_ASSOCIATED_DEVICE = CommonNetReq.REQ_HOST + "api/remote/associated-device";
    public static final String NET_REGISTER_DEVICE = CommonNetReq.REQ_HOST + "api/remote/register-device";
    public static final String NET_GET_DEVICE = CommonNetReq.REQ_HOST + "api/remote/get-device";
    public static final String NET_GET_ALL_MAP_PAGES = CommonNetReq.REQ_HOST + "api/remote/get-map-pages";
    public static final String NET_GET_CLEAN_TASK = CommonNetReq.REQ_HOST + "api/remote/get-clean-task";
    public static final String NET_SAVE_BASIC_CONFIG = CommonNetReq.REQ_HOST + "api/remote/save-basic-config";
    public static final String NET_GET_CURRENT_MAP_PAGE_ALL_PATH = CommonNetReq.REQ_HOST + "api/remote/get-map-page-all-path";
    public static final String NET_GET_CURRENT_MAP_PAGE_ALL_VIRTUAL_TRACK_GROUP = CommonNetReq.REQ_HOST + "api/remote/get-map-page-all-virtual-track-group";
    public static final String NET_GET_CURRENT_MAP_PAGE_ALL_SPECIAL_TASK_SPOT = CommonNetReq.REQ_HOST + "api/remote/get-map-page-all-special-task-spot";

    public static final String NET_ADD_CLEAN_TASK = CommonNetReq.REQ_HOST + "api/remote/add-clean-task";
    public static final String NET_UPDATE_CLEAN_TASK = CommonNetReq.REQ_HOST + "api/remote/update-clean-task";
    public static final String NET_DELETE_CLEAN_TASK = CommonNetReq.REQ_HOST + "api/remote/delete-clean-task";

    public static final String REG_CODE = "reg_code";
    public static final String DEVICE_ID = "device_id";
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

    public static final int ERR_CODE_NOT_FOUND_DEVICE = 5200;         //未找到设备
    public static final int ERR_CODE_DEVICE_HAS_BEEN_REGISTERED = 5204;      //设备已经被其他用户注册
    public static final int ERR_CODE_REGISTER_DEVICE_FAIL = 5205;     //注册设备失败
}
