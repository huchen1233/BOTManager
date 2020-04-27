package com.evertrend.tiger.device.bean.event;

import com.evertrend.tiger.device.bean.VirtualTrackGroup;

import java.util.List;

public class GetAllVirtualTrackGroupSuccessEvent {
    private List<VirtualTrackGroup> virtualTrackGroups;
    public GetAllVirtualTrackGroupSuccessEvent(List<VirtualTrackGroup> virtualTrackGroups) {
        this.virtualTrackGroups = virtualTrackGroups;
    }

    public List<VirtualTrackGroup> getVirtualTrackGroups() {
        return virtualTrackGroups;
    }
}
