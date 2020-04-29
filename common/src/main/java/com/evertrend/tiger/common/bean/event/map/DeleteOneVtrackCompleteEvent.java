package com.evertrend.tiger.common.bean.event.map;

import com.slamtec.slamware.geometry.Line;

public class DeleteOneVtrackCompleteEvent {
    private Line line;
    private int type;
    public DeleteOneVtrackCompleteEvent(Line line, int type) {
        this.line = line;
        this.type = type;
    }

    public Line getLine() {
        return line;
    }

    public int getType() {
        return type;
    }
}
