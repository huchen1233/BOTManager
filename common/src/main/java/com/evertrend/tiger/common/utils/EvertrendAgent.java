package com.evertrend.tiger.common.utils;

import com.evertrend.tiger.common.bean.event.map.DeleteOneVwallCompleteEvent;
import com.evertrend.tiger.common.bean.event.slamtec.ConnectedEvent;
import com.evertrend.tiger.common.bean.event.slamtec.ConnectionLostEvent;
import com.slamtec.slamware.AbstractSlamwarePlatform;
import com.slamtec.slamware.geometry.Line;
import com.slamtec.slamware.robot.LaserScan;
import com.slamtec.slamware.robot.Location;
import com.slamtec.slamware.robot.Map;
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
    private static TaskClearMap sTaskClearMap;
    private static TaskLoadMap sTaskLoadMap;
    private static TaskRelocation sTaskRelocation;
    private static TaskGetRobotPose sTaskGetRobotPose;
    private static TaskGetLaserScan sTaskGetLaserScan;
    private static TaskGetWalls sTaskGetWalls;
    private static TaskAddVwall sTaskAddVwall;
    private static TaskClearAllVwalls sTaskClearAllVwalls;
    private static TaskClearOneVwall sTaskClearOneVwall;
    private static TaskGetTracks sTaskGetTracks;
    private static TaskAddVtrack sTaskAddVtrack;
    private static TaskClearAllVtracks sTaskClearAllVtracks;
    private static TaskClearOneVtrack sTaskClearOneVtrack;
    private static TaskNavigationPathPlanning sTaskNavigationPathPlanning;

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
        sTaskClearMap = new TaskClearMap();
        sTaskLoadMap = new TaskLoadMap();
        sTaskRelocation = new TaskRelocation();
        sTaskGetRobotPose = new TaskGetRobotPose();
        sTaskGetLaserScan = new TaskGetLaserScan();
        sTaskGetWalls = new TaskGetWalls();
        sTaskAddVwall = new TaskAddVwall();
        sTaskClearAllVwalls = new TaskClearAllVwalls();
        sTaskClearOneVwall = new TaskClearOneVwall();
        sTaskGetTracks = new TaskGetTracks();
        sTaskAddVtrack = new TaskAddVtrack();
        sTaskClearAllVtracks = new TaskClearAllVtracks();
        sTaskClearOneVtrack = new TaskClearOneVtrack();
        sTaskNavigationPathPlanning = new TaskNavigationPathPlanning();
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

    public void moveTo(Pose moveToPose) {
        sTaskMoveTo.setPose(moveToPose);
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

    public void clearMap() {
        pushTask(sTaskClearMap);
    }

    public void loadMap() {
        pushTask(sTaskLoadMap);
    }

    public void relocation() {
        pushTask(sTaskRelocation);
    }

    public void getRobotPose() {
        pushTask(sTaskGetRobotPose);
    }

    public void getLaserScan() {
        pushTask(sTaskGetLaserScan);
    }

    public void getWalls() {
        pushTask(sTaskGetWalls);
    }
    public void addVwall(Line line) {
        sTaskAddVwall.setVwall(line);
        pushTask(sTaskAddVwall);
    }
    public void clearAllVwalls() {
        pushTask(sTaskClearAllVwalls);
    }
    public void clearOneVwall(Line line) {
        sTaskClearOneVwall.setLine(line);
        pushTask(sTaskClearOneVwall);
    }

    public void getTracks() {
        pushTask(sTaskGetTracks);
    }
    public void addVtrack(Line line) {
        sTaskAddVtrack.setVtrack(line);
        pushTask(sTaskAddVtrack);
    }
    public void clearAllVtracks() {
        pushTask(sTaskClearAllVtracks);
    }
    public void clearOneVtrack(Line line) {
        sTaskClearOneVtrack.setLine(line);
        pushTask(sTaskClearOneVtrack);
    }
    public void getNavigationPathPlanning() {
        pushTask(sTaskNavigationPathPlanning);
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
                int count = 0;
                //利用循环请求连接
                while (true) {
                    if (count == 100) {
                        mSessionManager = null;
                        break;
                    }
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
                        count++;
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
                mSessionManager = null;
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

            if (manager == null) {
                return;
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

            if (manager == null) {
                return;
            }
            if (manager.getIoSession() == null) {
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
        private Pose moveToPose;

        public TaskMoveTo() {
        }

        public void setPose(Pose moveToPose) {
            this.moveToPose = moveToPose;
        }


        @Override
        public void run() {
            SessionManager manager;

            synchronized (this) {
                manager = mSessionManager;
            }

            if (manager == null) {
                return;
            }
            if (mSessionManager.getIoSession() == null) {
                onRequestError(new Exception("connect closed"));
                return;
            }

            try {
                manager.moveTo(moveToPose);
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

            if (manager == null) {
                return;
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

    private class TaskClearMap implements Runnable {
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

            try {
                manager.clearMap();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class TaskLoadMap implements Runnable {
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

            try {
                manager.loadMap();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class TaskRelocation implements Runnable {
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

            try {
                manager.relocation();
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    private class TaskGetWalls implements Runnable {
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

            try {
                manager.getWalls();
            } catch (Exception e) {
                onRequestError(e);
                return;
            }
        }
    }

    private class TaskAddVwall implements Runnable {
        private Line line;

        public TaskAddVwall() {
        }

        public void setVwall(Line line) {
            this.line = line;
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

            try {
                manager.addWall(line);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            EventBus.getDefault().post(new AddWall(typeAddVirtualWall));
        }
    }

    private class TaskClearAllVwalls implements Runnable {
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

            try {
                manager.clearWalls();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class TaskClearOneVwall implements Runnable {
        private Line line;

        public TaskClearOneVwall() {
        }

        public void setLine(Line line) {
            this.line = line;
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

            try {
                manager.clearWallById(line.getSegmentId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class TaskGetTracks implements Runnable {
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

            try {
                manager.getTracks();
            } catch (Exception e) {
                onRequestError(e);
                return;
            }
        }
    }

    private class TaskAddVtrack implements Runnable {
        private Line line;

        public TaskAddVtrack() {
        }

        public void setVtrack(Line line) {
            this.line = line;
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

            try {
                manager.addTrack(line);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            EventBus.getDefault().post(new AddWall(typeAddVirtualWall));
        }
    }

    private class TaskClearAllVtracks implements Runnable {
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

            try {
                manager.clearTracks();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class TaskClearOneVtrack implements Runnable {
        private Line line;

        public TaskClearOneVtrack() {
        }

        public void setLine(Line line) {
            this.line = line;
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

            try {
                manager.clearTrackById(line.getSegmentId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class TaskNavigationPathPlanning implements Runnable {
        public TaskNavigationPathPlanning() {
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

            try {
                manager.getNavigationPathPlanning();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
