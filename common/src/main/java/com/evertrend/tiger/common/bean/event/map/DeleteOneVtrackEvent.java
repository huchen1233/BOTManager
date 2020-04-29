package com.evertrend.tiger.common.bean.event.map;

import com.slamtec.slamware.geometry.Line;

public class DeleteOneVtrackEvent {
    private Line line;
    public DeleteOneVtrackEvent(Line line) {
        this.line = line;
    }

    public Line getLine() {
        return line;
    }
}
