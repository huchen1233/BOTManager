package com.evertrend.tiger.common.bean.event.map;

import com.evertrend.tiger.common.bean.TracePath;

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
