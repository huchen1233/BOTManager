package com.evertrend.tiger.common.bean.event.map;

import com.slamtec.slamware.geometry.Line;

import java.util.List;

public class ClearVirtualWalls {
    private List<Line> clearLines;
    public ClearVirtualWalls(List<Line> clearLines) {
        this.clearLines = clearLines;
    }

    public List<Line> getClearLines() {
        return clearLines;
    }
}
