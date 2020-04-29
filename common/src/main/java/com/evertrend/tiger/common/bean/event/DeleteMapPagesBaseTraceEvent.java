package com.evertrend.tiger.common.bean.event;

import com.evertrend.tiger.common.bean.BaseTrace;

public class DeleteMapPagesBaseTraceEvent {
    private BaseTrace baseTrace;
    public DeleteMapPagesBaseTraceEvent(BaseTrace baseTrace) {
        this.baseTrace = baseTrace;
    }

    public BaseTrace getBaseTrace() {
        return baseTrace;
    }
}
