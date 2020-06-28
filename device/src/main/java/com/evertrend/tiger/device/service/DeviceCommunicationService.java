package com.evertrend.tiger.device.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;

import com.alibaba.fastjson.JSONObject;
import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.bean.event.DeviceExceptionEvent;
import com.evertrend.tiger.common.utils.general.CommonConstants;
import com.evertrend.tiger.common.utils.general.JsonAnalysisUtil;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.common.utils.general.Utils;
import com.evertrend.tiger.common.utils.network.CommTaskUtils;
import com.evertrend.tiger.common.utils.network.CommonNetReq;
import com.evertrend.tiger.device.R;
import com.evertrend.tiger.device.activity.DeviceExceptionPageActivity;
import com.evertrend.tiger.device.activity.DeviceMainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DeviceCommunicationService extends Service {
    private static final String TAG = DeviceCommunicationService.class.getSimpleName();

    private ScheduledThreadPoolExecutor scheduledThreadQueryDeviceException;
    private ScheduledThreadPoolExecutor scheduledThreadGetDeviceException;

    public DeviceCommunicationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(TAG, "==onCreate==");
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(TAG, "scheduledThreadTask:"+scheduledThreadQueryDeviceException);
        if (scheduledThreadQueryDeviceException == null) {
            scheduledThreadQueryDeviceException = new ScheduledThreadPoolExecutor(4);
            scheduledThreadQueryDeviceException.scheduleAtFixedRate(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            startQueryDeviceExceptionTimer();
                                                        }
                                                    },
                    0, CommonConstants.GET_DEVICE_EXCEPTION_TIME, TimeUnit.MINUTES);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopQueryDeviceExceptionTimer();
        stopGetDeviceExceptionTimer();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventMainThread(DeviceExceptionEvent event) {
        stopGetDeviceExceptionTimer();
        JSONObject jsonObject = event.getJsonObject();
        List<Device> exceptionDeviceList = JsonAnalysisUtil.loadAllDevice(jsonObject.getJSONArray(CommonNetReq.RESULT_DATA));
        List<Device> deviceList = JsonAnalysisUtil.loadAllDevice(jsonObject.getJSONArray(CommonNetReq.RESULT_EXTRA));
        LogUtil.d(TAG, "deviceList size: "+deviceList.size());
        LogUtil.d(TAG, "fault notice: "+exceptionDeviceList.size());
        if (exceptionDeviceList.size() > 0) {
            for (Device device: exceptionDeviceList) {
                List<Device> devices = LitePal.where("device_id = ?", device.getDevice_id()).find(Device.class);
                if (devices.size() > 0) {
                    if (device.getAlarm_info().equals(devices.get(0).getAlarm_info())) {
                        LogUtil.d(TAG, "alarm info same");
//                        popupFaultNotice(device);
                    } else {
                        device.save();
                        popupFaultNotice(device);
                    }
                } else {
                    device.save();
                    popupFaultNotice(device);
                }
            }
        }
        if (deviceList.size() > 0) {
            for (Device device: deviceList) {
//                LogUtil.d(TAG, "device.getBattery_level():"+device.getBattery_level());
                if (CommonConstants.LOW_BATTERY_NOTICE_VALUE_20.equals(device.getBattery_level())) {
                    popupLowBatteryNotice(device);
                } else if (CommonConstants.LOW_BATTERY_NOTICE_VALUE_15.equals(device.getBattery_level())) {
                    popupLowBatteryNotice(device);
                }
            }
        }
    }

    private void popupLowBatteryNotice(Device device) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Intent intent = new Intent(this, DeviceExceptionPageActivity.class);
//        intent.putExtra("device", device);
//        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setContentTitle(getResources().getString(R.string.yl_device_low_battery_notice_title))
                .setContentText(String.format(getResources().getString(R.string.yl_device_low_battery_device_text),
                        device.getDescription(), device.getBattery_level()))
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_danger)
                .setColor(Color.RED)
//                .setContentIntent(pi)
                .setAutoCancel(true)//读后删除
                .setDefaults(Notification.DEFAULT_ALL)//震动、铃声、LED灯使用系统默认
                .setPriority(Notification.PRIORITY_MAX)
                .build();
        manager.notify(device.getId(), notification);
    }

    private void popupFaultNotice(Device device) {
        List<String> stringList = Utils.parseAlarmInfo(device.getAlarm_info());
        if (stringList.size() > 0) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Intent intent = new Intent(this, DeviceExceptionPageActivity.class);
            intent.putExtra("device", device);
            PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = new Notification.Builder(this)
                    .setContentTitle(getResources().getString(R.string.yl_device_exception_notice_title))
                    .setContentText(String.format(getResources().getString(R.string.yl_device_exception_device_text),
                            device.getDescription()))
                    .setStyle(new Notification.BigTextStyle().bigText(
                            String.format(getResources().getString(R.string.yl_device_exception_notice_text), stringList.toString())
                    ))
                .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_danger)
                    .setColor(Color.RED)
                    .setContentIntent(pi)
                    .setAutoCancel(true)//读后删除
                    .setDefaults(Notification.DEFAULT_ALL)//震动、铃声、LED灯使用系统默认
                    .setPriority(Notification.PRIORITY_MAX)
                    .build();
            manager.notify(device.getId(), notification);
        }
    }

    private void startQueryDeviceExceptionTimer() {
        scheduledThreadGetDeviceException = new ScheduledThreadPoolExecutor(4);
        scheduledThreadGetDeviceException.scheduleAtFixedRate(new CommTaskUtils.TaskGetDeviceException(),
                0, 10, TimeUnit.SECONDS);
    }

    private void stopQueryDeviceExceptionTimer() {
        if (scheduledThreadQueryDeviceException != null) {
            scheduledThreadQueryDeviceException.shutdownNow();
            scheduledThreadQueryDeviceException = null;
        }
    }

    private void stopGetDeviceExceptionTimer() {
        if (scheduledThreadGetDeviceException != null) {
            scheduledThreadGetDeviceException.shutdownNow();
            scheduledThreadGetDeviceException = null;
        }
    }
}
