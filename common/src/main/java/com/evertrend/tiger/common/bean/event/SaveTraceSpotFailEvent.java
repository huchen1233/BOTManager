package com.evertrend.tiger.common.bean.event;

import com.alibaba.fastjson.JSONObject;

public class SaveTraceSpotFailEvent {
    private JSONObject jsonObject;
    public SaveTraceSpotFailEvent(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }
}
