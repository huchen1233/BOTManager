package com.evertrend.tiger.common.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.evertrend.tiger.common.R;
import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.bean.MapPages;
import com.evertrend.tiger.common.bean.event.DeviceMessageEvent;
import com.evertrend.tiger.common.bean.event.GetGpsFenceFailEvent;
import com.evertrend.tiger.common.bean.event.GetGpsFenceOKEvent;
import com.evertrend.tiger.common.utils.general.DialogUtil;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.common.utils.network.CommTaskUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GpsFenceActivity extends AppCompatActivity {
    private static final String TAG = GpsFenceActivity.class.getSimpleName();

    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private Device device;
    private MapPages mapPages;
    //多边形顶点位置
    List<LatLng> points;

    private ScheduledThreadPoolExecutor scheduledThreadGetGPSFence;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.yl_common_activity_gps_fence);
        device = (Device) getIntent().getSerializableExtra("device");
        mapPages = (MapPages) getIntent().getSerializableExtra("mappage");
        initView();
//        showFence();
        showMarker();
        points = new ArrayList<>();
        scheduledThreadGetGPSFence = new ScheduledThreadPoolExecutor(3);
        scheduledThreadGetGPSFence.scheduleAtFixedRate(new CommTaskUtils.TaskGetGPSFence(device, mapPages),
                0, 6, TimeUnit.SECONDS);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(DeviceMessageEvent messageEvent) {
        device = messageEvent.getMessage();
//        updateMarker();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GetGpsFenceOKEvent messageEvent) {
        stopGetGPSFenceTimer();
        points = messageEvent.getPoints();
        LogUtil.d(TAG, "points.size(): "+points.size());
        if (points.size() > 3) {
            showFence();
        } else {
            DialogUtil.showToast(this, "围栏点过少，无法形成围栏", Toast.LENGTH_LONG);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GetGpsFenceFailEvent messageEvent) {
        DialogUtil.showToast(this, messageEvent.getDesc(), Toast.LENGTH_LONG);
        stopGetGPSFenceTimer();
    }

    private void showMarker() {
//        mBaiduMap.clear();
        LatLng ll = null;
        LogUtil.d(TAG, "lat=" + device.getLatitude());
        LogUtil.d(TAG, "lng=" + device.getLongitude());
        device.setLatitude(31.40143);
        device.setLongitude(121.49022);
        if (device.getLongitude() != 0.0 && device.getLatitude() != 0.0) {
            LogUtil.d(TAG, "desc=" + device.getDescription());
            ll = new LatLng(device.getLatitude(), device.getLongitude());
            OverlayOptions overlayOptions = new MarkerOptions()
                    .position(ll)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka));
            mBaiduMap.addOverlay(overlayOptions);
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }

    private void showFence() {
//        points.add(new LatLng(31.38475, 121.49473));
//        points.add(new LatLng(31.39669, 121.48044));
//        points.add(new LatLng(31.39274, 121.48157));
//        points.add(new LatLng(31.39743, 121.49469));
//        points.add(new LatLng(31.38475, 121.49473));

        //构造PolygonOptions
        PolygonOptions mPolygonOptions = new PolygonOptions()
                .points(points)
                .fillColor(0xAAFFFF00) //填充颜色
                .stroke(new Stroke(5, 0xAA00FF00)); //边框宽度和颜色

        //在地图上显示多边形
        mBaiduMap.addOverlay(mPolygonOptions);
    }

    private void initView() {
        mMapView = findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        stopGetGPSFenceTimer();
        mMapView.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    private void stopGetGPSFenceTimer() {
        if (scheduledThreadGetGPSFence != null) {
            scheduledThreadGetGPSFence.shutdownNow();
            scheduledThreadGetGPSFence = null;
        }
    }
}