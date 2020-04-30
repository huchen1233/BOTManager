package com.evertrend.tiger.common.bean.event;

import com.evertrend.tiger.common.bean.BaseTrace;

public class UpdateBaseTraceSuccessEvent {
    private BaseTrace baseTrace;
    public UpdateBaseTraceSuccessEvent(BaseTrace baseTrace) {
        this.baseTrace = baseTrace;
    }

    public BaseTrace getBaseTrace() {
        return baseTrace;
    }
}
