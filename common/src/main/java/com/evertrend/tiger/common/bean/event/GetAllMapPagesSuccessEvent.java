package com.evertrend.tiger.common.bean.event;

import com.evertrend.tiger.common.bean.MapPages;

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
