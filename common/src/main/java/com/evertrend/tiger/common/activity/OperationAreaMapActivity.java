package com.evertrend.tiger.common.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evertrend.tiger.common.R;
import com.evertrend.tiger.common.adapter.BaseTraceAdapter;
import com.evertrend.tiger.common.adapter.MapPagesChoiceAdapter;
import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.bean.DeviceGrant;
import com.evertrend.tiger.common.bean.MapPages;
import com.evertrend.tiger.common.bean.RobotSpot;
import com.evertrend.tiger.common.bean.TracePath;
import com.evertrend.tiger.common.bean.VirtualTrackGroup;
import com.evertrend.tiger.common.bean.event.AutoRecordPathDistanceOKEvent;
import com.evertrend.tiger.common.bean.event.ChoiceMapPagesEvent;
import com.evertrend.tiger.common.bean.event.CreateNewBaseTraceSuccessEvent;
import com.evertrend.tiger.common.bean.event.DeviceMessageEvent;
import com.evertrend.tiger.common.bean.event.DialogChoiceEvent;
import com.evertrend.tiger.common.bean.event.GetAllMapPagesSuccessEvent;
import com.evertrend.tiger.common.bean.event.GetDeviceGrantSuccessEvent;
import com.evertrend.tiger.common.bean.event.SaveMapPageEvent;
import com.evertrend.tiger.common.bean.event.SaveTraceSpotFailEvent;
import com.evertrend.tiger.common.bean.event.SetPoseFailEvent;
import com.evertrend.tiger.common.bean.event.SetPoseOKEvent;
import com.evertrend.tiger.common.bean.event.map.AddTrack;
import com.evertrend.tiger.common.bean.event.map.AddVtracks;
import com.evertrend.tiger.common.bean.event.map.AddVwalls;
import com.evertrend.tiger.common.bean.event.map.AddWall;
import com.evertrend.tiger.common.bean.event.map.ChoiceMapPagesTracePathEvent;
import com.evertrend.tiger.common.bean.event.map.ClearAllVtracks;
import com.evertrend.tiger.common.bean.event.map.ClearAllVwalls;
import com.evertrend.tiger.common.bean.event.map.DeleteEvent;
import com.evertrend.tiger.common.bean.event.map.DeleteOneTraceSpotListEvent;
import com.evertrend.tiger.common.bean.event.map.DeleteOneVtrackCompleteEvent;
import com.evertrend.tiger.common.bean.event.map.DeleteOneVtrackEvent;
import com.evertrend.tiger.common.bean.event.map.DeleteOneVwallCompleteEvent;
import com.evertrend.tiger.common.bean.event.map.DeleteOneVwallEvent;
import com.evertrend.tiger.common.bean.event.map.DeleteTraceSpotListCompleteEvent;
import com.evertrend.tiger.common.bean.event.map.EditVtracksComplete;
import com.evertrend.tiger.common.bean.event.map.EditVwallsComplete;
import com.evertrend.tiger.common.bean.event.map.GetAllVirtualTrackGroupSuccessEvent;
import com.evertrend.tiger.common.bean.event.map.GetMapPagesAllPathSuccessEvent;
import com.evertrend.tiger.common.bean.event.map.GetSaveMapFlagEvent;
import com.evertrend.tiger.common.bean.event.map.GetTraceSpotEvent;
import com.evertrend.tiger.common.bean.event.map.MoveOneVtrackEvent;
import com.evertrend.tiger.common.bean.event.map.MoveOneVwallEvent;
import com.evertrend.tiger.common.bean.event.map.RelocationOrSetCurrentMapEvent;
import com.evertrend.tiger.common.bean.event.map.SaveTraceSpotEvent;
import com.evertrend.tiger.common.bean.event.map.SaveTraceSpotListCompleteEvent;
import com.evertrend.tiger.common.bean.event.map.SaveVirtualTrackEvent;
import com.evertrend.tiger.common.bean.event.map.UpdateMapPageEvent;
import com.evertrend.tiger.common.bean.event.slamtec.ConnectedEvent;
import com.evertrend.tiger.common.bean.event.slamtec.ConnectionLostEvent;
import com.evertrend.tiger.common.bean.event.slamtec.LaserScanGetEvent;
import com.evertrend.tiger.common.bean.event.slamtec.MapGetEvent;
import com.evertrend.tiger.common.bean.event.slamtec.MapUpdataEvent;
import com.evertrend.tiger.common.bean.event.slamtec.RemainingMilestonesGetEvent;
import com.evertrend.tiger.common.bean.event.slamtec.RemainingPathGetEvent;
import com.evertrend.tiger.common.bean.event.slamtec.RobotPoseGetEvent;
import com.evertrend.tiger.common.bean.event.slamtec.TrackGetEvent;
import com.evertrend.tiger.common.bean.event.slamtec.WallGetEvent;
import com.evertrend.tiger.common.bean.event.uploadPathPicFailEvent;
import com.evertrend.tiger.common.bean.mapview.MapView;
import com.evertrend.tiger.common.bean.mapview.utils.RadianUtil;
import com.evertrend.tiger.common.utils.SlamwareAgent;
import com.evertrend.tiger.common.utils.general.AppSharePreference;
import com.evertrend.tiger.common.utils.general.CommonConstants;
import com.evertrend.tiger.common.utils.general.DBUtil;
import com.evertrend.tiger.common.utils.general.DialogUtil;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.common.utils.network.CommTaskUtils;
import com.evertrend.tiger.common.utils.network.CommonNetReq;
import com.evertrend.tiger.common.widget.BaseTraceBottomPopupView;
import com.evertrend.tiger.common.widget.LongClickImageView;
import com.evertrend.tiger.common.widget.MapBottomPopupView;
import com.evertrend.tiger.common.widget.PoseBottomPopupView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.slamtec.slamware.action.MoveDirection;
import com.slamtec.slamware.action.Path;
import com.slamtec.slamware.geometry.Line;
import com.slamtec.slamware.geometry.PointF;
import com.slamtec.slamware.robot.LaserScan;
import com.slamtec.slamware.robot.Location;
import com.slamtec.slamware.robot.Map;
import com.slamtec.slamware.robot.Pose;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class OperationAreaMapActivity extends BaseActivity implements LongClickImageView.LongClickRepeatListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {
    public static final String TAG = OperationAreaMapActivity.class.getCanonicalName();

    private MapView mMapView;
    private LongClickImageView iv_action_forward, iv_action_left, iv_action_stop, iv_action_right, iv_action_backward;
    private ImageButton ibtn_back, ibtn_map_settings, ibtn_trace_path_close;
    private TextView tv_location_pose, tv_current_map;
    private ImageButton ibtn_map_set_centred;
    private Button btn_save_map, btn_relocation, btn_trace_path, btn_action, btn_set_spot, btn_edit;
    private Button btn_trace_path_show, btn_trace_path_hide, btn_trace_path_rollback;
    private ConstraintLayout cl_area_map_view, cl_action;
    private LinearLayout ll_map_settings, ll_set_spot, ll_edit, ll_trace_path_spot;
    private LinearLayout ll_trace_path_operation;
    private ImageView iv_settings_back;
    private SeekBar sb_device_speed;
    private TextView tv_device_speed;
    private RadioGroup rg_navigation_mode;
    private EditText et_rollback_trace_path_num;
    private Button btn_set_recharge, btn_set_add_water, btn_set_garage, btn_set_empty_trash, btn_set_trace_spot;
    private Button btn_set_common_spot, btn_set_start_spot, btn_reset_start_spot, btn_set_fence_spot;
    private ListView lv_spot_data;
    private Button btn_trace_path_spot_save, btn_trace_path_spot_not_save;
    private Switch btn_auto_record_trace_spot, btn_enable_gps_fence, btn_log_gps_map_slam, btn_delete_gps_map_slam;
    private Button btn_virtual_walls, btn_virtual_tracks, btn_clear_map, btn_load_map;
    private LinearLayout ll_top, ll_bottom;
    private ConstraintLayout ll_map_virtual_walls, ll_map_virtual_tracks;
    private LinearLayout ll_add_virtual_wall_line, ll_add_virtual_wall_all_delete, ll_virtual_wall_delete;
    private ImageButton ibtn_add_virtual_wall_ok, ibtn_add_virtual_wall_cancel;
    private LinearLayout ll_add_virtual_track_line, ll_add_virtual_track_all_delete, ll_virtual_track_delete;
    private ImageButton ibtn_add_virtual_track_ok, ibtn_add_virtual_track_cancel;
    private TextView save_virtual_track;
//    private RadioGroup rg_trace_spot_add_mode, rg_trace_spot_auto_mode_chioce;
    private EditText et_auto_mode_config_distance; //et_auto_mode_config_time;
    private MapBottomPopupView mapBottomPopupView;

    private Device device;
    private MapPages mapPages;
    private DeviceGrant deviceGrant;
    private SlamwareAgent mAgent;
    private String currentPose = "0";
    private String lastPose;
    private String speed;
    private List<String> mTraceSpotList;
    private ArrayAdapter<String> traceSpotAdapter;
    private List<TracePath> tracePathList;
    private List<VirtualTrackGroup> virtualTrackGroupList;
    private List<Line> showTracePathLines;
    private TracePath tracePath;
    private List<RobotSpot> mServerTraceRobotSpotList;
    private List<Line> addVwallsList;
    private List<Line> addVTracksList;
    private List<MapPages> mapPagesList;
    private int connectionLostCount = 0;
    private boolean isAutoRecordSpot = false;
    private boolean isFirstClearMap = false;

    private AlertDialog mDialogInputIp;
    private AlertDialog tracePathChoiceDialog;
    private android.app.AlertDialog operationDialog;

    private ScheduledThreadPoolExecutor scheduledThreadSaveTraceSpot;
    private ScheduledThreadPoolExecutor scheduledThreadGetMapPagesAllPath;
    private ScheduledThreadPoolExecutor scheduledThreadSaveTraceSpotList;
    private ScheduledThreadPoolExecutor scheduledThreadSaveIsCreateMap;
    private ScheduledThreadPoolExecutor scheduledThreadGetSaveMapFlag;
    private ScheduledThreadPoolExecutor scheduledThreadUpdateMapPages;
    private ScheduledThreadPoolExecutor scheduledThreadRelocationMapPages;
    private ScheduledThreadPoolExecutor scheduledThreadDeleteTraceSpot;
    private ScheduledThreadPoolExecutor scheduledThreadGetTraceSpot;
    private ScheduledThreadPoolExecutor scheduledThreadGetMapPagesAllVirtualTrackGroup;
    private ScheduledThreadPoolExecutor scheduledThreadSaveVirtualTrack;
    private ScheduledThreadPoolExecutor scheduledThreadGetDeviceGrant;
    private ScheduledThreadPoolExecutor scheduledThreadSetPose;

    private Runnable mRobotStateUpdateRunnable = new Runnable() {
        int cnt;

        @Override
        public void run() {
            cnt = 0;
            mAgent.getGetRobotInfo();

            while (true) {
                if (mRobotStateUpdateRunnable == null || !mRobotStateUpdateThread.isAlive() || mRobotStateUpdateThread.isInterrupted()) {
                    break;
                }

                if ((cnt % 3) == 0) {
                    mAgent.getRobotPose();
                    mAgent.getLaserScan();
                    mAgent.setDeviceSpeed(AppSharePreference.getAppSharedPreference().loadDeviceSpeed());
                }

                if ((cnt % 20) == 0) {
                    mAgent.getMap();
//                    mAgent.getWalls();
//                    mAgent.getTracks();
                    mAgent.getMoveAction();
                }

                if ((cnt % 30) == 0) {
                    mAgent.getHomePose();
//                    mAgent.getRobotHealth();
                }

                SystemClock.sleep(33);
                cnt++;
            }
        }
    };
    Thread mRobotStateUpdateThread = new Thread(mRobotStateUpdateRunnable);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yl_common_activity_operation_area_map);
        device = (Device) getIntent().getSerializableExtra("device");
        mapPages = (MapPages) getIntent().getSerializableExtra("mappage");
        lastPose = AppSharePreference.getAppSharedPreference().loadTraceSpotAutoModeLastPose();
        mTraceSpotList = new ArrayList<>();
        tracePathList = new ArrayList<>();
        deviceGrant = new DeviceGrant();
        isFirstClearMap = true;
        loadTraceSpotList();
        scheduledThreadGetMapPagesAllPath = new ScheduledThreadPoolExecutor(3);
        scheduledThreadGetMapPagesAllPath.scheduleAtFixedRate(new CommTaskUtils.TaskGetMapPagesAllPath(device, mapPages),
                0, 6, TimeUnit.SECONDS);
        scheduledThreadGetMapPagesAllVirtualTrackGroup = new ScheduledThreadPoolExecutor(4);
        scheduledThreadGetMapPagesAllVirtualTrackGroup.scheduleAtFixedRate(new CommTaskUtils.TaskGetMapPagesAllVirtualTrackGroup(device, mapPages),
                0, 10, TimeUnit.SECONDS);
        if (device.getGrant_flag() == 1) {
            scheduledThreadGetDeviceGrant = new ScheduledThreadPoolExecutor(3);
            scheduledThreadGetDeviceGrant.scheduleAtFixedRate(new CommTaskUtils.TaskGetDeviceGrant(device),
                    0, 6, TimeUnit.SECONDS);
        }
        initView();
        tv_current_map.setText(String.format(getResources().getString(R.string.yl_common_current_map), mapPages.getName()));
        mAgent = getSlamwareAgent();
        connectSlamware(device);
        EventBus.getDefault().register(this);
        startRelocationOrSetCurrentMapOrAutoRecordPath(CommonConstants.TYPE_SET_CURRENT_MAP, 1);
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
    protected void onStop() {
        DialogUtil.hideProgressDialog();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (ll_map_settings.getVisibility() == View.VISIBLE) {
            ll_map_settings.setVisibility(View.GONE);
            cl_area_map_view.setVisibility(View.VISIBLE);
        } else if (ll_map_virtual_walls.getVisibility() == View.VISIBLE) {
            virtualWallsComplete();
        } else if (ll_map_virtual_tracks.getVisibility() == View.VISIBLE) {
            virtualTracksComplete();
        } else {
            exit();
        }
    }

    @Override
    protected void onDestroy() {
        exit();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case CommonConstants.CREATE_TRACE_PATH_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    TracePath tracePath = (TracePath) data.getSerializableExtra("tracePath");
                    DialogUtil.showProgressDialog(this, getResources().getString(R.string.yl_common_saving), false, false);
                    scheduledThreadSaveTraceSpotList = new ScheduledThreadPoolExecutor(5);
                    scheduledThreadSaveTraceSpotList.scheduleAtFixedRate(new CommTaskUtils.TaskSaveTraceSpotList(device, mTraceSpotList, mapPages, tracePath),
                            0, 30, TimeUnit.SECONDS);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void stopSetPoseTimer() {
        if (scheduledThreadSetPose != null) {
            scheduledThreadSetPose.shutdownNow();
            scheduledThreadSetPose = null;
        }
    }

    private void stopSaveTraceSpotTimer() {
        if (scheduledThreadSaveTraceSpot != null) {
            scheduledThreadSaveTraceSpot.shutdownNow();
            scheduledThreadSaveTraceSpot = null;
        }
    }

    private void stopGetMapPagesAllPathTimer() {
        if (scheduledThreadGetMapPagesAllPath != null) {
            scheduledThreadGetMapPagesAllPath.shutdownNow();
            scheduledThreadGetMapPagesAllPath = null;
        }
    }

    private void stopSaveTraceSpotListTimer() {
        if (scheduledThreadSaveTraceSpotList != null) {
            scheduledThreadSaveTraceSpotList.shutdownNow();
            scheduledThreadSaveTraceSpotList = null;
        }
    }

    private void stopSaveIsCreateMapTimer() {
        if (scheduledThreadSaveIsCreateMap != null) {
            scheduledThreadSaveIsCreateMap.shutdownNow();
            scheduledThreadSaveIsCreateMap = null;
        }
    }

    private void stopGetSaveMapFlagTimer() {
        if (scheduledThreadGetSaveMapFlag != null) {
            scheduledThreadGetSaveMapFlag.shutdownNow();
            scheduledThreadGetSaveMapFlag = null;
        }
    }

    private void stopUpdateMapPagesTimer() {
        if (scheduledThreadUpdateMapPages != null) {
            scheduledThreadUpdateMapPages.shutdownNow();
            scheduledThreadUpdateMapPages = null;
        }
    }

    private void stopRelocationMapPagesTimer() {
        if (scheduledThreadRelocationMapPages != null) {
            scheduledThreadRelocationMapPages.shutdownNow();
            scheduledThreadRelocationMapPages = null;
        }
    }

    private void stopDeleteTraceSpotTimer() {
        if (scheduledThreadDeleteTraceSpot != null) {
            scheduledThreadDeleteTraceSpot.shutdownNow();
            scheduledThreadDeleteTraceSpot = null;
        }
    }

    private void stopGetTraceSpotTimer() {
        if (scheduledThreadGetTraceSpot != null) {
            scheduledThreadGetTraceSpot.shutdownNow();
            scheduledThreadGetTraceSpot = null;
        }
    }

    private void stopGetMapPagesAllVirtualTrackGroupTimer() {
        if (scheduledThreadGetMapPagesAllVirtualTrackGroup != null) {
            scheduledThreadGetMapPagesAllVirtualTrackGroup.shutdownNow();
            scheduledThreadGetMapPagesAllVirtualTrackGroup = null;
        }
    }

    private void stopSaveVirtualTrackTimer() {
        if (scheduledThreadSaveVirtualTrack != null) {
            scheduledThreadSaveVirtualTrack.shutdownNow();
            scheduledThreadSaveVirtualTrack = null;
        }
    }

    private void stopGetDeviceGrantTimer() {
        if (scheduledThreadGetDeviceGrant != null) {
            scheduledThreadGetDeviceGrant.shutdownNow();
            scheduledThreadGetDeviceGrant = null;
        }
    }

    private void updateSaveStatus(Device device) {
        AppSharePreference.getAppSharedPreference().saveAutoRecordPath(device.getAuto_record_trace_path() == 0 ? false : true);
        AppSharePreference.getAppSharedPreference().saveEnableGPSFence(device.getEnable_gps_fence() == 0 ? false : true);
        AppSharePreference.getAppSharedPreference().saveLogGPSSlam(device.getLog_gps_map_slam() == 0 ? false : true);
        AppSharePreference.getAppSharedPreference().saveDeleteGPSSlam(device.getDelete_gps_map_slam() == 0 ? false : true);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(DeviceMessageEvent messageEvent) {
        device = messageEvent.getMessage();
        updateSaveStatus(device);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventMainThread(ConnectedEvent event) {
        DialogUtil.hideProgressDialog();
        hideInputIpDialog();
        LogUtil.i(this, TAG, "===ConnectedEvent===");
        mAgent.getMapUpdata();
        mAgent.getWalls(-1);
        mAgent.getTracks(-1);
        ibtn_map_settings.setEnabled(true);
        btn_set_spot.setEnabled(true);
        btn_action.setEnabled(true);
        btn_edit.setEnabled(true);
        btn_relocation.setEnabled(true);
        btn_trace_path.setEnabled(true);
        mMapView.setCentred();
        if (device.getCurrent_map_page() != mapPages.getId() && isFirstClearMap) {//当前地图与要加载地图不一致时，先清空当前地图
            LogUtil.d(TAG, "map diff="+device.getCurrent_map_page()+":"+mapPages.getId());
            mAgent.clearMap();
            isFirstClearMap = false;
        }

        isAutoRecordSpot = AppSharePreference.getAppSharedPreference().loadAutoRecordPath();
        if (device.getGrant_flag() != 1) {
            btn_save_map.setEnabled(true);
            switchAutoRecordSpot();
        } else {
            if (deviceGrant.getAuthorization_item().contains("1")) {//建图
                btn_save_map.setEnabled(true);
            } else {
                btn_save_map.setEnabled(false);
            }
            if (deviceGrant.getAuthorization_item().contains("2")) {//循迹路径
                switchAutoRecordSpot();
            } else {
                startRelocationOrSetCurrentMapOrAutoRecordPath(CommonConstants.TYPE_AUTO_RECORD_PATH, 0);
                btn_set_trace_spot.setVisibility(View.GONE);
                ll_trace_path_spot.setVisibility(View.GONE);
            }
        }
    }

    private void switchAutoRecordSpot() {
        btn_set_trace_spot.setVisibility(View.VISIBLE);
        if (isAutoRecordSpot) {
            ll_trace_path_spot.setVisibility(View.GONE);
            startRelocationOrSetCurrentMapOrAutoRecordPath(CommonConstants.TYPE_AUTO_RECORD_PATH, 1);
        } else {
            startRelocationOrSetCurrentMapOrAutoRecordPath(CommonConstants.TYPE_AUTO_RECORD_PATH, 0);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventMainThread(ConnectionLostEvent event) {
        DialogUtil.hideProgressDialog();
//        Toast.makeText(OperationAreaMapActivity.this, "连接失败，请检查网络、设备、IP", Toast.LENGTH_SHORT).show();
        LogUtil.i(this, TAG, "===ConnectionLostEvent===");
        if (connectionLostCount == 10) {
            connectionLostCount = 0;
            DialogUtil.showMessageDialog(this, R.string.yl_common_connection_lost, CommonConstants.TYPE_CONNECTED_LOST);
        } else {
            DialogUtil.showProgressDialog(this, getResources().getString(R.string.yl_common_connecting), false, true);
            mAgent.connectTo(device.getLocal_chassis_ip());
        }
        ibtn_map_settings.setEnabled(false);
        btn_set_spot.setEnabled(false);
        btn_action.setEnabled(false);
        btn_edit.setEnabled(false);
        btn_save_map.setEnabled(false);
        btn_relocation.setEnabled(false);
        btn_trace_path.setEnabled(false);
        connectionLostCount++;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DialogChoiceEvent event) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LaserScanGetEvent event) {
        LaserScan laserScan = event.getLaserScan();
        mMapView.setLaserScan(laserScan);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventMainThread(MapGetEvent event) {
        Map map = event.getMap();
        mMapView.setMap(map);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventMainThread(WallGetEvent event) {
        addVwallsList = event.getWalls();
        LogUtil.i(this, TAG, "WallGetEvent type: "+event.getType());
        mMapView.setVwalls(addVwallsList, event.getType());
//        if (CommonConstants.TYPE_ADD_VIRTUAL_WALL == event.getType()) {
//            mMapView.setVwalls(addVwallsList, CommonConstants.TYPE_ADD_VIRTUAL_WALL);
//        } else if (CommonConstants.TYPE_DELETE_VIRTUAL_WALL == event.getType()) {
//            mMapView.setVwalls(addVwallsList, CommonConstants.TYPE_DELETE_VIRTUAL_WALL);
//        } else {
//            mMapView.setVwalls(addVwallsList, -1);
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventMainThread(TrackGetEvent event) {
        addVTracksList = event.getTracks();
        LogUtil.i(this, TAG, "TrackGetEvent type: "+event.getType());
        mMapView.setVtracks(addVTracksList, event.getType());
//        if (CommonConstants.TYPE_ADD_VIRTUAL_TRACK == event.getType()) {
//            mMapView.setVtracks(addVTracksList, CommonConstants.TYPE_ADD_VIRTUAL_TRACK);
//        } else if (CommonConstants.TYPE_DELETE_VIRTUAL_TRACK == event.getType()) {
//            mMapView.setVtracks(addVTracksList, CommonConstants.TYPE_DELETE_VIRTUAL_TRACK);
//        } else {
//            mMapView.setVtracks(addVTracksList, -1);
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventMainThread(RobotPoseGetEvent event) {
        Pose pose = event.getPose();
        mMapView.setRobotPose(pose);
        if (pose != null) {
            currentPose = String.format("%.3f,%.3f,%.3f,%.3f", pose.getX(), pose.getY(), pose.getZ(), RadianUtil.toAngel(pose.getYaw()));
            tv_location_pose.setText(currentPose);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventMainThread(RemainingMilestonesGetEvent event) {
        Path remainingMilestones = event.getRemainingMilestones();
        mMapView.setRemainingMilestones(remainingMilestones);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventMainThread(RemainingPathGetEvent event) {
        Path remainingPath = event.getRemainingPath();
        mMapView.setRemainingPath(remainingPath);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventMainThread(MapUpdataEvent event) {
        LogUtil.i(this, TAG, "onEventMainThread event.isMapUpdata():" + event.isMapUpdata());
//        if (event.isMapUpdata()) {
//            updateMapFlag = true;
//        } else {
//            updateMapFlag = false;
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ChoiceMapPagesEvent event) {
        if (CommonConstants.TYPE_MAPPAGE_OPERATION_ADD_COMMON_SPOT_CHOICE == event.getType()) {
            LogUtil.d(TAG, "mappage : "+event.getMapPages().toString());
            saveSpot(5, event.getMapPages().getId());
            tracePathChoiceDialog.dismiss();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventMainThread(GetAllMapPagesSuccessEvent event) {
        mapPagesList = new ArrayList<>();
        List<MapPages> tmpList = event.getMapPagesList();
        for (MapPages m : tmpList) {
            if (mapPages.getId() == m.getId()) {
                continue;
            } else {
                mapPagesList.add(m);
            }
        }
        LogUtil.d(TAG, "mapPagesList size:"+mapPagesList.size());
        EventBus.getDefault().removeStickyEvent(event);
        tmpList = null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SaveTraceSpotEvent messageEvent) {
        LogUtil.i(this, TAG, "===SaveTraceSpotEvent===");
        DialogUtil.hideProgressDialog();
        stopSaveTraceSpotTimer();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SaveTraceSpotFailEvent messageEvent) {
        LogUtil.i(this, TAG, "===SaveTraceSpotFailEvent===");
        DialogUtil.hideProgressDialog();
        stopSaveTraceSpotTimer();
        DialogUtil.showToast(this, messageEvent.getJsonObject().getString(CommonNetReq.RESULT_DESC), Toast.LENGTH_LONG);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(GetMapPagesAllPathSuccessEvent event) {
        LogUtil.i(this, TAG, "===GetMapPagesAllPathSuccessEvent===");
        stopGetMapPagesAllPathTimer();
        tracePathList = event.getTracePathList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SaveTraceSpotListCompleteEvent messageEvent) {
        LogUtil.i(this, TAG, "===SaveTraceSpotListCompleteEvent===");
        stopSaveTraceSpotListTimer();
        DialogUtil.hideProgressDialog();
        scheduledThreadGetTraceSpot = new ScheduledThreadPoolExecutor(6);
        scheduledThreadGetTraceSpot.scheduleAtFixedRate(new CommTaskUtils.TaskGetTraceSpot(device, tracePath, mMapView, true),
                0, 8, TimeUnit.SECONDS);
        mTraceSpotList.clear();
        traceSpotAdapter.notifyDataSetChanged();
        ll_trace_path_spot.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GetTraceSpotEvent messageEvent) {
        LogUtil.i(this, TAG, "messageEvent.isActive():" + messageEvent.isActive());
        LogUtil.i(this, TAG, "messageEvent.getTraceSpotList().size():" + messageEvent.getTraceSpotList().size());
        stopGetTraceSpotTimer();
        DialogUtil.hideProgressDialog();
        if (messageEvent.getTraceSpotList().size() > 0) {
            mServerTraceRobotSpotList = messageEvent.getTraceSpotList();
        } else {
            mServerTraceRobotSpotList = new ArrayList<>();
        }
        saveTracePathPic();
        showTracePath();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(uploadPathPicFailEvent messageEvent) {
        saveTracePathPic();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ChoiceMapPagesTracePathEvent event) {
        tracePath = (TracePath) event.getBaseTrace();
        int type = event.getMark();
        tracePathChoiceDialog.dismiss();
        if (CommonConstants.TYPE_TRACE_PATH_OPERATION_SAVE_SPOT == type) {
            DialogUtil.showProgressDialog(this, getResources().getString(R.string.yl_common_saving), false, false);
            scheduledThreadSaveTraceSpotList = new ScheduledThreadPoolExecutor(5);
            scheduledThreadSaveTraceSpotList.scheduleAtFixedRate(new CommTaskUtils.TaskSaveTraceSpotList(device, mTraceSpotList, mapPages, tracePath),
                    0, 30, TimeUnit.SECONDS);
        } else if (CommonConstants.TYPE_TRACE_PATH_OPERATION_SHOW_PATH == type) {
            scheduledThreadGetTraceSpot = new ScheduledThreadPoolExecutor(6);
            scheduledThreadGetTraceSpot.scheduleAtFixedRate(new CommTaskUtils.TaskGetTraceSpot(device, tracePath, mMapView, true),
                    0, 8, TimeUnit.SECONDS);
//            showTracePath();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UpdateMapPageEvent event) {
        mapPages = event.getMapPages();
        stopUpdateMapPagesTimer();
        getSaveMapFlag();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GetSaveMapFlagEvent messageEvent) {
        if (messageEvent.getSaveFlag() == 0) {
            stopGetSaveMapFlagTimer();
            DialogUtil.hideProgressDialog();
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        } else {
            LogUtil.i(this, TAG, "save map : " + messageEvent.getSaveFlag());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RelocationOrSetCurrentMapEvent messageEvent) {
        LogUtil.i(this, TAG, "===RelocationOrSetCurrentMapEvent===");
        stopRelocationMapPagesTimer();
        DialogUtil.hideProgressDialog();
//        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DeleteTraceSpotListCompleteEvent messageEvent) {
        stopDeleteTraceSpotTimer();
        DialogUtil.hideProgressDialog();
        mServerTraceRobotSpotList = messageEvent.getmRobotSpotList();
        LogUtil.i(this, TAG, "DeleteTraceSpotListCompleteEvent: "+mServerTraceRobotSpotList.size());
//        List<RobotSpot> list = DBUtil.getTracePathSpotList(tracePath, mapPages);
        if (mServerTraceRobotSpotList.size() > 0) {
             RobotSpot localRobotSpot = mServerTraceRobotSpotList.get(mServerTraceRobotSpotList.size() - 1);
            LogUtil.i(this, TAG, "localRobotSpot: "+localRobotSpot.toString());
            if (localRobotSpot != null) {
                Location location = new Location(localRobotSpot.getX(), localRobotSpot.getY(), localRobotSpot.getZ());
                mAgent.moveTo(location);
            }
        }
        showTracePath();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DeleteOneTraceSpotListEvent event) {//收不到消息！！
        RobotSpot robotSpot = event.getRobotSpot();
        LogUtil.i(this, TAG, "robotSpot: "+robotSpot.toString());
        if (robotSpot != null) {
            Location location = new Location(robotSpot.getX(), robotSpot.getY(), robotSpot.getZ());
            mAgent.moveTo(location);
        }
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AddVwalls messageEvent) {
        LogUtil.i(this, TAG, "===AddVwalls===");
        virtualWallsComplete();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AddVtracks messageEvent) {
        LogUtil.i(this, TAG, "===AddVtracks===");
        virtualTracksComplete();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AddWall messageEvent) {
        LogUtil.i(this, TAG, "type: "+messageEvent.getType());
        mAgent.getWalls(messageEvent.getType());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AddTrack messageEvent) {
        LogUtil.i(this, TAG, "type: "+messageEvent.getType());
        mAgent.getTracks(messageEvent.getType());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DeleteEvent messageEvent) {
        LogUtil.i(this, TAG, "===DeleteEvent type: "+messageEvent.getType());
        if (CommonConstants.TYPE_DELETE_ALL_VIRTUAL_WALL == messageEvent.getType()) {
            mAgent.clearAllVwalls();
        } else if (CommonConstants.TYPE_DELETE_ALL_VIRTUAL_TRACK == messageEvent.getType()) {
            mAgent.clearAllVtracks();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ClearAllVwalls messageEvent) {
        mAgent.getWalls(-1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ClearAllVtracks messageEvent) {
        mAgent.getTracks(-1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DeleteOneVwallEvent messageEvent) {
        mAgent.removeOneVwalls(messageEvent.getLine(), CommonConstants.TYPE_DELETE_VIRTUAL_WALL);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DeleteOneVtrackEvent messageEvent) {
        mAgent.removeOneVtracks(messageEvent.getLine(), CommonConstants.TYPE_DELETE_VIRTUAL_TRACK);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DeleteOneVwallCompleteEvent messageEvent) {
        if (CommonConstants.TYPE_MOVE_VIRTUAL_WALL == messageEvent.getType()) {
//            mAgent.addVwall(messageEvent.getLine(), CommonConstants.TYPE_ADD_VIRTUAL_WALL);
        } else {
            mAgent.getWalls(CommonConstants.TYPE_DELETE_VIRTUAL_WALL);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DeleteOneVtrackCompleteEvent messageEvent) {
        if (CommonConstants.TYPE_MOVE_VIRTUAL_TRACK == messageEvent.getType()) {

        } else {
            mAgent.getTracks(CommonConstants.TYPE_DELETE_VIRTUAL_TRACK);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MoveOneVwallEvent messageEvent) {
        if (messageEvent.isComplete()) {
            mAgent.addVwall(messageEvent.getLine(), CommonConstants.TYPE_ADD_VIRTUAL_WALL);
        } else {
            mAgent.removeOneVwalls(messageEvent.getLine(), messageEvent.getMoveType());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MoveOneVtrackEvent messageEvent) {
        if (messageEvent.isComplete()) {
            mAgent.addVtrack(messageEvent.getLine(), CommonConstants.TYPE_ADD_VIRTUAL_TRACK);
        } else {
            mAgent.removeOneVtracks(messageEvent.getLine(), messageEvent.getMoveType());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(GetAllVirtualTrackGroupSuccessEvent event) {
        stopGetMapPagesAllVirtualTrackGroupTimer();
        virtualTrackGroupList = event.getVirtualTrackGroups();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(SaveVirtualTrackEvent event) {
        stopSaveVirtualTrackTimer();
        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CreateNewBaseTraceSuccessEvent event) {
        tracePath = (TracePath) event.getBaseTrace();
        tracePathChoiceDialog.dismiss();
        DialogUtil.showProgressDialog(this, getResources().getString(R.string.yl_common_saving), false, false);
        scheduledThreadSaveTraceSpotList = new ScheduledThreadPoolExecutor(5);
        scheduledThreadSaveTraceSpotList.scheduleAtFixedRate(new CommTaskUtils.TaskSaveTraceSpotList(device, mTraceSpotList, mapPages, tracePath),
                0, 30, TimeUnit.SECONDS);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(AutoRecordPathDistanceOKEvent event) {
        addSpotToList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SaveMapPageEvent event) {
        mapPages = event.getMapPages();
        saveMapPic();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(GetDeviceGrantSuccessEvent event) {
        stopGetDeviceGrantTimer();
        deviceGrant = event.getDeviceGrant();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SetPoseOKEvent event) {
        stopSetPoseTimer();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SetPoseFailEvent event) {
        stopSetPoseTimer();
        DialogUtil.showToast(this, event.getDesc(), Toast.LENGTH_LONG);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void virtualWallsComplete() {
        ll_top.setVisibility(View.VISIBLE);
        ll_bottom.setVisibility(View.VISIBLE);
        ll_map_virtual_walls.setVisibility(View.GONE);
        mAgent.getWalls(-1);
        EventBus.getDefault().post(new EditVwallsComplete());
    }

    private void virtualTracksComplete() {
        ll_top.setVisibility(View.VISIBLE);
        ll_bottom.setVisibility(View.VISIBLE);
        ll_map_virtual_tracks.setVisibility(View.GONE);
        mAgent.getTracks(-1);
        EventBus.getDefault().post(new EditVtracksComplete());
    }

    private void exit() {
        LogUtil.i(this, TAG, "slamtec disconnect");
        mAgent.disconnect();
        stopSaveTraceSpotTimer();
        stopGetMapPagesAllPathTimer();
        stopSaveTraceSpotListTimer();
        stopSaveIsCreateMapTimer();
        stopGetSaveMapFlagTimer();
        stopUpdateMapPagesTimer();
        stopRelocationMapPagesTimer();
        stopDeleteTraceSpotTimer();
        stopGetTraceSpotTimer();
        stopGetMapPagesAllVirtualTrackGroupTimer();
        stopSaveVirtualTrackTimer();
        stopGetDeviceGrantTimer();
        stopUpdate();
        stopRecordSpot();
        stopSetPoseTimer();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        saveTmpPath();
        finish();
    }

    private void saveTmpPath() {
        if (mTraceSpotList.size() > 1) {
            Set<String> tmpPaths = new LinkedHashSet<>(mTraceSpotList.size());
            for (String s : mTraceSpotList) {
                tmpPaths.add(s);
            }
            AppSharePreference.getAppSharedPreference().saveTmpPath(mapPages.getName()+mapPages.getId(), tmpPaths);
        } else {
            AppSharePreference.getAppSharedPreference().saveTmpPath(mapPages.getName()+mapPages.getId(), null);
        }
    }

    private void loadTraceSpotList() {
        Set<String> tmpPaths = AppSharePreference.getAppSharedPreference().loadTmpPath(mapPages.getName()+mapPages.getId());
        if (tmpPaths != null) {
            for (String s : tmpPaths) {
                mTraceSpotList.add(s);
            }
        }
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

    private void connectSlamware(Device device) {
        LogUtil.i(this, TAG, "connectSlamware ip=" + device.getLocal_chassis_ip());

        if (TextUtils.isEmpty(device.getLocal_chassis_ip()) || device.getLocal_chassis_ip() == null) {
            inputConnectIPDialog();
        } else {
            DialogUtil.showProgressDialog(this, getResources().getString(R.string.yl_common_connecting), false, true);
            mAgent.connectTo(device.getLocal_chassis_ip());
        }
    }

    private void inputConnectIPDialog() {
        new XPopup.Builder(this).asInputConfirm("我是标题", "请输入内容。",
                new OnInputConfirmListener() {
                    @Override
                    public void onConfirm(String ip) {
                        LogUtil.d(TAG, "etInput = " + ip);
                        if (TextUtils.isEmpty(ip)) {
                            DialogUtil.showToast(OperationAreaMapActivity.this, R.string.yl_common_input_empty, Toast.LENGTH_SHORT);
                            inputConnectIPDialog();
                        } else {
                            mAgent.connectTo(ip);
                            DialogUtil.showProgressDialog(OperationAreaMapActivity.this, getResources().getString(R.string.yl_common_connecting), false, true);
                        }
                    }
                })
                .show();
    }

    private void hideInputIpDialog() {
        if (mDialogInputIp != null && mDialogInputIp.isShowing()) {
            mDialogInputIp.dismiss();
            mDialogInputIp = null;
        }
    }

    private void moveToLocation(float x, float y) {
        PointF target = mMapView.widgetCoordinateToMapCoordinate(x, y);
        if (target == null) return;
        Location location = new Location(target.getX(), target.getY(), 0);
        mAgent.moveTo(location);
    }

    private void initView() {
        mMapView = findViewById(R.id.device_map_view);
        mMapView.setEvertrend(false);
        mMapView.setSingleTapListener(new MapView.ISingleTapListener() {
            @Override
            public void onSingleTapListener(MotionEvent event) {
                moveToLocation(event.getX(), event.getY());
            }
        });

        iv_action_forward = findViewById(R.id.iv_action_forward);
        iv_action_left = findViewById(R.id.iv_action_left);
        iv_action_stop = findViewById(R.id.iv_action_stop);
        iv_action_right = findViewById(R.id.iv_action_right);
        iv_action_backward = findViewById(R.id.iv_action_backward);
        ibtn_back = findViewById(R.id.ibtn_back);
        tv_location_pose = findViewById(R.id.tv_location_pose);
        tv_current_map = findViewById(R.id.tv_current_map);
        ibtn_map_set_centred = findViewById(R.id.ibtn_map_set_centred);
        ibtn_map_settings = findViewById(R.id.ibtn_map_settings);
        cl_area_map_view = findViewById(R.id.cl_area_map_view);
        ll_map_settings = findViewById(R.id.ll_map_settings);
        ll_trace_path_operation = findViewById(R.id.ll_trace_path_operation);
        ll_set_spot = findViewById(R.id.ll_set_spot);
        ll_edit = findViewById(R.id.ll_edit);
        iv_settings_back = findViewById(R.id.iv_settings_back);
        cl_action = findViewById(R.id.cl_action);
        btn_action = findViewById(R.id.btn_action);
        btn_edit = findViewById(R.id.btn_edit);
        btn_set_spot = findViewById(R.id.btn_set_spot);
        tv_device_speed = findViewById(R.id.tv_device_speed);
        rg_navigation_mode = findViewById(R.id.rg_navigation_mode);
        et_rollback_trace_path_num = findViewById(R.id.et_rollback_trace_path_num);
        et_auto_mode_config_distance = findViewById(R.id.et_auto_mode_config_distance);
        btn_reset_start_spot = findViewById(R.id.btn_reset_start_spot);
        btn_set_start_spot = findViewById(R.id.btn_set_start_spot);
        btn_set_recharge = findViewById(R.id.btn_set_recharge);
        btn_set_add_water = findViewById(R.id.btn_set_add_water);
        btn_set_garage = findViewById(R.id.btn_set_garage);
        btn_set_empty_trash = findViewById(R.id.btn_set_empty_trash);
        btn_set_trace_spot = findViewById(R.id.btn_set_trace_spot);
        btn_set_common_spot = findViewById(R.id.btn_set_common_spot);
        btn_set_fence_spot = findViewById(R.id.btn_set_fence_spot);
        lv_spot_data = findViewById(R.id.lv_spot_data);
        ll_trace_path_spot = findViewById(R.id.ll_trace_path_spot);
        btn_trace_path_spot_save = findViewById(R.id.btn_trace_path_spot_save);
        btn_trace_path_spot_not_save = findViewById(R.id.btn_trace_path_spot_not_save);
        btn_save_map = findViewById(R.id.btn_save_map);
        btn_relocation = findViewById(R.id.btn_relocation);
        btn_trace_path = findViewById(R.id.btn_trace_path);
        btn_trace_path_show = findViewById(R.id.btn_trace_path_show);
        btn_trace_path_hide = findViewById(R.id.btn_trace_path_hide);
        btn_trace_path_rollback = findViewById(R.id.btn_trace_path_rollback);
        ibtn_trace_path_close = findViewById(R.id.ibtn_trace_path_close);
        btn_auto_record_trace_spot = findViewById(R.id.btn_auto_record_trace_spot);
        btn_enable_gps_fence = findViewById(R.id.btn_enable_gps_fence);
        btn_log_gps_map_slam = findViewById(R.id.btn_log_gps_map_slam);
        btn_delete_gps_map_slam = findViewById(R.id.btn_delete_gps_map_slam);
        btn_virtual_walls = findViewById(R.id.btn_virtual_walls);
        btn_virtual_tracks = findViewById(R.id.btn_virtual_tracks);
        btn_clear_map = findViewById(R.id.btn_clear_map);
        btn_load_map = findViewById(R.id.btn_load_map);
        ll_top = findViewById(R.id.ll_top);
        ll_bottom = findViewById(R.id.ll_bottom);
        ll_map_virtual_walls = findViewById(R.id.ll_map_virtual_walls);
        ll_map_virtual_tracks = findViewById(R.id.ll_map_virtual_tracks);
        ll_add_virtual_wall_line = findViewById(R.id.ll_add_virtual_wall_line);
        ibtn_add_virtual_wall_ok = findViewById(R.id.ibtn_add_virtual_wall_ok);
        ibtn_add_virtual_wall_cancel = findViewById(R.id.ibtn_add_virtual_wall_cancel);
        ll_add_virtual_wall_all_delete = findViewById(R.id.ll_add_virtual_wall_all_delete);
        ll_virtual_wall_delete = findViewById(R.id.ll_virtual_wall_delete);
        ll_add_virtual_track_line = findViewById(R.id.ll_add_virtual_track_line);
        ibtn_add_virtual_track_ok = findViewById(R.id.ibtn_add_virtual_track_ok);
        ibtn_add_virtual_track_cancel = findViewById(R.id.ibtn_add_virtual_track_cancel);
        ll_add_virtual_track_all_delete = findViewById(R.id.ll_add_virtual_track_all_delete);
        ll_virtual_track_delete = findViewById(R.id.ll_virtual_track_delete);
        save_virtual_track = findViewById(R.id.save_virtual_track);
        iv_action_forward.setLongClickRepeatListener(this);
        iv_action_left.setLongClickRepeatListener(this);
        iv_action_stop.setOnClickListener(this);
        iv_action_right.setLongClickRepeatListener(this);
        iv_action_backward.setLongClickRepeatListener(this);
        ibtn_back.setOnClickListener(this);
        ibtn_map_settings.setOnClickListener(this);
        iv_settings_back.setOnClickListener(this);
        btn_action.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_set_spot.setOnClickListener(this);
        rg_navigation_mode.setOnCheckedChangeListener(this);
        btn_reset_start_spot.setOnClickListener(this);
        btn_set_start_spot.setOnClickListener(this);
        btn_set_recharge.setOnClickListener(this);
        btn_set_add_water.setOnClickListener(this);
        btn_set_garage.setOnClickListener(this);
        btn_set_empty_trash.setOnClickListener(this);
        btn_set_trace_spot.setOnClickListener(this);
        btn_set_common_spot.setOnClickListener(this);
        btn_set_fence_spot.setOnClickListener(this);
        btn_trace_path_spot_save.setOnClickListener(this);
        btn_trace_path_spot_not_save.setOnClickListener(this);
        btn_save_map.setOnClickListener(this);
        btn_relocation.setOnClickListener(this);
        btn_trace_path.setOnClickListener(this);
        btn_trace_path_show.setOnClickListener(this);
        btn_trace_path_hide.setOnClickListener(this);
        btn_trace_path_hide.setEnabled(false);
        btn_trace_path_rollback.setOnClickListener(this);
        btn_trace_path_rollback.setEnabled(false);
        ibtn_trace_path_close.setOnClickListener(this);
        btn_auto_record_trace_spot.setOnCheckedChangeListener(this);
        btn_enable_gps_fence.setOnCheckedChangeListener(this);
        btn_log_gps_map_slam.setOnCheckedChangeListener(this);
        btn_delete_gps_map_slam.setOnCheckedChangeListener(this);
        ibtn_map_set_centred.setOnClickListener(this);
        btn_virtual_walls.setOnClickListener(this);
        btn_virtual_tracks.setOnClickListener(this);
        btn_clear_map.setOnClickListener(this);
        btn_load_map.setOnClickListener(this);
        ll_add_virtual_wall_line.setOnClickListener(this);
        ibtn_add_virtual_wall_ok.setOnClickListener(this);
        ibtn_add_virtual_wall_cancel.setOnClickListener(this);
        ll_add_virtual_wall_all_delete.setOnClickListener(this);
        ll_virtual_wall_delete.setOnClickListener(this);
        ll_add_virtual_track_line.setOnClickListener(this);
        ibtn_add_virtual_track_ok.setOnClickListener(this);
        ibtn_add_virtual_track_cancel.setOnClickListener(this);
        ll_add_virtual_track_all_delete.setOnClickListener(this);
        ll_virtual_track_delete.setOnClickListener(this);
        save_virtual_track.setOnClickListener(this);

        sb_device_speed = findViewById(R.id.sb_device_speed);
        sb_device_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LogUtil.i(OperationAreaMapActivity.this, TAG, "progress: " + progress);
                if (progress == 0) {
                    speed = "0";
                } else {
                    double dSpeed = progress * 0.01;
                    speed = CommonConstants.df.format(dSpeed);
                    tv_device_speed.setText(getResources().getString(R.string.yl_common_maximum_speed) + speed + "M/S");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (speed.equals("0")) {
                    Toast.makeText(OperationAreaMapActivity.this, "Cannot be set to zero", Toast.LENGTH_SHORT).show();
                } else {
                    mAgent.setDeviceSpeed(speed);
                    Toast.makeText(OperationAreaMapActivity.this, "set speed success", Toast.LENGTH_SHORT).show();
                }
            }
        });

        traceSpotAdapter = new ArrayAdapter<>(OperationAreaMapActivity.this,
                R.layout.yl_common_listview_trace_spot_item, mTraceSpotList);
        lv_spot_data.setAdapter(traceSpotAdapter);

        et_rollback_trace_path_num.setText(AppSharePreference.getAppSharedPreference().loadTracePathRollbackNum()+"");
        et_rollback_trace_path_num.setSelection(1);
        et_rollback_trace_path_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String num = s.toString();
                LogUtil.i(OperationAreaMapActivity.this, TAG, "num : "+num);
                if (!TextUtils.isEmpty(num)) {
                    AppSharePreference.getAppSharedPreference().saveTracePathRollbackNum(Integer.parseInt(num));
                }
            }
        });
        et_auto_mode_config_distance.setText(AppSharePreference.getAppSharedPreference().loadAutoRecordPathDistance()+"");
        et_auto_mode_config_distance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String distance = s.toString();
                LogUtil.i(OperationAreaMapActivity.this, TAG, "distance : "+distance);
                if (!TextUtils.isEmpty(distance)) {
                    AppSharePreference.getAppSharedPreference().saveAutoRecordPathDistance(Integer.parseInt(distance));
                }
            }
        });
    }

    @Override
    public void repeatAction(View view) {
        if (view.getId() == R.id.iv_action_left) {
            mAgent.moveBy(MoveDirection.TURN_LEFT);
        } else if (view.getId() == R.id.iv_action_right) {
            mAgent.moveBy(MoveDirection.TURN_RIGHT);
        } else if (view.getId() == R.id.iv_action_forward) {
            mAgent.moveBy(MoveDirection.FORWARD);
        } else if (view.getId() == R.id.iv_action_backward) {
            mAgent.moveBy(MoveDirection.BACKWARD);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_action_stop) {
            mAgent.cancelAllActions();
        } else if (v.getId() == R.id.ibtn_back) {
            exit();
        } else if (v.getId() == R.id.ibtn_map_settings) {
            cl_area_map_view.setVisibility(View.GONE);
            ll_map_settings.setVisibility(View.VISIBLE);
            String speed = AppSharePreference.getAppSharedPreference().loadDeviceSpeed();
            tv_device_speed.setText(getResources().getString(R.string.yl_common_maximum_speed) + speed + "M/S");
            int dSpeed = (int) (Double.valueOf(speed) * 100);
            sb_device_speed.setProgress(dSpeed);
            et_rollback_trace_path_num.setText(AppSharePreference.getAppSharedPreference().loadTracePathRollbackNum()+"");
            et_rollback_trace_path_num.setSelection(1);
            btn_enable_gps_fence.setChecked(AppSharePreference.getAppSharedPreference().loadEnableGPSFence());
            btn_log_gps_map_slam.setChecked(AppSharePreference.getAppSharedPreference().loadLogGPSSlam());
            btn_delete_gps_map_slam.setChecked(AppSharePreference.getAppSharedPreference().loadDeleteGPSSlam());
            btn_auto_record_trace_spot.setChecked(AppSharePreference.getAppSharedPreference().loadAutoRecordPath());
            et_rollback_trace_path_num.setText(AppSharePreference.getAppSharedPreference().loadAutoRecordPathDistance()+"");
        } else if (v.getId() == R.id.iv_settings_back) {
            cl_area_map_view.setVisibility(View.VISIBLE);
            ll_map_settings.setVisibility(View.GONE);
        } else if (v.getId() == R.id.btn_action) {
            if (cl_action.getVisibility() == View.VISIBLE) {
                cl_action.setVisibility(View.GONE);
            } else {
                cl_action.setAlpha(0F);
                cl_action.setVisibility(View.VISIBLE);
                cl_action.animate().alpha(1f).setDuration(2000).start();
                ll_set_spot.setVisibility(View.GONE);
                ll_edit.setVisibility(View.GONE);
            }
        } else if (v.getId() == R.id.btn_set_spot) {
            if (ll_set_spot.getVisibility() == View.VISIBLE) {
                ll_set_spot.setVisibility(View.GONE);
            } else {
                ll_set_spot.setAlpha(0F);
                ll_set_spot.setVisibility(View.VISIBLE);
                ll_set_spot.animate().alpha(1f).setDuration(2000).start();
                cl_action.setVisibility(View.GONE);
                ll_edit.setVisibility(View.GONE);
            }
        } else if (v.getId() == R.id.btn_edit) {
            if (ll_edit.getVisibility() == View.VISIBLE) {
                ll_edit.setVisibility(View.GONE);
            } else {
                ll_edit.setAlpha(0F);
                ll_edit.setVisibility(View.VISIBLE);
                ll_edit.animate().alpha(1f).setDuration(2000).start();
                cl_action.setVisibility(View.GONE);
                ll_set_spot.setVisibility(View.GONE);
            }
        } else if (v.getId() == R.id.btn_reset_start_spot) {
//            showSetPosePop();
            resetStartSpot();
        } else if (v.getId() == R.id.btn_set_start_spot) {
            saveSpot(7, 0);
        }  else if (v.getId() == R.id.btn_set_recharge) {
            saveSpot(1, 0);
        } else if (v.getId() == R.id.btn_set_add_water) {
            saveSpot(2, 0);
        } else if (v.getId() == R.id.btn_set_empty_trash) {
            saveSpot(3, 0);
        } else if (v.getId() == R.id.btn_set_garage) {
            saveSpot(4, 0);
        }  else if (v.getId() == R.id.btn_set_fence_spot) {
            startRelocationOrSetCurrentMapOrAutoRecordPath(CommonConstants.TYPE_SET_GPS_FENCE, 1);
        } else if (v.getId() == R.id.btn_set_trace_spot) {
            if (currentPose.equals(lastPose)) {
                Toast.makeText(OperationAreaMapActivity.this, "重复循迹点", Toast.LENGTH_SHORT).show();
            } else {
                addSpotToList();
            }
        } else if (v.getId() == R.id.btn_trace_path_spot_save) {
            showTracePathChoice(tracePathList, CommonConstants.TYPE_TRACE_PATH_OPERATION_SAVE_SPOT);
        } else if (v.getId() == R.id.btn_trace_path_spot_not_save) {
            mTraceSpotList.clear();
            traceSpotAdapter.notifyDataSetChanged();
            ll_trace_path_spot.setVisibility(View.GONE);
            lastPose = "0";
        } else if (v.getId() == R.id.btn_set_common_spot) {
            showMapChoice();
        } else if (v.getId() == R.id.btn_save_map) {
            showEditDialog();
        } else if (v.getId() == R.id.btn_relocation) {
//            mAgent.clearMap();
            clearTraceSpotList();
            DialogUtil.showProgressDialog(this, getResources().getString(R.string.yl_common_reloading), false, false);
            startRelocationOrSetCurrentMapOrAutoRecordPath(CommonConstants.TYPE_RELOCATION, 1);
        } else if (v.getId() == R.id.btn_trace_path) {
            ll_trace_path_operation.setVisibility(View.VISIBLE);
            ll_trace_path_operation.animate().translationY(200).setDuration(1000).start();
        } else if (v.getId() == R.id.btn_trace_path_show) {
            if (tracePathList.size() > 1) {
                showTracePathChoice(tracePathList, CommonConstants.TYPE_TRACE_PATH_OPERATION_SHOW_PATH);
            } else if (tracePathList.size() == 1) {
                tracePath = tracePathList.get(0);
                scheduledThreadGetTraceSpot = new ScheduledThreadPoolExecutor(6);
                scheduledThreadGetTraceSpot.scheduleAtFixedRate(new CommTaskUtils.TaskGetTraceSpot(device, tracePath, true),
                        0, 10, TimeUnit.SECONDS);
//                showTracePath();
            } else {
                Toast.makeText(this, "请先创建循迹路径并添加循迹点", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.btn_trace_path_hide) {
            hideTracePath();
        } else if (v.getId() == R.id.btn_trace_path_rollback) {
            rollBackTracePath();
        } else if (v.getId() == R.id.ibtn_trace_path_close) {
            ll_trace_path_operation.setVisibility(View.GONE);
        } else if (v.getId() == R.id.ibtn_map_set_centred) {
            mMapView.setCentred();
        } else if (v.getId() == R.id.btn_virtual_walls) {
            setVWalls();
        } else if (v.getId() == R.id.btn_virtual_tracks) {
            setVTracks();
        }  else if (v.getId() == R.id.btn_clear_map) {
            mAgent.clearMap();
            clearTraceSpotList();
        }  else if (v.getId() == R.id.btn_load_map) {
            startRelocationOrSetCurrentMapOrAutoRecordPath(CommonConstants.TYPE_LOAD_MAP, 1);
        } else if (v.getId() == R.id.ll_add_virtual_wall_line) {
            addVirtualWall();
        } else if (v.getId() == R.id.ibtn_add_virtual_wall_ok) {
            mAgent.addVwalls(addVwallsList);
        } else if (v.getId() == R.id.ibtn_add_virtual_wall_cancel) {
            virtualWallsComplete();
        } else if (v.getId() == R.id.ll_add_virtual_wall_all_delete) {
            DialogUtil.showChoiceDialog(this, R.string.yl_common_delete_confirm, CommonConstants.TYPE_DELETE_ALL_VIRTUAL_WALL);
        } else if (v.getId() == R.id.ll_virtual_wall_delete) {
            mMapView.setVwalls(addVwallsList, CommonConstants.TYPE_DELETE_VIRTUAL_WALL);
        } else if (v.getId() == R.id.ll_add_virtual_track_line) {
            addVirtualTrack();
        } else if (v.getId() == R.id.ibtn_add_virtual_track_ok) {
            mAgent.addVtracks(addVTracksList);
        } else if (v.getId() == R.id.ibtn_add_virtual_track_cancel) {
            virtualTracksComplete();
        } else if (v.getId() == R.id.ll_add_virtual_track_all_delete) {
            DialogUtil.showChoiceDialog(this, R.string.yl_common_delete_confirm, CommonConstants.TYPE_DELETE_ALL_VIRTUAL_TRACK);
        } else if (v.getId() == R.id.ll_virtual_track_delete) {
            mMapView.setVtracks(addVTracksList, CommonConstants.TYPE_DELETE_VIRTUAL_TRACK);
        } else if (v.getId() == R.id.save_virtual_track) {
            showVirtualTrackChoice(virtualTrackGroupList);
        }
    }

    private void resetStartSpot() {
        scheduledThreadSetPose = new ScheduledThreadPoolExecutor(4);
        scheduledThreadSetPose.scheduleAtFixedRate(new CommTaskUtils.TaskSetPose(device, mapPages),
                0, 5, TimeUnit.SECONDS);
    }

    private void showSetPosePop() {
        new XPopup.Builder(this)
                .autoOpenSoftInput(true)
                .asCustom(new PoseBottomPopupView(this, device, mapPages))
                .show();
    }

    private void saveTracePathPic() {
        mAgent.saveTracePathPic(this, mMapView, mServerTraceRobotSpotList, tracePath);
    }

    private void saveMapPic() {
        mAgent.saveMapPic(this, mMapView, mapPages);
    }

    private void showVirtualTrackChoice(final List<VirtualTrackGroup> virtualTrackGroupList) {
        if (virtualTrackGroupList.size() == 1) {

        } else if (virtualTrackGroupList.size() > 1) {
            String[] vTracks = new String[virtualTrackGroupList.size()];
            for (int i = 0; i < virtualTrackGroupList.size(); i++){
                vTracks[i] = virtualTrackGroupList.get(i).getName();
            }
            new XPopup.Builder(this)
                    .asCenterList(getResources().getString(R.string.yl_common_choice_virtual_track_group), vTracks,
                            new OnSelectListener() {
                                @Override
                                public void onSelect(int position, String text) {
                                    LogUtil.i(OperationAreaMapActivity.this, TAG, "click : "+virtualTrackGroupList.get(position).getName());
                                    saveVirtualTrackNotice(virtualTrackGroupList.get(position));
                                }
                            })
                    .show();
        }
    }

    private void saveVirtualTrackNotice(VirtualTrackGroup virtualTrackGroup) {
        scheduledThreadSaveVirtualTrack = new ScheduledThreadPoolExecutor(5);
        scheduledThreadSaveVirtualTrack.scheduleAtFixedRate(new CommTaskUtils.TaskSaveVirtualTrack(device, mapPages, virtualTrackGroup),
                0, 5, TimeUnit.SECONDS);
    }

    private void addVirtualWall() {
        PointF startF = mMapView.widgetCoordinateToMapCoordinate(mMapView.getWidth() / 2 - 100, mMapView.getHeight() / 2);
        PointF endF = mMapView.widgetCoordinateToMapCoordinate(mMapView.getWidth() / 2 + 100, mMapView.getHeight() / 2);
        if (addVwallsList == null ) {
            addVwallsList = new ArrayList<>();
        }
        Line line = new Line(0, startF, endF);
        mAgent.addVwall(line, CommonConstants.TYPE_ADD_VIRTUAL_WALL);
    }

    private void addVirtualTrack() {
        PointF startF = mMapView.widgetCoordinateToMapCoordinate(mMapView.getWidth() / 2 - 100, mMapView.getHeight() / 2);
        PointF endF = mMapView.widgetCoordinateToMapCoordinate(mMapView.getWidth() / 2 + 100, mMapView.getHeight() / 2);
        if (addVTracksList == null ) {
            addVTracksList = new ArrayList<>();
        }
        Line line = new Line(0, startF, endF);
        mAgent.addVtrack(line, CommonConstants.TYPE_ADD_VIRTUAL_TRACK);
    }

    private void setVWalls() {
        ll_top.setVisibility(View.GONE);
        ll_bottom.setVisibility(View.GONE);
        ll_edit.setVisibility(View.GONE);
        ll_map_virtual_walls.setVisibility(View.VISIBLE);
    }

    private void setVTracks() {
        ll_top.setVisibility(View.GONE);
        ll_bottom.setVisibility(View.GONE);
        ll_edit.setVisibility(View.GONE);
        ll_map_virtual_tracks.setVisibility(View.VISIBLE);
    }

    private void startRelocationOrSetCurrentMapOrAutoRecordPath(int type, int status) {
        if (scheduledThreadRelocationMapPages == null) scheduledThreadRelocationMapPages = new ScheduledThreadPoolExecutor(4);
        scheduledThreadRelocationMapPages.scheduleAtFixedRate(new CommTaskUtils.TaskRelocationOrSetCurrentMap(device, mapPages, type, status),
                0, 5, TimeUnit.SECONDS);
    }

    private void showEditDialog() {
        mapBottomPopupView = new MapBottomPopupView(this, device, mapPages);
        new XPopup.Builder(this)
                .autoOpenSoftInput(true)
                .asCustom(mapBottomPopupView)
                .show();
    }

    /**
     * 获取工控机保存标志位
     */
    private void getSaveMapFlag() {
        scheduledThreadGetSaveMapFlag = new ScheduledThreadPoolExecutor(3);
        scheduledThreadGetSaveMapFlag.scheduleAtFixedRate(new CommTaskUtils.TaskGetSaveMapFlag(device),
                0, 4, TimeUnit.SECONDS);
    }

    private void showTracePathChoice(List<TracePath> list, int typeOperation) {
        View view = LayoutInflater.from(this).inflate(R.layout.yl_common_dialog_list_choice, null);
        RecyclerView recyclerView = view.findViewById(R.id.rl_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //添加Android自带的分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new BaseTraceAdapter(this, list, typeOperation));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择循迹路径");
        builder.setView(view);
        if (typeOperation == CommonConstants.TYPE_TRACE_PATH_OPERATION_SAVE_SPOT) {
            builder.setNeutralButton(R.string.yl_common_create_new, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    BaseTraceBottomPopupView traceBottomPopupView = new BaseTraceBottomPopupView(OperationAreaMapActivity.this, device, mapPages, CommonConstants.TYPE_MAPPAGE_OPERATION_TRACE_PATH);
                    new XPopup.Builder(OperationAreaMapActivity.this)
                            .autoOpenSoftInput(true)
                            .asCustom(traceBottomPopupView)
                            .show();
                }
            });
        }
        tracePathChoiceDialog = builder.create();
        tracePathChoiceDialog.show();
    }

    private void showMapChoice() {
        View view = LayoutInflater.from(this).inflate(R.layout.yl_common_dialog_list_choice, null);
        RecyclerView recyclerView = view.findViewById(R.id.rl_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //添加Android自带的分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new MapPagesChoiceAdapter(this, mapPagesList, CommonConstants.TYPE_MAPPAGE_OPERATION_ADD_COMMON_SPOT_CHOICE));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("区域地图");
        builder.setMessage("选择一个需要设置公共定位点的区域，方便设备进行地图切换");
        builder.setView(view);
        tracePathChoiceDialog = builder.create();
        tracePathChoiceDialog.show();
    }

    private void showTracePath() {
        showTracePathLines = DBUtil.getTracePathLines(mServerTraceRobotSpotList);
        if (showTracePathLines.size() > 0) {
            mMapView.setVTracePath(showTracePathLines);
            btn_trace_path_hide.setEnabled(true);
            btn_trace_path_rollback.setEnabled(true);
        } else {
            Toast.makeText(this, "请先添加循迹点", Toast.LENGTH_SHORT).show();
        }
    }

    private void hideTracePath() {
        if (showTracePathLines.size() > 0) {
            btn_trace_path_hide.setEnabled(false);
            mMapView.clearVTracePath(showTracePathLines);
        }
    }

    private void rollBackTracePath() {
//        List<RobotSpot> localTracePathSpotList = DBUtil.getTracePathSpotList(tracePath, mapPages);
        if (mServerTraceRobotSpotList != null && mServerTraceRobotSpotList.size() > 0) {
            DialogUtil.showProgressDialog(this, getResources().getString(R.string.yl_common_deleting), false, false);
            scheduledThreadDeleteTraceSpot = new ScheduledThreadPoolExecutor(8);
//            scheduledThreadDeleteTraceSpot.scheduleAtFixedRate(new CommTaskUtils.TaskDeleteTraceSpot(mServerTraceRobotSpotList, localTracePathSpotList),
            scheduledThreadDeleteTraceSpot.scheduleAtFixedRate(new CommTaskUtils.TaskDeleteTraceSpot(mServerTraceRobotSpotList),
                    0, 20, TimeUnit.SECONDS);
        } else {
            Toast.makeText(this, "当前没有循迹路径", Toast.LENGTH_SHORT).show();
        }
//        showTracePathLines = DBUtil.rollbackTracePathLines(showTracePathLines, mapPages);
//        if (showTracePathLines.size() > 0) {
//            mMapView.setVTracePath(showTracePathLines);
//            btn_trace_path_hide.setEnabled(true);
//        }
    }

    private void saveSpot(int spotFlag, int targetMapPageId) {
        if (!"0".equals(currentPose)) {
            DialogUtil.showProgressDialog(this, getResources().getString(R.string.yl_common_saving), false, false);
            scheduledThreadSaveTraceSpot = new ScheduledThreadPoolExecutor(4);
            scheduledThreadSaveTraceSpot.scheduleAtFixedRate(new CommTaskUtils.TaskSaveTraceSpot(device, spotFlag, currentPose, mapPages.getId(), targetMapPageId),
                    0, 5, TimeUnit.SECONDS);
        } else {
            Toast.makeText(this, "点位错误", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.getCheckedRadioButtonId() == R.id.rb_navigation_mode_freedom) {
            mAgent.setNavigationMode(0);
        } else if (group.getCheckedRadioButtonId() == R.id.rb_navigation_mode_track) {
            mAgent.setNavigationMode(1);
        } else if (group.getCheckedRadioButtonId() == R.id.rb_navigation_mode_track_first) {
            mAgent.setNavigationMode(2);
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                if (currentPose.equals(lastPose)) {
                    Toast.makeText(OperationAreaMapActivity.this, "重复路径点", Toast.LENGTH_SHORT).show();
                } else {
                    if (!"0".equals(currentPose)) {
                        LogUtil.d(TAG, "lastPose:"+lastPose);
                        LogUtil.d(TAG, "currentPose:"+currentPose);
                        if (TextUtils.isEmpty(lastPose) || "0".equals(lastPose)) {
                            addSpotToList();
                        } else {
                            if (!lastPose.equals(currentPose)) {
                                mAgent.calculateDistance(OperationAreaMapActivity.this, mMapView, lastPose, currentPose);
                            }
                        }
                    }
                }
            }
        }
    };

    private void addSpotToList() {
        mTraceSpotList.add(currentPose);
        traceSpotAdapter.notifyDataSetChanged();
        ll_trace_path_spot.setVisibility(View.VISIBLE);
        lastPose = currentPose;
        Toast.makeText(OperationAreaMapActivity.this, "已添加路径点到列表", Toast.LENGTH_SHORT).show();
    }

    private void clearTraceSpotList() {
        mTraceSpotList.clear();
        traceSpotAdapter.notifyDataSetChanged();
        currentPose = "0";
    }

    private Runnable mRecordSpotRunnable = new Runnable() {

        @Override
        public void run() {
            try {
                while (isAutoRecordSpot) {
                    if (mRecordSpotThread.isInterrupted()) {
                        throw new InterruptedException();
                    }
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                    SystemClock.sleep(5000);
                }
            } catch (InterruptedException e) {
                LogUtil.d(TAG, "mRecordSpotRunnable stop");
            }
        }
    };
    Thread mRecordSpotThread = new Thread(mRecordSpotRunnable);

    private void stopRecordSpot() {
        if (mRecordSpotThread != null && !mRecordSpotThread.isInterrupted()) {
            mRecordSpotThread.interrupt();
        }
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.btn_auto_record_trace_spot) {
            LogUtil.i(this, TAG, "isChecked: "+isChecked);
            AppSharePreference.getAppSharedPreference().saveAutoRecordPath(isChecked);
            isAutoRecordSpot = isChecked;
            if (isChecked) {
                startRelocationOrSetCurrentMapOrAutoRecordPath(CommonConstants.TYPE_AUTO_RECORD_PATH, 1);
//                ll_trace_path_spot.setVisibility(View.VISIBLE);
//                if (!mRecordSpotThread.isAlive()) {
//                    mRecordSpotThread.start();
//                }
            } else {
                startRelocationOrSetCurrentMapOrAutoRecordPath(CommonConstants.TYPE_AUTO_RECORD_PATH, 0);
//                stopRecordSpot();
            }
        } else if (buttonView.getId() == R.id.btn_enable_gps_fence) {
            LogUtil.i(this, TAG, "isChecked: "+isChecked);
            AppSharePreference.getAppSharedPreference().saveEnableGPSFence(isChecked);
            if (isChecked) {
                startRelocationOrSetCurrentMapOrAutoRecordPath(CommonConstants.TYPE_ENABLE_GPS_FENCE, 1);
            } else {
                startRelocationOrSetCurrentMapOrAutoRecordPath(CommonConstants.TYPE_ENABLE_GPS_FENCE, 0);
            }
        } else if (buttonView.getId() == R.id.btn_log_gps_map_slam) {
            LogUtil.i(this, TAG, "isChecked: "+isChecked);
            AppSharePreference.getAppSharedPreference().saveLogGPSSlam(isChecked);
            if (isChecked) {
                startRelocationOrSetCurrentMapOrAutoRecordPath(CommonConstants.TYPE_LOG_GPS_MAP_SLAM, 1);
            } else {
                startRelocationOrSetCurrentMapOrAutoRecordPath(CommonConstants.TYPE_LOG_GPS_MAP_SLAM, 0);
            }
        } else if (buttonView.getId() == R.id.btn_delete_gps_map_slam) {
            LogUtil.i(this, TAG, "isChecked: "+isChecked);
            AppSharePreference.getAppSharedPreference().saveDeleteGPSSlam(isChecked);
            if (isChecked) {
                startRelocationOrSetCurrentMapOrAutoRecordPath(CommonConstants.TYPE_DELETE_GPS_MAP_SLAM, 1);
            } else {
                startRelocationOrSetCurrentMapOrAutoRecordPath(CommonConstants.TYPE_DELETE_GPS_MAP_SLAM, 0);
            }
        }
    }
}
