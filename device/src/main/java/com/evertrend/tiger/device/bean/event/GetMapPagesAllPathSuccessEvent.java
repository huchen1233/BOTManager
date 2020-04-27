package com.evertrend.tiger.device.bean.event;

import com.evertrend.tiger.device.bean.TracePath;

import java.util.List;

public class GetMapPagesAllPathSuccessEvent {
    private List<TracePath> tracePathList;
    public GetMapPagesAllPathSuccessEvent(List<TracePath> tracePathList) {
        this.tracePathList = tracePathList;
    }

    public List<TracePath> getTracePathList() {
        return tracePathList;
    }
}
