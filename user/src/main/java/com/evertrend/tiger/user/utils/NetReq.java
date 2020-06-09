package com.evertrend.tiger.user.utils;

import com.evertrend.tiger.common.utils.network.CommonNetReq;

public class NetReq {

    public static final String NET_LOGIN_PASS = CommonNetReq.REQ_HOST + "api/user/login-pass";
    public static final String NET_LOGIN_PASS_EMAIL = CommonNetReq.REQ_HOST + "api/user/login-pass-email";
    public static final String NET_LOGIN_CODE = CommonNetReq.REQ_HOST + "api/user/login-code";
    public static final String NET_LOGIN_CODE_EMAIL = CommonNetReq.REQ_HOST + "api/user/login-code-email";
    public static final String NET_MOBILE_VERIFICATION_CODE = CommonNetReq.REQ_HOST + "api/user/send-mobile-verification-code";
    public static final String NET_EMAIL_VERIFICATION_CODE = CommonNetReq.REQ_HOST + "api/user/send-email-verification-code";
    public static final String NET_SIGNUP = CommonNetReq.REQ_HOST + "api/user/signup";
    public static final String NET_SIGNUP_EMAIL = CommonNetReq.REQ_HOST + "api/user/signup-email";

    public static final String NET_CHECK_UPDATE = CommonNetReq.REQ_HOST + "api/remote/check-update";

    public static final String MOBILE = "mobile";
    public static final String EMAIL = "email";
    public static final String FLAG = "flag";
    public static final String VERI_CODE = "code";
    public static final String PASSWORD = "password";
    public static final String RESULT_TOKEN = "token";

    public static final int FLAG_SIGNUP = 1;
    public static final int FLAG_LOGIN = 2;
    public static final int FLAG_RESET_PASS = 3;

    public static final int CODE_FAIL_USER_NOT_EXIST = 5100;             //用户不存在
    public static final int CODE_FAIL_USER_EXIST = 5103;             //用户已存在
    public static final int ERR_CODE_SIGNUP_FAILURE = 5111;             //注册失败
}
