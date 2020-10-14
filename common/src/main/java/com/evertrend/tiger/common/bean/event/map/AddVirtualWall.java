package com.evertrend.tiger.common.bean.event.map;

import com.slamtec.slamware.geometry.Line;

public class AddVirtualWall {
    private Line wall;
    public AddVirtualWall(Line wall) {
        this.wall = wall;
    }

    public Line getWall() {
        return wall;
    }
}
