package com.evertrend.tiger.common.bean.event.map;

import com.evertrend.tiger.common.bean.RobotSpot;

import java.util.List;

public class DeleteTraceSpotListCompleteEvent {
    private List<RobotSpot> mRobotSpotList;

    public DeleteTraceSpotListCompleteEvent(List<RobotSpot> mRobotSpotList) {
        this.mRobotSpotList = mRobotSpotList;
    }

    public List<RobotSpot> getmRobotSpotList() {
        return mRobotSpotList;
    }
}
