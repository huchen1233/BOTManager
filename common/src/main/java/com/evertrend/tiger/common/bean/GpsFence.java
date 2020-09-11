package com.evertrend.tiger.common.bean;

public class GpsFence {
    private int id;
    private int device;
    private int mapPage;
    private double longitude;
    private double latitude;
    private int order;

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

    public int getMapPage() {
        return mapPage;
    }

    public void setMapPage(int mapPage) {
        this.mapPage = mapPage;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
