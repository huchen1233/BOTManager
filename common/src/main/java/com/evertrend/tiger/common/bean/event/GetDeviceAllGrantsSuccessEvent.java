package com.evertrend.tiger.common.bean.event;

import com.evertrend.tiger.common.bean.DeviceGrant;

import java.util.List;

public class GetDeviceAllGrantsSuccessEvent {
    private List<DeviceGrant> deviceGrantList;
    public GetDeviceAllGrantsSuccessEvent(List<DeviceGrant> deviceGrantList) {
        this.deviceGrantList = deviceGrantList;
    }

    public List<DeviceGrant> getDeviceGrantList() {
        return deviceGrantList;
    }
}
