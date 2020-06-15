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
import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.bean.MapPages;
import com.evertrend.tiger.common.bean.RobotSpot;
import com.evertrend.tiger.common.bean.TracePath;
import com.evertrend.tiger.common.bean.VirtualTrackGroup;
import com.evertrend.tiger.device.bean.event.DeleteMapPageEvent;
import com.evertrend.tiger.device.bean.event.DeleteCleanTaskEvent;
import com.evertrend.tiger.device.bean.event.DeviceMessageEvent;
import com.evertrend.tiger.device.bean.event.GetAllCleanTasksSuccessEvent;
import com.evertrend.tiger.common.bean.event.GetAllMapPagesSuccessEvent;
import com.evertrend.tiger.device.bean.event.GetAllSpecialTaskSpotSuccessEvent;
import com.evertrend.tiger.device.bean.event.GetAllVirtualTrackGroupSuccessEvent;
import com.evertrend.tiger.device.bean.event.GetMapPagesAllPathSuccessEvent;
import com.evertrend.tiger.device.bean.event.LoadDevicesEvent;
import com.evertrend.tiger.device.bean.event.SaveBasicConfigEvent;
import com.evertrend.tiger.device.bean.event.SaveCleanTaskSuccessEvent;
import com.evertrend.tiger.device.bean.event.SetStatusCompleteEvent;
import com.evertrend.tiger.device.bean.event.SetStatusSuccessEvent;

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
                                EventBus.getDefault().postSticky(new GetAllMapPagesSuccessEvent(mapPagesList));
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

    public static class TaskReadControlStatus implements Runnable {
        private Device device;
        private String mark;
        private int status;

        public TaskReadControlStatus(Device device, int status, String mark) {
            this.device = device;
            this.status = status;
            this.mark = mark;
        }

        @Override
        public void run() {
            startReadControlStatus();
        }

        private void startReadControlStatus() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(NetReq.DEVICE_ID, String.valueOf(device.getId()));
            map.put(NetReq.WHAT_STATUS, mark);
            OKHttpManager.getInstance().sendComplexForm(NetReq.NET_READ_CONTROL_STATUS, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        LogUtil.d(TAG, jsonObject.getString(CommonNetReq.RESULT_DESC));
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                int newStatus = jsonObject.getIntValue(CommonNetReq.RESULT_DATA);
                                LogUtil.d(TAG, "newStatus:" + newStatus);
                                LogUtil.d(TAG, "Mark:" + mark);
                                if ("rb_go_to_recharge".equals(mark)) {
                                    if (newStatus != 0) {
                                        EventBus.getDefault().post(new SetStatusSuccessEvent(mark, newStatus));
                                    }
                                } else if ("rb_go_to_add_water".equals(mark)) {
                                    if (newStatus != 0) {
                                        EventBus.getDefault().post(new SetStatusSuccessEvent(mark, newStatus));
                                    }
                                } else if ("rb_go_to_empty_trash".equals(mark)) {
                                    if (newStatus != 0) {
                                        EventBus.getDefault().post(new SetStatusSuccessEvent(mark, newStatus));
                                    }
                                } else if ("rb_go_to_garage".equals(mark)) {
                                    if (newStatus != 0) {
                                        EventBus.getDefault().post(new SetStatusSuccessEvent(mark, newStatus));
                                    }
                                } else if ("rb_go_to_work".equals(mark)) {
                                    if (newStatus != 0) {
                                        EventBus.getDefault().post(new SetStatusSuccessEvent(mark, newStatus));
                                    }
                                } else if ("rb_go_to_idle".equals(mark)) {
                                    if (newStatus != 0) {
                                        EventBus.getDefault().post(new SetStatusSuccessEvent(mark, newStatus));
                                    }
                                } else if ("sw_horn".equals(mark)) {
                                    if (newStatus == 0) {
                                        EventBus.getDefault().post(new SetStatusSuccessEvent(mark, newStatus));
                                    }
                                } else {
                                    if (status == newStatus) {//APP设置设备状态到服务器与工控机修改设备在服务器状态一致，认为操作成功
                                        EventBus.getDefault().post(new SetStatusSuccessEvent(mark, newStatus));
                                    }
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

    public static class TaskControlStatus implements Runnable {
        private Device device;
        private int status;
        private String mark;

        public TaskControlStatus(Device mDevice, int status, String mark) {
            this.device = mDevice;
            this.status = status;
            this.mark = mark;
        }

        @Override
        public void run() {
            startControlStatus();
        }

        private void startControlStatus() {
            LogUtil.d(TAG, "mark: "+mark);
            LogUtil.d(TAG, "status: "+status);
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(NetReq.DEVICE_ID, String.valueOf(device.getId()));
            map.put(NetReq.STATUS, String.valueOf(status));
            String request = "";
            switch (mark) {
                case "sw_device_run":
                    request = NetReq.NET_SET_DEVICE_STATUS;
                    break;
                case "sw_main_sweep":
                    request = NetReq.NET_SET_MAIN_SWEEP;
                    break;
                case "sw_side_sweep":
                    request = NetReq.NET_SET_SIDE_SWEEP;
                    break;
                case "sw_sprinkling_water":
                    request = NetReq.NET_SET_SPRINKLING_WATER;
                    break;
                case "sw_left_tail_light":
                    request = NetReq.NET_SET_LEFT_TAIL_LIGHT;
                    break;
                case "sw_right_tail_light":
                    request = NetReq.NET_SET_RIGHT_TAIL_LIGHT;
                    break;
                case "sw_alarm_light":
                    request = NetReq.NET_SET_ALARM_LIGHT;
                    break;
                case "sw_front_light":
                    request = NetReq.NET_SET_FRONT_LIGHT;
                    break;
                case "sw_horn":
                    request = NetReq.NET_SET_HORN;
                    break;
                case "sw_suck_fan":
                    request = NetReq.NET_SET_SUCK_FUN;
                    break;
                case "sw_vibrating_dust":
                    request = NetReq.NET_SET_VIBRATING_DUST;
                    break;
                case "sw_motor":
                    request = NetReq.NET_SET_MOTOR;
                    break;
                case "sw_emergency_stop":
                    request = NetReq.NET_SET_EMERGENCY_STOP;
                    break;
                case "rb_go_to_recharge":
                    request = NetReq.NET_GO_TO_RECHARGE;
                    break;
                case "rb_go_to_add_water":
                    request = NetReq.NET_GO_TO_ADD_WATER;
                    break;
                case "rb_go_to_empty_trash":
                    request = NetReq.NET_GO_TO_EMPTY_TRASH;
                    break;
                case "rb_go_to_garage":
                    request = NetReq.NET_GO_TO_GARAGE;
                    break;
                case "rb_go_to_work":
                    request = NetReq.NET_GO_TO_WORK;
                    break;
                case "rb_go_to_idle":
                    request = NetReq.NET_GO_TO_IDEL;
                    break;
            }
            OKHttpManager.getInstance().sendComplexForm(request, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        LogUtil.d(TAG, jsonObject.getString(CommonNetReq.RESULT_DESC));
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                EventBus.getDefault().post(new SetStatusCompleteEvent(status, mark));
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
