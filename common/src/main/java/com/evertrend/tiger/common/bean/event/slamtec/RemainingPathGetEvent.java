package com.evertrend.tiger.common.bean.event.slamtec;

import com.slamtec.slamware.action.Path;

public class RemainingPathGetEvent {
    private Path remainingPath;

    public RemainingPathGetEvent(Path remainingPath) {
        this.remainingPath = remainingPath;
    }

    public Path getRemainingPath() {
        return remainingPath;
    }
}
