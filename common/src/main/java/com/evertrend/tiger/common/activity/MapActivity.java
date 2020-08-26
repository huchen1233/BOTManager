package com.evertrend.tiger.common.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.evertrend.tiger.common.R;
import com.evertrend.tiger.common.bean.RobotAction;
import com.evertrend.tiger.common.bean.mapview.MapView;
import com.evertrend.tiger.common.service.ConnectService;
import com.evertrend.tiger.common.utils.ConnectManager;
import com.evertrend.tiger.common.utils.EvertrendAgent;
import com.evertrend.tiger.common.utils.SessionManager;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.common.widget.ActionControllerView;

public class MapActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ActionControllerView.LongClickRepeatListener {
    public static final String TAG = MapActivity.class.getCanonicalName();

    private MapView mv_map;
    private RadioGroup rg_navigation;
    private LinearLayout ll_set_spot, ll_edit;
    private ConstraintLayout cl_action;
    private ActionControllerView acv_action;

    private EvertrendAgent mAgent;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yl_common_activity_map);
        initView();
        mAgent = getEvertrendAgent();
        mAgent.connectTo("192.168.11.102");
        //注册广播 用来接收服务器返回的信息
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(ConnectManager.BROADCAST_ACTION);
//        LocalBroadcastManager.getInstance(this).registerReceiver(actionBroadcastReceiver, filter);
//        //开启服务
//        intent = new Intent(MapActivity.this, ConnectService.class);
//        intent.putExtra("IP", "192.168.11.102");
//        intent.putExtra("port", 28700);
//        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAgent.disconnect();
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(actionBroadcastReceiver);
//        SessionManager.getmInstance().closeSession();
//        stopService(intent);
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
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        LogUtil.d(TAG,"checkedId : "+checkedId);
        if (group.getCheckedRadioButtonId() == R.id.tab_spot) {
            LogUtil.d(TAG, "tab_spot");
            ll_set_spot.setVisibility(View.VISIBLE);
            cl_action.setVisibility(View.GONE);
            ll_edit.setVisibility(View.GONE);
        } else if (group.getCheckedRadioButtonId() == R.id.tab_action) {
            LogUtil.d(TAG, "tab_action");
            ll_set_spot.setVisibility(View.GONE);
            cl_action.setVisibility(View.VISIBLE);
            ll_edit.setVisibility(View.GONE);
        } else if (group.getCheckedRadioButtonId() == R.id.tab_map_edit) {
            LogUtil.d(TAG, "tab_map_edit");
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
                    LogUtil.d(TAG, "longclock LEFT");
                    mAgent.moveBy(RobotAction.CMD.TURN_LEFT);
                    break;
                case ActionControllerView.TouchArea.TOP:
                    LogUtil.d(TAG, "longclock TOP");
                    mAgent.moveBy(RobotAction.CMD.FORWARD);
                    break;
                case ActionControllerView.TouchArea.RIGHT:
                    LogUtil.d(TAG, "longclock RIGHT");
                    mAgent.moveBy(RobotAction.CMD.TURN_RIGHT);
                    break;
                case ActionControllerView.TouchArea.BOTTOM:
                    LogUtil.d(TAG, "longclock BOTTOM");
                    mAgent.moveBy(RobotAction.CMD.BACKWARD);
                    break;
                case ActionControllerView.TouchArea.CENTER:
                    LogUtil.d(TAG, "longclock CENTER");
                    mAgent.cancelAllActions();
                    break;
            }
        }
    }
}