package com.evertrend.tiger.common.utils.general;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AppSharePreference {
    public static final String SHARED_PREFERENCE_NAME = "evertrend_botmanager_pre";
    private static AppSharePreference appSP;
    private static SharedPreferences sp;

    public static final String LOG_FLAG = "log_flag";
    public static final String IS_INPUT_LOG_I = "isInputLogI";
    public static final String IS_INPUT_LOG_D = "isInputLogD";
    public static final String IS_INPUT_LOG_E = "isInputLogE";
    public static final String IS_LOGIN = "IS_LOGIN";

    //初始化保存的环境
    public static void initSharedPreference(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
        }
    }

    //获取本类的一个实例
    public static synchronized AppSharePreference getAppSharedPreference() {
        if (appSP == null) {
            appSP = new AppSharePreference();
        }
        return appSP;
    }

    public void saveIsLogin(boolean isLogin) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(IS_LOGIN, isLogin);
        editor.apply();
    }
    public boolean loadIsLogin() {
        return sp.getBoolean(IS_LOGIN, false);
    }

    public String loadLogFlag() {
        return sp.getString(LOG_FLAG, "chenhu");
    }
    public void saveLogFlag(String logFlag) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(LOG_FLAG, logFlag);
        editor.apply();
    }

    public boolean loadIsInputLogI() {
        return sp.getBoolean(IS_INPUT_LOG_I, false);
    }
    public void saveIsInputLogI(boolean isInputLogI) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(IS_INPUT_LOG_I, isInputLogI);
        editor.apply();
    }
//
//    public boolean loadIsInputLogD() {
//        return sp.getBoolean(IS_INPUT_LOG_D, true);
//    }
//
//    public void saveIsInputLogD(boolean isInputLogD) {
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putBoolean(IS_INPUT_LOG_D, isInputLogD);
//        editor.apply();
//    }
//
//    public boolean loadIsInputLogE() {
//        return sp.getBoolean(IS_INPUT_LOG_E, true);
//    }
//
//    public void saveIsInputLogE(boolean isInputLogE) {
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putBoolean(IS_INPUT_LOG_E, isInputLogE);
//        editor.apply();
//    }
}
