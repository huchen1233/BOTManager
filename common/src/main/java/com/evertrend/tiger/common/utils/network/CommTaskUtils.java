package com.evertrend.tiger.common.utils.network;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.evertrend.tiger.common.bean.BaseTrace;
import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.bean.MapPages;
import com.evertrend.tiger.common.bean.RobotSpot;
import com.evertrend.tiger.common.bean.RunLog;
import com.evertrend.tiger.common.bean.TracePath;
import com.evertrend.tiger.common.bean.VirtualTrackGroup;
import com.evertrend.tiger.common.bean.event.CreateNewBaseTraceSuccessEvent;
import com.evertrend.tiger.common.bean.event.DeleteBaseTraceEvent;
import com.evertrend.tiger.common.bean.event.DeviceExceptionEvent;
import com.evertrend.tiger.common.bean.event.GetAllMapPagesSuccessEvent;
import com.evertrend.tiger.common.bean.event.GetRunLogsSuccessEvent;
import com.evertrend.tiger.common.bean.event.SaveMapPageEvent;
import com.evertrend.tiger.common.bean.event.SaveTraceSpotFailEvent;
import com.evertrend.tiger.common.bean.event.UpdateBaseTraceSuccessEvent;
import com.evertrend.tiger.common.bean.event.map.DeleteOneTraceSpotListEvent;
import com.evertrend.tiger.common.bean.event.map.DeleteTraceSpotListCompleteEvent;
import com.evertrend.tiger.common.bean.event.map.GetAllVirtualTrackGroupSuccessEvent;
import com.evertrend.tiger.common.bean.event.map.GetMapPagesAllPathSuccessEvent;
import com.evertrend.tiger.common.bean.event.map.GetSaveMapFlagEvent;
import com.evertrend.tiger.common.bean.event.map.GetTraceSpotEvent;
import com.evertrend.tiger.common.bean.event.map.RelocationOrSetCurrentMapEvent;
import com.evertrend.tiger.common.bean.event.map.SaveTraceSpotEvent;
import com.evertrend.tiger.common.bean.event.map.SaveTraceSpotListCompleteEvent;
import com.evertrend.tiger.common.bean.event.map.SaveTraceSpotListEvent;
import com.evertrend.tiger.common.bean.event.map.SaveVirtualTrackEvent;
import com.evertrend.tiger.common.bean.mapview.MapView;
import com.evertrend.tiger.common.utils.general.AppSharePreference;
import com.evertrend.tiger.common.utils.general.CommonConstants;
import com.evertrend.tiger.common.utils.general.DBUtil;
import com.evertrend.tiger.common.utils.general.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommTaskUtils {
    public static final String TAG = CommTaskUtils.class.getSimpleName();

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
            map.put(CommonNetReq.MAP_PAGE, String.valueOf(mapPages.getId()));
            map.put(CommonNetReq.DEVICE_ID, String.valueOf(device.getId()));
            OKHttpManager.getInstance().sendComplexForm(CommonNetReq.NET_GET_CURRENT_MAP_PAGE_ALL_PATH, map, new OKHttpManager.FuncJsonObj() {
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
                                        tracePath.setId(jsonArray.getJSONObject(i).getIntValue(CommonNetReq.ID));
                                        tracePath.setName(jsonArray.getJSONObject(i).getString(CommonNetReq.NAME));
                                        tracePath.setDesc(jsonArray.getJSONObject(i).getString(CommonNetReq.DESC));
                                        tracePath.setDeviceId(jsonArray.getJSONObject(i).getIntValue(CommonNetReq.DEVICE));
                                        tracePath.setFlag(jsonArray.getJSONObject(i).getIntValue(CommonNetReq.FLAG));
                                        tracePath.setMapPage(jsonArray.getJSONObject(i).getIntValue(CommonNetReq.MAP_PAGE));
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
            map.put(CommonNetReq.MAP_PAGE, String.valueOf(mapPages.getId()));
            map.put(CommonNetReq.DEVICE_ID, String.valueOf(device.getId()));
            OKHttpManager.getInstance().sendComplexForm(CommonNetReq.NET_GET_CURRENT_MAP_PAGE_ALL_VIRTUAL_TRACK_GROUP, map, new OKHttpManager.FuncJsonObj() {
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
                                        virtualTrackGroup.setId(jsonArray.getJSONObject(i).getIntValue(CommonNetReq.ID));
                                        virtualTrackGroup.setName(jsonArray.getJSONObject(i).getString(CommonNetReq.NAME));
                                        virtualTrackGroup.setDesc(jsonArray.getJSONObject(i).getString(CommonNetReq.DESCRIPTION));
                                        virtualTrackGroup.setDeviceId(jsonArray.getJSONObject(i).getIntValue(CommonNetReq.DEVICE));
                                        virtualTrackGroup.setNumber(jsonArray.getJSONObject(i).getIntValue(CommonNetReq.NUMBER));
                                        virtualTrackGroup.setMapPage(jsonArray.getJSONObject(i).getIntValue(CommonNetReq.MAP_PAGE));
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

    public static class TaskSaveTraceSpotList implements Runnable {
        private Device device;
        private List<String> mTraceSpotList;
        private MapPages mapPages;
        private TracePath tracePath;
        private boolean saveSpotFlag = false;

        public TaskSaveTraceSpotList(Device device, List<String> mTraceSpotList, MapPages mapPages, TracePath tracePath) {
            this.device = device;
            this.mTraceSpotList = mTraceSpotList;
            this.mapPages = mapPages;
            this.tracePath = tracePath;
        }

        @Subscribe(threadMode = ThreadMode.BACKGROUND)
        public void onEventMainThread(SaveTraceSpotListEvent event) {
            saveSpotFlag = true;
        }

        @Override
        public void run() {
            EventBus.getDefault().register(this);
            startSaveTraceSpotList();
        }

        private void startSaveTraceSpotList() {
            int size = mTraceSpotList.size();
            LogUtil.d(TAG, "trace list size: "+size);
            startSaveTraceSpot(device, mapPages.getId(), 0, tracePath.getId(), 0, mTraceSpotList.get(0), false);
            for (int i = 1; i <= size; ) {
                if (i == size) {
                    LogUtil.d(TAG, "i = "+i);
                    DBUtil.saveSpotListToLocal(device, mapPages, tracePath, mTraceSpotList);
                    EventBus.getDefault().post(new SaveTraceSpotListCompleteEvent());
                    if (EventBus.getDefault().isRegistered(this)) {
                        EventBus.getDefault().unregister(this);
                    }
                    break;
                }
                if (saveSpotFlag) {
                    startSaveTraceSpot(device, mapPages.getId(), 0, tracePath.getId(), 0, mTraceSpotList.get(i), false);
                    i++;
                    saveSpotFlag = false;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 保存当前标志点位姿数据到服务器
     */
    private static void startSaveTraceSpot(final Device device, final int mapPageId, int targetMapPageId, final int tracePathId, final int spotFlag, final String currentPose, final boolean isSingle) {
        LogUtil.d(TAG, "saveSpot spotFlag=" + spotFlag);
        HashMap<String, String> map = new HashMap<>();
        map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
        map.put(CommonNetReq.DEVICE_ID, String.valueOf(device.getId()));
        map.put(CommonNetReq.MAP_PAGE, String.valueOf(mapPageId));
        map.put(CommonNetReq.TARGET_MAP_PAGE, String.valueOf(targetMapPageId));
        map.put(CommonNetReq.SPOT_FLAG, String.valueOf(spotFlag));
        map.put(CommonNetReq.TRACE_PATH_ID, String.valueOf(tracePathId));//注意当前选择循迹路径为空的情况
        map.put(CommonNetReq.SPOT_FLAG_DATA, currentPose);
        OKHttpManager.getInstance().sendComplexForm(CommonNetReq.NET_ADD_SPOT, map, new OKHttpManager.FuncJsonObj() {
            @Override
            public void onResponse(JSONObject jsonObject) throws JSONException {
                try {
                    switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                        case CommonNetReq.CODE_SUCCESS:
//                            loadDeviceSuccess(jsonObject.getJSONArray(NetReq.RESULT_DATA));
                            if (spotFlag != 0) {
                                DBUtil.saveSpotToLocal(device.getId(), mapPageId, tracePathId, spotFlag, currentPose);
                                EventBus.getDefault().post(new SaveTraceSpotEvent());
                            } else {
                                EventBus.getDefault().post(new SaveTraceSpotListEvent(isSingle));
//                                if (isSingle) {
//                                    EventBus.getDefault().postSticky(new SaveTraceSpotListEvent(isSingle));
//                                } else {
//                                    EventBus.getDefault().postSticky(new SaveTraceSpotListEvent(isSingle));
//                                }
                            }
                            break;
                        case CommonNetReq.ERR_CODE_ADD_SPOT_FAIL:
                            if (spotFlag != 0) {
                                EventBus.getDefault().post(new SaveTraceSpotFailEvent(jsonObject));
                            }  else {
                                EventBus.getDefault().post(new SaveTraceSpotListEvent(isSingle));
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

    public static class TaskGetTraceSpot implements Runnable {
        private Device device;
        private TracePath tracePath;
        private MapView mMapView;
        private boolean isActive;

        public TaskGetTraceSpot(Device device, TracePath tracePath, MapView mMapView, boolean b) {
            this.device = device;
            this.tracePath = tracePath;
            this.mMapView = mMapView;
            this.isActive = b;
        }

        public TaskGetTraceSpot(Device device, TracePath tracePath, boolean b) {
            this.device = device;
            this.tracePath = tracePath;
            this.isActive = b;
        }

        @Override
        public void run() {
            getTraceSpot(device);
        }

        private void getTraceSpot(Device device) {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(CommonNetReq.DEVICE_ID, String.valueOf(device.getId()));
            map.put(CommonNetReq.TRACE_PATH_ID, String.valueOf(tracePath.getId()));
            OKHttpManager.getInstance().sendComplexForm(CommonNetReq.NET_GET_TRACE_SPOT_LIST, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        LogUtil.d(TAG, jsonObject.getString(CommonNetReq.RESULT_DESC));
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                JSONArray jsonArray = jsonObject.getJSONArray(CommonNetReq.RESULT_DATA);
                                LogUtil.d(TAG, "jsonArray: " + jsonArray.toString());
                                List<RobotSpot> serverTraceSpotList = new ArrayList<>();
                                if (jsonArray.size() > 0) {
                                    for (int i = 0; i < jsonArray.size(); i++) {
                                        RobotSpot robotSpot = new RobotSpot();
                                        robotSpot.setId(jsonArray.getJSONObject(i).getIntValue("id"));
                                        String[] data = jsonArray.getJSONObject(i).getString("data").split(",");
                                        robotSpot.setX(Float.parseFloat(data[0]));
                                        robotSpot.setY(Float.parseFloat(data[1]));
                                        robotSpot.setZ(Float.parseFloat(data[2]));
                                        robotSpot.setYaw(Float.parseFloat(data[3]));
                                        serverTraceSpotList.add(robotSpot);
                                    }
                                }
                                EventBus.getDefault().post(new GetTraceSpotEvent(serverTraceSpotList, isActive));
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

    public static class TaskSaveVirtualTrack implements Runnable {
        private Device device;
        private MapPages mapPages;
        private VirtualTrackGroup virtualTrackGroup;

        public TaskSaveVirtualTrack(Device device, MapPages mapPages, VirtualTrackGroup virtualTrackGroup) {
            this.device = device;
            this.mapPages = mapPages;
            this.virtualTrackGroup = virtualTrackGroup;
        }

        @Override
        public void run() {
            startSaveVirtualTrack();
        }

        private void startSaveVirtualTrack() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(CommonNetReq.DEVICE_ID, String.valueOf(device.getId()));
            map.put(CommonNetReq.TRACK_ID, String.valueOf(virtualTrackGroup.getId()));
            OKHttpManager.getInstance().sendComplexForm(CommonNetReq.NET_SAVE_VIRTUAL_TRACK, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        LogUtil.d(TAG, jsonObject.getString(CommonNetReq.RESULT_DESC));
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                EventBus.getDefault().post(new SaveVirtualTrackEvent(virtualTrackGroup));
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

    public static class TaskRelocationOrSetCurrentMap implements Runnable {
        private Device device;
        private MapPages mapPages;
        private int type;
        public TaskRelocationOrSetCurrentMap(Device device, MapPages mapPages, int type) {
            this.device = device;
            this.mapPages = mapPages;
            this.type = type;
        }

        @Override
        public void run() {
            startRelocationMapPage();
        }

        private void startRelocationMapPage() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(CommonNetReq.DEVICE_ID, String.valueOf(device.getId()));
            map.put(CommonNetReq.MAP_PAGE, String.valueOf(mapPages.getId()));
            String net = null;
            if (type == CommonConstants.TYPE_SET_CURRENT_MAP) {
                net = CommonNetReq.NET_SET_CURRENT_MAP;
            } else if (type == CommonConstants.TYPE_RELOCATION) {
                net = CommonNetReq.NET_RELOCATION_MAP_APGE;
            }
            OKHttpManager.getInstance().sendComplexForm(net, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        LogUtil.d(TAG, jsonObject.getString(CommonNetReq.RESULT_DESC));
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                LogUtil.d(TAG, "type:"+type);
                                LogUtil.d(TAG, "mapPages:"+mapPages);
                                EventBus.getDefault().post(new RelocationOrSetCurrentMapEvent(type));
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
            map.put(CommonNetReq.DEVICE_ID, String.valueOf(device.getId()));
            map.put(CommonNetReq.PAGE_NAME, mapPages.getName());
            map.put(CommonNetReq.PAGE_DESC, mapPages.getDescription());
            String net = null;
            if (isUpdate) {
                map.put(CommonNetReq.MAP_PAGE, String.valueOf(mapPages.getId()));
                map.put(CommonNetReq.PAGE_DO_SAVE_MAP, String.valueOf(doSaveMap));
                net = CommonNetReq.NET_UPDATE_MAP_PAGE;
            } else {
                net = CommonNetReq.NET_CREATE_NEW_MAP_APGE;
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
                                    mapPages.setName(jsonObject.getJSONObject(CommonNetReq.RESULT_DATA).getString(CommonNetReq.NAME));
                                    mapPages.setDescription(jsonObject.getJSONObject(CommonNetReq.RESULT_DATA).getString(CommonNetReq.DESCRIPTION));
                                    mapPages.setDeviceId(jsonObject.getJSONObject(CommonNetReq.RESULT_DATA).getIntValue(CommonNetReq.DEVICE));
                                    mapPages.setId(jsonObject.getJSONObject(CommonNetReq.RESULT_DATA).getIntValue(CommonNetReq.ID));
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

    public static class TaskGetSaveMapFlag implements Runnable {
        private Device device;

        public TaskGetSaveMapFlag(Device device) {
            this.device = device;
        }

        @Override
        public void run() {
            getSaveMapFlag();
        }

        /**
         * 获取工控机保存标志位
         */
        private void getSaveMapFlag() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(CommonNetReq.DEVICE_ID, String.valueOf(device.getId()));
            OKHttpManager.getInstance().sendComplexForm(CommonNetReq.NET_GET_SAVE_MAP, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                EventBus.getDefault().post(new GetSaveMapFlagEvent(jsonObject.getIntValue(CommonNetReq.RESULT_DATA)));
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

    public static class TaskDeleteTraceSpot implements Runnable {
        private List<RobotSpot> mRobotSpotList;
        private List<RobotSpot> localTracePathSpotList;
        private RobotSpot robotSpot;
        private boolean deleteSpotFlag = false;

        public TaskDeleteTraceSpot(List<RobotSpot> robotSpots, RobotSpot robotSpot) {
            this.mRobotSpotList = robotSpots;
            this.robotSpot = robotSpot;
        }

        public TaskDeleteTraceSpot(List<RobotSpot> mTraceRobotSpotList) {
            this.mRobotSpotList = mTraceRobotSpotList;
        }

        public TaskDeleteTraceSpot(List<RobotSpot> mTraceRobotSpotList, List<RobotSpot> localTracePathSpotList) {
            this.mRobotSpotList = mTraceRobotSpotList;
            this.localTracePathSpotList = localTracePathSpotList;
        }

        @Subscribe(threadMode = ThreadMode.BACKGROUND)
        public void onEventMainThread(DeleteOneTraceSpotListEvent event) {
            deleteSpotFlag = true;
        }

        @Override
        public void run() {
            EventBus.getDefault().register(this);
            deleteSpotFlag = true;
            rollBackdeleteTraceSpot();
        }

        private void rollBackdeleteTraceSpot() {
            int size = mRobotSpotList.size();
            LogUtil.d(TAG, "mRobotSpotList size : "+size);
            int rollbackNum = AppSharePreference.getAppSharedPreference().loadTracePathRollbackNum();
            LogUtil.d(TAG, "rollbackNum: "+rollbackNum);
            for (int i = size - 1; i >= -1; ) {
                LogUtil.d(TAG, "i:"+i);
                LogUtil.d(TAG, "deleteSpotFlag:"+deleteSpotFlag);
                if (i == -1 || i == size - rollbackNum) {
                    EventBus.getDefault().post(new DeleteTraceSpotListCompleteEvent(mRobotSpotList));
                    if (EventBus.getDefault().isRegistered(this)) {
                        EventBus.getDefault().unregister(this);
                    }
                    break;
                }
                if (deleteSpotFlag) {
//                    startDeleteTraceSpot(mRobotSpotList.get(i), localTracePathSpotList.get(i));
                    startDeleteTraceSpot(mRobotSpotList.get(i));
                    mRobotSpotList.remove(i);
                    i--;
                    deleteSpotFlag = false;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    private static void startDeleteTraceSpot(final RobotSpot robotSpot, final RobotSpot localRobotSpot) {
    private static void startDeleteTraceSpot(final RobotSpot robotSpot) {
        HashMap<String, String> map = new HashMap<>();
        map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
        map.put(CommonNetReq.ID, String.valueOf(robotSpot.getId()));
        OKHttpManager.getInstance().sendComplexForm(CommonNetReq.NET_DELETE_TRACE_SPOT, map, new OKHttpManager.FuncJsonObj() {
            @Override
            public void onResponse(JSONObject jsonObject) throws JSONException {
                try {
                    LogUtil.d(TAG, jsonObject.getString(CommonNetReq.RESULT_DESC));
                    switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                        case CommonNetReq.CODE_SUCCESS:
//                            int delete = LitePal.delete(RobotSpot.class, localRobotSpot.getId());
//                            LogUtil.d(TAG, "delete: " + delete);
//                            EventBus.getDefault().post(new DeleteOneTraceSpotListEvent(robotSpot, localRobotSpot));
                            EventBus.getDefault().post(new DeleteOneTraceSpotListEvent(robotSpot));
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

    public static class TaskSaveTraceSpot implements Runnable {
        private Device device;
        private int spotFlag;//标志该点类型，0：路径点，1: 充电点，2: 加水点，3: 倾倒垃圾点，4：车库点，5：公共点
        private String currentPose;
        private int mapPageId;
        private int targetMapPageId;

        public TaskSaveTraceSpot(Device device, int flag, String pose, int mapPageId, int targetMapPageId) {
            this.device = device;
            this.spotFlag = flag;
            this.currentPose = pose;
            this.mapPageId = mapPageId;
            this.targetMapPageId = targetMapPageId;
        }

        @Override
        public void run() {
            startSaveTraceSpot(device, mapPageId, targetMapPageId, device.getCurrent_trace_path_id(), spotFlag, currentPose, true);
        }
    }

    public static class TaskSaveBaseTrace implements Runnable {
        private BaseTrace baseTrace;
        private int type;

        public TaskSaveBaseTrace(BaseTrace baseTrace, int type) {
            this.baseTrace = baseTrace;
            this.type = type;
        }

        @Override
        public void run() {
            startSaveBaseTrace();
        }

        private void startSaveBaseTrace() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(CommonNetReq.DEVICE_ID, String.valueOf(baseTrace.getDeviceId()));
            map.put(CommonNetReq.MAP_PAGE, String.valueOf(baseTrace.getMapPage()));
            map.put(CommonNetReq.NAME, baseTrace.getName());
            map.put(CommonNetReq.DESCRIPTION, baseTrace.getDesc());
            String host = null;
            if (type == CommonConstants.TYPE_MAPPAGE_OPERATION_TRACE_PATH) {
                host = CommonNetReq.NET_NEW_TRACE_PATH;
            } else if (type == CommonConstants.TYPE_MAPPAGE_OPERATION_VIRTUAL_TRACK) {
                host = CommonNetReq.NET_NEW_VT_GROUP;
            }
            OKHttpManager.getInstance().sendComplexForm(host, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        LogUtil.d(TAG, "startSaveTracePath:" + jsonObject.getString(CommonNetReq.RESULT_DESC));
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                JSONObject jsonObject1 = jsonObject.getJSONObject(CommonNetReq.RESULT_DATA);
                                if (type == CommonConstants.TYPE_MAPPAGE_OPERATION_TRACE_PATH) {
                                    BaseTrace baseTrace = new TracePath();
                                    baseTrace.setId(jsonObject1.getIntValue(CommonNetReq.ID));
                                    baseTrace.setName(jsonObject1.getString(CommonNetReq.NAME));
                                    baseTrace.setDesc(jsonObject1.getString(CommonNetReq.DESC));
                                    baseTrace.setMapPage(jsonObject1.getIntValue(CommonNetReq.MAP_PAGE));
                                    baseTrace.setDeviceId(jsonObject1.getIntValue(CommonNetReq.DEVICE));
                                    EventBus.getDefault().post(new CreateNewBaseTraceSuccessEvent(baseTrace));
                                } else if (type == CommonConstants.TYPE_MAPPAGE_OPERATION_VIRTUAL_TRACK) {
                                    BaseTrace baseTrace = new VirtualTrackGroup();
                                    baseTrace.setId(jsonObject1.getIntValue(CommonNetReq.ID));
                                    baseTrace.setName(jsonObject1.getString(CommonNetReq.NAME));
                                    baseTrace.setDesc(jsonObject1.getString(CommonNetReq.DESCRIPTION));
                                    baseTrace.setMapPage(jsonObject1.getIntValue(CommonNetReq.MAP_PAGE));
                                    baseTrace.setDeviceId(jsonObject1.getIntValue(CommonNetReq.DEVICE));
                                    EventBus.getDefault().post(new CreateNewBaseTraceSuccessEvent(baseTrace));
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

    public static class TaskUpdateBaseTrace implements Runnable {
        private BaseTrace baseTrace;
        private int type;

        public TaskUpdateBaseTrace(BaseTrace baseTrace, int type) {
            this.baseTrace = baseTrace;
            this.type = type;
        }

        @Override
        public void run() {
            startUpdateBaseTrace();
        }

        private void startUpdateBaseTrace() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(CommonNetReq.DEVICE_ID, String.valueOf(baseTrace.getDeviceId()));
            map.put(CommonNetReq.ID, String.valueOf(baseTrace.getId()));
            map.put(CommonNetReq.NAME, baseTrace.getName());
            map.put(CommonNetReq.DESCRIPTION, baseTrace.getDesc());
            String host = null;
            if (type == CommonConstants.TYPE_MAPPAGE_OPERATION_TRACE_PATH) {
                host = CommonNetReq.NET_UPDATE_TRACE_PATH;
            } else if (type == CommonConstants.TYPE_MAPPAGE_OPERATION_VIRTUAL_TRACK) {
                host = CommonNetReq.NET_UPDATE_VT_GROUP;
            }
            OKHttpManager.getInstance().sendComplexForm(host, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        LogUtil.d(TAG, "startUpdateBaseTrace:" + jsonObject.getString(CommonNetReq.RESULT_DESC));
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                JSONObject jsonObject1 = jsonObject.getJSONObject(CommonNetReq.RESULT_DATA);
                                if (type == CommonConstants.TYPE_MAPPAGE_OPERATION_TRACE_PATH) {
                                    BaseTrace baseTrace = new TracePath();
                                    baseTrace.setId(jsonObject1.getIntValue(CommonNetReq.ID));
                                    baseTrace.setName(jsonObject1.getString(CommonNetReq.NAME));
                                    baseTrace.setDesc(jsonObject1.getString(CommonNetReq.DESC));
                                    baseTrace.setMapPage(jsonObject1.getIntValue(CommonNetReq.MAP_PAGE));
                                    baseTrace.setDeviceId(jsonObject1.getIntValue(CommonNetReq.DEVICE));
                                    EventBus.getDefault().post(new UpdateBaseTraceSuccessEvent(baseTrace));
                                } else if (type == CommonConstants.TYPE_MAPPAGE_OPERATION_VIRTUAL_TRACK) {
                                    BaseTrace baseTrace = new VirtualTrackGroup();
                                    baseTrace.setId(jsonObject1.getIntValue(CommonNetReq.ID));
                                    baseTrace.setName(jsonObject1.getString(CommonNetReq.NAME));
                                    baseTrace.setDesc(jsonObject1.getString(CommonNetReq.DESCRIPTION));
                                    baseTrace.setMapPage(jsonObject1.getIntValue(CommonNetReq.MAP_PAGE));
                                    baseTrace.setDeviceId(jsonObject1.getIntValue(CommonNetReq.DEVICE));
                                    EventBus.getDefault().post(new UpdateBaseTraceSuccessEvent(baseTrace));
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

    public static class TaskDeleteBaseTrace implements Runnable {
        private Device device;
        private BaseTrace baseTrace;
        private int type;

        public TaskDeleteBaseTrace(Device device, BaseTrace baseTrace, int type) {
            this.device = device;
            this.baseTrace = baseTrace;
            this.type = type;
        }

        @Override
        public void run() {
            startDeleteBaseTrace();
        }

        private void startDeleteBaseTrace() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(CommonNetReq.DEVICE_ID, String.valueOf(device.getId()));
            String requset = null;
            if (type == CommonConstants.TYPE_MAPPAGE_OPERATION_TRACE_PATH) {
                map.put(CommonNetReq.TRACE_PATH_ID, String.valueOf(baseTrace.getId()));
                requset = CommonNetReq.NET_DELETE_MAP_PAGE_PATH;
            } else if (type == CommonConstants.TYPE_MAPPAGE_OPERATION_VIRTUAL_TRACK) {
                map.put(CommonNetReq.VIRTUAL_TRACK, String.valueOf(baseTrace.getId()));
                requset = CommonNetReq.NET_DELETE_MAP_PAGE_VTGROUP;
            }
            OKHttpManager.getInstance().sendComplexForm(requset, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        LogUtil.d(TAG, jsonObject.getString(CommonNetReq.RESULT_DESC));
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                EventBus.getDefault().post(new DeleteBaseTraceEvent(baseTrace));
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
            map.put(CommonNetReq.DEVICE_ID, String.valueOf(device.getId()));
            OKHttpManager.getInstance().sendComplexForm(CommonNetReq.NET_GET_ALL_MAP_PAGES, map, new OKHttpManager.FuncJsonObj() {
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
                                        mapPages.setId(jsonArray.getJSONObject(i).getIntValue(CommonConstants.ID));
                                        mapPages.setName(jsonArray.getJSONObject(i).getString(CommonConstants.NAME));
                                        mapPages.setDescription(jsonArray.getJSONObject(i).getString(CommonConstants.DESCRIPTION));
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

    public static class TaskGetRunLogs implements Runnable {
        private Device device;
        private int page;

        public TaskGetRunLogs(Device device, int page) {
            this.device = device;
            this.page = page;
        }

        @Override
        public void run() {
            startGetRunLogs();
        }

        private void startGetRunLogs() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(CommonNetReq.DEVICE_ID, String.valueOf(device.getId()));
            map.put(CommonNetReq.PAGE, String.valueOf(page));
            LogUtil.d(TAG, "page: "+page);
            OKHttpManager.getInstance().sendComplexForm(CommonNetReq.NET_GET_RUN_LOGS, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                JSONArray jsonArray = jsonObject.getJSONArray(CommonNetReq.RESULT_DATA);
                                List<RunLog> runLogList = new ArrayList<>();
                                if (jsonArray.size() > 0) {
                                    for (int i = 0; i < jsonArray.size(); i++) {
                                        RunLog runLog = new RunLog();
                                        runLog.setId(jsonArray.getJSONObject(i).getIntValue(CommonConstants.ID));
                                        runLog.setLevel(jsonArray.getJSONObject(i).getIntValue(CommonConstants.LEVEL));
                                        runLog.setType_code(jsonArray.getJSONObject(i).getIntValue(CommonConstants.TYPE_CODE));
                                        runLog.setName(jsonArray.getJSONObject(i).getString(CommonConstants.NAME));
                                        runLog.setDescription(jsonArray.getJSONObject(i).getString(CommonConstants.DESCRIPTION));
                                        runLog.setLog_time(jsonArray.getJSONObject(i).getLongValue(CommonConstants.LOG_TIME));
                                        runLog.setDevice(jsonArray.getJSONObject(i).getIntValue(CommonConstants.DEVICE));
                                        runLogList.add(runLog);
                                    }
                                }
                                EventBus.getDefault().post(new GetRunLogsSuccessEvent(runLogList));
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

    public static class TaskGetDeviceException implements Runnable {
        @Override
        public void run() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            OKHttpManager.getInstance().sendComplexForm(CommonNetReq.NET_GET_DEVICE_EXCEPTION, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        LogUtil.d(TAG, jsonObject.getString(CommonNetReq.RESULT_DESC));
                        EventBus.getDefault().post(new DeviceExceptionEvent(jsonObject));
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
