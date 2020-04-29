package com.evertrend.tiger.common.bean.event.map;

public class AddTrack {
    private int type;
    public AddTrack(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
