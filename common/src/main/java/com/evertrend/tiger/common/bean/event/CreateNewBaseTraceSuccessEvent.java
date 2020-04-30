package com.evertrend.tiger.common.bean.event;

import com.evertrend.tiger.common.bean.BaseTrace;

public class CreateNewBaseTraceSuccessEvent {
    private BaseTrace baseTrace;
    public CreateNewBaseTraceSuccessEvent(BaseTrace baseTrace) {
        this.baseTrace = baseTrace;
    }

    public BaseTrace getBaseTrace() {
        return baseTrace;
    }
}
