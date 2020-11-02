package com.evertrend.tiger.common.activity;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.evertrend.tiger.common.R;
import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.bean.MapPages;
import com.evertrend.tiger.common.bean.Robot;
import com.evertrend.tiger.common.bean.RobotAction;
import com.evertrend.tiger.common.bean.event.SaveMapPageEvent;
import com.evertrend.tiger.common.bean.event.ServerMsgEvent;
import com.evertrend.tiger.common.bean.event.map.AddNavigationLocation;
import com.evertrend.tiger.common.bean.event.map.RelocationOrSetCurrentMapEvent;
import com.evertrend.tiger.common.bean.event.slamtec.ConnectedEvent;
import com.evertrend.tiger.common.bean.event.slamtec.ConnectionLostEvent;
import com.evertrend.tiger.common.bean.event.uploadPathPicFailEvent;
import com.evertrend.tiger.common.bean.event.uploadPathPicSuccessEvent;
import com.evertrend.tiger.common.bean.mapview.MapView;
import com.evertrend.tiger.common.bean.mapview.utils.RadianUtil;
import com.evertrend.tiger.common.bean.mapview.utils.SlamGestureDetector;
import com.evertrend.tiger.common.utils.EvertrendAgent;
import com.evertrend.tiger.common.utils.general.AppSharePreference;
import com.evertrend.tiger.common.utils.general.CommonConstants;
import com.evertrend.tiger.common.utils.general.DialogUtil;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.common.utils.general.Utils;
import com.evertrend.tiger.common.utils.network.CommTaskUtils;
import com.evertrend.tiger.common.widget.ActionControllerView;
import com.evertrend.tiger.common.widget.MapBottomPopupView;
import com.evertrend.tiger.common.widget.MapSettingsBottomPopupView;
import com.lxj.xpopup.XPopup;
import com.slamtec.slamware.action.Path;
import com.slamtec.slamware.geometry.Line;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.graphics.Bitmap.Config.ARGB_8888;

public class MapActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ActionControllerView.LongClickRepeatListener, View.OnClickListener {
    public static final String TAG = MapActivity.class.getCanonicalName();

    private Toolbar tb_map;
    private MapView mv_map;
    private RadioGroup rg_navigation;
    private LinearLayout ll_set_spot, ll_edit;
    private ConstraintLayout cl_action;
    private ActionControllerView acv_action;
    private TextView tv_robot_pose;
    private Button btn_virtual_walls;
    private Button btn_virtual_tracks;

    private EvertrendAgent mAgent;
    private Pose robotPose;
    private String currentPose = "0";
    private Intent intent;
    private String ip = "192.168.0.129";
    private Device device;
    private MapPages mapPages;
    private boolean saveMapPic = false;

    private float speed = 0.7f;
//    private static final String IP = "192.168.0.129";
    private MapBottomPopupView mapBottomPopupView;

    private ScheduledThreadPoolExecutor scheduledThreadUploadMapPic;
    private ScheduledThreadPoolExecutor scheduledThreadRelocationMapPages;

    private Runnable mRobotStateUpdateRunnable = new Runnable() {

        @Override
        public void run() {
            int count = 0;
//            mAgent.getGetRobotInfo();

            while (true) {
                if (mRobotStateUpdateRunnable == null || mRobotStateUpdateThread == null || !mRobotStateUpdateThread.isAlive() || mRobotStateUpdateThread.isInterrupted()) {
                    break;
                }

                if ((count % 5) == 0) {
//                    mAgent.getLaserScan();
//                    mAgent.setDeviceSpeed(AppSharePreference.getAppSharedPreference().loadDeviceSpeed());
                }
                if ((count % 10) == 0) {
//                    mAgent.getRobotPose();
                }

                if ((count % 40) == 0) {
//                    mAgent.getMap(RobotAction.CMD.GET_MAP);
//                mAgent.getMap(RobotAction.CMD.GET_MAP_CONDENSE);
//                mAgent.getMap(RobotAction.CMD.GET_MAP_CON_BIN);
//                    mAgent.getMoveAction();
                }

                if ((count % 30) == 0) {
//                    mAgent.getHomePose();
                }
//                mAgent.getMap(RobotAction.CMD.GET_MAP);
//                mAgent.getMap(RobotAction.CMD.GET_MAP_CONDENSE);
                mAgent.getMap(RobotAction.CMD.GET_MAP_CON_BIN);
                SystemClock.sleep(1000);
                mAgent.getRobotPose();
                SystemClock.sleep(200);
                mAgent.getLaserScan();
                SystemClock.sleep(200);
                mAgent.getWalls();
                SystemClock.sleep(200);
                mAgent.getTracks();
                SystemClock.sleep(200);
                mAgent.getNavigationPathPlanning();
                SystemClock.sleep(200);
//                SystemClock.sleep(5000);
//                SystemClock.sleep(50);
//                count++;
            }
        }
    };
    Thread mRobotStateUpdateThread = new Thread(mRobotStateUpdateRunnable);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yl_common_activity_map);
        device = (Device) getIntent().getSerializableExtra("device");
        mapPages = (MapPages) getIntent().getSerializableExtra("mappage");
        initView();
        ip = getIntent().getStringExtra("ip");
        EventBus.getDefault().register(this);
        mAgent = getEvertrendAgent();
        mAgent.connectTo(ip, device.getDevice_id(), "1993");
        startRelocationOrSetCurrentMapOrAutoRecordPath(CommonConstants.TYPE_SET_CURRENT_MAP, 1);
//        String str = Utils.decompress("FF04000564FF07");
//        LogUtil.d(TAG, "str: "+str);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.yl_common_map_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.item_settings) {
            new XPopup.Builder(this)
                    .asCustom(new MapSettingsBottomPopupView(this, device, mAgent, mv_map))
                    .show();
        } else if (item.getItemId() == R.id.item_save_map) {
            showEditDialog();
        } else if (item.getItemId() == R.id.item_set_centred) {
            mv_map.setCentred();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopUpdate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppSharePreference.getAppSharedPreference().saveMapTouchMode(SlamGestureDetector.MODE_NONE);
        mAgent.disconnect();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        stopUploadMapPicTimer();
        stopRelocationMapPagesTimer();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RelocationOrSetCurrentMapEvent messageEvent) {
        LogUtil.i(this, TAG, "===RelocationOrSetCurrentMapEvent===");
        stopRelocationMapPagesTimer();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SaveMapPageEvent event) {
        mapPages = event.getMapPages();
        saveMapPic = true;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(uploadPathPicSuccessEvent event) {
        stopUploadMapPicTimer();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(uploadPathPicFailEvent event) {
        stopUploadMapPicTimer();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ConnectedEvent event) {
        LogUtil.d(TAG, "ConnectedEvent");
        startUpdate();
//        mAgent.getMap();
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
    public void onEventMainThread(AddNavigationLocation event) {
        moveToLocation(event.getX(), event.getY(), event.getAngle());
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
                        updateMap(jsonObject.getJSONObject(RobotAction.DATA), true);
                    case RobotAction.CMD.GET_MAP_CON_BIN:
                        updateMapBin(jsonObject.getJSONObject(RobotAction.DATA), true);
                        break;
                    case RobotAction.CMD.GET_LASER_SCAN:
                        updateLaserScan(jsonObject.getJSONObject(RobotAction.DATA));
                        break;
                    case RobotAction.CMD.GET_ROBOT_POSE:
                        updateRobotPose(jsonObject.getJSONObject(RobotAction.DATA));
                        break;
                    case RobotAction.CMD.GET_VIRTUAL_WALL:
                        updateVWalls(jsonObject.getJSONArray(RobotAction.DATA));
                        break;
                    case RobotAction.CMD.GET_NAVIGATION_PATH_PLANNING:
                        updateNavigationPathPlanning(jsonObject.getJSONArray(RobotAction.DATA));
                        break;
                }

            } else {
                LogUtil.d(TAG, "fail: "+jsonObject.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Map map = event.getMap();
//        mv_map.setMap(map);
    }

    private void updateMapBin(JSONObject jsonObject, boolean b) throws JSONException {
//        LogUtil.d(TAG, "updateMap: "+jsonObject.toString());
        PointF origin = new PointF((float)jsonObject.getDouble(RobotAction.ORIGIN_X), (float)jsonObject.getDouble(RobotAction.ORIGIN_Y));
        Size dimension = new Size(jsonObject.getInt(RobotAction.WIDTH), jsonObject.getInt(RobotAction.HEIGHT));
//        PointF resolution = new PointF(0.05f, 0.05f);
        PointF resolution = new PointF((float)jsonObject.getDouble(RobotAction.RESOLUTION), (float)jsonObject.getDouble(RobotAction.RESOLUTION));
        long timestamp = System.currentTimeMillis();
        byte[] data = null;
        long startT = System.currentTimeMillis();
        byte[] test = Base64.decode(jsonObject.getString(RobotAction.DATA), Base64.DEFAULT);
//        LogUtil.d(TAG, "data length: "+test.length);
        data = Utils.decompress(test);
        LogUtil.d(TAG, "time all: "+(System.currentTimeMillis() - startT));
//        for (byte b : data) {
//            LogUtil.d(TAG, "b: "+b);
//        }
//        LogUtil.d(TAG, "data length: "+data.length);
        Map map = new Map(origin, dimension, resolution, timestamp, data);
//        LogUtil.d(TAG, "getDimension: "+map.getDimension().getWidth()+","+map.getDimension().getHeight());
//        LogUtil.d(TAG, "getOrigin: "+map.getOrigin().getX()+","+map.getOrigin().getY());
//        LogUtil.d(TAG, "getResolution: "+map.getResolution().getX()+","+map.getResolution().getY());
        mv_map.setMap(map);
    }

    private void showEditDialog() {
        mapBottomPopupView = new MapBottomPopupView(this, device, mapPages);
        new XPopup.Builder(this)
                .autoOpenSoftInput(true)
                .asCustom(mapBottomPopupView)
                .show();
    }

    private void updateNavigationPathPlanning(JSONArray jsonArray) throws JSONException {
//        LogUtil.d(TAG, "planning: "+jsonArray.toString());
        Vector<Location> points = new Vector(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++){
            JSONArray poinJA = jsonArray.getJSONArray(i);
            Location location = new Location((float) poinJA.getDouble(0), (float) poinJA.getDouble(1), 0f);
            points.add(location);
        }
        Path planningingPath = new Path(points);
        mv_map.setRemainingPath(planningingPath);
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

    private void updateRobotPose(JSONObject jsonObject) throws JSONException {
//        LogUtil.d(TAG, "updateRobotPose: "+jsonObject.toString());
        robotPose = Utils.toPose(jsonObject);
        mv_map.setRobotPose(robotPose, Utils.toRobot(jsonObject));
        if (robotPose != null) {
            currentPose = String.format("%.3f,%.3f,%.3f", robotPose.getX(), robotPose.getY(), RadianUtil.toAngel(robotPose.getYaw()));
//            currentPose = String.format("%.3f,%.3f,%.3f", robotPose.getX(), robotPose.getY(), robotPose.getYaw());
            tv_robot_pose.setText(currentPose);
        }
    }

    private void updateLaserScan(JSONObject jsonObject) throws JSONException {
        LaserScan laserScan = new LaserScan(Utils.toLaserPoint(jsonObject), Utils.toPose(jsonObject));
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
        //1、decompress解压缩时间700-900毫秒，需要继续优化；
        //2、新修改decompress需要400-550毫秒，随着地图增大，时间也会变长，需要继续优化；
        //3、新修改multiThreadDecompress需要100-200毫秒，随着地图增大，开启更多线程，时间维持100-200毫秒，需要注意线程回收；
        //multiThreadDecompress开启过多线程，出现Background partial concurrent mark sweep GC freed 30280(1214KB) AllocSpace objects, 27(6MB) LOS objects, 9% free, 38MB/42MB, paused 1.280ms total 116.615ms
        if (isCompress) {
            long startT = System.currentTimeMillis();
            data = Utils.hexStringToByte(Utils.multiThreadDecompress(jsonObject.getString(RobotAction.DATA)));
            LogUtil.d(TAG, "time all: "+(System.currentTimeMillis() - startT));
        } else {//20毫秒左右，地图越大时间越长，
//            LogUtil.d(TAG, "length: "+jsonObject.getString(RobotAction.DATA).length());
            long startT = System.currentTimeMillis();
            data = Utils.hexStringToByte(jsonObject.getString(RobotAction.DATA));
            LogUtil.d(TAG, "time all: "+(System.currentTimeMillis() - startT));
        }
//        for (byte b : data) {
//            LogUtil.d(TAG, "b: "+b);
//        }
//        LogUtil.d(TAG, "length: "+data.length);
        Map map = new Map(origin, dimension, resolution, timestamp, data);
//        LogUtil.d(TAG, "getDimension: "+map.getDimension().getWidth()+","+map.getDimension().getHeight());
//        LogUtil.d(TAG, "getOrigin: "+map.getOrigin().getX()+","+map.getOrigin().getY());
//        LogUtil.d(TAG, "getResolution: "+map.getResolution().getX()+","+map.getResolution().getY());
//        LogUtil.d(TAG, "getTimestamp: "+map.getTimestamp());
//        LogUtil.d(TAG, "getMapArea: "+map.getMapArea());
//        LogUtil.d(TAG, "data: "+ Arrays.toString(data));
        mv_map.setMap(map);
        if (saveMapPic) {
            saveMapPic = false;
            saveMapPic(map);
        }
    }

    private void saveMapPic(Map map) {
        int mapWidth =0;
        int mapHeight = 0;
        mapWidth = map.getDimension().getWidth();
        mapHeight = map.getDimension().getHeight();
//        LogUtil.d(TAG, "mapWidth:"+mapWidth);
//        LogUtil.d(TAG, "mapHeight:"+mapHeight);
        Bitmap bitmap = Bitmap.createBitmap(mapWidth, mapHeight, ARGB_8888);
        for (int posY = 0; posY < mapHeight; ++posY) {
            for (int posX = 0; posX < mapWidth; ++posX) {
                // get map pixel
                byte[] data = map.getData();
                // (-128, 127) to (0, 255)
                int rawColor = data[posX + posY * mapWidth];
                rawColor += 128;
                // fill the bitmap data, by data of B/G/R/A
                bitmap.setPixel(posX, posY, rawColor | rawColor<<8 | rawColor<<16 | 0xC0<<24);
            }
        }

//        LogUtil.d(TAG, "path: "+ this.getFilesDir());
        boolean saveStatus = false;
        try {
            saveStatus = Utils.saveBitmap(bitmap, String.valueOf(this.getFilesDir()), mapPages.getName()+"_"+mapPages.getId()+".png");
            LogUtil.d(TAG, "saveStatus: "+saveStatus);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (saveStatus) {
            uploadMapPic();
        } else {
//            EventBus.getDefault().post(new uploadPathPicFailEvent());
        }
    }

    private void initView() {
        tb_map = findViewById(R.id.tb_map);
        tb_map.setTitle(mapPages.getName());
        //设置左侧导航图标
        tb_map.setNavigationIcon(R.drawable.yl_common_ic_arrow_back_white_36dp);
        setSupportActionBar(tb_map);
        //返回按钮的监听
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mv_map = findViewById(R.id.mv_map);
        mv_map.setEvertrend(true);
        mv_map.setSingleTapListener(new MapView.ISingleTapListener() {
            @Override
            public void onSingleTapListener(MotionEvent event) {
//                moveToLocation(event.getX(), event.getY());
            }
        });
        mv_map.drawZeroAxis();
        mv_map.setGestureMode(AppSharePreference.getAppSharedPreference().loadMapTouchMode());
        rg_navigation = findViewById(R.id.rg_navigation);
        rg_navigation.setOnCheckedChangeListener(this);
        ll_set_spot = findViewById(R.id.ll_set_spot);
        cl_action = findViewById(R.id.cl_action);
        ll_edit = findViewById(R.id.ll_edit);
        acv_action = findViewById(R.id.acv_action);
        acv_action.setLongClickRepeatListener(this);
        tv_robot_pose = findViewById(R.id.tv_robot_pose);
        btn_virtual_walls = findViewById(R.id.btn_virtual_walls);
        btn_virtual_walls.setOnClickListener(this);
        btn_virtual_tracks = findViewById(R.id.btn_virtual_tracks);
        btn_virtual_tracks.setOnClickListener(this);
    }

    private void moveToLocation(float x, float y) {
        PointF target = mv_map.widgetCoordinateToMapCoordinate(x, y);
        if (target == null) return;
        Location location = new Location(target.getX(), target.getY(), 0);
        Pose moveToPose = new Pose(location, robotPose.getRotation());
//        LogUtil.d(TAG, "X: "+moveToPose.getX());
//        LogUtil.d(TAG, "Y: "+moveToPose.getY());
//        LogUtil.d(TAG, "yaw: "+moveToPose.getYaw());
        mAgent.moveTo(moveToPose);
    }

    private void moveToLocation(float x, float y, float radians) {
        PointF target = mv_map.widgetCoordinateToMapCoordinate(x, y);
        if (target == null) return;
        Location location = new Location(target.getX(), target.getY(), 0);
//        Location location = new Location(robotPose.getX(), robotPose.getY(), 0);//设备所在点旋转，测试方向用
        Rotation rotation = new Rotation(radians);
        Pose moveToPose = new Pose(location, rotation);
        LogUtil.d(TAG, "X: "+moveToPose.getX());
        LogUtil.d(TAG, "Y: "+moveToPose.getY());
        LogUtil.d(TAG, "yaw: "+moveToPose.getYaw());
        mAgent.moveTo(moveToPose);
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_virtual_walls) {
            Intent intent = new Intent();
            intent.putExtra("ip", ip);
            intent.putExtra("device", device);
            intent.setAction("android.intent.action.VirtualWallActivity");
            startActivity(intent);
        } else if (view.getId() == R.id.btn_virtual_tracks) {
            Intent intent = new Intent();
            intent.putExtra("ip", ip);
            intent.putExtra("device", device);
            intent.setAction("android.intent.action.VirtualTrackActivity");
            startActivity(intent);
        }
    }

    private void startRelocationOrSetCurrentMapOrAutoRecordPath(int type, int status) {
        if (scheduledThreadRelocationMapPages == null) scheduledThreadRelocationMapPages = new ScheduledThreadPoolExecutor(4);
        scheduledThreadRelocationMapPages.scheduleAtFixedRate(new CommTaskUtils.TaskRelocationOrSetCurrentMap(device, mapPages, type, status),
                0, 5, TimeUnit.SECONDS);
    }

    private void uploadMapPic() {
        scheduledThreadUploadMapPic = new ScheduledThreadPoolExecutor(3);
        scheduledThreadUploadMapPic.scheduleAtFixedRate(new CommTaskUtils.TaskUploadMapPic(this, mapPages),
                0, 6, TimeUnit.SECONDS);
    }

    private void stopRelocationMapPagesTimer() {
        if (scheduledThreadRelocationMapPages != null) {
            scheduledThreadRelocationMapPages.shutdownNow();
            scheduledThreadRelocationMapPages = null;
        }
    }

    private void stopUploadMapPicTimer() {
        if (scheduledThreadUploadMapPic != null) {
            scheduledThreadUploadMapPic.shutdownNow();
            scheduledThreadUploadMapPic = null;
        }
    }
}