package com.evertrend.tiger.common.bean.event;

public class SetPoseFailEvent {
    private String desc;
    public SetPoseFailEvent(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
