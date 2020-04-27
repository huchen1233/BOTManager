package com.evertrend.tiger.device.bean.event;

import com.evertrend.tiger.device.bean.CleanTask;

public class SaveCleanTaskSuccessEvent {
    private CleanTask cleanTask;
    private boolean isUpdate;
    public SaveCleanTaskSuccessEvent(CleanTask cleanTask, boolean isUpdate) {
        this.cleanTask = cleanTask;
        this.isUpdate = isUpdate;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public CleanTask getCleanTask() {
        return cleanTask;
    }
}
