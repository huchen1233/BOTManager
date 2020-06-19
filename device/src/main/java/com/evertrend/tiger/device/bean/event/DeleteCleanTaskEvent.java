package com.evertrend.tiger.device.bean.event;

import com.evertrend.tiger.common.bean.CleanTask;

public class DeleteCleanTaskEvent {
    private CleanTask cleanTask;
    public DeleteCleanTaskEvent(CleanTask cleanTask) {
        this.cleanTask = cleanTask;
    }

    public CleanTask getCleanTask() {
        return cleanTask;
    }
}
