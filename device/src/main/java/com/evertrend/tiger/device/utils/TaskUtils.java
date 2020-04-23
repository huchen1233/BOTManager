package com.evertrend.tiger.device.utils;

import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.evertrend.tiger.common.utils.general.AppSharePreference;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.common.utils.network.CommonNetReq;
import com.evertrend.tiger.common.utils.network.OKHttpManager;
import com.evertrend.tiger.device.bean.Device;
import com.evertrend.tiger.device.bean.event.LoadDevicesEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

public class TaskUtils {
    public static final String TAG = TaskUtils.class.getSimpleName();

    public static class TaskGetAssocitedDevice implements Runnable {
        @Override
        public void run() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            OKHttpManager.getInstance().sendComplexForm(NetReq.NET_ASSOCIATED_DEVICE, map, new OKHttpManager.FuncJsonObj() {
                        @Override
                        public void onResponse(JSONObject jsonObject) throws JSONException {
                            try {
                                LogUtil.i(TAG, jsonObject.getString(CommonNetReq.RESULT_DESC));
                                switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                                    case CommonNetReq.CODE_SUCCESS:
                                        List<Device> deviceList = JsonAnalysisUtil.loadAllDevice(jsonObject.getJSONArray(CommonNetReq.RESULT_DATA));
                                        EventBus.getDefault().post(new LoadDevicesEvent(deviceList));
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
