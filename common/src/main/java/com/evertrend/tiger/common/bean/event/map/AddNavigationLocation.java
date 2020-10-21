package com.evertrend.tiger.common.bean.event.map;

public class AddNavigationLocation {
    private float x;
    private float y;
    private float angle;
    public AddNavigationLocation(float x, float y, float angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getAngle() {
        return angle;
    }
}
