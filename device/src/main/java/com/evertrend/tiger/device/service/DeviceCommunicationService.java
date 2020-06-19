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
import com.evertrend.tiger.common.utils.network.CommTaskUtils;
import com.evertrend.tiger.common.utils.network.CommonNetReq;
import com.evertrend.tiger.device.R;
import com.evertrend.tiger.device.activity.DeviceMainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
        List<Device> deviceList = JsonAnalysisUtil.loadAllDevice(jsonObject.getJSONArray(CommonNetReq.RESULT_DATA));
        LogUtil.d(TAG, "toast notice: "+deviceList.size());
        if (deviceList.size() > 0) {
            for (Device device: deviceList) {
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Intent intent = new Intent(this, DeviceMainActivity.class);
                PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                Notification notification = new Notification.Builder(this)
                        .setContentTitle(getResources().getString(R.string.yl_device_exception_notice_title))
//                    .setContentText(getResources().getString(R.string.contact_person_infection_notice_text))
                        .setStyle(new Notification.BigTextStyle().bigText(
                                String.format(getResources().getString(R.string.yl_device_exception_notice_text),
                                        device.getDescription(), device.getAlarm_info())
                        ))
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.ic_danger)
                        .setColor(Color.RED)
                        .setContentIntent(pi)
                        .setAutoCancel(true)//读后删除
                        .setDefaults(Notification.DEFAULT_ALL)//震动、铃声、LED灯使用系统默认
                        .setPriority(Notification.PRIORITY_MAX)
                        .build();
                manager.notify(0, notification);
            }
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
