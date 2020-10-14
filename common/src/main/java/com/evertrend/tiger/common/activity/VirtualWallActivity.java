package com.evertrend.tiger.common.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.evertrend.tiger.common.R;
import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.bean.RobotAction;
import com.evertrend.tiger.common.bean.event.DialogChoiceEvent;
import com.evertrend.tiger.common.bean.event.ServerMsgEvent;
import com.evertrend.tiger.common.bean.event.map.AddVirtualWall;
import com.evertrend.tiger.common.bean.event.map.ClearVirtualWalls;
import com.evertrend.tiger.common.bean.event.slamtec.ConnectedEvent;
import com.evertrend.tiger.common.bean.event.slamtec.ConnectionLostEvent;
import com.evertrend.tiger.common.bean.mapview.MapView;
import com.evertrend.tiger.common.bean.mapview.utils.SlamGestureDetector;
import com.evertrend.tiger.common.utils.EvertrendAgent;
import com.evertrend.tiger.common.utils.general.CommonConstants;
import com.evertrend.tiger.common.utils.general.DialogUtil;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.common.utils.general.Utils;
import com.slamtec.slamware.geometry.Line;
import com.slamtec.slamware.geometry.PointF;
import com.slamtec.slamware.geometry.Size;
import com.slamtec.slamware.robot.Map;
import com.slamtec.slamware.robot.Pose;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VirtualWallActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    public static final String TAG = VirtualWallActivity.class.getCanonicalName();

    private MapView mv_map;
    private RadioGroup rg_navigation;

    private EvertrendAgent mAgent;
    private String ip = "192.168.0.129";
    private Device device;

    private List<Line> clearLines = new ArrayList<>();

    private Runnable mRobotStateUpdateRunnable = new Runnable() {

        @Override
        public void run() {
            int count = 0;
            while (true) {
                if (mRobotStateUpdateRunnable == null || mRobotStateUpdateThread == null || !mRobotStateUpdateThread.isAlive() || mRobotStateUpdateThread.isInterrupted()) {
                    break;
                }

                if ((count % 5000) == 0) {//250秒
                    mAgent.getMap(RobotAction.CMD.GET_MAP);
//                    mAgent.getMap(RobotAction.CMD.GET_MAP_CONDENSE);
//                    mAgent.getMap(RobotAction.CMD.GET_MAP_CON_BIN);
                }
                if ((count % 10) == 0) {
                    mAgent.getWalls();
                }

                SystemClock.sleep(50);
                count++;
            }
        }
    };
    Thread mRobotStateUpdateThread = new Thread(mRobotStateUpdateRunnable);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yl_common_activity_virtual_wall);
        initView();
        device = (Device) getIntent().getSerializableExtra("device");
        ip = getIntent().getStringExtra("ip");
        EventBus.getDefault().register(this);
        mAgent = getEvertrendAgent();
//        mAgent.connectTo(ip, device.getDevice_id(), "1993");
        startUpdate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopUpdate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mv_map.setGestureMode(SlamGestureDetector.MODE_NONE);
//        mAgent.disconnect();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ConnectedEvent event) {
        LogUtil.d(TAG, "ConnectedEvent");
        startUpdate();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ConnectionLostEvent event) {
        LogUtil.d(TAG, "connect lost");
        DialogUtil.showToast(this, "connect lost", Toast.LENGTH_SHORT);
        mAgent.disconnect();
        stopUpdate();
        SystemClock.sleep(1000);
        mAgent.connectTo(ip, device.getDevice_id(), "1993");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(AddVirtualWall event) {
        Line vWall = event.getWall();
        PointF startF = mv_map.widgetCoordinateToMapCoordinate(vWall.getStartX(), vWall.getStartY());
        PointF endF = mv_map.widgetCoordinateToMapCoordinate(vWall.getEndX(), vWall.getEndY());
        mAgent.addVwall(new Line(startF, endF));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ClearVirtualWalls event) {
        DialogUtil.showChoiceDialog(this, "确定要删除吗?", CommonConstants.TYPE_DELETE_VIRTUAL_WALL);
        clearLines = event.getClearLines();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DialogChoiceEvent event) {
        if (event.getType() == CommonConstants.TYPE_DELETE_VIRTUAL_WALL) {
            for (Line line : clearLines) {
                mAgent.clearOneVwall(line);
                SystemClock.sleep(200);
            }
        }
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
                    case RobotAction.CMD.GET_VIRTUAL_WALL:
                        updateVWalls(jsonObject.getJSONArray(RobotAction.DATA));
                        break;
                }

            } else {
                LogUtil.d(TAG, "fail: "+jsonObject.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateVWalls(JSONArray jsonArray) throws JSONException {
//        LogUtil.d(TAG, "walls: "+jsonArray.toString());
        List<Line> lines = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            String id = object.keys().next();
            JSONArray array = object.getJSONArray(id);
            Line line = new Line(Integer.valueOf(id), new PointF((float) array.getDouble(0), (float) array.getDouble(1)),
                    new PointF((float) array.getDouble(2), (float) array.getDouble(3)));
            lines.add(line);
        }
        mv_map.setVwalls(lines);
    }

    private void updateMap(JSONObject jsonObject, boolean isCompress) throws JSONException {
//        LogUtil.d(TAG, "updateMap: "+jsonObject.toString());
        PointF origin = new PointF((float)jsonObject.getDouble(RobotAction.ORIGIN_X), (float)jsonObject.getDouble(RobotAction.ORIGIN_Y));
        Size dimension = new Size(jsonObject.getInt(RobotAction.WIDTH), jsonObject.getInt(RobotAction.HEIGHT));
        PointF resolution = new PointF((float)jsonObject.getDouble(RobotAction.RESOLUTION), (float)jsonObject.getDouble(RobotAction.RESOLUTION));
        long timestamp = System.currentTimeMillis();
        byte[] data = null;
        if (isCompress) {
            data = Utils.hexStringToByte(Utils.decompress(jsonObject.getString(RobotAction.DATA)));
        } else {
            data = Utils.hexStringToByte(jsonObject.getString(RobotAction.DATA));
        }
        Map map = new Map(origin, dimension, resolution, timestamp, data);
        mv_map.setMap(map);
    }

    private void initView() {
        mv_map = findViewById(R.id.mv_map);
        mv_map.setEvertrend(true);
        rg_navigation = findViewById(R.id.rg_navigation);
        rg_navigation.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int i) {
        if (group.getCheckedRadioButtonId() == R.id.tab_clear_all) {
            mv_map.setGestureMode(SlamGestureDetector.MODE_NONE);
//            mv_map.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, mv_map.getLeft()+5, mv_map.getTop()+5, 0));
//            mv_map.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, mv_map.getLeft()+5, mv_map.getTop()+5, 0));
            mAgent.clearAllVwalls();
        } else if (group.getCheckedRadioButtonId() == R.id.tab_area_clear) {
            mv_map.setGestureMode(SlamGestureDetector.MODE_AREA_CLEAR);
        } else if (group.getCheckedRadioButtonId() == R.id.tab_map_add_wall) {
            mv_map.setGestureMode(SlamGestureDetector.MODE_VIRTUAL_WALL);
        } else if (group.getCheckedRadioButtonId() == R.id.tab_map_save) {
            mv_map.setGestureMode(SlamGestureDetector.MODE_NONE);
            finish();
        } else {
            mv_map.setGestureMode(SlamGestureDetector.MODE_NONE);
            mv_map.setVwalls(null);
        }
    }

    private void startUpdate() {
        mRobotStateUpdateThread = new Thread(mRobotStateUpdateRunnable);
        mRobotStateUpdateThread.start();
    }

    private void stopUpdate() {
        if (mRobotStateUpdateThread != null && !mRobotStateUpdateThread.isInterrupted()) {
            mRobotStateUpdateThread.interrupt();
            mRobotStateUpdateThread = null;
        }
    }
}