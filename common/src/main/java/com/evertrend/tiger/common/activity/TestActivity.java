package com.evertrend.tiger.common.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.evertrend.tiger.common.R;
import com.evertrend.tiger.common.adapter.IPConnectionHistoryAdapter;
import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.bean.IPConnection;
import com.evertrend.tiger.common.bean.MapPages;
import com.evertrend.tiger.common.bean.event.IPConnectionChoiceEvent;
import com.evertrend.tiger.common.bean.event.slamtec.ConnectedEvent;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.common.utils.general.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    public static final String TAG = TestActivity.class.getCanonicalName();

    private EditText et_ip;
    private Button btn_connect;
    private RecyclerView rlv_connection_history;

    private Device device;
    private MapPages mapPages;
    private List<IPConnection> connectionList;
    private IPConnectionHistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yl_common_activity_test);
        EventBus.getDefault().register(this);
        device = (Device) getIntent().getSerializableExtra("device");
        mapPages = (MapPages) getIntent().getSerializableExtra("mappage");
        et_ip = findViewById(R.id.et_ip);
        btn_connect = findViewById(R.id.btn_connect);
        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ip = et_ip.getText().toString().intern();
                if (TextUtils.isEmpty(ip)) {
                    Toast.makeText(TestActivity.this, "请输入IP", Toast.LENGTH_LONG).show();
                } else if (!Utils.isIP(ip)) {
                    Toast.makeText(TestActivity.this, "IP格式错误", Toast.LENGTH_LONG).show();
                } else {
                    saveIP(ip);
                    Intent intent = new Intent();
                    intent.putExtra("ip", ip);
                    intent.putExtra("device", device);
                    intent.putExtra("mappage", mapPages);
//                    intent.setAction("android.intent.action.VirtualWallActivity");
                    intent.setAction("android.intent.action.MapActivity");
                    startActivity(intent);
                    finish();
                }
            }
        });
        rlv_connection_history = findViewById(R.id.rlv_connection_history);
        rlv_connection_history.setHasFixedSize(true);
        rlv_connection_history.setItemAnimator(new DefaultItemAnimator());
        rlv_connection_history.setLayoutManager(new LinearLayoutManager(this));
        //添加Android自带的分割线
        rlv_connection_history.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        connectionList = LitePal.order("timeStamp desc").find(IPConnection.class);
        historyAdapter = new IPConnectionHistoryAdapter(this, connectionList);
        View view = LayoutInflater.from(this).inflate(R.layout.yl_common_view_ip_connection_item_header, null);
        historyAdapter.setHeaderView(view);
        rlv_connection_history.setAdapter(historyAdapter);
        et_ip.setText(connectionList.get(0).getIp());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(IPConnectionChoiceEvent event) {
        LogUtil.d(TAG, "IPConnectionChoiceEvent");
        et_ip.setText(event.getIpConnection().getIp());
    }

    private void saveIP(String ip) {
        boolean isOld = false;
        for (int i = 0; i < connectionList.size(); i++) {
            IPConnection ipConnection = connectionList.get(i);
            if (ip.equals(ipConnection.getIp())) {
                ipConnection.setTimeStamp(System.currentTimeMillis());
                ipConnection.save();
                isOld = true;
                break;
            }
        }
        if (!isOld) {
            IPConnection ipConnection = new IPConnection();
            ipConnection.setIp(ip);
            ipConnection.setTimeStamp(System.currentTimeMillis());
            ipConnection.save();
        }
    }
}