package com.evertrend.tiger.common.utils.general;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.PowerManager;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.evertrend.tiger.common.bean.RobotAction;
import com.slamtec.slamware.robot.LaserPoint;
import com.slamtec.slamware.robot.Location;
import com.slamtec.slamware.robot.Pose;
import com.slamtec.slamware.robot.Rotation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final String TAG = "Utils";

    public static boolean isIP(String addr)
    {
        if(addr.length() < 7 || addr.length() > 15 || "".equals(addr))
        {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(addr);
        boolean ipAddress = mat.find();
        return ipAddress;
    }

    public static Pose toPose(JSONObject jsonObject) throws JSONException {
        Location location = new Location();
        location.setX((float)jsonObject.getDouble(RobotAction.POSE_X));
        location.setY((float)jsonObject.getDouble(RobotAction.POSE_Y));
        Rotation rotation = new Rotation((float)jsonObject.getDouble(RobotAction.POSE_YAW));
        return new Pose(location, rotation);
    }

    public static Vector<LaserPoint> toLaserPoint(JSONObject jsonObject) throws JSONException {
//        LogUtil.d(TAG, "updateLaserScan: "+jsonObject.toString());
        String str = jsonObject.getString(RobotAction.DATA);
        String[] data = str.substring(1, str.length()-1).split(",");
//        LogUtil.d(TAG, "length: "+data.length);
        double angle_increment = jsonObject.getDouble(RobotAction.ANGLE_INCREMENT);
        double angle_max = jsonObject.getDouble(RobotAction.ANGLE_MAX);
        double angle_min = jsonObject.getDouble(RobotAction.ANGLE_MIN);
        double angle = 0;
        Vector<LaserPoint> laserPoints = new Vector();
        for (int i = 0; i < data.length; i++) {
            LaserPoint point = new LaserPoint();
            if (i == 0) {
                angle = angle_max;
            } else if (i == data.length - 1) {
                angle = angle_min;
            } else {
//                angle = angle + Math.abs(angle_increment);
                angle = angle + angle_increment;
            }
            point.setAngle((float) angle);
            point.setDistance(Float.parseFloat(data[i]));
            point.setValid(true);
            laserPoints.add(point);
        }
        return laserPoints;
    }

    public static String decompress(String source) {
        String compress = source.replaceAll("(.{2})", "$1,");
        String[] compresss = compress.split(",");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < compresss.length - 1;) {
            if (compresss[i].equalsIgnoreCase("FF")) {
                sb.append(repeatString("FF", Integer.parseInt(compresss[i+1], 16)));
                i+=2;
                continue;
            } else if (compresss[i].equalsIgnoreCase("00")) {
                sb.append(repeatString("00", Integer.parseInt(compresss[i+1], 16)));
                i+=2;
                continue;
            } else {
                sb.append(compresss[i]);
                i++;
                continue;
            }
        }
        return sb.toString();
    }

    public static String repeatString(String str, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    public static LatLng GPSCoordinateToBaiduCoordinate(LatLng sourceLatLng) {
        //初始化坐标转换工具类，指定源坐标类型和坐标数据
        // sourceLatLng待转换坐标
        CoordinateConverter converter  = new CoordinateConverter()
                .from(CoordinateConverter.CoordType.GPS)
                .coord(sourceLatLng);

        //desLatLng 转换后的坐标
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }

    /**
     * 把16进制字符串转换成字节数组
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    public static boolean saveBitmap(Bitmap bitmap, String path, String filename) throws IOException
    {
        boolean saveStatus = false;
        File file = new File(path + "/" + filename);
        if(file.exists()){
            file.delete();
        }
        FileOutputStream out;
        try{
            out = new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.PNG, 100, out))
            {
                out.flush();
                out.close();
                saveStatus = true;
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } finally {
            return saveStatus;
        }
    }

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
        if((valCode & 0x01) != 0) stringList.add("第0路超声通信中断");
        if((valCode & 0x02) != 0) stringList.add("第1路超声通信中断");
        if((valCode & 0x04) != 0) stringList.add("第2路超声通信中断");
        if((valCode & 0x08) != 0) stringList.add("第3路超声通信中断");
        if((valCode & 0x10) != 0) stringList.add("第4路超声通信中断");
        if((valCode & 0x20) != 0) stringList.add("第5路超声通信中断");
        if((valCode & 0x40) != 0) stringList.add("第6路超声通信中断");
        if((valCode & 0x80) != 0) stringList.add("第7路超声通信中断");

        valCode = Integer.parseInt(alarmInfo.substring(8, 10), 16);
        if((valCode & 0x01) != 0) stringList.add("第8路超声通信中断");
        if((valCode & 0x02) != 0) stringList.add("第9路超声通信中断");
        if((valCode & 0x04) != 0) stringList.add("第10路超声通信中断");
        if((valCode & 0x08) != 0) stringList.add("第11路超声通信中断");
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

    /**
     * 判断输入是否为数字（包括负数和小数）
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]+(\\\\.[0-9]+)?");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
