package com.evertrend.tiger.common.bean.event;

public class DialogChoiceEvent {
    private int type;
    public DialogChoiceEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
