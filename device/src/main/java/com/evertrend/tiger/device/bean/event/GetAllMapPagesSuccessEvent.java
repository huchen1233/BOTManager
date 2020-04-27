package com.evertrend.tiger.device.bean.event;

import com.evertrend.tiger.device.bean.MapPages;

import java.util.List;

public class GetAllMapPagesSuccessEvent {
    private List<MapPages> mapPagesList;
    public GetAllMapPagesSuccessEvent(List<MapPages> mapPagesList) {
        this.mapPagesList = mapPagesList;
    }

    public List<MapPages> getMapPagesList() {
        return mapPagesList;
    }
}
