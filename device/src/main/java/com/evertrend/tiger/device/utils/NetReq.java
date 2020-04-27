package com.evertrend.tiger.device.utils;

import com.evertrend.tiger.common.utils.network.CommonNetReq;

public class NetReq {

    public static final String NET_ASSOCIATED_DEVICE = CommonNetReq.REQ_HOST + "api/remote/associated-device";
    public static final String NET_REGISTER_DEVICE = CommonNetReq.REQ_HOST + "api/remote/register-device";
    public static final String NET_GET_DEVICE = CommonNetReq.REQ_HOST + "api/remote/get-device";

    public static final String REG_CODE = "reg_code";
    public static final String DEVICE_ID = "device_id";

    public static final int ERR_CODE_NOT_FOUND_DEVICE = 5200;         //未找到设备
    public static final int ERR_CODE_DEVICE_HAS_BEEN_REGISTERED = 5204;      //设备已经被其他用户注册
    public static final int ERR_CODE_REGISTER_DEVICE_FAIL = 5205;     //注册设备失败
}
