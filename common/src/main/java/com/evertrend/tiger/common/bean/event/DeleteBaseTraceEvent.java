package com.evertrend.tiger.common.bean.event;

import com.evertrend.tiger.common.bean.BaseTrace;

public class DeleteBaseTraceEvent {
    private BaseTrace baseTrace;
    public DeleteBaseTraceEvent(BaseTrace baseTrace) {
        this.baseTrace = baseTrace;
    }

    public BaseTrace getBaseTrace() {
        return baseTrace;
    }
}
