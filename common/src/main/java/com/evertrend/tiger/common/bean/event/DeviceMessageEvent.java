package com.evertrend.tiger.common.bean.event;

import com.evertrend.tiger.common.bean.Device;

public class DeviceMessageEvent {
    private Device message;
    public DeviceMessageEvent(Device message){
        this.message=message;
    }
    public Device getMessage() {
        return message;
    }

    public void setMessage(Device message) {
        this.message = message;
    }
}
