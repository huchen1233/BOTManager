package com.evertrend.tiger.common.bean.event.map;

import com.evertrend.tiger.common.bean.TracePath;

public class ChoiceMapPagesTracePathEvent {
    private TracePath tracePath;
    private int mark;
    public ChoiceMapPagesTracePathEvent(TracePath tracePath, int mark) {
        this.tracePath = tracePath;
        this.mark = mark;
    }

    public int getMark() {
        return mark;
    }

    public TracePath getTracePath() {
        return tracePath;
    }
}
