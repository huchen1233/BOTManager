package com.evertrend.tiger.common.bean.event.map;

import com.slamtec.slamware.geometry.Line;

import java.util.List;

public class ClearVirtualTracks {
    private List<Line> clearLines;
    public ClearVirtualTracks(List<Line> clearLines) {
        this.clearLines = clearLines;
    }

    public List<Line> getClearLines() {
        return clearLines;
    }
}
