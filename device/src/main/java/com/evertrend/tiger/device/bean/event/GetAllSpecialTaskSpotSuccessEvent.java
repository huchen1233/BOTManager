package com.evertrend.tiger.device.bean.event;

import com.evertrend.tiger.common.bean.RobotSpot;

import java.util.List;

public class GetAllSpecialTaskSpotSuccessEvent {
    private List<RobotSpot> robotSpotList;
    public GetAllSpecialTaskSpotSuccessEvent(List<RobotSpot> robotSpotList) {
        this.robotSpotList = robotSpotList;
    }

    public List<RobotSpot> getRobotSpotList() {
        return robotSpotList;
    }
}
