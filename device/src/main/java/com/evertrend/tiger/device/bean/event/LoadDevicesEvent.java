package com.evertrend.tiger.device.bean.event;

import com.evertrend.tiger.device.bean.Device;

import java.util.List;

public class LoadDevicesEvent {
    private List<Device> deviceList;
    public LoadDevicesEvent(List<Device> deviceList) {
        this.deviceList = deviceList;
    }

    public List<Device> getDeviceList() {
        return deviceList;
    }
}
