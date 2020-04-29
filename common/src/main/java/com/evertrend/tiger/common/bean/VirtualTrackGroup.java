package com.evertrend.tiger.common.bean;

import java.io.Serializable;

public class VirtualTrackGroup extends BaseTrace implements Serializable {
    private int number;

    public VirtualTrackGroup() {
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

}
