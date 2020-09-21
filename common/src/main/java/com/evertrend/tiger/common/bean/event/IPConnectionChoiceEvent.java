package com.evertrend.tiger.common.bean.event;

import com.evertrend.tiger.common.bean.IPConnection;

public class IPConnectionChoiceEvent {
    private IPConnection ipConnection;
    public IPConnectionChoiceEvent(IPConnection ipConnection) {
        this.ipConnection = ipConnection;
    }

    public IPConnection getIpConnection() {
        return ipConnection;
    }
}
