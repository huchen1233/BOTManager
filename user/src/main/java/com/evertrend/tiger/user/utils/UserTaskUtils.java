package com.evertrend.tiger.user.utils;

import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.evertrend.tiger.common.utils.general.AppSharePreference;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.common.utils.network.CommonNetReq;
import com.evertrend.tiger.common.utils.network.OKHttpManager;
import com.evertrend.tiger.user.bean.UpdateApp;
import com.evertrend.tiger.user.bean.event.CheckUpdateEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

public class UserTaskUtils {
    private static final String TAG = UserTaskUtils.class.getSimpleName();

    public static class TaskCheckUpdate implements Runnable {
        @Override
        public void run() {
            startCheckUpdate();
        }

        private void startCheckUpdate() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            OKHttpManager.getInstance().sendComplexForm(NetReq.NET_CHECK_UPDATE, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        LogUtil.i(TAG, jsonObject.getString(CommonNetReq.RESULT_DESC));
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                UpdateApp updateApp = new UpdateApp();
                                updateApp.setIsUpdate(jsonObject.getJSONObject(CommonNetReq.RESULT_DATA).getIntValue("isUpdate"));
                                updateApp.setNewVersion(jsonObject.getJSONObject(CommonNetReq.RESULT_DATA).getString("newVersion"));
                                updateApp.setApkFileUrl(jsonObject.getJSONObject(CommonNetReq.RESULT_DATA).getString("apkFileUrl"));
                                updateApp.setUpdateLog(jsonObject.getJSONObject(CommonNetReq.RESULT_DATA).getString("updateLog"));
                                updateApp.setTargetSize(jsonObject.getJSONObject(CommonNetReq.RESULT_DATA).getString("targetSize"));
                                updateApp.setIsConstraint(jsonObject.getJSONObject(CommonNetReq.RESULT_DATA).getIntValue("isConstraint"));
                                EventBus.getDefault().post(new CheckUpdateEvent(updateApp));
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "出错：解析数据失败");
                    }
                }
            }, new OKHttpManager.FuncFailure() {
                @Override
                public void onFailure() {
                    Log.e(TAG, "出错：请求网络失败");
                }
            });
        }
    }
}
