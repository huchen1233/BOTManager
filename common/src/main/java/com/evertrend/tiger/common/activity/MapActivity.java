package com.evertrend.tiger.common.activity;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.evertrend.tiger.common.R;
import com.evertrend.tiger.common.bean.RobotAction;
import com.evertrend.tiger.common.bean.event.ServerMsgEvent;
import com.evertrend.tiger.common.bean.event.slamtec.ConnectedEvent;
import com.evertrend.tiger.common.bean.event.slamtec.ConnectionLostEvent;
import com.evertrend.tiger.common.bean.mapview.MapView;
import com.evertrend.tiger.common.bean.mapview.utils.RadianUtil;
import com.evertrend.tiger.common.utils.EvertrendAgent;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.common.utils.general.Utils;
import com.evertrend.tiger.common.widget.ActionControllerView;
import com.slamtec.slamware.geometry.PointF;
import com.slamtec.slamware.geometry.Size;
import com.slamtec.slamware.robot.LaserScan;
import com.slamtec.slamware.robot.Location;
import com.slamtec.slamware.robot.Map;
import com.slamtec.slamware.robot.Pose;
import com.slamtec.slamware.robot.Rotation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

public class MapActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ActionControllerView.LongClickRepeatListener {
    public static final String TAG = MapActivity.class.getCanonicalName();

    private MapView mv_map;
    private RadioGroup rg_navigation;
    private LinearLayout ll_set_spot, ll_edit;
    private ConstraintLayout cl_action;
    private ActionControllerView acv_action;
    private TextView tv_robot_pose;

    private EvertrendAgent mAgent;
    private Pose robotPose;
    private String currentPose = "0";
    private Intent intent;

    private float speed = 0f;
    private static final String IP = "192.168.0.129";

    private Runnable mRobotStateUpdateRunnable = new Runnable() {
        int cnt;

        @Override
        public void run() {
            cnt = 0;
//            mAgent.getGetRobotInfo();

            while (true) {
                if (mRobotStateUpdateRunnable == null || !mRobotStateUpdateThread.isAlive() || mRobotStateUpdateThread.isInterrupted()) {
                    break;
                }

                if ((cnt % 3) == 0) {
//                    mAgent.getRobotPose();
//                    mAgent.getLaserScan();
//                    mAgent.setDeviceSpeed(AppSharePreference.getAppSharedPreference().loadDeviceSpeed());
                }

                if ((cnt % 20) == 0) {
//                    mAgent.getMap(RobotAction.CMD.GET_MAP_CON_BIN);
//                    mAgent.getMoveAction();
                }

                if ((cnt % 30) == 0) {
//                    mAgent.getHomePose();
                }
//                mAgent.getMap(RobotAction.CMD.GET_MAP);
                mAgent.getMap(RobotAction.CMD.GET_MAP_CONDENSE);
//                mAgent.getMap(RobotAction.CMD.GET_MAP_CON_BIN);
                SystemClock.sleep(1000);
                mAgent.getRobotPose();
                SystemClock.sleep(200);
                mAgent.getLaserScan();
                SystemClock.sleep(200);
//                SystemClock.sleep(5000);
                cnt++;
            }
        }
    };
    Thread mRobotStateUpdateThread = new Thread(mRobotStateUpdateRunnable);

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yl_common_activity_map);
        initView();
        EventBus.getDefault().register(this);
        mAgent = getEvertrendAgent();
        mAgent.connectTo(IP);
    }

        @Override
    protected void onPause() {
        super.onPause();
        stopUpdate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startUpdate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAgent.disconnect();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ConnectedEvent event) {
        LogUtil.d(TAG, "ConnectedEvent");
//        mAgent.getMap();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ConnectionLostEvent event) {
        LogUtil.d(TAG, "connect lost");
        mAgent.connectTo(IP);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ServerMsgEvent event) {
        try {
            JSONObject jsonObject = new JSONObject(event.getMsg());
//            LogUtil.d(TAG, "json: "+jsonObject.toString());
            if (jsonObject.getInt(RobotAction.RESULT_CODE) == 0) {
                switch (jsonObject.getInt(RobotAction.CMD_CODE)) {
                    case RobotAction.CMD.GET_MAP:
                        updateMap(jsonObject.getJSONObject(RobotAction.DATA), false);
                        break;
                    case RobotAction.CMD.GET_MAP_CONDENSE:
                    case RobotAction.CMD.GET_MAP_CON_BIN:
                        updateMap(jsonObject.getJSONObject(RobotAction.DATA), true);
                        break;
                    case RobotAction.CMD.GET_LASER_SCAN:
                        updateLaserScan(jsonObject.getJSONObject(RobotAction.DATA));
                        break;
                    case RobotAction.CMD.GET_ROBOT_POSE:
                        updateRobotPose(jsonObject.getJSONObject(RobotAction.DATA));
                        break;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Map map = event.getMap();
//        mv_map.setMap(map);
    }

    private void updateRobotPose(JSONObject jsonObject) throws JSONException {
//        LogUtil.d(TAG, "updateRobotPose: "+jsonObject.toString());
        Location location = new Location();
        location.setX((float)jsonObject.getDouble(RobotAction.POSE_X));
        location.setY((float)jsonObject.getDouble(RobotAction.POSE_Y));
        Rotation rotation = new Rotation((float)jsonObject.getDouble(RobotAction.POSE_YAW));
        robotPose = new Pose(location, rotation);
        mv_map.setRobotPose(robotPose);
        if (robotPose != null) {
            currentPose = String.format("%.3f,%.3f,%.3f", robotPose.getX(), robotPose.getY(), RadianUtil.toAngel(robotPose.getYaw()));
            tv_robot_pose.setText(currentPose);
        }
    }

    private void updateLaserScan(JSONObject jsonObject) throws JSONException {
        LaserScan laserScan = new LaserScan(Utils.toLaserPoint(jsonObject), robotPose);
        mv_map.setLaserScan(laserScan);
    }

    private void updateMap(JSONObject jsonObject, boolean isCompress) throws JSONException {
//        LogUtil.d(TAG, "updateMap: "+jsonObject.toString());
        PointF origin = new PointF((float)jsonObject.getDouble(RobotAction.ORIGIN_X), (float)jsonObject.getDouble(RobotAction.ORIGIN_Y));
        Size dimension = new Size(jsonObject.getInt(RobotAction.WIDTH), jsonObject.getInt(RobotAction.HEIGHT));
//        PointF resolution = new PointF(0.05f, 0.05f);
        PointF resolution = new PointF((float)jsonObject.getDouble(RobotAction.RESOLUTION), (float)jsonObject.getDouble(RobotAction.RESOLUTION));
        long timestamp = System.currentTimeMillis();
        byte[] data = null;
        if (isCompress) {
//            LogUtil.d(TAG, "time start: "+System.currentTimeMillis());
            data = Utils.hexStringToByte(Utils.decompress(jsonObject.getString(RobotAction.DATA)));
//            LogUtil.d(TAG, "time end: "+System.currentTimeMillis());
        } else {
            data = Utils.hexStringToByte(jsonObject.getString(RobotAction.DATA));
        }
//        LogUtil.d(TAG, "length: "+data.length);
        Map map = new Map(origin, dimension, resolution, timestamp, data);
//        LogUtil.d(TAG, "getDimension: "+map.getDimension().getWidth()+","+map.getDimension().getHeight());
//        LogUtil.d(TAG, "getOrigin: "+map.getOrigin().getX()+","+map.getOrigin().getY());
//        LogUtil.d(TAG, "getResolution: "+map.getResolution().getX()+","+map.getResolution().getY());
//        LogUtil.d(TAG, "getTimestamp: "+map.getTimestamp());
//        LogUtil.d(TAG, "getMapArea: "+map.getMapArea());
//        LogUtil.d(TAG, "data: "+ Arrays.toString(data));
        mv_map.setMap(map);
    }

    private void initView() {
        mv_map = findViewById(R.id.mv_map);
        rg_navigation = findViewById(R.id.rg_navigation);
        rg_navigation.setOnCheckedChangeListener(this);
        ll_set_spot = findViewById(R.id.ll_set_spot);
        cl_action = findViewById(R.id.cl_action);
        ll_edit = findViewById(R.id.ll_edit);
        acv_action = findViewById(R.id.acv_action);
        acv_action.setLongClickRepeatListener(this);
        tv_robot_pose = findViewById(R.id.tv_robot_pose);
    }

    private void startUpdate() {
        mRobotStateUpdateThread = new Thread(mRobotStateUpdateRunnable);
        mRobotStateUpdateThread.start();
    }

    private void stopUpdate() {
        if (mRobotStateUpdateThread != null && !mRobotStateUpdateThread.isInterrupted()) {
            mRobotStateUpdateThread.interrupt();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.getCheckedRadioButtonId() == R.id.tab_spot) {
            ll_set_spot.setVisibility(View.VISIBLE);
            cl_action.setVisibility(View.GONE);
            ll_edit.setVisibility(View.GONE);
        } else if (group.getCheckedRadioButtonId() == R.id.tab_action) {
            ll_set_spot.setVisibility(View.GONE);
            cl_action.setVisibility(View.VISIBLE);
            ll_edit.setVisibility(View.GONE);
        } else if (group.getCheckedRadioButtonId() == R.id.tab_map_edit) {
            ll_set_spot.setVisibility(View.GONE);
            cl_action.setVisibility(View.GONE);
            ll_edit.setVisibility(View.VISIBLE);
        } else {
            ll_set_spot.setVisibility(View.GONE);
            cl_action.setVisibility(View.GONE);
            ll_edit.setVisibility(View.GONE);
        }
    }

    @Override
    public void repeatAction(View view, int what) {
        if (view.getId() == R.id.acv_action) {
            switch (what) {
                case ActionControllerView.TouchArea.LEFT:
                    mAgent.moveBy(RobotAction.CMD.TURN_LEFT, speed);
                    break;
                case ActionControllerView.TouchArea.TOP:
                    mAgent.moveBy(RobotAction.CMD.FORWARD, speed);
                    break;
                case ActionControllerView.TouchArea.RIGHT:
                    mAgent.moveBy(RobotAction.CMD.TURN_RIGHT, -speed);
                    break;
                case ActionControllerView.TouchArea.BOTTOM:
                    mAgent.moveBy(RobotAction.CMD.BACKWARD, -speed);
                    break;
                case ActionControllerView.TouchArea.CENTER:
                    mAgent.cancelAllActions();
                    break;
            }
        }
    }
}