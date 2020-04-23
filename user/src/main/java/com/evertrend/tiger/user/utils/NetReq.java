package com.evertrend.tiger.user.utils;

import com.evertrend.tiger.common.utils.network.CommonNetReq;

public class NetReq {

    public static final String NET_LOGIN_PASS = CommonNetReq.REQ_HOST + "api/user/login-pass";

    public static final String MOBILE = "mobile";
    public static final String VERI_CODE = "code";
    public static final String PASSWORD = "password";
    public static final String RESULT_TOKEN = "token";

    public static final int CODE_FAIL_USER_NOT_EXIST = 5100;             //用户不存在
}
