package com.evertrend.tiger.common.bean.event.map;

public class DeleteEvent {
    private int type;
    public DeleteEvent() {
    }
    public DeleteEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
