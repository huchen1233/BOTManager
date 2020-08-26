package com.evertrend.tiger.common.bean.event;

public class ServerMsgEvent {
    private String msg;
    public ServerMsgEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
