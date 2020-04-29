package com.evertrend.tiger.common.bean.event.slamtec;

import com.slamtec.slamware.action.Path;


public class RemainingMilestonesGetEvent {
    private Path remainingMilestones;

    public RemainingMilestonesGetEvent(Path remainingMilestones) {
        this.remainingMilestones = remainingMilestones;
    }

    public Path getRemainingMilestones() {
        return remainingMilestones;
    }
}
