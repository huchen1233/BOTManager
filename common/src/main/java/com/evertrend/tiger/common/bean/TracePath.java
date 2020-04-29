package com.evertrend.tiger.common.bean;

import java.io.Serializable;

public class TracePath extends BaseTrace implements Serializable {
    private int flag;

    public TracePath() {
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

}
