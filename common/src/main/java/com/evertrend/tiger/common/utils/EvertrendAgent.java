package com.evertrend.tiger.common.utils;

import com.evertrend.tiger.common.bean.event.slamtec.ConnectedEvent;
import com.evertrend.tiger.common.bean.event.slamtec.ConnectionLostEvent;
import com.evertrend.tiger.common.utils.general.LogUtil;

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

    public EvertrendAgent() {
        mThreadManager = ThreadManager.getInstance();
        mPoolProxy = mThreadManager.createLongPool();

        sTaskConnect = new TaskConnect();
        sTaskDisconnect = new TaskDisconnect();
        sTaskCancelAllActions = new TaskCancelAllActions();
        sTaskMoveBy = new TaskMoveBy();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void connectTo(String local_ins_ip) {
        mIp = local_ins_ip;
        pushTask(sTaskConnect);
    }

    public void disconnect() {
        pushTask(sTaskDisconnect);
    }

    public void moveBy(int robotAction) {
        sTaskMoveBy.setRobotAction(robotAction);
        pushTask(sTaskMoveBy);
    }

    public void cancelAllActions() {
        pushTaskHead(sTaskCancelAllActions);
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
        EventBus.getDefault().postSticky(new ConnectionLostEvent());
    }

    //////////////////////////////////// Runnable //////////////////////////////////////////////////
    private class TaskConnect implements Runnable {

        @Override
        public void run() {
            if (mIp == null || mIp.isEmpty()) {
                onRequestError(new Exception("robot ip is empty"));
                return;
            }

            synchronized (this) {
                //创建配置文件类
                ConnectConfig config = new ConnectConfig.Builder()
                        .setIp(mIp)
                        .setPort(ROBOT_PORT)
                        .setReadBufferSize(10240)
                        .setConnectionTimeout(10000)
                        .bulid();
                //创建连接的管理类
                mConnectManager = new ConnectManager(config);
                //利用循环请求连接
                while (true) {
                    boolean isConnection = mConnectManager.connect();
                    LogUtil.d(TAG, "isConnection: "+isConnection);
                    if (isConnection) {
                        //当请求成功的时候,跳出循环
                        mSessionManager = SessionManager.getmInstance();
                        EventBus.getDefault().postSticky(new ConnectedEvent());
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

        public TaskMoveBy() {
            robotAction = 0;
        }

        public void setRobotAction(int robotAction) {
            this.robotAction = robotAction;
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
                manager.moveBy(robotAction);
            } catch (Exception e) {
                onRequestError(e);
            }
        }
    }

}
