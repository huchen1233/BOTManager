package com.evertrend.tiger.common.bean.event.map;

import com.evertrend.tiger.common.bean.RobotSpot;

public class DeleteOneTraceSpotListEvent {
    private RobotSpot robotSpot;
//    private RobotSpot localRobotSpot;
    public DeleteOneTraceSpotListEvent(RobotSpot spot) {
        this.robotSpot = robotSpot;
    }
//    public DeleteOneTraceSpotListEvent(RobotSpot spot, RobotSpot localRobotSpot) {
//        this.robotSpot = robotSpot;
//        this.localRobotSpot = localRobotSpot;
//    }

//    public RobotSpot getLocalRobotSpot() {
//        return localRobotSpot;
//    }

    public RobotSpot getRobotSpot() {
        return robotSpot;
    }
}
