package com.evertrend.tiger.device.utils;

public class NetReq {
    public static final String REQ_HOST = "http://172.27.35.1/nalcol/frontend/web/index.php?r=";

    public static final String NET_ASSOCIATED_DEVICE = REQ_HOST + "api/remote/associated-device";
    public static final String NET_REGISTER_DEVICE = REQ_HOST + "api/remote/register-device";

    public static final String TOKEN = "token";

    public static final String RESULT_CODE = "res_code";
    public static final String RESULT_DESC = "res_desc";
    public static final String RESULT_DATA = "res_data";
    public static final String REG_CODE = "reg_code";

    public static final int ERR_CODE_NOT_FOUND_DEVICE = 5200;         //未找到设备
    public static final int ERR_CODE_DEVICE_HAS_BEEN_REGISTERED = 5204;      //设备已经被其他用户注册
    public static final int ERR_CODE_REGISTER_DEVICE_FAIL = 5205;     //注册设备失败

    public static final int CODE_SUCCESS = 0;
}
