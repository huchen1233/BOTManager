package com.evertrend.tiger.device.bean.event;

import com.evertrend.tiger.device.bean.Device;

public class SaveBasicConfigEvent {
    private Device device;
    public SaveBasicConfigEvent(Device device) {
        this.device = device;
    }

    public Device getDevice() {
        return device;
    }
}
