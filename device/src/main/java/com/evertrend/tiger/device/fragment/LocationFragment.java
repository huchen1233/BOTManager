package com.evertrend.tiger.device.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.bean.event.SuccessEvent;
import com.evertrend.tiger.common.fragment.BaseFragment;
import com.evertrend.tiger.common.utils.general.CommonConstants;
import com.evertrend.tiger.common.utils.general.DialogUtil;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.device.R;
import com.evertrend.tiger.device.bean.event.LoadDevicesEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class LocationFragment extends BaseFragment {
    public static final String TAG = LocationFragment.class.getSimpleName();

    private View root;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;

    private List<Device> deviceList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.yl_device_fragment_location, container, false);
        initView();
        deviceList = new ArrayList<>();
        EventBus.getDefault().register(this);
        return root;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(LoadDevicesEvent event) {
        LogUtil.d(TAG, "===LoadDevicesEvent===");
        deviceList.clear();
        deviceList.addAll(event.getDeviceList());
        setMarker();
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SuccessEvent event) {
        LogUtil.i(getContext(), TAG, "===SuccessEvent===");
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mMapView = null;
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void initView() {
        mMapView = root.findViewById(R.id.bmapView);
        //是否显示缩放按钮
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        //普通地图 ,mBaiduMap是地图控制器对象
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //开启交通图
        mBaiduMap.setTrafficEnabled(true);
        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
//                showCarDetailInfo();
                return true;
            }
        });
    }

    private void setMarker() {
        mBaiduMap.clear();
        LogUtil.d(TAG, "size : "+deviceList.size());
        if (deviceList.size() > 0) {
            for (Device device : deviceList) {
                LatLng ll = null;
                LogUtil.d(TAG, "device.getLatitude()=" + device.getLatitude());
                LogUtil.d(TAG, "device.getLongitude()=" + device.getLongitude());
                ll = new LatLng(device.getLatitude(), device.getLongitude());
                mBaiduMap.addOverlay(new MarkerOptions().position(ll)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        } else {
            LogUtil.d(TAG, "显示手机获取GPS位置");
        }
    }
}
