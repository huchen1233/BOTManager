package com.evertrend.tiger.common.bean.event;

import com.baidu.mapapi.model.LatLng;

import java.util.List;

public class GetGpsFenceOKEvent {
    List<LatLng> points;
    public GetGpsFenceOKEvent(List<LatLng> points) {
        this.points = points;
    }

    public List<LatLng> getPoints() {
        return points;
    }
}
