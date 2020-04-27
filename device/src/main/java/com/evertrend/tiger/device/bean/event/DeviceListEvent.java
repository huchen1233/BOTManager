package com.evertrend.tiger.device.bean.event;

import com.evertrend.tiger.device.bean.Device;

import java.util.List;

public class DeviceListEvent {
    private List<Device> deviceList;
    public DeviceListEvent(List<Device> deviceList) {
        this.deviceList = deviceList;
    }

    public List<Device> getDeviceList() {
        return deviceList;
    }
}
