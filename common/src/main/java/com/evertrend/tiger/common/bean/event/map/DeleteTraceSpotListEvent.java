package com.evertrend.tiger.common.bean.event.map;

import com.evertrend.tiger.common.bean.RobotSpot;

import java.util.List;

public class DeleteTraceSpotListEvent {
    private List<RobotSpot> mRobotSpotList;
    private RobotSpot robotSpot;
    public DeleteTraceSpotListEvent(List<RobotSpot> mTraces, RobotSpot robotSpot) {
        this.mRobotSpotList = mTraces;
        this.robotSpot = robotSpot;
    }

    public List<RobotSpot> getmRobotSpotList() {
        return mRobotSpotList;
    }

    public RobotSpot getRobotSpot() {
        return robotSpot;
    }
}
