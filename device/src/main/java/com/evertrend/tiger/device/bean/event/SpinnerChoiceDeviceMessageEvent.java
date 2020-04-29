package com.evertrend.tiger.device.bean.event;

import com.evertrend.tiger.common.bean.Device;

public class SpinnerChoiceDeviceMessageEvent {
    private Device device;
    public SpinnerChoiceDeviceMessageEvent(Device device) {
        this.device = device;
    }

    public Device getDevice() {
        return device;
    }
}
