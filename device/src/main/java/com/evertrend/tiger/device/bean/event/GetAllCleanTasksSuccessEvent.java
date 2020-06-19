package com.evertrend.tiger.device.bean.event;

import com.evertrend.tiger.common.bean.CleanTask;

import java.util.List;

public class GetAllCleanTasksSuccessEvent {
    private List<CleanTask> cleanTaskList;
    public GetAllCleanTasksSuccessEvent(List<CleanTask> cleanTaskList) {
        this.cleanTaskList = cleanTaskList;
    }

    public List<CleanTask> getCleanTaskList() {
        return cleanTaskList;
    }
}
