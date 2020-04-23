package com.evertrend.tiger.common.bean.event;

public class SuccessEvent {
    private int type;
    public SuccessEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
