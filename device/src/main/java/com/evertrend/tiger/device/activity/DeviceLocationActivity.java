package com.evertrend.tiger.device.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.common.utils.general.Utils;
import com.evertrend.tiger.device.R;
import com.evertrend.tiger.device.utils.Constants;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class DeviceLocationActivity extends AppCompatActivity {
    public static final String TAG = DeviceLocationActivity.class.getSimpleName();

    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.yl_device_fragment_location);
        device = (Device) getIntent().getSerializableExtra("device");
        mLocationClient = new LocationClient(this);
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        initView();
        setMarker();
        DeviceLocationActivityPermissionsDispatcher.mulPermissionWithPermissionCheck(DeviceLocationActivity.this);
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
        mLocationClient.stop();
    }

    private void initView() {
        mMapView = findViewById(R.id.bmapView);
        //是否显示缩放按钮
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        //普通地图 ,mBaiduMap是地图控制器对象
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //开启交通图
        mBaiduMap.setTrafficEnabled(true);
        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，设置定位模式，默认高精度
//LocationMode.Hight_Accuracy：高精度；
//LocationMode. Battery_Saving：低功耗；
//LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType(BDLocation.BDLOCATION_GCJ02_TO_BD09LL);
//可选，设置返回经纬度坐标类型，默认GCJ02
//GCJ02：国测局坐标；
//BD09ll：百度经纬度坐标；
//BD09：百度墨卡托坐标；
//海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        option.setScanSpan(5*1000);
//可选，设置发起定位请求的间隔，int类型，单位ms
//如果设置为0，则代表单次定位，即仅定位一次，默认为0
//如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
//可选，设置是否使用gps，默认false
//使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
//可选，定位SDK内部是一个service，并放到了独立进程。
//设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
//可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000);
//可选，V7.2版本新增能力
//如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false);
//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        option.setNeedNewVersionRgc(true);
//可选，设置是否需要最新版本的地址信息。默认需要，即参数为true

        mLocationClient.setLocOption(option);
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
    }

    private void setMarker() {
        mBaiduMap.clear();
        LatLng ll = null;
        LogUtil.d(TAG, "lat=" + device.getLatitude());
        LogUtil.d(TAG, "lng=" + device.getLongitude());
        if (device.getLongitude() != 0.0 && device.getLatitude() != 0.0) {
            mLocationClient.stop();
            LogUtil.d(TAG, "desc=" + device.getDescription());
//                    ll = new LatLng(device.getLatitude(), device.getLongitude());
            ll = Utils.GPSCoordinateToBaiduCoordinate(new LatLng(device.getLatitude(), device.getLongitude()));
            Bundle mbundle = new Bundle();
            mbundle.putSerializable("device", device);
            OverlayOptions overlayOptions = new MarkerOptions()
                    .position(ll)
//                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_device))
                    .icon(BitmapDescriptorFactory.fromBitmap(getViewBitmap(device.getDescription())))
                    .extraInfo(mbundle);
            mBaiduMap.addOverlay(overlayOptions);
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        } else {
            LogUtil.d(TAG, "显示手机获取GPS位置");
            mLocationClient.start();
        }
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            LogUtil.d(TAG, "lat: " + latitude);
            LogUtil.d(TAG, "lon: " + longitude);
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f

            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

            int errorCode = location.getLocType();
            LogUtil.d(TAG, "errorCode: " + errorCode);
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            LatLng ll = new LatLng(latitude, longitude);
            mBaiduMap.addOverlay(new MarkerOptions().position(ll)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.set_spot)));
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }

    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION})
    void mulPermission() {
        if (device.getLongitude() != 0.0 && device.getLatitude() != 0.0) {
            mLocationClient.stop();
        } else {
            mLocationClient.start();
        }
    }

    @OnShowRationale({Manifest.permission.ACCESS_COARSE_LOCATION})
    void showRationaleForMulPermission(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.yl_device_permission_location)
                .setPositiveButton(android.R.string.yes, (dialog, button) -> request.proceed())
                .setNegativeButton(android.R.string.no, (dialog, button) -> request.cancel())
                .show();
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_COARSE_LOCATION)
    void showDeniedForLocation() {
        Toast.makeText(this, R.string.yl_device_permission_location_denied, Toast.LENGTH_LONG).show();
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_COARSE_LOCATION)
    void showNeverAskForLocation() {
        Toast.makeText(this, R.string.yl_device_permission_location_neverask, Toast.LENGTH_LONG).show();
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        DeviceLocationActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private Bitmap getViewBitmap(String text) {
        View addViewContent = LayoutInflater.from(this).inflate(R.layout.yl_common_view_map_point, null);
        ImageView iv_device = addViewContent.findViewById(R.id.iv_device);
        TextView textView = addViewContent.findViewById(R.id.tv_name);
        switch (device.getDevice_type()) {
            case Constants.DEVICE_TYPE_EVBOT_SL:
                iv_device.setImageResource(R.drawable.evbot_sl);
                break;
            case Constants.DEVICE_TYPE_SWBOT_SL:
                iv_device.setImageResource(R.drawable.swbot_sl);
                break;
            case Constants.DEVICE_TYPE_MFBOT_SL:
                iv_device.setImageResource(R.drawable.mfbot_sl);
                break;
            case Constants.DEVICE_TYPE_SWBOT_AP:
                iv_device.setImageResource(R.drawable.swbot_ap);
                break;
            case Constants.DEVICE_TYPE_SWBOT_MINI:
                iv_device.setImageResource(R.drawable.swbot_mini);
                break;
            default:
                iv_device.setImageResource(R.drawable.evbot_sl);
                break;
        }
        textView.setText(text);
        addViewContent.setDrawingCacheEnabled(true);
        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());
        addViewContent.buildDrawingCache();

        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        return bitmap;
    }
}