package com.evertrend.tiger.common.utils;

import com.evertrend.tiger.common.bean.RobotAction;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.slamtec.slamware.robot.Location;

import org.apache.mina.core.session.IoSession;
import org.json.JSONException;
import org.json.JSONObject;

public class SessionManager {
    public static final String TAG = SessionManager.class.getCanonicalName();
    private static  SessionManager mInstance = null;
    private static final String DEVICE_ID = "9005";
    private static final String KEY = "1993";

    private IoSession ioSession;//最终与服务器 通信的对象

    public static SessionManager getmInstance(){
        if (mInstance == null) {
            synchronized (SessionManager.class) {
                if (mInstance == null) {
                    mInstance = new SessionManager();
                }
            }
        }
        return mInstance;
    }

    private SessionManager(){
    }

    public void setIoSession(IoSession ioSession) {
        this.ioSession = ioSession;
    }

    public IoSession getIoSession() {
        return ioSession;
    }

    /**
     * 将对象写到服务器
     */
    public void writeToServer(Object msg) {
        LogUtil.d(TAG, "send msg: "+msg);
        if (ioSession != null) {
            ioSession.write(msg);
        }
    }

    /**
     * 关闭连接
     */
    public void closeSession() {
        if (ioSession != null) {
            ioSession.closeOnFlush();
        }
    }

    public void removeSession() {
        ioSession = null;
    }

    private long getTime() {
        long time = System.currentTimeMillis();
        return time/1000;
    }

    public void stop() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(RobotAction.CMD_CODE, RobotAction.CMD.STOP);
        object.put(RobotAction.DEVICE_ID, DEVICE_ID);
        object.put(RobotAction.KEY, KEY);
        object.put(RobotAction.TIME_STAMP, getTime());
        writeToServer(object);
    }

    public void moveBy(int robotAction, float speed) throws JSONException {
        JSONObject object = new JSONObject();
        object.put(RobotAction.CMD_CODE, robotAction);
        object.put(RobotAction.DATA, speed);
        object.put(RobotAction.DEVICE_ID, DEVICE_ID);
        object.put(RobotAction.KEY, KEY);
        object.put(RobotAction.TIME_STAMP, getTime());
        writeToServer(object);
    }

    public void moveTo(Location location, float yaw) throws JSONException {
        JSONObject object = new JSONObject();
        JSONObject data = new JSONObject();
        data.put(RobotAction.POSE_X, location.getX());
        data.put(RobotAction.POSE_Y, location.getY());
        data.put(RobotAction.POSE_YAW, yaw);
        object.put(RobotAction.CMD_CODE, RobotAction.CMD.SET_ROBOT_POSE);
        object.put(RobotAction.DATA, data);
        object.put(RobotAction.DEVICE_ID, DEVICE_ID);
        object.put(RobotAction.KEY, KEY);
        object.put(RobotAction.TIME_STAMP, getTime());
        writeToServer(object);
    }

    public void getMap(int getType) throws JSONException {
        JSONObject object = new JSONObject();
        object.put(RobotAction.CMD_CODE, getType);
        object.put(RobotAction.DEVICE_ID, DEVICE_ID);
        object.put(RobotAction.KEY, KEY);
        object.put(RobotAction.TIME_STAMP, getTime());
        writeToServer(object);
    }

    public void getRobotPose() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(RobotAction.CMD_CODE, RobotAction.CMD.GET_ROBOT_POSE);
        object.put(RobotAction.DEVICE_ID, DEVICE_ID);
        object.put(RobotAction.KEY, KEY);
        object.put(RobotAction.TIME_STAMP, getTime());
        writeToServer(object);
    }

    public void getLaserScan() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(RobotAction.CMD_CODE, RobotAction.CMD.GET_LASER_SCAN);
        object.put(RobotAction.DEVICE_ID, DEVICE_ID);
        object.put(RobotAction.KEY, KEY);
        object.put(RobotAction.TIME_STAMP, getTime());
        writeToServer(object);
    }
}
