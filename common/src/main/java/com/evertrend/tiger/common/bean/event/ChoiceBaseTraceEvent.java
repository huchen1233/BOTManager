package com.evertrend.tiger.common.bean.event;

import com.evertrend.tiger.common.bean.BaseTrace;

public class ChoiceBaseTraceEvent {
    private BaseTrace baseTrace;
    private int type;

    public ChoiceBaseTraceEvent(BaseTrace baseTrace, int type) {
        this.baseTrace = baseTrace;
        this.type = type;
    }

    public BaseTrace getBaseTrace() {
        return baseTrace;
    }

    public int getType() {
        return type;
    }
}
