package com.evertrend.tiger.device.utils;

import com.evertrend.tiger.common.bean.Device;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadUtils {
    private static ScheduledThreadPoolExecutor scheduledThreadGetAllMapPages;

    public static void ThreadGetAllMapPages(Device device) {
        if (scheduledThreadGetAllMapPages == null) scheduledThreadGetAllMapPages = new ScheduledThreadPoolExecutor(4);
        scheduledThreadGetAllMapPages.scheduleAtFixedRate(new TaskUtils.TaskGetAllMapPages(device),
                0, 10, TimeUnit.SECONDS);
    }

    public static void stopGetAllMapPagesTimer() {
        if (scheduledThreadGetAllMapPages != null) {
            scheduledThreadGetAllMapPages.shutdownNow();
            scheduledThreadGetAllMapPages = null;
        }
    }
}
