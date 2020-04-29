package com.evertrend.tiger.common.bean.event.map;

import com.slamtec.slamware.geometry.Line;

public class DeleteOneVwallCompleteEvent {
    private Line line;
    private int type;
    public DeleteOneVwallCompleteEvent(Line line, int type) {
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
