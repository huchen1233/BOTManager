package com.evertrend.tiger.common.bean.event.map;

import com.slamtec.slamware.geometry.Line;

public class AddVirtualTrack {
    private Line track;
    public AddVirtualTrack(Line track) {
        this.track = track;
    }

    public Line getTrack() {
        return track;
    }
}
