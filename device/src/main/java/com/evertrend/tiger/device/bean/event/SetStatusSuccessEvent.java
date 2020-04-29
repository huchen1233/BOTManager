package com.evertrend.tiger.device.bean.event;

public class SetStatusSuccessEvent {
    private String mark;
    private int status;
    public SetStatusSuccessEvent(String mark, int status) {
        this.mark = mark;
        this.status = status;
    }

    public String getMark() {
        return mark;
    }

    public int getStatus() {
        return status;
    }
}
