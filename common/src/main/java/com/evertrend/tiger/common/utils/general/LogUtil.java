package com.evertrend.tiger.common.utils.general;

import android.content.Context;
import android.util.Log;

public class LogUtil {
    //可以全局控制是否打印log日志
    private static boolean isPrintLog = true;
    private static int LOG_MAXLENGTH = 4*1024;

    private static final String FLAG = " chenhu";
    private static boolean isInputLogI = true;
    private static boolean isInputLogD = true;
    private static boolean isInputLogE = true;

    public static void i(Context context, String TAG, String message) {
        if (Utils.isDebug(context)) {
            if (isInputLogI) {
                Log.i(TAG + AppSharePreference.getAppSharedPreference().loadLogFlag() + FLAG, message);
            }
        } else {
            if (AppSharePreference.getAppSharedPreference().loadIsInputLogI()) {
                Log.i(TAG + AppSharePreference.getAppSharedPreference().loadLogFlag() + FLAG, message);
            }
        }
    }

    public static void d(String TAG, String message) {
        if (isInputLogD) {
            if (isPrintLog) {
                int strLength = message.length();
                int start = 0;
                int end = LOG_MAXLENGTH;
                for (int i = 0; i < 100; i++) {
                    if (strLength > end) {
                        Log.d(TAG + AppSharePreference.getAppSharedPreference().loadLogFlag() + FLAG + i, message.substring(start, end));
                        start = end;
                        end = end + LOG_MAXLENGTH;
                    } else {
                        Log.d(TAG + AppSharePreference.getAppSharedPreference().loadLogFlag() + FLAG + i, message.substring(start, strLength));
                        break;
                    }
                }
            }
        }
    }

    public static void e(String TAG, String message) {
        if (isInputLogE) {
            Log.e(TAG + AppSharePreference.getAppSharedPreference().loadLogFlag() + FLAG, message);
        }
    }
}
