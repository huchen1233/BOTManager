package com.evertrend.tiger.common.bean.event.slamtec;

public class MapUpdataEvent {
    private boolean isMapUpdata;

    public MapUpdataEvent(boolean isMapUpdata) {
        this.isMapUpdata = isMapUpdata;
    }

    public boolean isMapUpdata() {
        return this.isMapUpdata;
    }
}
