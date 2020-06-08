package com.evertrend.tiger.common.bean.event.map;

import com.evertrend.tiger.common.bean.BaseTrace;
import com.evertrend.tiger.common.bean.TracePath;

public class ChoiceMapPagesTracePathEvent {
    private BaseTrace baseTrace;
    private int mark;
    public ChoiceMapPagesTracePathEvent(BaseTrace baseTrace, int mark) {
        this.baseTrace = baseTrace;
        this.mark = mark;
    }

    public int getMark() {
        return mark;
    }

    public BaseTrace getBaseTrace() {
        return baseTrace;
    }
}
