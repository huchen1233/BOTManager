package com.evertrend.tiger.device.utils;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.evertrend.tiger.common.utils.general.AppSharePreference;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.common.utils.network.CommonNetReq;
import com.evertrend.tiger.common.utils.network.OKHttpManager;
import com.evertrend.tiger.device.bean.CleanTask;
import com.evertrend.tiger.device.bean.Device;
import com.evertrend.tiger.device.bean.MapPages;
import com.evertrend.tiger.device.bean.RobotSpot;
import com.evertrend.tiger.device.bean.TracePath;
import com.evertrend.tiger.device.bean.VirtualTrackGroup;
import com.evertrend.tiger.device.bean.event.DeleteMapPageEvent;
import com.evertrend.tiger.device.bean.event.SaveMapPageEvent;
import com.evertrend.tiger.device.bean.event.DeleteCleanTaskEvent;
import com.evertrend.tiger.device.bean.event.DeviceMessageEvent;
import com.evertrend.tiger.device.bean.event.GetAllCleanTasksSuccessEvent;
import com.evertrend.tiger.device.bean.event.GetAllMapPagesSuccessEvent;
import com.evertrend.tiger.device.bean.event.GetAllSpecialTaskSpotSuccessEvent;
import com.evertrend.tiger.device.bean.event.GetAllVirtualTrackGroupSuccessEvent;
import com.evertrend.tiger.device.bean.event.GetMapPagesAllPathSuccessEvent;
import com.evertrend.tiger.device.bean.event.LoadDevicesEvent;
import com.evertrend.tiger.device.bean.event.SaveBasicConfigEvent;
import com.evertrend.tiger.device.bean.event.SaveCleanTaskSuccessEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskUtils {
    public static final String TAG = TaskUtils.class.getSimpleName();

    public static class TaskGetAssocitedDevice implements Runnable {
        @Override
        public void run() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            OKHttpManager.getInstance().sendComplexForm(NetReq.NET_ASSOCIATED_DEVICE, map, new OKHttpManager.FuncJsonObj() {
                        @Override
                        public void onResponse(JSONObject jsonObject) throws JSONException {
                            try {
                                LogUtil.d(TAG, jsonObject.getString(CommonNetReq.RESULT_DESC));
                                switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                                    case CommonNetReq.CODE_SUCCESS:
                                        List<Device> deviceList = JsonAnalysisUtil.loadAllDevice(jsonObject.getJSONArray(CommonNetReq.RESULT_DATA));
                                        EventBus.getDefault().post(new LoadDevicesEvent(deviceList));
                                        break;
                                    default:
                                        break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(TAG, "出错：解析数据失败");
                            }
                        }
                    }, new OKHttpManager.FuncFailure() {
                        @Override
                        public void onFailure() {
                            Log.e(TAG, "出错：请求网络失败");
                        }
                    });
        }
    }

    public static class TaskGetDevice implements Runnable {
        private String deviceId;

        public TaskGetDevice(String deviceId) {
            this.deviceId = deviceId;
        }

        @Override
        public void run() {
            getDevice(deviceId);
        }

        private void getDevice(String deviceId) {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(NetReq.DEVICE_ID, deviceId);
            OKHttpManager.getInstance().sendComplexForm(NetReq.NET_GET_DEVICE, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                Device device = JsonAnalysisUtil.getDevice(
                                        jsonObject.getJSONObject(CommonNetReq.RESULT_DATA));
                                EventBus.getDefault().postSticky(
                                        new DeviceMessageEvent(device));
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "出错：解析数据失败");
                    }
                }
            }, new OKHttpManager.FuncFailure() {
                @Override
                public void onFailure() {
                    Log.e(TAG, "出错：请求网络失败");
                }
            });
        }
    }

    public static class TaskGetAllMapPages implements Runnable {
        private Device device;

        public TaskGetAllMapPages(Device device) {
            this.device = device;
        }

        @Override
        public void run() {
            startGetAllMappPages();
        }

        private void startGetAllMappPages() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(NetReq.DEVICE_ID, String.valueOf(device.getId()));
            OKHttpManager.getInstance().sendComplexForm(NetReq.NET_GET_ALL_MAP_PAGES, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                JSONArray jsonArray = jsonObject.getJSONArray(CommonNetReq.RESULT_DATA);
                                List<MapPages> mapPagesList = new ArrayList<>();
                                if (jsonArray.size() > 0) {
                                    for (int i = 0; i < jsonArray.size(); i++) {
                                        MapPages mapPages = new MapPages();
                                        mapPages.setId(jsonArray.getJSONObject(i).getIntValue(Constants.ID));
                                        mapPages.setName(jsonArray.getJSONObject(i).getString(Constants.NAME));
                                        mapPages.setDescription(jsonArray.getJSONObject(i).getString(Constants.DESCRIPTION));
                                        mapPagesList.add(mapPages);
                                    }
                                }
                                EventBus.getDefault().post(new GetAllMapPagesSuccessEvent(mapPagesList));
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "出错：解析数据失败");
                    }
                }
            }, new OKHttpManager.FuncFailure() {
                @Override
                public void onFailure() {
                    Log.e(TAG, "出错：请求网络失败");
                }
            });
        }
    }

    public static class TaskGetAllCleanTasks implements Runnable {
        private Device device;
        public TaskGetAllCleanTasks(Device device) {
            this.device = device;
        }

        @Override
        public void run() {
            startGetAllCleanTasks();
        }

        private void startGetAllCleanTasks() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(NetReq.DEVICE_ID, String.valueOf(device.getId()));
            OKHttpManager.getInstance().sendComplexForm(NetReq.NET_GET_CLEAN_TASK, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                List<CleanTask> cleanTaskList = JsonAnalysisUtil.loadAllCLeanTask(jsonObject.getJSONArray(CommonNetReq.RESULT_DATA));
                                EventBus.getDefault().post(new GetAllCleanTasksSuccessEvent(cleanTaskList));
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "出错：解析数据失败");
                    }
                }
            }, new OKHttpManager.FuncFailure() {
                @Override
                public void onFailure() {
                    Log.e(TAG, "出错：请求网络失败");
                }
            });
        }
    }

    public static class TaskSaveBasicConfigValue implements Runnable {
        private Device device;

        public TaskSaveBasicConfigValue(Device device) {
            this.device = device;
        }

        @Override
        public void run() {
            startSaveBasicConfig();
        }

        private void startSaveBasicConfig() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(NetReq.DEVICE_ID, String.valueOf(device.getId()));
            map.put(NetReq.BATTERY_LOW_LIMIT, String.valueOf(device.getBattery_low_limit()));
            map.put(NetReq.WATER_LOW_LIMIT, String.valueOf(device.getWater_low_limit()));
            map.put(NetReq.EMPTY_TRASH_INTERVAL, String.valueOf(device.getEmpty_trash_interval()));
            map.put(NetReq.ENABLE_AUTO_RECHARGE, String.valueOf(device.getEnable_auto_recharge()));
            map.put(NetReq.ENABLE_AUTO_ADD_WATER, String.valueOf(device.getEnable_auto_add_water()));
            map.put(NetReq.ENABLE_AUTO_EMPTY_TRASH, String.valueOf(device.getEnable_auto_empty_trash()));
            OKHttpManager.getInstance().sendComplexForm(NetReq.NET_SAVE_BASIC_CONFIG, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                EventBus.getDefault().post(new SaveBasicConfigEvent(JsonAnalysisUtil.getDevice(jsonObject.getJSONObject(CommonNetReq.RESULT_DATA))));
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "出错：解析数据失败");
                    }
                }
            }, new OKHttpManager.FuncFailure() {
                @Override
                public void onFailure() {
                    Log.e(TAG, "出错：请求网络失败");
                }
            });
        }
    }

    public static class TaskGetMapPagesAllPath implements Runnable {
        private Device device;
        private MapPages mapPages;

        public TaskGetMapPagesAllPath(Device device, MapPages mapPages) {
            this.device = device;
            this.mapPages = mapPages;
        }

        @Override
        public void run() {
            startGetMapPagesAllPath();
        }

        private void startGetMapPagesAllPath() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(NetReq.MAP_PAGE, String.valueOf(mapPages.getId()));
            map.put(NetReq.DEVICE_ID, String.valueOf(device.getId()));
            OKHttpManager.getInstance().sendComplexForm(NetReq.NET_GET_CURRENT_MAP_PAGE_ALL_PATH, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                JSONArray jsonArray = jsonObject.getJSONArray(CommonNetReq.RESULT_DATA);
                                List<TracePath> tracePathList = new ArrayList<>();
                                if (jsonArray.size() > 0) {
                                    for (int i = 0; i < jsonArray.size(); i++) {
                                        TracePath tracePath = new TracePath();
                                        tracePath.setId(jsonArray.getJSONObject(i).getIntValue(Constants.ID));
                                        tracePath.setName(jsonArray.getJSONObject(i).getString(Constants.NAME));
                                        tracePath.setDesc(jsonArray.getJSONObject(i).getString(Constants.DESC));
                                        tracePath.setDeviceId(jsonArray.getJSONObject(i).getIntValue(Constants.DEVICE));
                                        tracePath.setFlag(jsonArray.getJSONObject(i).getIntValue(Constants.FLAG));
                                        tracePath.setMapPage(jsonArray.getJSONObject(i).getIntValue(Constants.MAP_PAGE));
                                        tracePathList.add(tracePath);
                                    }
                                }
                                EventBus.getDefault().post(new GetMapPagesAllPathSuccessEvent(tracePathList));
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "出错：解析数据失败");
                    }
                }
            }, new OKHttpManager.FuncFailure() {
                @Override
                public void onFailure() {
                    Log.e(TAG, "出错：请求网络失败");
                }
            });
        }
    }

    public static class TaskGetMapPagesAllVirtualTrackGroup implements Runnable {
        private Device device;
        private MapPages mapPages;

        public TaskGetMapPagesAllVirtualTrackGroup(Device device, MapPages mapPages) {
            this.device = device;
            this.mapPages = mapPages;
        }

        @Override
        public void run() {
            startGetMapPagesAllVirtualTrackGroup();
        }

        private void startGetMapPagesAllVirtualTrackGroup() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(NetReq.MAP_PAGE, String.valueOf(mapPages.getId()));
            map.put(NetReq.DEVICE_ID, String.valueOf(device.getId()));
            OKHttpManager.getInstance().sendComplexForm(NetReq.NET_GET_CURRENT_MAP_PAGE_ALL_VIRTUAL_TRACK_GROUP, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                JSONArray jsonArray = jsonObject.getJSONArray(CommonNetReq.RESULT_DATA);
                                List<VirtualTrackGroup> virtualTrackGroups = new ArrayList<>();
                                if (jsonArray.size() > 0) {
                                    for (int i = 0; i < jsonArray.size(); i++) {
                                        VirtualTrackGroup virtualTrackGroup = new VirtualTrackGroup();
                                        virtualTrackGroup.setId(jsonArray.getJSONObject(i).getIntValue(Constants.ID));
                                        virtualTrackGroup.setName(jsonArray.getJSONObject(i).getString(Constants.NAME));
                                        virtualTrackGroup.setDesc(jsonArray.getJSONObject(i).getString(Constants.DESCRIPTION));
                                        virtualTrackGroup.setDeviceId(jsonArray.getJSONObject(i).getIntValue(Constants.DEVICE));
                                        virtualTrackGroup.setNumber(jsonArray.getJSONObject(i).getIntValue(Constants.NUMBER));
                                        virtualTrackGroup.setMapPage(jsonArray.getJSONObject(i).getIntValue(Constants.MAP_PAGE));
                                        virtualTrackGroups.add(virtualTrackGroup);
                                    }
                                }
                                EventBus.getDefault().post(new GetAllVirtualTrackGroupSuccessEvent(virtualTrackGroups));
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "出错：解析数据失败");
                    }
                }
            }, new OKHttpManager.FuncFailure() {
                @Override
                public void onFailure() {
                    Log.e(TAG, "出错：请求网络失败");
                }
            });
        }
    }

    public static class TaskGetMapPagesAllSpecialWorkSpot implements Runnable {
        private Device device;
        private MapPages mapPages;

        public TaskGetMapPagesAllSpecialWorkSpot(Device device, MapPages mapPages) {
            this.device = device;
            this.mapPages = mapPages;
        }

        @Override
        public void run() {
            startGetMapPagesAllSpecialWorkSpot();
        }

        private void startGetMapPagesAllSpecialWorkSpot() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(NetReq.MAP_PAGE, String.valueOf(mapPages.getId()));
            map.put(NetReq.DEVICE_ID, String.valueOf(device.getId()));
            OKHttpManager.getInstance().sendComplexForm(NetReq.NET_GET_CURRENT_MAP_PAGE_ALL_SPECIAL_TASK_SPOT, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                JSONArray jsonArray = jsonObject.getJSONArray(CommonNetReq.RESULT_DATA);
                                List<RobotSpot> robotSpotList = new ArrayList<>();
                                if (jsonArray.size() > 0) {
                                    for (int i = 0; i < jsonArray.size(); i++) {
                                        RobotSpot robotSpot = new RobotSpot();
                                        robotSpot.setId(jsonArray.getJSONObject(i).getIntValue(Constants.ID));
                                        robotSpot.setName(jsonArray.getJSONObject(i).getString(Constants.NAME));
                                        robotSpot.setSpot_flag(jsonArray.getJSONObject(i).getIntValue(Constants.SPOT_FLAG));
                                        robotSpotList.add(robotSpot);
                                    }
                                }
                                EventBus.getDefault().post(new GetAllSpecialTaskSpotSuccessEvent(robotSpotList));
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "出错：解析数据失败");
                    }
                }
            }, new OKHttpManager.FuncFailure() {
                @Override
                public void onFailure() {
                    Log.e(TAG, "出错：请求网络失败");
                }
            });
        }
    }

    public static class TaskSaveCleanTask implements Runnable {
        private Device device;
        private MapPages mapPages;
        private CleanTask cleanTask;
        private boolean isUpdate;

        public TaskSaveCleanTask(Device device, MapPages mapPages, CleanTask cleanTask, boolean isUpdate) {
            this.device = device;
            this.mapPages = mapPages;
            this.cleanTask = cleanTask;
            this.isUpdate = isUpdate;
        }

        @Override
        public void run() {
            startSaveCleanTask();
        }

        private void startSaveCleanTask() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(NetReq.DEVICE_ID, String.valueOf(cleanTask.getDevice()));
            map.put(NetReq.MAP_PAGE, String.valueOf(mapPages.getId()));
            map.put(NetReq.NAME, cleanTask.getName());
            map.put(NetReq.DESCRIPTION, cleanTask.getDesc());
            map.put(NetReq.PRIORITY, String.valueOf(cleanTask.getTaskPriority()));
            map.put(NetReq.TRACE_PATH, cleanTask.getTracePaths());
            map.put(NetReq.TRACE_PATH_PRIORITY, String.valueOf(cleanTask.getTracePathsPriority()));
            map.put(NetReq.VIRTUAL_TRACK, cleanTask.getVirtualTrackGroups());
            map.put(NetReq.VIRTUAL_TRACK_PRIORITY, String.valueOf(cleanTask.getVirtualTrackGroupsPriority()));
            map.put(NetReq.SPE_WROK, cleanTask.getSpecialWorks());
            map.put(NetReq.SPE_WROK_PRIORITY, String.valueOf(cleanTask.getSpecialWorksPriority()));
            map.put(NetReq.START_TIME, cleanTask.getStartTime());
            map.put(NetReq.EXEC_FLAG, String.valueOf(cleanTask.getTaskOption()));
            map.put(NetReq.TASK_TYPE, String.valueOf(cleanTask.getTaskType()));
            String net = null;
            if (isUpdate) {
                map.put(NetReq.ID, String.valueOf(cleanTask.getId()));
                net = NetReq.NET_UPDATE_CLEAN_TASK;
            } else {
                net = NetReq.NET_ADD_CLEAN_TASK;
            }
            OKHttpManager.getInstance().sendComplexForm(net, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                CleanTask cleanTask = JsonAnalysisUtil.getCleanTask(jsonObject.getJSONObject(CommonNetReq.RESULT_DATA));
                                EventBus.getDefault().post(new SaveCleanTaskSuccessEvent(cleanTask, isUpdate));
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "出错：解析数据失败");
                    }
                }
            }, new OKHttpManager.FuncFailure() {
                @Override
                public void onFailure() {
                    Log.e(TAG, "出错：请求网络失败");
                }
            });
        }
    }

    public static class TaskDeleteCleanTask implements Runnable {
        private Device device;
        private CleanTask cleanTask;
        public TaskDeleteCleanTask(Device device, CleanTask cleanTask) {
            this.device = device;
            this.cleanTask = cleanTask;
        }

        @Override
        public void run() {
            startDeleteCleanTask();
        }

        private void startDeleteCleanTask() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(NetReq.DEVICE_ID, String.valueOf(device.getId()));
            map.put(NetReq.ID, String.valueOf(cleanTask.getId()));
            OKHttpManager.getInstance().sendComplexForm(NetReq.NET_DELETE_CLEAN_TASK, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                EventBus.getDefault().post(new DeleteCleanTaskEvent(cleanTask));
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "出错：解析数据失败");
                    }
                }
            }, new OKHttpManager.FuncFailure() {
                @Override
                public void onFailure() {
                    Log.e(TAG, "出错：请求网络失败");
                }
            });
        }
    }

    public static class TaskSaveMapPages implements Runnable {
        private Device device;
        private MapPages mapPages;
        private int doSaveMap;
        private boolean isUpdate;

        public TaskSaveMapPages(Device device, MapPages mapPages, int doSaveMap, boolean isUpdate) {
            this.device = device;
            this.mapPages = mapPages;
            this.doSaveMap = doSaveMap;
            this.isUpdate = isUpdate;
        }

        @Override
        public void run() {
            startCreateNewMapPage();
        }

        private void startCreateNewMapPage() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(NetReq.DEVICE_ID, String.valueOf(device.getId()));
            map.put(NetReq.PAGE_NAME, mapPages.getName());
            map.put(NetReq.PAGE_DESC, mapPages.getDescription());
            String net = null;
            if (isUpdate) {
                map.put(NetReq.MAP_PAGE, String.valueOf(mapPages.getId()));
                map.put(NetReq.PAGE_DO_SAVE_MAP, String.valueOf(doSaveMap));
                net = NetReq.NET_UPDATE_MAP_PAGE;
            } else {
                net = NetReq.NET_CREATE_NEW_MAP_APGE;
            }
            OKHttpManager.getInstance().sendComplexForm(net, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                if (isUpdate) {
                                    EventBus.getDefault().post(new SaveMapPageEvent(mapPages, isUpdate));
                                } else {
                                    MapPages mapPages = new MapPages();
                                    mapPages.setName(jsonObject.getJSONObject(CommonNetReq.RESULT_DATA).getString(Constants.NAME));
                                    mapPages.setDescription(jsonObject.getJSONObject(CommonNetReq.RESULT_DATA).getString(Constants.DESCRIPTION));
                                    mapPages.setDeviceId(jsonObject.getJSONObject(CommonNetReq.RESULT_DATA).getIntValue(Constants.DEVICE));
                                    mapPages.setId(jsonObject.getJSONObject(CommonNetReq.RESULT_DATA).getIntValue(Constants.ID));
                                    EventBus.getDefault().post(new SaveMapPageEvent(mapPages, isUpdate));
                                }
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "出错：解析数据失败");
                    }
                }
            }, new OKHttpManager.FuncFailure() {
                @Override
                public void onFailure() {
                    Log.e(TAG, "出错：请求网络失败");
                }
            });
        }
    }

    public static class TaskDeleteMapPages implements Runnable {
        private Device device;
        private MapPages mapPages;
        public TaskDeleteMapPages(Device device, MapPages mapPages) {
            this.device = device;
            this.mapPages = mapPages;
        }

        @Override
        public void run() {
            startDeleteMapPage();
        }

        private void startDeleteMapPage() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(NetReq.DEVICE_ID, String.valueOf(device.getId()));
            map.put(NetReq.MAP_PAGE, String.valueOf(mapPages.getId()));
            OKHttpManager.getInstance().sendComplexForm(NetReq.NET_DELETE_MAP_PAGE, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                EventBus.getDefault().post(new DeleteMapPageEvent(mapPages));
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "出错：解析数据失败");
                    }
                }
            }, new OKHttpManager.FuncFailure() {
                @Override
                public void onFailure() {
                    Log.e(TAG, "出错：请求网络失败");
                }
            });
        }
    }
}
