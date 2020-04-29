package com.evertrend.tiger.device.bean.event;

import com.evertrend.tiger.common.bean.MapPages;

public class ChoiceMapPagesEvent {
    private MapPages mapPages;
    private int type;
    public ChoiceMapPagesEvent(MapPages mapPages, int type) {
        this.mapPages = mapPages;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public MapPages getMapPages() {
        return mapPages;
    }
}
