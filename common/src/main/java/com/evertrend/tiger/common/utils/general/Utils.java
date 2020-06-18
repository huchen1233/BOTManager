package com.evertrend.tiger.common.utils.general;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

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
