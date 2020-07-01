package com.evertrend.tiger.common.bean.event;

import com.evertrend.tiger.common.bean.DeviceGrant;

public class UpdateDeviceGrantSuccessEvent {
    private DeviceGrant deviceGrant;
    public UpdateDeviceGrantSuccessEvent(DeviceGrant deviceGrant) {
        this.deviceGrant = deviceGrant;
    }

    public DeviceGrant getDeviceGrant() {
        return deviceGrant;
    }
}
