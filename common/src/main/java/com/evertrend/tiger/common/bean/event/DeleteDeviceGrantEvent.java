package com.evertrend.tiger.common.bean.event;

import com.evertrend.tiger.common.bean.DeviceGrant;

public class DeleteDeviceGrantEvent {
    private DeviceGrant deviceGrant;
    public DeleteDeviceGrantEvent(DeviceGrant deviceGrant) {
        this.deviceGrant = deviceGrant;
    }

    public DeviceGrant getDeviceGrant() {
        return deviceGrant;
    }
}
