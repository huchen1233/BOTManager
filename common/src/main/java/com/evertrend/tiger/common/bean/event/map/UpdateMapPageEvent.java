package com.evertrend.tiger.common.bean.event.map;

import com.evertrend.tiger.common.bean.MapPages;

public class UpdateMapPageEvent {
    private MapPages mapPages;
    public UpdateMapPageEvent(MapPages mapPages) {
        this.mapPages = mapPages;
    }

    public MapPages getMapPages() {
        return mapPages;
    }
}
