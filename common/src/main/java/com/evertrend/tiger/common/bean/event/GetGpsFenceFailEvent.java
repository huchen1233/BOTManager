package com.evertrend.tiger.common.bean.event;

public class GetGpsFenceFailEvent {
    String desc;
    public GetGpsFenceFailEvent(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
