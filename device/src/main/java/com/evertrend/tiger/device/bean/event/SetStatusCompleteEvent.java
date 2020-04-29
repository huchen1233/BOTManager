package com.evertrend.tiger.device.bean.event;

public class SetStatusCompleteEvent {
    private String mark;
    private int status;
    public SetStatusCompleteEvent(int status, String mark) {
        this.mark = mark;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getMark() {
        return mark;
    }
}
