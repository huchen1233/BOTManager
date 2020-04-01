package com.evertrend.tiger.common;

import android.content.Context;

import com.evertrend.tiger.common.utils.general.AppSharePreference;

public class EvertrendCommon {

    public static void initOption(Context context) {
        AppSharePreference.initSharedPreference(context);
    }
}
