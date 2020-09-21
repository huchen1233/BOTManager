package com.evertrend.tiger.common.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class IPConnection extends LitePalSupport implements Serializable {
    private String ip;
    private long timeStamp;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
