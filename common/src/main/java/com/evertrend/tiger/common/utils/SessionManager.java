package com.evertrend.tiger.common.utils;

import com.evertrend.tiger.common.bean.RobotAction;
import com.evertrend.tiger.common.utils.general.LogUtil;

import org.apache.mina.core.session.IoSession;
import org.json.JSONException;
import org.json.JSONObject;

public class SessionManager {
    public static final String TAG = SessionManager.class.getCanonicalName();
    private static  SessionManager mInstance = null;

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
//    public void writeToServer(Object msg) {
//        LogUtil.d(TAG, "ioSession write: "+ioSession);
//        if (ioSession != null) {
//            ioSession.write(msg);
//        }
//    }

    public void stop() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(RobotAction.CMD_CODE, RobotAction.CMD.STOP);
        object.put(RobotAction.DEVICE_ID, "9002");
        object.put(RobotAction.KEY, "1993");
        object.put(RobotAction.TIME_STAMP, getTime());
        if (ioSession != null) {
            ioSession.write(object.toString());
        }
    }

    public void moveBy(int robotAction) throws JSONException {
        JSONObject object = new JSONObject();
        if (robotAction == RobotAction.CMD.FORWARD || robotAction == RobotAction.CMD.TURN_LEFT) {
            object.put(RobotAction.DATA, 0.1);
        } else if (robotAction == RobotAction.CMD.BACKWARD || robotAction == RobotAction.CMD.TURN_RIGHT) {
            object.put(RobotAction.DATA, -0.1);
        }
        object.put(RobotAction.CMD_CODE, robotAction);
        object.put(RobotAction.DEVICE_ID, "9002");
        object.put(RobotAction.KEY, "1993");
        object.put(RobotAction.TIME_STAMP, getTime());
        if (ioSession != null) {
            ioSession.write(object.toString());
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
}
