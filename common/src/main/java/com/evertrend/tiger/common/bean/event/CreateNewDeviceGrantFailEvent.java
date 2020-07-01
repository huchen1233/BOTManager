package com.evertrend.tiger.common.bean.event;

public class CreateNewDeviceGrantFailEvent {
    private int failCode;
    public CreateNewDeviceGrantFailEvent(int failCode) {
        this.failCode = failCode;
    }

    public int getFailCode() {
        return failCode;
    }
}
