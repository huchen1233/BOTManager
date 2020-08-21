package com.evertrend.tiger.common.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.evertrend.tiger.common.R;
import com.evertrend.tiger.common.bean.mapview.MapView;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.common.widget.ActionControllerView;

public class MapActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, ActionControllerView.LongClickRepeatListener {
    public static final String TAG = MapActivity.class.getCanonicalName();

    private MapView mv_map;
    private RadioGroup rg_navigation;
    private LinearLayout ll_set_spot, ll_edit;
    private ConstraintLayout cl_action;
    private ActionControllerView acv_action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yl_common_activity_map);
        initView();
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
                    break;
                case ActionControllerView.TouchArea.TOP:
                    LogUtil.d(TAG, "longclock TOP");
                    break;
                case ActionControllerView.TouchArea.RIGHT:
                    LogUtil.d(TAG, "longclock RIGHT");
                    break;
                case ActionControllerView.TouchArea.BOTTOM:
                    LogUtil.d(TAG, "longclock BOTTOM");
                    break;
                case ActionControllerView.TouchArea.CENTER:
                    LogUtil.d(TAG, "longclock CENTER");
                    break;
            }
        }
    }
}