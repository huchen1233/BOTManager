package com.evertrend.tiger.common.bean.event.slamtec;


import com.slamtec.slamware.robot.Map;

public class MapGetEvent {
    private Map map;

    public MapGetEvent(Map map) {
        this.map = map;
    }

    public Map getMap() {
        return map;
    }
}
