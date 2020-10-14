package com.evertrend.tiger.common.bean.event.slamtec;

import com.slamtec.slamware.geometry.Line;

import java.util.List;

public class WallGetEvent {
    private List<Line> walls;
    private int type;

    public WallGetEvent(List<Line> walls) {
        this.walls = walls;
    }

    public WallGetEvent(List<Line> walls, int type) {
        this.walls = walls;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public List<Line> getWalls() {
        return walls;
    }
}

