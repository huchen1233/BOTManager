package com.evertrend.tiger.user.bean.event;

import com.evertrend.tiger.user.bean.UpdateApp;

public class CheckUpdateEvent {
    private UpdateApp updateApp;
    public CheckUpdateEvent(UpdateApp updateApp) {
        this.updateApp = updateApp;
    }

    public UpdateApp getUpdateApp() {
        return updateApp;
    }
}
