package com.evertrend.tiger.common.bean;

import java.io.Serializable;

public class MapPages implements Serializable {
    private int id;
    private String name;
    private String description;
    private int deviceId;

    public void setMapPages(MapPages mapPages) {
        this.name = mapPages.getName();
        this.description = mapPages.getDescription();
        this.id = mapPages.getId();
        this.deviceId = mapPages.getDeviceId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }
}
