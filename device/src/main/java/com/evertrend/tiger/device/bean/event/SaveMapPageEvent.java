package com.evertrend.tiger.device.bean.event;

import com.evertrend.tiger.common.bean.MapPages;

public class SaveMapPageEvent {
    private MapPages mapPages;
    private boolean isUpdate;
    public SaveMapPageEvent(MapPages mapPages, boolean isUpdate) {
        this.mapPages = mapPages;
        this.isUpdate = isUpdate;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public MapPages getMapPages() {
        return mapPages;
    }
}
