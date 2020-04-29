package com.evertrend.tiger.common.bean.event.map;

import com.evertrend.tiger.common.bean.VirtualTrackGroup;

public class SaveVirtualTrackEvent {
    private VirtualTrackGroup virtualTrackGroup;
    public SaveVirtualTrackEvent(VirtualTrackGroup virtualTrackGroup) {
        this.virtualTrackGroup = virtualTrackGroup;
    }

    public VirtualTrackGroup getVirtualTrackGroup() {
        return virtualTrackGroup;
    }
}
