package com.evertrend.tiger.common.bean.event.slamtec;

import com.slamtec.slamware.geometry.Line;

import java.util.List;

public class TrackGetEvent {
    private List<Line> tracks;
    private int type;

    public TrackGetEvent(List<Line> tracks, int type) {
        this.tracks = tracks;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public List<Line> getTracks() {
        return tracks;
    }
}

