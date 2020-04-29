package com.evertrend.tiger.common.bean.event.map;

public class SaveTraceSpotListEvent {
    private boolean isSingle;//true 单个点，false List
    public SaveTraceSpotListEvent(boolean isSingle) {
        this.isSingle = isSingle;
    }

    public boolean isSingle() {
        return isSingle;
    }
}
