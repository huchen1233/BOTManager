package com.evertrend.tiger.common.bean.event.map;

public class RelocationOrSetCurrentMapEvent {
    private int type;
    public RelocationOrSetCurrentMapEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
