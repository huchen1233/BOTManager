package com.evertrend.tiger.common.bean;

import android.content.Context;

import com.evertrend.tiger.common.R;

import java.io.Serializable;

public class RunLog implements Serializable {
    public static final int LEVEL_DEBUG = 0;
    public static final int LEVEL_INFO = 1;
    public static final int LEVEL_WARNING = 2;
    public static final int LEVEL_ERROR = 3;
    public static final int LEVEL_CRITICAL = 4;

    private int id;
    private int device;
    private int level;
    private int type_code;//类别代号, 比如是哪一类错误，串口通信，重定位等
    private String name;
    private String description;
    private long log_time;

    public static String getLevelStr(Context context, int level) {
        String[] levels = context.getResources().getStringArray(R.array.yl_common_run_log_level);
        return levels[level];
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getType_code() {
        return type_code;
    }

    public void setType_code(int type_code) {
        this.type_code = type_code;
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

    public long getLog_time() {
        return log_time;
    }

    public void setLog_time(long log_time) {
        this.log_time = log_time;
    }
}
