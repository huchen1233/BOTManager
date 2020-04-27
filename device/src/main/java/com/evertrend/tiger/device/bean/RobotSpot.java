package com.evertrend.tiger.device.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class RobotSpot extends LitePalSupport implements Serializable {
    private int id;
    private String name;
    private String data;//机器人位姿或地图的直线、区域，由标志决定
    private int flag;//标志，该条记录的数据标志, 1: 机器人自动上报，2：通过手机上报
    private int device_id;//所属机器人, 为设备表项的ID
    private String user_id;//
    private int map_page;
    private int trace_path_id;//该点所属路径，对于特别的点，该值为null(不设置任何值)
    private int spot_flag;//标志该点类型，0：路径点，1: 充电点，2: 加水点，3: 倾倒垃圾点，4：车库点
    private float x;
    private float y;
    private float z;
    private float yaw;//角度
    private int deleteNum;//对应需要删除循迹点段的值（目前为5的倍数）

    public RobotSpot() {
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public int getSpot_flag() {
        return spot_flag;
    }

    public void setSpot_flag(int spot_flag) {
        this.spot_flag = spot_flag;
    }

    public int getMap_page() {
        return map_page;
    }

    public void setMap_page(int map_page) {
        this.map_page = map_page;
    }

    public int getTrace_path_id() {
        return trace_path_id;
    }

    public void setTrace_path_id(int trace_path_id) {
        this.trace_path_id = trace_path_id;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getDeleteNum() {
        return deleteNum;
    }

    public void setDeleteNum(int deleteNum) {
        this.deleteNum = deleteNum;
    }

    @Override
    public String toString() {
        return "RobotSpot{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", data='" + data + '\'' +
                ", flag=" + flag +
                ", device_id=" + device_id +
                ", user_id='" + user_id + '\'' +
                ", map_page=" + map_page +
                ", trace_path_id=" + trace_path_id +
                ", spot_flag=" + spot_flag +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", yaw=" + yaw +
                ", deleteNum=" + deleteNum +
                '}';
    }
}
