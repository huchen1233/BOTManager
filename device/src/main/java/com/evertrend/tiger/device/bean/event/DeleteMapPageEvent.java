package com.evertrend.tiger.device.bean.event;

import com.evertrend.tiger.device.bean.MapPages;
public class DeleteMapPageEvent {
    private MapPages mapPages;
    public DeleteMapPageEvent(MapPages mapPages) {
        this.mapPages = mapPages;
    }

    public MapPages getMapPages() {
        return mapPages;
    }
}
