package com.evertrend.tiger.common.bean.event;

import com.evertrend.tiger.common.bean.BaseTrace;

public class ChoiceMapPagesBaseTraceEvent {
    private BaseTrace baseTrace;
    private int mark;
    private int type;
    public ChoiceMapPagesBaseTraceEvent(BaseTrace baseTrace, int mark, int type) {
        this.baseTrace = baseTrace;
        this.mark = mark;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public BaseTrace getBaseTrace() {
        return baseTrace;
    }

    public int getMark() {
        return mark;
    }
}
