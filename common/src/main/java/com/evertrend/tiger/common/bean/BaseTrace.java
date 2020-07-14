package com.evertrend.tiger.common.bean;

import java.io.Serializable;

public class BaseTrace implements Serializable {
    private int id;
    private String name;
    private String desc;
    private int deviceId;
    private int mapPage;
    private int points_num;
    private double distance;
    private int estimated_time;

    public void setBaseTrace(BaseTrace baseTrace) {
        this.id = baseTrace.getId();
        this.name = baseTrace.getName();
        this.desc = baseTrace.getDesc();
        this.deviceId = baseTrace.getDeviceId();
        this.mapPage = baseTrace.getMapPage();
    }

    public int getPoints_num() {
        return points_num;
    }

    public void setPoints_num(int points_num) {
        this.points_num = points_num;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getEstimated_time() {
        return estimated_time;
    }

    public void setEstimated_time(int estimated_time) {
        this.estimated_time = estimated_time;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getMapPage() {
        return mapPage;
    }

    public void setMapPage(int mapPage) {
        this.mapPage = mapPage;
    }

    @Override
    public String toString() {
        return "BaseTrace{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", deviceId=" + deviceId +
                ", mapPage=" + mapPage +
                ", points_num=" + points_num +
                ", distance=" + distance +
                ", estimated_time=" + estimated_time +
                '}';
    }
}
