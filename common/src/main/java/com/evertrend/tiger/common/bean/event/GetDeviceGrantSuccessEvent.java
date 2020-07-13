package com.evertrend.tiger.common.bean.event;

import com.evertrend.tiger.common.bean.DeviceGrant;

public class GetDeviceGrantSuccessEvent {
    private DeviceGrant deviceGrant;
    public GetDeviceGrantSuccessEvent(DeviceGrant deviceGrant) {
        this.deviceGrant = deviceGrant;
    }

    public DeviceGrant getDeviceGrant() {
        return deviceGrant;
    }
}
