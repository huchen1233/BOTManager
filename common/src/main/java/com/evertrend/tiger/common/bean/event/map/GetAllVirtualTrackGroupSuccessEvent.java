package com.evertrend.tiger.common.bean.event.map;

import com.evertrend.tiger.common.bean.VirtualTrackGroup;

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
