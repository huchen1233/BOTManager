package com.evertrend.tiger.common.bean.event;

import com.evertrend.tiger.common.bean.Device;

public class ChoiceDeviceEvent {
    private Device device;
    private int operation;
    public ChoiceDeviceEvent(Device device, int operation) {
        this.device = device;
        this.operation = operation;
    }

    public int getOperation() {
        return operation;
    }

    public Device getDevice() {
        return device;
    }
}
