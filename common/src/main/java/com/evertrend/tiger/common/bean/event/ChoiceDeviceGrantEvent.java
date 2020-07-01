package com.evertrend.tiger.common.bean.event;

import com.evertrend.tiger.common.bean.DeviceGrant;

public class ChoiceDeviceGrantEvent {
    private DeviceGrant deviceGrant;
    private int type;

    public ChoiceDeviceGrantEvent(DeviceGrant deviceGrant, int type) {
        this.deviceGrant = deviceGrant;
        this.type = type;
    }

    public DeviceGrant getDeviceGrant() {
        return deviceGrant;
    }

    public int getType() {
        return type;
    }
}
