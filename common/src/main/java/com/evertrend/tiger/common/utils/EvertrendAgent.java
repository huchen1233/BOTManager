package com.evertrend.tiger.common.utils;

import com.evertrend.tiger.common.bean.event.slamtec.ConnectedEvent;
import com.evertrend.tiger.common.bean.event.slamtec.ConnectionLostEvent;
import com.slamtec.slamware.AbstractSlamwarePlatform;
import com.slamtec.slamware.robot.LaserScan;
import com.slamtec.slamware.robot.Location;
import com.slamtec.slamware.robot.Map;
import com.slamtec.slamware.robot.MoveOption;
import com.slamtec.slamware.robot.Pose;

import org.greenrobot.eventbus.EventBus;

public class EvertrendAgent {
    private final static String TAG = "EvertrendAgent";
    private final static int ROBOT_PORT = 28700;

    private SessionManager mSessionManager;
    private ConnectManager mConnectManager;
    private String mIp;

    private ThreadManager mThreadManager;
    private ThreadManager.ThreadPoolProxy mPoolProxy;

    private static TaskConnect sTaskConnect;
    private static TaskDisconnect sTaskDisconnect;
    private static TaskCancelAllActions sTaskCancelAllActions;
    private static TaskMoveBy sTaskMoveBy;
    private static TaskSetPose sTaskSetPose;
    private static TaskMoveTo sTaskMoveTo;
    private static TaskGetMap sTaskGetMap;
    private static TaskGetRobotPose sTaskGetRobotPose;
    private static TaskGetLaserScan sTaskGetLaserScan;

    public EvertrendAgent() {
        mThreadManager = ThreadManager.getInstance();
        mPoolProxy = mThreadManager.createLongPool();

        sTaskConnect = new TaskConnect();
        sTaskDisconnect = new TaskDisconnect();
        sTaskCancelAllActions = new TaskCancelAllActions();
        sTaskMoveBy = new TaskMoveBy();
        sTaskSetPose = new TaskSetPose();
        sTaskMoveTo = new TaskMoveTo();
        sTaskGetMap = new TaskGetMap();
        sTaskGetRobotPose = new TaskGetRobotPose();
        sTaskGetLaserScan = new TaskGetLaserScan();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void connectTo(String local_ins_ip, String deviceID, String key) {
        mIp = local_ins_ip;
        sTaskConnect.setConnect(deviceID, key);
        pushTask(sTaskConnect);
    }

    public void disconnect() {
        pushTask(sTaskDisconnect);
    }

    public void moveBy(int robotAction, float speed) {
        sTaskMoveBy.setRobotAction(robotAction, speed);
        pushTask(sTaskMoveBy);
    }

    public void moveTo(Location location) {
        sTaskMoveTo.setlocation(location);
        pushTaskHead(sTaskMoveTo);
    }

    public void setPose(Pose pose) {
        sTaskSetPose.setPose(pose);
        pushTaskHead(sTaskSetPose);
    }

    public void cancelAllActions() {
        pushTaskHead(sTaskCancelAllActions);
    }

    public void getMap(int getType) {
        sTaskGetMap.setGetType(getType);
        pushTask(sTaskGetMap);
    }

    public void getRobotPose() {
        pushTask(sTaskGetRobotPose);
    }

    public void getLaserScan() {
        pushTask(sTaskGetLaserScan);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private synchronized void pushTask(Runnable Task) {
        mPoolProxy.execute(Task);
    }

    private synchronized void pushTaskHead(Runnable Task) {
        mPoolProxy.execute(Task);
    }

    private void onRequestError(Exception e) {
        synchronized (this) {
            mPoolProxy.cancleAll();
            mSessionManager = null;
        }
        EventBus.getDefault().post(new ConnectionLostEvent());
    }

    //////////////////////////////////// Runnable //////////////////////////////////////////////////
    private class TaskConnect implements Runnable {
        String deviceID;
        String key;

        public void setConnect(String deviceID, String key) {
            this.deviceID = deviceID;
            this.key = key;
        }

        @Override
        public void run() {
            if (mIp == null || mIp.isEmpty()) {
                onRequestError(new Exception("robot ip is empty"));
                return;
            }

            if (mSessionManager != null) {
                return;
            }

            synchronized (this) {
                //创建配置文件类
                ConnectConfig config = new ConnectConfig.Builder()
                        .setIp(mIp)
                        .setPort(ROBOT_PORT)
                        .setReadBufferSize(2*1024*1024)
                        .setReceiveBufferSize(2*1024*1024)
                        .setConnectionTimeout(10000)
                        .bulid();
                //创建连接的管理类
                mConnectManager = new ConnectManager(config);
                //利用循环请求连接
                while (true) {
                    boolean isConnection = mConnectManager.connect();
                    if (isConnection) {
                        //当请求成功的时候,跳出循环
                        mSessionManager = SessionManager.getmInstance();
                        mSessionManager.setDeviceID(deviceID);
                        mSessionManager.setKey(key);
                        EventBus.getDefault().post(new ConnectedEvent());
                        break;
                    }
                    try {
                        Thread.sleep(3000);
                    } catch (Exception e) {
                        onRequestError(e);
                    }
                }
            }
        }
    }

    private class TaskDisconnect implements Runnable {
        @Override
        public void run() {
            synchronized (this) {
                if (mSessionManager == null) {
                    return;
                }

                if (mSessionManager.getIoSession() == null) {
                    return;
                }
                mPoolProxy.cancleAll();
                mSessionManager.closeSession();
                mConnectManager.disConnect();
            }
        }
    }

    private class TaskCancelAllActions implements Runnable {
        @Override
        public void run() {
            SessionManager manager;

            synchronized (this) {
                manager = mSessionManager;
            }

            if (mSessionManager.getIoSession() == null) {
                onRequestError(new Exception("connect closed"));
                return;
            }

            try {
                manager.stop();
            } catch (Exception e) {
                onRequestError(e);
            } finally {
                mPoolProxy.cancleAll();
            }

        }
    }

    private class TaskMoveBy implements Runnable {
        int robotAction;
        float speed;

        public TaskMoveBy() {
            robotAction = 0;
        }

        public void setRobotAction(int robotAction, float speed) {
            this.robotAction = robotAction;
            this.speed = speed;
        }

        @Override
        public void run() {
            SessionManager manager;

            synchronized (this) {
                manager = mSessionManager;
            }

            if (mSessionManager.getIoSession() == null) {
                onRequestError(new Exception("connect closed"));
                return;
            }

            try {
                manager.moveBy(robotAction, speed);
            } catch (Exception e) {
                onRequestError(e);
            }
        }
    }

    private class TaskMoveTo implements Runnable {
        private Location location;

        public TaskMoveTo() {
        }

        public void setlocation(Location location) {
            this.location = location;
        }


        @Override
        public void run() {
            SessionManager manager;

            synchronized (this) {
                manager = mSessionManager;
            }

            if (mSessionManager.getIoSession() == null) {
                onRequestError(new Exception("connect closed"));
                return;
            }

            try {
                manager.moveTo(location, 0f);
            } catch (Exception e) {
                onRequestError(e);
            }
        }
    }

    private class TaskSetPose implements Runnable {
        private Pose pose;

        public TaskSetPose() {
        }

        public void setPose(Pose pose) {
            this.pose = pose;
        }


        @Override
        public void run() {
            SessionManager manager;

            synchronized (this) {
                manager = mSessionManager;
            }

            if (mSessionManager.getIoSession() == null) {
                onRequestError(new Exception("connect closed"));
                return;
            }

            try {
                manager.setPose(pose);
            } catch (Exception e) {
                onRequestError(e);
            }
        }
    }

    private class TaskGetMap implements Runnable {
        private int getType;

        public void setGetType(int getType) {
            this.getType = getType;
        }

        @Override
        public void run() {
            SessionManager manager;

            synchronized (this) {
                manager = mSessionManager;
            }

            if (mSessionManager == null) {
                return;
            }
            if (mSessionManager.getIoSession() == null) {
                onRequestError(new Exception("connect closed"));
                return;
            }

            Map map = null;

            try {
//                RectF area = platform.getKnownArea(BITMAP_8BIT, MapKind.EXPLORE_MAP);
//                map = platform.getMap(BITMAP_8BIT, MapKind.EXPLORE_MAP, area);
                manager.getMap(getType);
            } catch (Exception e) {
                onRequestError(e);
                return;
            }

//            EventBus.getDefault().postSticky(new MapGetEvent(map));
        }
    }

    private class TaskGetRobotPose implements Runnable {
        @Override
        public void run() {
            SessionManager manager;

            synchronized (this) {
                manager = mSessionManager;
            }

            if (mSessionManager == null) {
                return;
            }
            if (mSessionManager.getIoSession() == null) {
                onRequestError(new Exception("connect closed"));
                return;
            }

            Pose pose;

            try {
                manager.getRobotPose();
            } catch (Exception e) {
                onRequestError(e);
                return;
            }

//            EventBus.getDefault().postSticky(new RobotPoseGetEvent(pose));
        }
    }

    private class TaskGetLaserScan implements Runnable {
        @Override
        public void run() {
            SessionManager manager;

            synchronized (this) {
                manager = mSessionManager;
            }

            if (mSessionManager == null) {
                return;
            }
            if (mSessionManager.getIoSession() == null) {
                onRequestError(new Exception("connect closed"));
                return;
            }

            LaserScan laserScan;

            try {
                manager.getLaserScan();
            } catch (Exception e) {
                onRequestError(e);
                return;
            }

//            EventBus.getDefault().postSticky(new LaserScanGetEvent(laserScan));
        }
    }
}
