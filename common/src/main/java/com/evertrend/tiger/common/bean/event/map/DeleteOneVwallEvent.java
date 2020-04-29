package com.evertrend.tiger.common.bean.event.map;

import com.slamtec.slamware.geometry.Line;

public class DeleteOneVwallEvent {
    private Line line;
    public DeleteOneVwallEvent(Line line) {
        this.line = line;
    }

    public Line getLine() {
        return line;
    }
}
