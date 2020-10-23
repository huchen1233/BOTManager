package com.evertrend.tiger.common.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.evertrend.tiger.common.R;
import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.bean.MapPages;
import com.evertrend.tiger.common.bean.RobotAction;
import com.evertrend.tiger.common.bean.event.SetPoseOKEvent;
import com.evertrend.tiger.common.bean.mapview.MapView;
import com.evertrend.tiger.common.bean.mapview.utils.SlamGestureDetector;
import com.evertrend.tiger.common.utils.EvertrendAgent;
import com.evertrend.tiger.common.utils.general.AppSharePreference;
import com.evertrend.tiger.common.utils.general.DialogUtil;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.common.utils.general.Utils;
import com.evertrend.tiger.common.utils.network.CommTaskUtils;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.slamtec.slamware.robot.Location;
import com.slamtec.slamware.robot.Pose;
import com.slamtec.slamware.robot.Rotation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MapSettingsBottomPopupView extends BottomPopupView implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private static final String TAG = MapSettingsBottomPopupView.class.getSimpleName();

    private EditText et_pose_x;
    private EditText et_pose_y;
    private EditText et_pose_yaw;
    private Button btn_set_pose;
    private RadioGroup rg_map_touch_mode;
    private RadioButton rb_mode_move_map, rb_mode_rotate_pose_angle;

    private Context context;
    private Device device;
    private MapPages mapPages;
    private EvertrendAgent agent;
    private MapView mapView;

    public MapSettingsBottomPopupView(Context context, Device device, EvertrendAgent agent, MapView mapView) {
        super(context);
        this.context = context;
        this.device = device;
        this.agent = agent;
        this.mapView = mapView;
    }

    public MapSettingsBottomPopupView(Context context, Device device, MapPages mapPages) {
        super(context);
        this.context = context;
        this.device = device;
        this.mapPages = mapPages;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.yl_common_dialog_settings;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        exit();
        super.onDetachedFromWindow();
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext()) * .5f);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SetPoseOKEvent event) {
        DialogUtil.hideProgressDialog();
        dismiss();
    }

    private void exit() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void initView() {
        et_pose_x = findViewById(R.id.et_pose_x);
        et_pose_y = findViewById(R.id.et_pose_y);
        et_pose_yaw = findViewById(R.id.et_pose_yaw);
        btn_set_pose = findViewById(R.id.btn_set_pose);
        btn_set_pose.setOnClickListener(this);
        rb_mode_move_map = findViewById(R.id.rb_mode_move_map);
        rb_mode_rotate_pose_angle = findViewById(R.id.rb_mode_rotate_pose_angle);
        if (AppSharePreference.getAppSharedPreference().loadMapTouchMode() == SlamGestureDetector.MODE_NONE) {
            rb_mode_move_map.setChecked(true);
        } else {
            rb_mode_rotate_pose_angle.setChecked(true);
        }
        rg_map_touch_mode = findViewById(R.id.rg_map_touch_mode);
        rg_map_touch_mode.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_set_pose) {
            String poseX = et_pose_x.getText().toString().trim();
            String poseY = et_pose_y.getText().toString().trim();
            String poseYaw = et_pose_yaw.getText().toString().trim();
            if (TextUtils.isEmpty(poseX) || TextUtils.isEmpty(poseY) || TextUtils.isEmpty(poseYaw)) {
                Toast.makeText(context, "请输入坐标", Toast.LENGTH_SHORT).show();
            } else if (!Utils.isNumeric(poseX) || !Utils.isNumeric(poseY) || !Utils.isNumeric(poseYaw)) {
                Toast.makeText(context, "输入坐标类型错误", Toast.LENGTH_SHORT).show();
            } else {
                String pose = poseX+","+poseY+","+poseYaw;
                LogUtil.d(TAG, "set pose");
                Location location = new Location();
                location.setX(Float.parseFloat(poseX));
                location.setY(Float.parseFloat(poseY));
                Rotation rotation = new Rotation(Float.parseFloat(poseYaw));
                agent.setPose(new Pose(location, rotation));
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int i) {
        if (group.getCheckedRadioButtonId() == R.id.rb_mode_move_map) {
            LogUtil.d(TAG, "rb_mode_move_map");
            mapView.setGestureMode(SlamGestureDetector.MODE_NONE);
            AppSharePreference.getAppSharedPreference().saveMapTouchMode(SlamGestureDetector.MODE_NONE);
            dismiss();
        } else if (group.getCheckedRadioButtonId() == R.id.rb_mode_rotate_pose_angle) {
            LogUtil.d(TAG, "rb_mode_rotate_pose_angle");
            mapView.setGestureMode(SlamGestureDetector.MODE_ROTATE_POSE_ANGLE);
            AppSharePreference.getAppSharedPreference().saveMapTouchMode(SlamGestureDetector.MODE_ROTATE_POSE_ANGLE);
            dismiss();
        }
    }
}
