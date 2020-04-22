package com.evertrend.tiger.user.bean.event;

public class UserOperationItemEvent {
    private int position;
    public UserOperationItemEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
