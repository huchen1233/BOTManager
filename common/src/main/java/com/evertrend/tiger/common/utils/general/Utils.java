package com.evertrend.tiger.common.utils.general;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.PowerManager;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isIgnoringBatteryOptimizations(Context context) {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(context.getPackageName());
        }
        return isIgnoring;
    }

    public static List<String> getAlarmInfoDescription(String alarmInfo) {
        List<String> stringList = new ArrayList<>();
        if (alarmInfo == null) {
            return stringList;
        }
        if (alarmInfo.length() != 12) {
            return stringList;
        }

        int valCode = Integer.parseInt(alarmInfo.substring(0, 2), 16);
        if((valCode & 0x01) != 0) stringList.add("主控板和左行走电机之间的信号连接线脱落或断开，或者左行走电机的电源线脱落或断路，请检查它们之间的连接线及电源线");
        if((valCode & 0x02) != 0) stringList.add("主控板和右行走电机之间的信号连接线脱落或断开，或者右行走电机的电源线脱落或断路，请检查它们之间的连接线及电源线");
        if((valCode & 0x04) != 0) stringList.add("左行走电机的驱动电压过高，请检查电池的电压");
        if((valCode & 0x08) != 0) stringList.add("右行走电机的驱动电压过高，请检查电池的电压");
        if((valCode & 0x10) != 0) stringList.add("左行走电机的电流过大，请检查左行走电机是不是堵转，导致过流，比如是不是掉到坑里、越障时轮子被卡住或者有未能检测到的障碍物让它不能行走");
        if((valCode & 0x20) != 0) stringList.add("右行走电机的电流过大，请检查右行走电机是不是堵转，导致过流，比如是不是掉到坑里、越障时轮子被卡住或者有未能检测到的障碍物让它不能行走");
        if((valCode & 0x40) != 0) stringList.add("未接收到左行走电机编码器发送的数据，请检查主控板与左行走电机编码器之间的连线是不是脱落、接触不良或断路");
        if((valCode & 0x80) != 0) stringList.add("未接收到右行走电机编码器发送的数据，请检查主控板与右行走电机编码器之间的连线是不是脱落、接触不良或断路");

        valCode = Integer.parseInt(alarmInfo.substring(2, 4), 16);
        if((valCode & 0x01) != 0) stringList.add("左行走电机的温度过高，请检查小车上有没有放置了重物或者工作环境温度太高");
        if((valCode & 0x02) != 0) stringList.add("右行走电机的温度过高，请检查小车上有没有放置了重物或者工作环境温度太高");
        if((valCode & 0x04) != 0) stringList.add("左行走电机的驱动电压过低，请检查电池电压是不是过低");
        if((valCode & 0x08) != 0) stringList.add("右行走电机的驱动电压过低，请检查电池电压是不是过低");
        if((valCode & 0x10) != 0) stringList.add("左行走电机负载过限，请检查小车上有没有放置了重物，是不是掉到坑里、越障时轮子被卡住或者有未能检测到的障碍物让它不能行走");
        if((valCode & 0x20) != 0) stringList.add("右行走电机负载过限，请检查小车上有没有放置了重物，是不是掉到坑里、越障时轮子被卡住或者有未能检测到的障碍物让它不能行走");
        if((valCode & 0x40) != 0) stringList.add("底刷(主刷)不转动或者转动异常，请检查是不是有异物卡住了主刷");
        if((valCode & 0x80) != 0) stringList.add("左边刷不转动或者转动异常，请检查是不是有异物卡住了左边刷");

        valCode = Integer.parseInt(alarmInfo.substring(4, 6), 16);
        if((valCode & 0x01) != 0) stringList.add("右边刷不转动或者转动异常，请检查是不是有异物卡住了右边刷");
        if((valCode & 0x02) != 0) stringList.add("请检查主控板与导航模块之间的连接线是不是脱落、接触不良或断路");
        if((valCode & 0x04) != 0) stringList.add("请检查主控板与传感器板之间的连接线是不是脱落、接触不良或断路");

        valCode = Integer.parseInt(alarmInfo.substring(6, 8), 16);
        if((valCode & 0x01) != 0) stringList.add("请检查该路超声传感器与传感器板之间的连接线是不是脱落、接触不良或断路，或者该路超声传感器已经损坏，需要更换");
        if((valCode & 0x02) != 0) stringList.add("请检查该路超声传感器与传感器板之间的连接线是不是脱落、接触不良或断路，或者该路超声传感器已经损坏，需要更换");
        if((valCode & 0x04) != 0) stringList.add("请检查该路超声传感器与传感器板之间的连接线是不是脱落、接触不良或断路，或者该路超声传感器已经损坏，需要更换");
        if((valCode & 0x08) != 0) stringList.add("请检查该路超声传感器与传感器板之间的连接线是不是脱落、接触不良或断路，或者该路超声传感器已经损坏，需要更换");
        if((valCode & 0x10) != 0) stringList.add("请检查该路超声传感器与传感器板之间的连接线是不是脱落、接触不良或断路，或者该路超声传感器已经损坏，需要更换");
        if((valCode & 0x20) != 0) stringList.add("请检查该路超声传感器与传感器板之间的连接线是不是脱落、接触不良或断路，或者该路超声传感器已经损坏，需要更换");
        if((valCode & 0x40) != 0) stringList.add("请检查该路超声传感器与传感器板之间的连接线是不是脱落、接触不良或断路，或者该路超声传感器已经损坏，需要更换");
        if((valCode & 0x80) != 0) stringList.add("请检查该路超声传感器与传感器板之间的连接线是不是脱落、接触不良或断路，或者该路超声传感器已经损坏，需要更换");

        valCode = Integer.parseInt(alarmInfo.substring(8, 10), 16);
        if((valCode & 0x01) != 0) stringList.add("请检查该路超声传感器与传感器板之间的连接线是不是脱落、接触不良或断路，或者该路超声传感器已经损坏，需要更换");
        if((valCode & 0x02) != 0) stringList.add("请检查该路超声传感器与传感器板之间的连接线是不是脱落、接触不良或断路，或者该路超声传感器已经损坏，需要更换");
        if((valCode & 0x04) != 0) stringList.add("请检查该路超声传感器与传感器板之间的连接线是不是脱落、接触不良或断路，或者该路超声传感器已经损坏，需要更换");
        if((valCode & 0x08) != 0) stringList.add("请检查该路超声传感器与传感器板之间的连接线是不是脱落、接触不良或断路，或者该路超声传感器已经损坏，需要更换");
        if((valCode & 0x10) != 0) stringList.add("请检查该路碰撞条与传感器板之间的连接线是不是脱落、接触不良或断路，或者该路碰撞条传感器已经损坏，需要更换");
        if((valCode & 0x20) != 0) stringList.add("请检查该路碰撞条与传感器板之间的连接线是不是脱落、接触不良或断路，或者该路碰撞条传感器已经损坏，需要更换");
        if((valCode & 0x40) != 0) stringList.add("请检查该路碰撞条与传感器板之间的连接线是不是脱落、接触不良或断路，或者该路碰撞条传感器已经损坏，需要更换");
        if((valCode & 0x80) != 0) stringList.add("请检查该路碰撞条与传感器板之间的连接线是不是脱落、接触不良或断路，或者该路碰撞条传感器已经损坏，需要更换");

        valCode = Integer.parseInt(alarmInfo.substring(10, 12), 16);
        if((valCode & 0x01) != 0) stringList.add("给电池充电时，充电电流过大，请检查充电线路上有没有短路的情况，或者电池已经损坏");
        if((valCode & 0x02) != 0) stringList.add("检测到异常振动，请到现场检查是不是有人故意损坏");
        if((valCode & 0x04) != 0) stringList.add("主控板与GPS模块通信中断，请检查GPS模块与主控板之间的连接线是不是脱落、接触不良或断路，或者该GPS模块已经损坏，需要更换");
        if((valCode & 0x08) != 0) stringList.add("获取GPS信号失败或无GPS信号，请检查小车所在位置是不是空旷的");
        if((valCode & 0x10) != 0) stringList.add("温湿度传感器与主控板之间通信中断，请检查温湿度传感器与主控板之间的连接线是不是脱落、接触不良或断路，或者该GPS模块已经损坏，需要更换");

        return stringList;
    }

    public static List<String> parseAlarmInfo(String alarmInfo) {
        List<String> stringList = new ArrayList<>();
        if (alarmInfo == null) {
            return stringList;
        }
        if (alarmInfo.length() != 12) {
            return stringList;
        }

        int valCode = Integer.parseInt(alarmInfo.substring(0, 2), 16);
        if((valCode & 0x01) != 0) stringList.add("主控板和左行走电机之间通信故障");
        if((valCode & 0x02) != 0) stringList.add("主控板和右行走电机之间通信故障");
        if((valCode & 0x04) != 0) stringList.add("左行走电机过压");
        if((valCode & 0x08) != 0) stringList.add("右行走电机过压");
        if((valCode & 0x10) != 0) stringList.add("左行走电机过流");
        if((valCode & 0x20) != 0) stringList.add("右行走电机过流");
        if((valCode & 0x40) != 0) stringList.add("左行走电机编码器故障");
        if((valCode & 0x80) != 0) stringList.add("右行走电机编码器故障");

        valCode = Integer.parseInt(alarmInfo.substring(2, 4), 16);
        if((valCode & 0x01) != 0) stringList.add("左行走电机过热");
        if((valCode & 0x02) != 0) stringList.add("右行走电机过热");
        if((valCode & 0x04) != 0) stringList.add("左行走电机欠压");
        if((valCode & 0x08) != 0) stringList.add("右行走电机欠压");
        if((valCode & 0x10) != 0) stringList.add("左行走电机过载");
        if((valCode & 0x20) != 0) stringList.add("右行走电机过载");
        if((valCode & 0x40) != 0) stringList.add("底刷(主刷)故障");
        if((valCode & 0x80) != 0) stringList.add("左边刷故障");

        valCode = Integer.parseInt(alarmInfo.substring(4, 6), 16);
        if((valCode & 0x01) != 0) stringList.add("右边刷故障");
        if((valCode & 0x02) != 0) stringList.add("主控板与导航模块通信中断");
        if((valCode & 0x04) != 0) stringList.add("主控板与传感器板通信中断");

        valCode = Integer.parseInt(alarmInfo.substring(6, 8), 16);
        if((valCode & 0x01) != 0) stringList.add("普通超声第0路通信中断");
        if((valCode & 0x02) != 0) stringList.add("普通超声第1路通信中断");
        if((valCode & 0x04) != 0) stringList.add("普通超声第2路通信中断");
        if((valCode & 0x08) != 0) stringList.add("普通超声第3路通信中断");
        if((valCode & 0x10) != 0) stringList.add("普通超声第4路通信中断");
        if((valCode & 0x20) != 0) stringList.add("普通超声第5路通信中断");
        if((valCode & 0x40) != 0) stringList.add("普通超声第6路通信中断");
        if((valCode & 0x80) != 0) stringList.add("普通超声第7路通信中断");

        valCode = Integer.parseInt(alarmInfo.substring(8, 10), 16);
        if((valCode & 0x01) != 0) stringList.add("防水超声第0路通信中断");
        if((valCode & 0x02) != 0) stringList.add("防水超声第1路通信中断");
        if((valCode & 0x04) != 0) stringList.add("防水超声第2路通信中断");
        if((valCode & 0x08) != 0) stringList.add("防水超声第3路通信中断");
        if((valCode & 0x10) != 0) stringList.add("碰撞条第0路通信中断");
        if((valCode & 0x20) != 0) stringList.add("碰撞条第1路通信中断");
        if((valCode & 0x40) != 0) stringList.add("碰撞条第2路通信中断");
        if((valCode & 0x80) != 0) stringList.add("碰撞条第3路通信中断");

        valCode = Integer.parseInt(alarmInfo.substring(10, 12), 16);
        if((valCode & 0x01) != 0) stringList.add("充电电流过大");
        if((valCode & 0x02) != 0) stringList.add("检测到异常振动");
        if((valCode & 0x04) != 0) stringList.add("与GPS模块通信中断");
        if((valCode & 0x08) != 0) stringList.add("获取GPS信号失败（无GPS信号）");
        if((valCode & 0x10) != 0) stringList.add("温湿度传感器通信中断");

        return stringList;
    }

    public static String formatDateToTimestamp(long intValue) {
        Calendar beginC = Calendar.getInstance();
        beginC.setTimeInMillis(intValue);
        int year = beginC.get(Calendar.YEAR);
        int month = beginC.get(Calendar.MONTH) + 1;
        int day = beginC.get(Calendar.DAY_OF_MONTH);
        int hour = beginC.get(Calendar.HOUR_OF_DAY);
        int minute = beginC.get(Calendar.MINUTE);
        int second = beginC.get(Calendar.SECOND);
        StringBuffer date = new StringBuffer();
        date.append(year+"-");
        date.append(month+"-");
        date.append(day+" ");
        if (hour < 10) {
            date.append("0" + hour);
        } else {
            date.append(hour);
        }
        if (minute < 10) {
            date.append(":0" + minute);
        } else {
            date.append(":"+minute);
        }
        if (second < 10) {
            date.append(":0" + second);
        } else {
            date.append(":"+second);
        }
        return date.toString();
    }

    public static synchronized String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前时间戳
     * @return 当前时间戳
     */
    public static String getTimeStame() {
        //获取当前的毫秒值
        long time = System.currentTimeMillis();
        //将毫秒值转换为String类型数据
        String time_stamp = String.valueOf(time);
        //返回出去
        return time_stamp;
    }

    public static boolean isDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param phone 字符串类型的手机号
     * 传入手机号,判断后返回
     * true为手机号,false相反
     * */
    public static boolean isPhone(String phone) {
        String regex = "^((13[0-9])|(14[5-9])|(15([0-3]|[5-9]))|(16[5|6])|(17[1-8])|(18[0-9])|(19[1,8|9]))\\d{8}$";
        if (phone.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            return m.matches();
        }
    }

    /**
     * 邮箱格式判断
     * @param strEmail 需要验证的邮箱
     * @return true为格式正确,false相反
     */
    public static boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        if (TextUtils.isEmpty(strPattern)) {
            return false;
        } else {
            return strEmail.matches(strPattern);
        }
    }
}
