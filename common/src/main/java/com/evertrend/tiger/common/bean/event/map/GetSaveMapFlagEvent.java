package com.evertrend.tiger.common.bean.event.map;

public class GetSaveMapFlagEvent {
    private int saveFlag;

    public GetSaveMapFlagEvent(int saveFlag) {
        this.saveFlag = saveFlag;
    }

    public int getSaveFlag() {
        return saveFlag;
    }

}
