package com.evertrend.tiger.common.bean.event.map;

import com.evertrend.tiger.common.bean.RobotSpot;

import java.util.List;

public class GetTraceSpotEvent {
    private List<RobotSpot> traceSpotList;
    private boolean isActive;
    public GetTraceSpotEvent(List<RobotSpot> traceSpotList, boolean isActive) {
        this.traceSpotList = traceSpotList;
        this.isActive = isActive;
    }

    public List<RobotSpot> getTraceSpotList() {
        return traceSpotList;
    }

    public boolean isActive() {
        return isActive;
    }
}
