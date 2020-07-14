package com.evertrend.tiger.common.bean;
import java.io.Serializable;
import java.util.Objects;

public class CleanTask implements Serializable {
    public static final int TASK_STATUS_NO = 0;//无状态或未知状态或未开始
    public static final int TASK_STATUS_ING = 1;//正在进行中
    public static final int TASK_STATUS_AT_SPOT = 2;//正在任务点上；如充电，在充电点上；如加水，在加水点上
    public static final int TASK_STAUS_NO_SPOT = 3;//未找到任务点
    public static final int TASK_STATUS_ON_THE_WAY = 4;//正在去执行任务的路上
    public static final int TASK_STATUS_PAUSE = 5;//任务暂停
    public static final int TASK_STATUS_STOP = 6;//任务停止
    public static final int TASK_STATUS_ERROR = 7;//任务出错
    public static final int TASK_STATUS_ACCOMPLISH = 8;//任务成功完成

    private int id;
    private String name;
    private String desc;
    private String tracePaths;
    private String virtualTrackGroups;
    private String specialWorks;
    private int taskPriority;//此次任务优先级
    private int tracePathsPriority;
    private int virtualTrackGroupsPriority;
    private int specialWorksPriority;
    private String startTime;
    private String est_end_time;//预估结束时间
    private String est_consume_time;//预估耗时
    private int taskOption;//任务仅执行一次 1、每天一次 2
    private int run_once;//是否曾经执行过
    private int device;
    private int mapPage;
    private int taskType;
    private int runStatus;
    private int points_num;
    private double distance;
    private int estimated_time;
    private double actual_distance;
    private int actual_time;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CleanTask cleanTask = (CleanTask) o;
        return id == cleanTask.id &&
                taskPriority == cleanTask.taskPriority &&
                tracePathsPriority == cleanTask.tracePathsPriority &&
                virtualTrackGroupsPriority == cleanTask.virtualTrackGroupsPriority &&
                specialWorksPriority == cleanTask.specialWorksPriority &&
                taskOption == cleanTask.taskOption &&
                run_once == cleanTask.run_once &&
                device == cleanTask.device &&
                mapPage == cleanTask.mapPage &&
                taskType == cleanTask.taskType &&
                runStatus == cleanTask.runStatus &&
                points_num == cleanTask.points_num &&
                Double.compare(cleanTask.distance, distance) == 0 &&
                estimated_time == cleanTask.estimated_time &&
                Double.compare(cleanTask.actual_distance, actual_distance) == 0 &&
                actual_time == cleanTask.actual_time &&
                Objects.equals(name, cleanTask.name) &&
                Objects.equals(desc, cleanTask.desc) &&
                Objects.equals(tracePaths, cleanTask.tracePaths) &&
                Objects.equals(virtualTrackGroups, cleanTask.virtualTrackGroups) &&
                Objects.equals(specialWorks, cleanTask.specialWorks) &&
                Objects.equals(startTime, cleanTask.startTime) &&
                Objects.equals(est_end_time, cleanTask.est_end_time) &&
                Objects.equals(est_consume_time, cleanTask.est_consume_time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, desc, tracePaths, virtualTrackGroups, specialWorks, taskPriority, tracePathsPriority, virtualTrackGroupsPriority, specialWorksPriority, startTime, est_end_time, est_consume_time, taskOption, run_once, device, mapPage, taskType, runStatus, points_num, distance, estimated_time, actual_distance, actual_time);
    }

    public void setCleanTask(CleanTask cleanTask) {
        this.id = cleanTask.getId();
        this.name = cleanTask.getName();
        this.desc = cleanTask.getDesc();
        this.tracePaths = cleanTask.getTracePaths();
        this.virtualTrackGroups = cleanTask.getVirtualTrackGroups();
        this.specialWorks = cleanTask.getSpecialWorks();
        this.tracePathsPriority = cleanTask.getTracePathsPriority();
        this.virtualTrackGroupsPriority = cleanTask.getVirtualTrackGroupsPriority();
        this.specialWorksPriority = cleanTask.getSpecialWorksPriority();
        this.taskPriority = cleanTask.getTaskPriority();
        this.startTime = cleanTask.getStartTime();
        this.est_end_time = cleanTask.getEst_end_time();
        this.est_consume_time = cleanTask.getEst_consume_time();
        this.taskOption = cleanTask.getTaskOption();
        this.run_once = cleanTask.getRun_once();
        this.device = cleanTask.getDevice();
        this.mapPage = cleanTask.getMapPage();
        this.taskType = cleanTask.getTaskType();
        this.runStatus = cleanTask.getTaskType();
        this.points_num = cleanTask.getPoints_num();
        this.distance = cleanTask.getDistance();
        this.estimated_time = cleanTask.getEstimated_time();
        this.actual_distance = cleanTask.getActual_distance();
        this.actual_time = cleanTask.getActual_time();
    }

    @Override
    public String toString() {
        return "CleanTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", tracePaths='" + tracePaths + '\'' +
                ", virtualTrackGroups='" + virtualTrackGroups + '\'' +
                ", specialWorks='" + specialWorks + '\'' +
                ", taskPriority=" + taskPriority +
                ", tracePathsPriority=" + tracePathsPriority +
                ", virtualTrackGroupsPriority=" + virtualTrackGroupsPriority +
                ", specialWorksPriority=" + specialWorksPriority +
                ", startTime='" + startTime + '\'' +
                ", est_end_time='" + est_end_time + '\'' +
                ", est_consume_time='" + est_consume_time + '\'' +
                ", taskOption=" + taskOption +
                ", run_once=" + run_once +
                ", device=" + device +
                ", mapPage=" + mapPage +
                ", taskType=" + taskType +
                ", runStatus=" + runStatus +
                ", points_num=" + points_num +
                ", distance=" + distance +
                ", estimated_time=" + estimated_time +
                ", actual_distance=" + actual_distance +
                ", actual_time=" + actual_time +
                '}';
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

    public double getActual_distance() {
        return actual_distance;
    }

    public void setActual_distance(double actual_distance) {
        this.actual_distance = actual_distance;
    }

    public int getActual_time() {
        return actual_time;
    }

    public void setActual_time(int actual_time) {
        this.actual_time = actual_time;
    }

    public int getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(int runStatus) {
        this.runStatus = runStatus;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public int getMapPage() {
        return mapPage;
    }

    public void setMapPage(int mapPage) {
        this.mapPage = mapPage;
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

    public String getTracePaths() {
        return tracePaths;
    }

    public void setTracePaths(String tracePaths) {
        this.tracePaths = tracePaths;
    }

    public String getVirtualTrackGroups() {
        return virtualTrackGroups;
    }

    public void setVirtualTrackGroups(String virtualTrackGroups) {
        this.virtualTrackGroups = virtualTrackGroups;
    }

    public int getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(int taskPriority) {
        this.taskPriority = taskPriority;
    }

    public int getTracePathsPriority() {
        return tracePathsPriority;
    }

    public void setTracePathsPriority(int tracePathsPriority) {
        this.tracePathsPriority = tracePathsPriority;
    }

    public int getVirtualTrackGroupsPriority() {
        return virtualTrackGroupsPriority;
    }

    public void setVirtualTrackGroupsPriority(int virtualTrackGroupsPriority) {
        this.virtualTrackGroupsPriority = virtualTrackGroupsPriority;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEst_end_time() {
        return est_end_time;
    }

    public void setEst_end_time(String est_end_time) {
        this.est_end_time = est_end_time;
    }

    public String getEst_consume_time() {
        return est_consume_time;
    }

    public void setEst_consume_time(String est_consume_time) {
        this.est_consume_time = est_consume_time;
    }

    public int getTaskOption() {
        return taskOption;
    }

    public void setTaskOption(int taskOption) {
        this.taskOption = taskOption;
    }

    public int getRun_once() {
        return run_once;
    }

    public void setRun_once(int run_once) {
        this.run_once = run_once;
    }

    public String getSpecialWorks() {
        return specialWorks;
    }

    public void setSpecialWorks(String specialWorks) {
        this.specialWorks = specialWorks;
    }

    public int getSpecialWorksPriority() {
        return specialWorksPriority;
    }

    public void setSpecialWorksPriority(int specialWorksPriority) {
        this.specialWorksPriority = specialWorksPriority;
    }
}
