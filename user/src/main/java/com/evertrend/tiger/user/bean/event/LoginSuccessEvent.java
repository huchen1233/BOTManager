package com.evertrend.tiger.user.bean.event;

import com.evertrend.tiger.user.bean.User;

public class LoginSuccessEvent {
    private User user;
    public LoginSuccessEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
