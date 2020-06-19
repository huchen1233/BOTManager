package com.evertrend.tiger.common.bean.event;

import com.alibaba.fastjson.JSONObject;

public class DeviceExceptionEvent {
    private JSONObject jsonObject;
    public DeviceExceptionEvent(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }
}
