package com.evertrend.tiger.common.bean;

import java.io.Serializable;
import java.util.Objects;

public class DeviceGrant implements Serializable {
    private int id;
    private int device;
    private String authorization_item;
    private String user;
    private int user_flag;//标志所添加的用户是用户名、手机号还是邮箱地址

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceGrant that = (DeviceGrant) o;
        return id == that.id &&
                device == that.device &&
                user_flag == that.user_flag &&
                authorization_item.equals(that.authorization_item) &&
                user.equals(that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, device, authorization_item, user, user_flag);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDevice() {
        return device;
    }

    public void setDevice(int device) {
        this.device = device;
    }

    public String getAuthorization_item() {
        return authorization_item;
    }

    public void setAuthorization_item(String authorization_item) {
        this.authorization_item = authorization_item;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getUser_flag() {
        return user_flag;
    }

    public void setUser_flag(int user_flag) {
        this.user_flag = user_flag;
    }

    public void setDeviceGrant(DeviceGrant deviceGrant) {
        this.id = deviceGrant.getId();
        this.authorization_item = deviceGrant.getAuthorization_item();
        this.device = deviceGrant.getDevice();
        this.user = deviceGrant.getUser();
        this.user_flag = deviceGrant.getUser_flag();
    }
}
