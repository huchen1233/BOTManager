package com.evertrend.tiger.common;

import android.content.Context;

import com.evertrend.tiger.common.utils.general.AppSharePreference;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.litepal.LitePal;

public class EvertrendCommon {

    public static void initOption(Context context) {
        AppSharePreference.initSharedPreference(context);
        //二维码扫描初始化
        ZXingLibrary.initDisplayOpinion(context);
        //LitePal初始化
        LitePal.initialize(context);
    }
}
