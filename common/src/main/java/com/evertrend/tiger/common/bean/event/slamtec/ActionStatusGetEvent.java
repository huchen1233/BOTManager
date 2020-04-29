package com.evertrend.tiger.common.bean.event.slamtec;

import com.slamtec.slamware.action.ActionStatus;

public class ActionStatusGetEvent {

    private ActionStatus actionStatus;

    public ActionStatusGetEvent(ActionStatus actionStatus) {
        this.actionStatus = actionStatus;
    }

    public ActionStatus getActionStatus() {
        return actionStatus;
    }

}
