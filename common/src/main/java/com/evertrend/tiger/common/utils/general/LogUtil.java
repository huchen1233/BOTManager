package com.evertrend.tiger.common.utils.general;

import android.util.Log;

public class LogUtil {
    private static final String FLAG = " chenhu";
    private static boolean isInputLogI = true;
    private static boolean isInputLogD = true;
    private static boolean isInputLogE = true;

    public static void i(String TAG, String message) {
        if (AppSharePreference.getAppSharedPreference().loadIsInputLogI()) {
            Log.i(TAG + AppSharePreference.getAppSharedPreference().loadLogFlag() + FLAG, message);
        }
    }

    public static void d(String TAG, String message) {
        if (isInputLogD) {
            Log.d(TAG + AppSharePreference.getAppSharedPreference().loadLogFlag() + FLAG, message);
        }
    }

    public static void e(String TAG, String message) {
        if (isInputLogE) {
            Log.e(TAG + AppSharePreference.getAppSharedPreference().loadLogFlag() + FLAG, message);
        }
    }
}
