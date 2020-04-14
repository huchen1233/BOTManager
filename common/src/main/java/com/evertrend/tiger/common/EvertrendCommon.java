package com.evertrend.tiger.common;

import android.content.Context;

import com.evertrend.tiger.common.utils.general.AppSharePreference;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

public class EvertrendCommon {

    public static void initOption(Context context) {
        AppSharePreference.initSharedPreference(context);
        //二维码扫描初始化
        ZXingLibrary.initDisplayOpinion(context);
    }
}
