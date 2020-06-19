package com.evertrend.tiger.device.bean.event;

import com.evertrend.tiger.common.bean.CleanTask;

public class ChoiceCleanTaskEvent {
    private CleanTask cleanTask;
    private String mark;
    public ChoiceCleanTaskEvent(CleanTask cleanTask, String mark) {
        this.cleanTask = cleanTask;
        this.mark = mark;
    }

    public CleanTask getCleanTask() {
        return cleanTask;
    }

    public String getMark() {
        return mark;
    }
}
