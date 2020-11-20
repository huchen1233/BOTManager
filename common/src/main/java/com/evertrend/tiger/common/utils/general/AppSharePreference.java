package com.evertrend.tiger.common.utils.general;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.evertrend.tiger.common.bean.RobotAction;
import com.evertrend.tiger.common.bean.mapview.utils.SlamGestureDetector;

import java.util.Set;

public class AppSharePreference {
    public static final String SHARED_PREFERENCE_NAME = "evertrend_botmanager_pre";
    private static AppSharePreference appSP;
    private static SharedPreferences sp;

    public static final String LOG_FLAG = "log_flag";
    public static final String IS_INPUT_LOG_I = "isInputLogI";
    public static final String IS_INPUT_LOG_D = "isInputLogD";
    public static final String IS_INPUT_LOG_E = "isInputLogE";
    public static final String IS_LOGIN = "IS_LOGIN";
    public static final String USER_TOKEN = "USER_TOKEN";
    public static final String ACCOUNT = "account";
    public static final String PASS = "pass";
    public static final String REMEMBER_ME = "remember_me";
    public static final String REMEMBER_PASS = "remember_pass";
    public static final String DEVICE_SPEED = "DEVICE_SPEED";
    public static final String DEVICE_TRACE_SPOT_LAST_POSE = "DEVICE_TRACE_SPOT_LAST_POSE";//最后一点
    public static final String DEVICE_TRACE_PATH_ROLLBACK_NUM = "DEVICE_TRACE_PATH_ROLLBACK_NUM";//循迹路径回滚个数
    public static final String DEVICE_TRACE_PATH_AUTO_RECORD = "DEVICE_TRACE_PATH_AUTO_RECORD";//自动记录清扫路径点
    public static final String DEVICE_TRACE_PATH_AUTO_RECORD_DISTANCE = "DEVICE_TRACE_PATH_AUTO_RECORD_DISTANCE";//自动记录清扫路径点间隔距离
    public static final String DEVICE_ENABLE_GPS_FENCE = "DEVICE_ENABLE_GPS_FENCE";//使能GPS电子围栏
    public static final String DEVICE_LOG_GPS_SLAM = "DEVICE_LOG_GPS_SLAM";//
    public static final String DEVICE_DELETE_GPS_SLAM = "DEVICE_DELETE_GPS_SLAM";//
    public static final String MAP_TOUCH_MODE = "MAP_TOUCH_MODE";//地图单指触摸操作选择
    public static final String RUN_MODE = "RUN_MODE";//
//    public static final String DEVICE_TMP_TRACE_PATH = "DEVICE_TMP_TRACE_PATH";

    //初始化保存的环境
    public static void initSharedPreference(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
        }
    }

    //获取本类的一个实例
    public static synchronized AppSharePreference getAppSharedPreference() {
        if (appSP == null) {
            appSP = new AppSharePreference();
        }
        return appSP;
    }

    //自动记录清扫路径点临时保存集合，防止用户在建图时，不小心返回而没有保存路径点问题
    public void saveTmpPath(String key, Set<String> paths) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet(key, paths);
        editor.apply();
    }

    public Set<String> loadTmpPath(String key) {
        return sp.getStringSet(key, null);
    }

    public void saveMapLoad(String key, int mode) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, mode);
        editor.apply();
    }

    public int loadMapLoad(String key) {
        return sp.getInt(key, 0);
    }

    public void saveRunMode(int mode) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(RUN_MODE, mode);
        editor.apply();
    }

    public int loadRunMode() {
        return sp.getInt(RUN_MODE, RobotAction.RUN_MODE_CREATE_MAP);
    }

    public void saveMapTouchMode(int mode) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(MAP_TOUCH_MODE, mode);
        editor.apply();
    }

    public int loadMapTouchMode() {
        return sp.getInt(MAP_TOUCH_MODE, SlamGestureDetector.MODE_NONE);
    }

    public void saveAutoRecordPathDistance(int distance) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(DEVICE_TRACE_PATH_AUTO_RECORD_DISTANCE, distance);
        editor.apply();
    }

    public int loadAutoRecordPathDistance() {
        return sp.getInt(DEVICE_TRACE_PATH_AUTO_RECORD_DISTANCE, CommonConstants.DEVICE_TRACE_PATH_AUTO_RECORD_DISTANCE);
    }

    public void saveDeleteGPSSlam(boolean deleteGPSSlam) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(DEVICE_DELETE_GPS_SLAM, deleteGPSSlam);
        editor.apply();
    }
    public boolean loadDeleteGPSSlam() {
        return sp.getBoolean(DEVICE_DELETE_GPS_SLAM, false);
    }

    public void saveLogGPSSlam(boolean logGPSSlam) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(DEVICE_LOG_GPS_SLAM, logGPSSlam);
        editor.apply();
    }
    public boolean loadLogGPSSlam() {
        return sp.getBoolean(DEVICE_LOG_GPS_SLAM, false);
    }

    public void saveEnableGPSFence(boolean enableGPSFence) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(DEVICE_ENABLE_GPS_FENCE, enableGPSFence);
        editor.apply();
    }
    public boolean loadEnableGPSFence() {
        return sp.getBoolean(DEVICE_ENABLE_GPS_FENCE, false);
    }

    public void saveAutoRecordPath(boolean autoRecordPath) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(DEVICE_TRACE_PATH_AUTO_RECORD, autoRecordPath);
        editor.apply();
    }
    public boolean loadAutoRecordPath() {
        return sp.getBoolean(DEVICE_TRACE_PATH_AUTO_RECORD, false);
    }

    public void saveTracePathRollbackNum(int num) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(DEVICE_TRACE_PATH_ROLLBACK_NUM, num);
        editor.apply();
    }

    public int loadTracePathRollbackNum() {
        return sp.getInt(DEVICE_TRACE_PATH_ROLLBACK_NUM, 5);
    }

    public void saveTraceSpotAutoModeLastPose(String pose) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(DEVICE_TRACE_SPOT_LAST_POSE, pose);
        editor.apply();
    }

    public String loadTraceSpotAutoModeLastPose() {
        return sp.getString(DEVICE_TRACE_SPOT_LAST_POSE, null);
    }

    public void saveDeviceSpeed(String speed) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(DEVICE_SPEED, speed);
        editor.apply();
    }

    public String loadDeviceSpeed() {
        return sp.getString(DEVICE_SPEED, "0.3");
    }

    public void saveRememberMe(boolean rememberMe) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(REMEMBER_ME, rememberMe);
        editor.apply();
    }
    public boolean loadRememberMe() {
        return sp.getBoolean(REMEMBER_ME, false);
    }

    public void saveRememberPass(boolean rememberPass) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(REMEMBER_PASS, rememberPass);
        editor.apply();
    }
    public boolean loadRememberPass() {
        return sp.getBoolean(REMEMBER_PASS, false);
    }

    public void saveAccount(String account) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(ACCOUNT, account);
        editor.apply();
    }
    public String loadaccount() {
        return sp.getString(ACCOUNT, null);
    }

    public void savePass(String pass) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PASS, Base64.encodeToString(pass.getBytes(), Base64.DEFAULT));
        editor.apply();
    }
    public String loadPass() {
        String pass = sp.getString(PASS, null);
        if (pass == null) {
            return null;
        } else {
            return new String(Base64.decode(pass, Base64.DEFAULT));
        }
    }

    public void saveUserToken(String userToken) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_TOKEN, Base64.encodeToString(userToken.getBytes(), Base64.DEFAULT));
        editor.apply();
    }
    public String loadUserToken() {
        String token = sp.getString(USER_TOKEN, "error");
        if (token.equals("error")) {
            return "error";
        } else {
            return new String(Base64.decode(token, Base64.DEFAULT));
        }
    }

    public void saveIsLogin(boolean isLogin) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(IS_LOGIN, isLogin);
        editor.apply();
    }
    public boolean loadIsLogin() {
        return sp.getBoolean(IS_LOGIN, false);
    }

    public String loadLogFlag() {
        return sp.getString(LOG_FLAG, "chenhu");
    }
    public void saveLogFlag(String logFlag) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(LOG_FLAG, logFlag);
        editor.apply();
    }

    public boolean loadIsInputLogI() {
        return sp.getBoolean(IS_INPUT_LOG_I, false);
    }
    public void saveIsInputLogI(boolean isInputLogI) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(IS_INPUT_LOG_I, isInputLogI);
        editor.apply();
    }
//
//    public boolean loadIsInputLogD() {
//        return sp.getBoolean(IS_INPUT_LOG_D, true);
//    }
//
//    public void saveIsInputLogD(boolean isInputLogD) {
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putBoolean(IS_INPUT_LOG_D, isInputLogD);
//        editor.apply();
//    }
//
//    public boolean loadIsInputLogE() {
//        return sp.getBoolean(IS_INPUT_LOG_E, true);
//    }
//
//    public void saveIsInputLogE(boolean isInputLogE) {
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putBoolean(IS_INPUT_LOG_E, isInputLogE);
//        editor.apply();
//    }
}
