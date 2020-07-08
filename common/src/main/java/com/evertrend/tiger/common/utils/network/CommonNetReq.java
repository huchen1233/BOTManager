package com.evertrend.tiger.common.utils.network;

public class CommonNetReq {
    public static final String REQ_HOST = "http://172.27.35.1/nalcol/frontend/web/index.php?r=";
//    public static final String REQ_HOST = "https://robot.360yongli.com?r=";

    public static final String NET_GET_CURRENT_MAP_PAGE_ALL_PATH = REQ_HOST + "api/remote/get-map-page-all-path";
    public static final String NET_GET_DEVICE_ALL_GRANTS = REQ_HOST + "api/remote/get-device-all-grants";
    public static final String NET_GET_CURRENT_MAP_PAGE_ALL_VIRTUAL_TRACK_GROUP = REQ_HOST + "api/remote/get-map-page-all-virtual-track-group";
    public static final String NET_GET_CURRENT_MAP_PAGE_ALL_SPECIAL_TASK_SPOT = REQ_HOST + "api/remote/get-map-page-all-special-task-spot";
    public static final String NET_GET_TRACE_SPOT_LIST = REQ_HOST + "api/remote/get-trace-spot-list";
    public static final String NET_SAVE_CREATE_MAP = REQ_HOST + "api/remote/save-create-map";
    public static final String NET_GET_SAVE_MAP = REQ_HOST + "api/remote/get-save-map";
    public static final String NET_GET_DEVICE = REQ_HOST + "api/remote/get-device";
    public static final String NET_ADD_SPOT = REQ_HOST + "api/remote/add-spot";
    public static final String NET_ADD_SPOT_COMPLETE = REQ_HOST + "api/remote/add-spot-complete";
    public static final String NET_DELETE_TRACE_SPOT = REQ_HOST + "api/remote/delete-trace-spot";
    public static final String NET_SAVE_BATTERY = REQ_HOST + "api/remote/save-battery";
    public static final String NET_SAVE_WATER = REQ_HOST + "api/remote/save-water";
    public static final String NET_SAVE_TRASH_INTERVAL = REQ_HOST + "api/remote/save-trash-interval";
    public static final String NET_SAVE_BASIC_CONFIG = REQ_HOST + "api/remote/save-basic-config";
    public static final String NET_SET_DEVICE_STATUS = REQ_HOST + "api/remote/set-device-status";
    public static final String NET_SET_MAIN_SWEEP = REQ_HOST + "api/remote/set-main-sweep";
    public static final String NET_SET_SIDE_SWEEP = REQ_HOST + "api/remote/set-side-sweep";
    public static final String NET_SET_SPRINKLING_WATER = REQ_HOST + "api/remote/set-sprinkling-water";
    public static final String NET_SET_ALARM_LIGHT = REQ_HOST + "api/remote/set-alarm-light";
    public static final String NET_SET_FRONT_LIGHT = REQ_HOST + "api/remote/set-front-light";
    public static final String NET_SET_HORN = REQ_HOST + "api/remote/set-horn";
    public static final String NET_SET_SUCK_FUN = REQ_HOST + "api/remote/set-suck-fun";
    public static final String NET_SET_VIBRATING_DUST = REQ_HOST + "api/remote/set-vibrating-dust";
    public static final String NET_SET_MOTOR = REQ_HOST + "api/remote/set-motor";
    public static final String NET_SET_EMERGENCY_STOP = REQ_HOST + "api/remote/set-emergency-stop";
    public static final String NET_SET_TAIL_LIGHT = REQ_HOST + "api/remote/set-tail-light";
    public static final String NET_SET_LEFT_TAIL_LIGHT = REQ_HOST + "api/remote/set-left-tail-light";
    public static final String NET_SET_RIGHT_TAIL_LIGHT = REQ_HOST + "api/remote/set-right-tail-light";
    public static final String NET_GO_TO_RECHARGE = REQ_HOST + "api/remote/go-to-recharge";
    public static final String NET_GO_TO_EMPTY_TRASH = REQ_HOST + "api/remote/go-to-empty-trash";
    public static final String NET_GO_TO_GARAGE = REQ_HOST + "api/remote/go-to-garage";
    public static final String NET_GO_TO_WORK = REQ_HOST + "api/remote/go-to-work";
    public static final String NET_GO_TO_IDEL = REQ_HOST + "api/remote/go-to-idel";
    public static final String NET_GO_TO_ADD_WATER = REQ_HOST + "api/remote/go-to-add-water";
    public static final String NET_SAVE_WORK_TIME = REQ_HOST + "api/remote/save-work-time";
    public static final String NET_GET_ALL_TRACE_PATH = REQ_HOST + "api/remote/get-trace-path";
    public static final String NET_GET_ALL_MAP_PAGES = REQ_HOST + "api/remote/get-map-pages";
    public static final String NET_SET_CURRENT_TRACE_PATH = REQ_HOST + "api/remote/current-trace-path";
    public static final String NET_NEW_TRACE_PATH = REQ_HOST + "api/remote/new-trace-path";
    public static final String NET_UPDATE_TRACE_PATH = REQ_HOST + "api/remote/update-trace-path";
    public static final String NET_GET_CURRENT_TRACE_PATH = REQ_HOST + "api/remote/get-current-trace-path";
    public static final String NET_GET_CURRENT_MAP_PAGE = REQ_HOST + "api/remote/get-current-map-page";
    public static final String NET_DELETE_MAP_PAGE_PATH = REQ_HOST + "api/remote/delete-map-page-path";
    public static final String NET_READ_CONTROL_STATUS = REQ_HOST + "api/remote/read-control-status";
    public static final String NET_UPDATE_MAP_PAGE = REQ_HOST + "api/remote/update-map-page";
    public static final String NET_DELETE_MAP_PAGE = REQ_HOST + "api/remote/delete-map-page";
    public static final String NET_CREATE_NEW_MAP_APGE = REQ_HOST + "api/remote/create-new-map-page";
    public static final String NET_RELOCATION_MAP_APGE = REQ_HOST + "api/remote/relocation-map-page";
    public static final String NET_SET_CURRENT_MAP = REQ_HOST + "api/remote/set-current-map";
    public static final String NET_SAVE_VIRTUAL_TRACK = REQ_HOST + "api/remote/save-virtual-track";
    public static final String NET_NEW_VT_GROUP = REQ_HOST + "api/remote/new-vtgroup";
    public static final String NET_UPDATE_VT_GROUP = REQ_HOST + "api/remote/update-vtgroup";
    public static final String NET_DELETE_MAP_PAGE_VTGROUP = REQ_HOST + "api/remote/delete-map-page-vtgroup";
    public static final String NET_GET_RUN_LOGS = REQ_HOST + "api/remote/get-run-logs";
    public static final String NET_GET_DEVICE_EXCEPTION = REQ_HOST + "api/remote/get-device-exception";
    public static final String NET_GET_DEVICE_CRITICAL = REQ_HOST + "api/remote/get-device-critical";
    public static final String NET_NEW_DEVICE_GRANT = REQ_HOST + "api/remote/new-device-grant";
    public static final String NET_UPDATE_DEVICE_GRANT = REQ_HOST + "api/remote/update-device-grant";
    public static final String NET_DELETE_DEVICE_GRANT = REQ_HOST + "api/remote/delete-device-grant";
    public static final String NET_UPLOAD_TRACE_PATH_PIC = REQ_HOST + "api/remote/upload-trace-path-pic";
    public static final String NET_UPLOAD_MAP_PIC = REQ_HOST + "api/remote/upload-map-pic";

    public static final String TOKEN = "token";
    public static final String RESULT_CODE = "res_code";
    public static final String RESULT_DESC = "res_desc";
    public static final String RESULT_DATA = "res_data";
    public static final String RESULT_EXTRA = "res_extra";
    public static final String RESULT_PIN = "pin";

    public static final String DEVICE_ID = "device_id";
    public static final String MAP_PAGE = "map_page";
    public static final String TARGET_MAP_PAGE = "target_map_page";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String ID = "id";
    public static final String DESC = "desc";
    public static final String DEVICE = "device";
    public static final String FLAG = "flag";
    public static final String NUMBER = "number";
    public static final String SPOT_FLAG = "spot_flag";
    public static final String TRACE_PATH_ID = "trace_path_id";
    public static final String SPOT_FLAG_DATA = "spot_flag_data";
    public static final String TRACK_ID = "track_id";
    public static final String PAGE_NAME = "page_name";
    public static final String PAGE_DESC = "page_desc";
    public static final String PAGE_DO_SAVE_MAP = "do_save_map";
    public static final String VIRTUAL_TRACK = "virtual_track";
    public static final String PAGE = "page";
    public static final String AUTHORIZATION_ITEM = "authorization_item";
    public static final String USER_GRANTED = "user_granted";
    public static final String USER_FLAG = "user_flag";

    public static final int CODE_SUCCESS = 0;

    public static final int ERR_CODE_ADD_SPOT_FAIL = 5206;
    public static final int ERR_CODE_SAVE_CREATE_MAP_FAIL = 5207;
    public static final int ERR_CODE_GRANTED_USER_NON_EXISTENT = 5208;
    public static final int ERR_CODE_DUPLICATE_AUTHORIZATION = 5209;
    public static final int ERR_CODE_CANNOT_AUTHORIZE_TOYOURSELF = 5210;
    public static final int ERR_CODE_SAVE_FAIL = 5211;
    public static final int ERR_CODE_DELETE_GRANT_DEVICE_NON_EXISTENT = 5212;
}
