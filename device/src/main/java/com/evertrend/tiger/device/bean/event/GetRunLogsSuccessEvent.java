package com.evertrend.tiger.device.bean.event;

import com.evertrend.tiger.common.bean.RunLog;

import java.util.List;

public class GetRunLogsSuccessEvent {
    private List<RunLog> runLogList;
    public GetRunLogsSuccessEvent(List<RunLog> runLogList) {
        this.runLogList = runLogList;
    }

    public List<RunLog> getRunLogList() {
        return runLogList;
    }
}
