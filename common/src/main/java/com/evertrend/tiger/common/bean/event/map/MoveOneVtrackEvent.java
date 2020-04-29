package com.evertrend.tiger.common.bean.event.map;

import com.slamtec.slamware.geometry.Line;

public class MoveOneVtrackEvent {
    private Line line;
    private int moveType;
    private boolean isComplete;
    public MoveOneVtrackEvent(Line line, int moveType, boolean isComplete) {
        this.line = line;
        this.moveType = moveType;
        this.isComplete = isComplete;
    }

    public Line getLine() {
        return line;
    }

    public int getMoveType() {
        return moveType;
    }

    public boolean isComplete() {
        return isComplete;
    }
}
