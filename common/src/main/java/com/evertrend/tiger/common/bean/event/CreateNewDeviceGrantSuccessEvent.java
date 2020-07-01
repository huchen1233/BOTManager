package com.evertrend.tiger.common.bean.event;

import com.evertrend.tiger.common.bean.DeviceGrant;

public class CreateNewDeviceGrantSuccessEvent {
    private DeviceGrant deviceGrant;
    public CreateNewDeviceGrantSuccessEvent(DeviceGrant deviceGrant) {
        this.deviceGrant = deviceGrant;
    }

    public DeviceGrant getDeviceGrant() {
        return deviceGrant;
    }
}
