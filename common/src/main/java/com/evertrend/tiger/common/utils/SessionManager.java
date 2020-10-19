package com.evertrend.tiger.common.utils;

import com.evertrend.tiger.common.bean.RobotAction;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.slamtec.slamware.geometry.Line;
import com.slamtec.slamware.robot.Location;
import com.slamtec.slamware.robot.Pose;

import org.apache.mina.core.session.IoSession;
import org.json.JSONException;
import org.json.JSONObject;

public class SessionManager {
    public static final String TAG = SessionManager.class.getCanonicalName();
    private static  SessionManager mInstance = null;
    private String deviceID = "9005";
    private String key = "1993";

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

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 将对象写到服务器
     */
    public void writeToServer(Object msg) {
//        LogUtil.d(TAG, "send msg: "+msg);
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

    private String lineToStr(Line line) {
        String str = line.getStartX()+","+line.getStartY()+","+line.getEndX()+","+line.getEndY();
        return str;
    }

    public void stop() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(RobotAction.CMD_CODE, RobotAction.CMD.STOP);
        object.put(RobotAction.DEVICE_ID, deviceID);
        object.put(RobotAction.KEY, key);
        object.put(RobotAction.TIME_STAMP, getTime());
        writeToServer(object);
    }

    public void moveBy(int robotAction, float speed) throws JSONException {
        JSONObject object = new JSONObject();
        object.put(RobotAction.CMD_CODE, robotAction);
        object.put(RobotAction.DATA, speed);
        object.put(RobotAction.DEVICE_ID, deviceID);
        object.put(RobotAction.KEY, key);
        object.put(RobotAction.TIME_STAMP, getTime());
        writeToServer(object);
    }

    public void moveTo(Pose moveToPose) throws JSONException {
        JSONObject object = new JSONObject();
        JSONObject data = new JSONObject();
        data.put(RobotAction.POSE_X, moveToPose.getX());
        data.put(RobotAction.POSE_Y, moveToPose.getY());
        data.put(RobotAction.POSE_YAW, moveToPose.getYaw());
        object.put(RobotAction.CMD_CODE, RobotAction.CMD.MOVE_TO_POSE);
        object.put(RobotAction.DATA, data);
        object.put(RobotAction.DEVICE_ID, deviceID);
        object.put(RobotAction.KEY, key);
        object.put(RobotAction.TIME_STAMP, getTime());
        writeToServer(object);
    }

    public void setPose(Pose pose) throws JSONException {
        JSONObject object = new JSONObject();
        JSONObject data = new JSONObject();
        data.put(RobotAction.POSE_X, pose.getLocation().getX());
        data.put(RobotAction.POSE_Y, pose.getLocation().getY());
        data.put(RobotAction.POSE_YAW, pose.getRotation().getYaw());
        object.put(RobotAction.CMD_CODE, RobotAction.CMD.SET_ROBOT_POSE);
        object.put(RobotAction.DATA, data);
        object.put(RobotAction.DEVICE_ID, deviceID);
        object.put(RobotAction.KEY, key);
        object.put(RobotAction.TIME_STAMP, getTime());
        writeToServer(object);
    }

    public void getMap(int getType) throws JSONException {
        JSONObject object = new JSONObject();
        object.put(RobotAction.CMD_CODE, getType);
        object.put(RobotAction.DEVICE_ID, deviceID);
        object.put(RobotAction.KEY, key);
        object.put(RobotAction.TIME_STAMP, getTime());
        writeToServer(object);
    }

    public void getRobotPose() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(RobotAction.CMD_CODE, RobotAction.CMD.GET_ROBOT_POSE);
        object.put(RobotAction.DEVICE_ID, deviceID);
        object.put(RobotAction.KEY, key);
        object.put(RobotAction.TIME_STAMP, getTime());
        writeToServer(object);
    }

    public void getLaserScan() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(RobotAction.CMD_CODE, RobotAction.CMD.GET_LASER_SCAN);
        object.put(RobotAction.DEVICE_ID, deviceID);
        object.put(RobotAction.KEY, key);
        object.put(RobotAction.TIME_STAMP, getTime());
        writeToServer(object);
    }

    public void getWalls() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(RobotAction.CMD_CODE, RobotAction.CMD.GET_VIRTUAL_WALL);
        object.put(RobotAction.DEVICE_ID, deviceID);
        object.put(RobotAction.KEY, key);
        object.put(RobotAction.TIME_STAMP, getTime());
        writeToServer(object);
    }
    public void addWall(Line line) throws JSONException {
        JSONObject object = new JSONObject();
        object.put(RobotAction.CMD_CODE, RobotAction.CMD.SET_VIRTUAL_WALL);
        object.put(RobotAction.DEVICE_ID, deviceID);
        object.put(RobotAction.KEY, key);
        object.put(RobotAction.TIME_STAMP, getTime());
        object.put(RobotAction.DATA, lineToStr(line));
        writeToServer(object);
    }
    public void clearWalls() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(RobotAction.CMD_CODE, RobotAction.CMD.CLEAR_ALL_VIRTUAL_WALL);
        object.put(RobotAction.DEVICE_ID, deviceID);
        object.put(RobotAction.KEY, key);
        object.put(RobotAction.TIME_STAMP, getTime());
//        object.put(RobotAction.DATA, 0);
        writeToServer(object);
    }
    public void clearWallById(int id) throws JSONException {
        JSONObject object = new JSONObject();
        object.put(RobotAction.CMD_CODE, RobotAction.CMD.CLEAR_ONE_VIRTUAL_WALL);
        object.put(RobotAction.DEVICE_ID, deviceID);
        object.put(RobotAction.KEY, key);
        object.put(RobotAction.TIME_STAMP, getTime());
        object.put(RobotAction.DATA, id);
        writeToServer(object);
    }

    public void getTracks() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(RobotAction.CMD_CODE, RobotAction.CMD.GET_VIRTUAL_TRACK);
        object.put(RobotAction.DEVICE_ID, deviceID);
        object.put(RobotAction.KEY, key);
        object.put(RobotAction.TIME_STAMP, getTime());
        writeToServer(object);
    }
    public void addTrack(Line line) throws JSONException {
        JSONObject object = new JSONObject();
        object.put(RobotAction.CMD_CODE, RobotAction.CMD.SET_VIRTUAL_TRACK);
        object.put(RobotAction.DEVICE_ID, deviceID);
        object.put(RobotAction.KEY, key);
        object.put(RobotAction.TIME_STAMP, getTime());
        object.put(RobotAction.DATA, lineToStr(line));
        writeToServer(object);
    }
    public void clearTracks() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(RobotAction.CMD_CODE, RobotAction.CMD.CLEAR_ALL_VIRTUAL_TRACK);
        object.put(RobotAction.DEVICE_ID, deviceID);
        object.put(RobotAction.KEY, key);
        object.put(RobotAction.TIME_STAMP, getTime());
//        object.put(RobotAction.DATA, 0);
        writeToServer(object);
    }
    public void clearTrackById(int id) throws JSONException {
        JSONObject object = new JSONObject();
        object.put(RobotAction.CMD_CODE, RobotAction.CMD.CLEAR_ONE_VIRTUAL_TRACK);
        object.put(RobotAction.DEVICE_ID, deviceID);
        object.put(RobotAction.KEY, key);
        object.put(RobotAction.TIME_STAMP, getTime());
        object.put(RobotAction.DATA, id);
        writeToServer(object);
    }
}
