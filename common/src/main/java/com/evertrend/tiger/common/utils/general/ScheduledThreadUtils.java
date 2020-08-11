package com.evertrend.tiger.common.utils.general;

import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.utils.network.CommTaskUtils;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadUtils {
    private static ScheduledThreadPoolExecutor scheduledThreadGetAllMapPages;
    private static ScheduledThreadPoolExecutor scheduledThreadGetRunLogs;
    private static ScheduledThreadPoolExecutor scheduledThreadControl;

    public static void startControlTimer(Device device, int status, String mark, int delayTime) {
//        DialogUtil.showProgressDialog(getActivity(), getResources().getString(R.string.yl_common_saving), false, false);
        scheduledThreadControl = new ScheduledThreadPoolExecutor(6);
        scheduledThreadControl.scheduleAtFixedRate(new CommTaskUtils.TaskControlStatus(device, status, mark),
                delayTime, 5, TimeUnit.SECONDS);
    }

    public static void ThreadGetAllMapPages(Device device) {
        if (scheduledThreadGetAllMapPages == null) {
            scheduledThreadGetAllMapPages = new ScheduledThreadPoolExecutor(4);
            scheduledThreadGetAllMapPages.scheduleAtFixedRate(new CommTaskUtils.TaskGetAllMapPages(device),
                    0, 10, TimeUnit.SECONDS);
        }
    }

    public static void ThreadGetRunLogs(Device device, int page) {
        if (scheduledThreadGetRunLogs == null) {
            scheduledThreadGetRunLogs = new ScheduledThreadPoolExecutor(4);
            scheduledThreadGetRunLogs.scheduleAtFixedRate(new CommTaskUtils.TaskGetRunLogs(device, page),
                    0, 10, TimeUnit.SECONDS);
        }
    }

    public static void stopGetAllMapPagesTimer() {
        if (scheduledThreadGetAllMapPages != null) {
            scheduledThreadGetAllMapPages.shutdownNow();
            scheduledThreadGetAllMapPages = null;
        }
    }
    public static void stopGetRunLogsTimer() {
        if (scheduledThreadGetRunLogs != null) {
            scheduledThreadGetRunLogs.shutdownNow();
            scheduledThreadGetRunLogs = null;
        }
    }
    public static void stopControlTimer() {
        if (scheduledThreadControl != null) {
            scheduledThreadControl.shutdownNow();
            scheduledThreadControl = null;
        }
    }
}
