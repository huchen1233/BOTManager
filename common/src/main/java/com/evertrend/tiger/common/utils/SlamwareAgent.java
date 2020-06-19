package com.evertrend.tiger.common.utils;

import android.graphics.RectF;

import com.evertrend.tiger.common.bean.event.map.AddTrack;
import com.evertrend.tiger.common.bean.event.map.AddVtracks;
import com.evertrend.tiger.common.bean.event.map.AddVwalls;
import com.evertrend.tiger.common.bean.event.map.AddWall;
import com.evertrend.tiger.common.bean.event.map.ClearAllVtracks;
import com.evertrend.tiger.common.bean.event.map.ClearAllVwalls;
import com.evertrend.tiger.common.bean.event.map.ClearMapSuccessEvent;
import com.evertrend.tiger.common.bean.event.map.DeleteOneVtrackCompleteEvent;
import com.evertrend.tiger.common.bean.event.map.DeleteOneVwallCompleteEvent;
import com.evertrend.tiger.common.bean.event.slamtec.ActionStatusGetEvent;
import com.evertrend.tiger.common.bean.event.slamtec.ConnectedEvent;
import com.evertrend.tiger.common.bean.event.slamtec.ConnectionLostEvent;
import com.evertrend.tiger.common.bean.event.slamtec.GetCompositeMapEvent;
import com.evertrend.tiger.common.bean.event.slamtec.HomePoseGetEvent;
import com.evertrend.tiger.common.bean.event.slamtec.LaserScanGetEvent;
import com.evertrend.tiger.common.bean.event.slamtec.MapGetEvent;
import com.evertrend.tiger.common.bean.event.slamtec.MapUpdataEvent;
import com.evertrend.tiger.common.bean.event.slamtec.RemainingMilestonesGetEvent;
import com.evertrend.tiger.common.bean.event.slamtec.RemainingPathGetEvent;
import com.evertrend.tiger.common.bean.event.slamtec.RobotHealthInfoEvent;
import com.evertrend.tiger.common.bean.event.slamtec.RobotInfoEvent;
import com.evertrend.tiger.common.bean.event.slamtec.RobotPoseGetEvent;
import com.evertrend.tiger.common.bean.event.slamtec.TrackGetEvent;
import com.evertrend.tiger.common.bean.event.slamtec.WallGetEvent;
import com.evertrend.tiger.common.utils.general.AppSharePreference;
import com.slamtec.slamware.AbstractSlamwarePlatform;
import com.slamtec.slamware.action.ActionStatus;
import com.slamtec.slamware.action.IMoveAction;
import com.slamtec.slamware.action.MoveDirection;
import com.slamtec.slamware.action.Path;
import com.slamtec.slamware.discovery.DeviceManager;
import com.slamtec.slamware.geometry.Line;
import com.slamtec.slamware.robot.CompositeMap;
import com.slamtec.slamware.robot.HealthInfo;
import com.slamtec.slamware.robot.LaserScan;
import com.slamtec.slamware.robot.Location;
import com.slamtec.slamware.robot.Map;
import com.slamtec.slamware.robot.MapKind;
import com.slamtec.slamware.robot.MoveOption;
import com.slamtec.slamware.robot.Pose;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static com.slamtec.slamware.action.ActionStatus.FINISHED;
import static com.slamtec.slamware.robot.ArtifactUsage.ArtifactUsageVirtualTrack;
import static com.slamtec.slamware.robot.MapType.BITMAP_8BIT;
import static com.slamtec.slamware.robot.SystemParameters.SYSPARAM_ROBOT_SPEED;

public class SlamwareAgent {
    private final static String TAG = "SlamwareAgent";
    private final static int ROBOT_PORT = 1445;
    private final static int NAVIGATION_MODE_FREE = 0;
    private final static int NAVIGATION_MODE_TRACK = 1;
    private final static int NAVIGATION_MODE_TRACK_OA = 2;

    private AbstractSlamwarePlatform mRobotPlatform;
    private String mIp;
    private int mNavigationMode;

    private ThreadManager mManager;
    private ThreadManager.ThreadPoolProxy mPoolProxy;

    private static TaskConnect sTaskConnect;
    private static TaskDisconnect sTaskDisconnect;
    private static TaskCancelAllActions sTaskCancelAllActions;
    private static TaskMoveBy sTaskMoveBy;
    private static TaskMoveTo sTaskMoveTo;
    private static TaskGetRobotPose sTaskGetRobotPose;
    private static TaskGetRobotInfo sTaskGetRobotInfo;
    private static TaskGetLaserScan sTaskGetLaserScan;
    private static TaskGetHomePose sTaskGetHomePose;
    private static TaskGetRobotHealth sTaskGetRobotHealth;
    private static TaskClearRobotHealth sTaskClearRobotHealth;
    private static TaskGetMapUpdata sTaskGetIsMapUpdata;
    private static TaskToggleMapUpdata sTaskToggleMapUpdata;
    private static TaskGetMap sTaskGetMap;
    private static TaskGetWalls sTaskGetWalls;
    private static TaskGetTracks sTaskGetTracks;
    private static TaskGetMoveAction sTaskGetMoveAction;
    private static TaskgetCompositeMap sTaskgetCompositeMap;
    private static TaskSetDeviceSpeed sTaskSetDeviceSpeed;
    private static TaskClearMap sTaskClearMap;
    private static TaskAddVwalls sTaskAddVwalls;
    private static TaskAddVtracks sTaskAddVtracks;
    private static TaskAddVwall sTaskAddVwall;
    private static TaskAddVtrack sTaskAddVtrack;
    private static TaskClearAllVwalls sTaskClearAllVwalls;
    private static TaskClearAllVtracks sTaskClearAllVtracks;
    private static TaskRemoveOneVwalls sTaskRemoveOneVwalls;
    private static TaskRemoveOneVtracks sTaskRemoveOneVtracks;

    public SlamwareAgent() {
        mManager = ThreadManager.getInstance();
        mPoolProxy = mManager.createLongPool();

        sTaskConnect = new TaskConnect();
        sTaskDisconnect = new TaskDisconnect();
        sTaskCancelAllActions = new TaskCancelAllActions();
        sTaskMoveBy = new TaskMoveBy();
        sTaskMoveTo = new TaskMoveTo();
        sTaskGetRobotPose = new TaskGetRobotPose();
        sTaskGetRobotInfo = new TaskGetRobotInfo();
        sTaskGetLaserScan = new TaskGetLaserScan();
        sTaskGetHomePose = new TaskGetHomePose();
        sTaskGetRobotHealth = new TaskGetRobotHealth();
        sTaskClearRobotHealth = new TaskClearRobotHealth();
        sTaskGetIsMapUpdata = new TaskGetMapUpdata();
        sTaskToggleMapUpdata = new TaskToggleMapUpdata();
        sTaskGetMap = new TaskGetMap();
        sTaskGetWalls = new TaskGetWalls();
        sTaskGetTracks = new TaskGetTracks();
        sTaskGetMoveAction = new TaskGetMoveAction();
        sTaskgetCompositeMap = new TaskgetCompositeMap();
        sTaskSetDeviceSpeed = new TaskSetDeviceSpeed();
        sTaskClearMap = new TaskClearMap();
        sTaskAddVwalls = new TaskAddVwalls();
        sTaskAddVtracks = new TaskAddVtracks();
        sTaskAddVwall = new TaskAddVwall();
        sTaskAddVtrack = new TaskAddVtrack();
        sTaskClearAllVwalls = new TaskClearAllVwalls();
        sTaskClearAllVtracks = new TaskClearAllVtracks();
        sTaskRemoveOneVwalls = new TaskRemoveOneVwalls();
        sTaskRemoveOneVtracks = new TaskRemoveOneVtracks();

        mNavigationMode = NAVIGATION_MODE_FREE;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void connectTo(String local_chassis_ip) {
        mIp = local_chassis_ip;
        pushTask(sTaskConnect);
    }

    public void disconnect() {
        pushTask(sTaskDisconnect);
    }

    public void saveCompositeMap() {
        pushTaskHead(sTaskgetCompositeMap);
    }

    public void getHomePose() {
        pushTask(sTaskGetHomePose);
    }

    public void getMap() {
        pushTask(sTaskGetMap);
    }

    public void getWalls(int type) {
        sTaskGetWalls.setType(type);
        pushTask(sTaskGetWalls);
    }

    public void getTracks(int type) {
        sTaskGetTracks.setType(type);
        pushTask(sTaskGetTracks);
    }

    public void getMoveAction() {
        pushTask(sTaskGetMoveAction);
    }

    public void getMapUpdata() {
        pushTaskHead(sTaskGetIsMapUpdata);
    }

    public void toggleMapUpdata() {
        pushTaskHead(sTaskToggleMapUpdata);
    }

    public void getRobotHealth() {
        pushTask(sTaskGetRobotHealth);
    }

    public void clearRobotHealth(List<Integer> errors) {
        sTaskClearRobotHealth.setErrorCodes(errors);
        pushTask(sTaskClearRobotHealth);
    }

    public void getRobotPose() {
        pushTask(sTaskGetRobotPose);
    }

    public void getGetRobotInfo() {
        pushTask(sTaskGetRobotInfo);
    }

    public void getLaserScan() {
        pushTask(sTaskGetLaserScan);
    }

    public void setNavigationMode(int mode) {
        mNavigationMode = mode;
    }

    public int getNavigationMode() {
        return mNavigationMode;
    }

    public void moveTo(Location location) {
        sTaskMoveTo.setlocation(location);
        pushTaskHead(sTaskMoveTo);
    }

    public void moveBy(MoveDirection direction) {
        sTaskMoveBy.setMoveDirection(direction);
        pushTask(sTaskMoveBy);
    }

    public void setDeviceSpeed(String speed) {
        sTaskSetDeviceSpeed.setSpeed(speed);
        pushTask(sTaskSetDeviceSpeed);
    }

    public void cancelAllActions() {
        pushTaskHead(sTaskCancelAllActions);
    }

    public void clearMap() {
        pushTask(sTaskClearMap);
    }

    public void addVwalls(List<Line> lineList) {
        sTaskAddVwalls.setVwalls(lineList);
        pushTask(sTaskAddVwalls);
    }

    public void addVtracks(List<Line> lineList) {
        sTaskAddVtracks.setVwalls(lineList);
        pushTask(sTaskAddVtracks);
    }

    public void addVwall(Line line, int typeAddVirtualWall) {
        sTaskAddVwall.setVwall(line, typeAddVirtualWall);
        pushTask(sTaskAddVwall);
    }

    public void addVtrack(Line line, int typeAddVirtualTrack) {
        sTaskAddVtrack.setVtrack(line, typeAddVirtualTrack);
        pushTask(sTaskAddVtrack);
    }

    public void clearAllVwalls() {
        pushTask(sTaskClearAllVwalls);
    }

    public void clearAllVtracks() {
        pushTask(sTaskClearAllVtracks);
    }

    public void removeOneVwalls(Line line, int moveType) {
        sTaskRemoveOneVwalls.setLine(line);
        sTaskRemoveOneVwalls.setType(moveType);
        pushTask(sTaskRemoveOneVwalls);
    }

    public void removeOneVtracks(Line line, int moveType) {
        sTaskRemoveOneVtracks.setLine(line);
        sTaskRemoveOneVtracks.setType(moveType);
        pushTask(sTaskRemoveOneVtracks);
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
            mRobotPlatform = null;
        }
        EventBus.getDefault().postSticky(new ConnectionLostEvent());
    }

    //////////////////////////////////// Runnable //////////////////////////////////////////////////
    private class TaskConnect implements Runnable {

        @Override
        public void run() {
            try {
                if (mIp == null || mIp.isEmpty()) {
                    onRequestError(new Exception("robot ip is empty"));
                    return;
                }

                synchronized (this) {
                    mRobotPlatform = DeviceManager.connect(mIp, ROBOT_PORT);
                }
            } catch (Exception exception) {
                onRequestError(exception);
                return;
            }

            EventBus.getDefault().postSticky(new ConnectedEvent());
        }
    }

    private class TaskDisconnect implements Runnable {
        @Override
        public void run() {
            synchronized (this) {
                if (mRobotPlatform == null) {
                    return;
                }
                mPoolProxy.cancleAll();
                mRobotPlatform.disconnect();
                mRobotPlatform = null;
            }
        }
    }

    private class TaskCancelAllActions implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;

            synchronized (this) {
                platform = mRobotPlatform;
            }

            if (platform == null) return;

            try {
                IMoveAction moveAction = platform.getCurrentAction();
                if (moveAction != null) {
                    moveAction.cancel();
                }
            } catch (Exception e) {
                onRequestError(e);
            }

            mPoolProxy.cancleAll();
        }
    }

    private class TaskMoveBy implements Runnable {
        MoveDirection moveDirection;

        public TaskMoveBy() {
            moveDirection = null;
        }

        public void setMoveDirection(MoveDirection moveDirection) {
            this.moveDirection = moveDirection;
        }

        @Override
        public void run() {
            AbstractSlamwarePlatform platform;

            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null || moveDirection == null) {
                    return;
                }
            }

            try {
                platform.moveBy(moveDirection);
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
            AbstractSlamwarePlatform platform;

            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null || location == null) return;
            }

            try {
                MoveOption moveOption = new MoveOption();

                switch (mNavigationMode) {
                    case NAVIGATION_MODE_FREE:
                        break;

                    case NAVIGATION_MODE_TRACK:
                        moveOption.setKeyPoints(true);
                        break;

                    case NAVIGATION_MODE_TRACK_OA:
                        moveOption.setKeyPoints(true);
                        moveOption.setTrackWithOA(true);
                        break;

                    default:
                        break;
                }

                platform.moveTo(location, moveOption, 0f);

            } catch (Exception e) {
                onRequestError(e);
            }
        }
    }

    private class TaskGetRobotPose implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null) {
                    return;
                }
            }

            Pose pose;

            try {
                pose = platform.getPose();
            } catch (Exception e) {
                onRequestError(e);
                return;
            }

            EventBus.getDefault().postSticky(new RobotPoseGetEvent(pose));
        }
    }

    private class TaskGetRobotInfo implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null) {
                    return;
                }
            }

            int modelId;
            String hardwareVersion;
            String softwareVersion;
            String modelName;

            try {
                modelId = platform.getModelId();
                hardwareVersion = platform.getHardwareVersion();
                softwareVersion = platform.getSoftwareVersion();
                modelName = platform.getModelName();
            } catch (Exception e) {
                onRequestError(e);
                return;
            }

            EventBus.getDefault().postSticky(new RobotInfoEvent(modelId, hardwareVersion, softwareVersion, modelName));
        }
    }

    private class TaskGetLaserScan implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null) return;
            }

            LaserScan laserScan;

            try {
                laserScan = platform.getLaserScan();
            } catch (Exception e) {
                onRequestError(e);
                return;
            }

            EventBus.getDefault().postSticky(new LaserScanGetEvent(laserScan));
        }
    }

    private class TaskGetHomePose implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;

            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null) return;
            }

            Pose homePose;

            try {
                homePose = platform.getHomePose();
            } catch (Exception e) {
                onRequestError(e);
                return;
            }

            EventBus.getDefault().postSticky(new HomePoseGetEvent(homePose));
        }
    }

    private class TaskGetMapUpdata implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null) {
                    return;
                }
            }

            boolean isMapUpdata;

            try {
                isMapUpdata = platform.getMapUpdate();
            } catch (Exception e) {
                onRequestError(e);
                return;
            }

            EventBus.getDefault().postSticky(new MapUpdataEvent(isMapUpdata));
        }
    }

    private class TaskGoHome implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
            }
            if (platform == null) return;

            try {
                platform.goHome();
            } catch (Exception e) {
                onRequestError(e);
            }
        }
    }

    private class TaskGetRobotHealth implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
            }

            if (platform == null) {
                return;
            }

            String errorMsg = "";
            List<Integer> errorList = new ArrayList<>();

            try {
                HealthInfo info = platform.getRobotHealth();

                if (info.isWarning() || info.isError() || info.isFatal() || (info.getErrors() != null && info.getErrors().size() > 0)) {

                    for (HealthInfo.BaseError error : info.getErrors()) {
                        String level;
                        switch (error.getErrorLevel()) {
                            case HealthInfo.BaseError.BaseErrorLevelWarn:
                                level = "Warning";
                                break;
                            case HealthInfo.BaseError.BaseErrorLevelError:
                                level = "Error";
                                break;
                            case HealthInfo.BaseError.BaseErrorLevelFatal:
                                level = "Fatal";
                                break;
                            default:
                                level = "Unknown";
                                break;
                        }
                        String component;
                        switch (error.getErrorComponent()) {
                            case HealthInfo.BaseError.BaseErrorComponentUser:
                                component = "User";
                                break;
                            case HealthInfo.BaseError.BaseErrorComponentMotion:
                                component = "Motion";
                                break;
                            case HealthInfo.BaseError.BaseErrorComponentPower:
                                component = "Power";
                                break;
                            case HealthInfo.BaseError.BaseErrorComponentSensor:
                                component = "Sensor";
                                break;
                            case HealthInfo.BaseError.BaseErrorComponentSystem:
                                component = "System";
                                break;
                            default:
                                component = "Unknown";
                                break;
                        }

                        errorList.add(error.getErrorCode());
                        errorMsg += String.format("Error ID: %d\nError level: %s\nError Component: %s\nError message: %s\nError ErrorCode: %d\n------\n", error.getId(), level, component, error.getErrorMessage(), error.getErrorCode());
                        EventBus.getDefault().postSticky(new RobotHealthInfoEvent(errorMsg, errorList));
                    }
                }
            } catch (Exception e) {
                onRequestError(e);
            }
        }
    }

    private class TaskClearRobotHealth implements Runnable {
        List<Integer> errors;

        @Override
        public void run() {
            AbstractSlamwarePlatform platform;

            synchronized (this) {
                platform = mRobotPlatform;
            }

            if (platform == null) {
                return;
            }

            try {
                for (Integer error : errors) {
                    platform.clearRobotHealth(error);
                }
            } catch (Exception e) {
                onRequestError(e);
            }
        }

        public void setErrorCodes(List<Integer> errors) {
            this.errors = errors;
        }
    }

    private class TaskToggleMapUpdata implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null) {
                    return;
                }
            }

            try {
                boolean mapUpdate = platform.getMapUpdate();
                platform.setMapUpdate(!mapUpdate);
            } catch (Exception e) {
                onRequestError(e);
                return;
            }
        }
    }

    private class TaskGetMap implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;

            synchronized (this) {
                platform = mRobotPlatform;
            }

            if (platform == null) {
                return;
            }

            Map map = null;

            try {
                RectF area = platform.getKnownArea(BITMAP_8BIT, MapKind.EXPLORE_MAP);
                map = platform.getMap(BITMAP_8BIT, MapKind.EXPLORE_MAP, area);
            } catch (Exception e) {
                onRequestError(e);
                return;
            }

            EventBus.getDefault().postSticky(new MapGetEvent(map));
        }
    }

    private class TaskGetWalls implements Runnable {
        private int type;
        private void setType(int type) {
            this.type = type;
        }
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null) {
                    return;
                }
            }

            Vector<Line> walls;

            try {
                walls = platform.getWalls();
            } catch (Exception e) {
                onRequestError(e);
                return;
            }

            EventBus.getDefault().postSticky(new WallGetEvent(walls, type));
        }
    }

    private class TaskGetTracks implements Runnable {
        private int type;
        private void setType(int type) {
            this.type = type;
        }
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null) {
                    return;
                }
            }

            List<Line> tracks;
            try {
                tracks = platform.getLines(ArtifactUsageVirtualTrack);
            } catch (Exception e) {
                onRequestError(e);
                return;
            }

            EventBus.getDefault().postSticky(new TrackGetEvent(tracks, type));
        }
    }

    private class TaskGetMoveAction implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
                if (platform == null) return;
            }

            Path remainingMilestones = null;
            Path remainingPath = null;
            ActionStatus actionStatus = FINISHED;

            try {
                IMoveAction moveAction = platform.getCurrentAction();

                if (moveAction != null) {
                    remainingMilestones = moveAction.getRemainingMilestones();
                    remainingPath = moveAction.getRemainingPath();
                    actionStatus = moveAction.getStatus();
                }

            } catch (Exception e) {
                onRequestError(e);
                return;
            }

            EventBus.getDefault().postSticky(new RemainingMilestonesGetEvent(remainingMilestones));
            EventBus.getDefault().postSticky(new RemainingPathGetEvent(remainingPath));
            EventBus.getDefault().postSticky(new ActionStatusGetEvent(actionStatus));
        }
    }

    private class TaskgetCompositeMap implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
            }

            if (platform == null) {
                return;
            }

            CompositeMap compositeMap = null;
            try {
                compositeMap = platform.getCompositeMap();
            } catch (Exception e) {
                onRequestError(e);
            }

            EventBus.getDefault().postSticky(new GetCompositeMapEvent(compositeMap));
        }
    }

    private class TaskSetDeviceSpeed implements Runnable {
        private String speed;

        public TaskSetDeviceSpeed() {
        }

        public void setSpeed(String speed) {
            this.speed = speed;
        }

        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
            }

            if (platform == null) {
                return;
            }

            try {
                platform.setSystemParameter(SYSPARAM_ROBOT_SPEED, speed);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            EventBus.getDefault().postSticky(new UpdateSpeedEvent(speed));
            AppSharePreference.getAppSharedPreference().saveDeviceSpeed(speed);
        }
    }

    private class TaskClearMap implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
            }

            if (platform == null) {
                return;
            }

            try {
                mRobotPlatform.clearMap();
                mRobotPlatform.setPose(new Pose(0, 0, 0, 0, 0, 0));
//                EventBus.getDefault().post(new ClearMapSuccessEvent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class TaskAddVwalls implements Runnable {
        private List<Line> lineList;

        public TaskAddVwalls() {
        }

        public void setVwalls(List<Line> lineList) {
            this.lineList = lineList;
        }

        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
            }

            if (platform == null) {
                return;
            }

            try {
                platform.addWalls(lineList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            EventBus.getDefault().post(new AddVwalls());
        }
    }

    private class TaskAddVtracks implements Runnable {
        private List<Line> lineList;

        public TaskAddVtracks() {
        }

        public void setVwalls(List<Line> lineList) {
            this.lineList = lineList;
        }

        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
            }

            if (platform == null) {
                return;
            }

            try {
                platform.addLines(ArtifactUsageVirtualTrack, lineList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            EventBus.getDefault().post(new AddVtracks());
        }
    }

    private class TaskAddVwall implements Runnable {
        private Line line;
        private int typeAddVirtualWall;

        public TaskAddVwall() {
        }

        public void setVwall(Line line, int typeAddVirtualWall) {
            this.line = line;
            this.typeAddVirtualWall = typeAddVirtualWall;
        }

        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
            }

            if (platform == null) {
                return;
            }

            try {
                platform.addWall(line);
            } catch (Exception e) {
                e.printStackTrace();
            }
            EventBus.getDefault().post(new AddWall(typeAddVirtualWall));
        }
    }

    private class TaskAddVtrack implements Runnable {
        private Line line;
        private int typeAddVirtualTrack;

        public TaskAddVtrack() {
        }

        public void setVtrack(Line line, int typeAddVirtualTrack) {
            this.line = line;
            this.typeAddVirtualTrack = typeAddVirtualTrack;
        }

        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
            }

            if (platform == null) {
                return;
            }

            try {
                platform.addLine(ArtifactUsageVirtualTrack, line);
            } catch (Exception e) {
                e.printStackTrace();
            }
            EventBus.getDefault().post(new AddTrack(typeAddVirtualTrack));
        }
    }

    private class TaskClearAllVwalls implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
            }

            if (platform == null) {
                return;
            }

            try {
                platform.clearWalls();
            } catch (Exception e) {
                e.printStackTrace();
            }
            EventBus.getDefault().post(new ClearAllVwalls());
        }
    }

    private class TaskClearAllVtracks implements Runnable {
        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
            }

            if (platform == null) {
                return;
            }

            try {
                platform.clearLines(ArtifactUsageVirtualTrack);
            } catch (Exception e) {
                e.printStackTrace();
            }
            EventBus.getDefault().post(new ClearAllVtracks());
        }
    }

    private class TaskRemoveOneVwalls implements Runnable {
        private Line line;
        private int type;

        public TaskRemoveOneVwalls() {
        }

        public void setLine(Line line) {
            this.line = line;
        }
        public void setType(int type) {
            this.type = type;
        }

        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
            }

            if (platform == null) {
                return;
            }

            try {
                platform.clearWallById(line.getSegmentId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            EventBus.getDefault().post(new DeleteOneVwallCompleteEvent(line, type));
        }
    }

    private class TaskRemoveOneVtracks implements Runnable {
        private Line line;
        private int type;

        public TaskRemoveOneVtracks() {
        }

        public void setLine(Line line) {
            this.line = line;
        }
        public void setType(int type) {
            this.type = type;
        }

        @Override
        public void run() {
            AbstractSlamwarePlatform platform;
            synchronized (this) {
                platform = mRobotPlatform;
            }

            if (platform == null) {
                return;
            }

            try {
                platform.removeLineById(ArtifactUsageVirtualTrack, line.getSegmentId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            EventBus.getDefault().post(new DeleteOneVtrackCompleteEvent(line, type));
        }
    }
}
