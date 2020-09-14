package com.evertrend.tiger.common.utils.network;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.model.LatLng;
import com.evertrend.tiger.common.bean.BaseTrace;
import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.bean.DeviceGrant;
import com.evertrend.tiger.common.bean.MapPages;
import com.evertrend.tiger.common.bean.RobotSpot;
import com.evertrend.tiger.common.bean.RunLog;
import com.evertrend.tiger.common.bean.TracePath;
import com.evertrend.tiger.common.bean.VirtualTrackGroup;
import com.evertrend.tiger.common.bean.event.CreateNewBaseTraceSuccessEvent;
import com.evertrend.tiger.common.bean.event.CreateNewDeviceGrantFailEvent;
import com.evertrend.tiger.common.bean.event.CreateNewDeviceGrantSuccessEvent;
import com.evertrend.tiger.common.bean.event.DeleteBaseTraceEvent;
import com.evertrend.tiger.common.bean.event.DeleteDeviceGrantEvent;
import com.evertrend.tiger.common.bean.event.DeviceExceptionEvent;
import com.evertrend.tiger.common.bean.event.GetAllMapPagesSuccessEvent;
import com.evertrend.tiger.common.bean.event.GetDeviceAllGrantsSuccessEvent;
import com.evertrend.tiger.common.bean.event.GetDeviceGrantSuccessEvent;
import com.evertrend.tiger.common.bean.event.GetGpsFenceFailEvent;
import com.evertrend.tiger.common.bean.event.GetGpsFenceOKEvent;
import com.evertrend.tiger.common.bean.event.GetRunLogsSuccessEvent;
import com.evertrend.tiger.common.bean.event.SaveMapPageEvent;
import com.evertrend.tiger.common.bean.event.SaveTraceSpotFailEvent;
import com.evertrend.tiger.common.bean.event.SetPoseFailEvent;
import com.evertrend.tiger.common.bean.event.SetPoseOKEvent;
import com.evertrend.tiger.common.bean.event.SetStatusCompleteEvent;
import com.evertrend.tiger.common.bean.event.UpdateBaseTraceSuccessEvent;
import com.evertrend.tiger.common.bean.event.UpdateDeviceGrantSuccessEvent;
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
import com.evertrend.tiger.common.utils.general.JsonAnalysisUtil;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.common.utils.general.Utils;

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
                                List<TracePath> tracePathList = JsonAnalysisUtil.loadAllPath(jsonArray);
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
//                    DBUtil.saveSpotListToLocal(device, mapPages, tracePath, mTraceSpotList);
                    startSaveTraceSpotComplete(device, tracePath.getId());
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

    //保存循迹路径点完成，设置标志位，等待工控机从服务器读取
    private static void startSaveTraceSpotComplete(Device device, int tracePathId) {
        HashMap<String, String> map = new HashMap<>();
        map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
        map.put(CommonNetReq.DEVICE_ID, String.valueOf(device.getId()));
        map.put(CommonNetReq.TRACE_PATH_ID, String.valueOf(tracePathId));//注意当前选择循迹路径为空的情况
        OKHttpManager.getInstance().sendComplexForm(CommonNetReq.NET_ADD_SPOT_COMPLETE, map, new OKHttpManager.FuncJsonObj() {
            @Override
            public void onResponse(JSONObject jsonObject) throws JSONException {
                try {
                    LogUtil.d(TAG, "startSaveTraceSpotComplete:"+jsonObject.getString(CommonNetReq.RESULT_DESC));
                    switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                        case CommonNetReq.CODE_SUCCESS:
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
        if (spotFlag == 7) {
            String[] strs = currentPose.split(",");
            String startPose = strs[0]+","+strs[1]+","+strs[3];
            map.put(CommonNetReq.SPOT_FLAG_DATA, startPose);
        } else {
            map.put(CommonNetReq.SPOT_FLAG_DATA, currentPose);
        }
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
        private int status;
        public TaskRelocationOrSetCurrentMap(Device device, MapPages mapPages, int type, int status) {
            this.device = device;
            this.mapPages = mapPages;
            this.type = type;
            this.status = status;
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
            } else if (type == CommonConstants.TYPE_AUTO_RECORD_PATH) {
                map.put(CommonNetReq.STATUS, String.valueOf(status));
                net = CommonNetReq.NET_AUTO_RECORD_PATH;
            } else if (type == CommonConstants.TYPE_ENABLE_GPS_FENCE) {
                map.put(CommonNetReq.STATUS, String.valueOf(status));
                net = CommonNetReq.NET_ENABLE_GPS_FENCE;
            } else if (type == CommonConstants.TYPE_LOG_GPS_MAP_SLAM) {
                map.put(CommonNetReq.STATUS, String.valueOf(status));
                net = CommonNetReq.NET_LOG_GPS_MAP_SLAM;
            } else if (type == CommonConstants.TYPE_DELETE_GPS_MAP_SLAM) {
                map.put(CommonNetReq.STATUS, String.valueOf(status));
                net = CommonNetReq.NET_DELETE_GPS_MAP_SLAM;
            } else if (type == CommonConstants.TYPE_LOAD_MAP) {
                map.put(CommonNetReq.STATUS, String.valueOf(status));
                net = CommonNetReq.NET_LOAD_MAP;
            } else if (type == CommonConstants.TYPE_SET_GPS_FENCE) {
                map.put(CommonNetReq.STATUS, String.valueOf(status));
                net = CommonNetReq.NET_SET_GPS_FENCE;
            }
            OKHttpManager.getInstance().sendComplexForm(net, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        LogUtil.d(TAG, jsonObject.getString(CommonNetReq.RESULT_DESC));
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                LogUtil.d(TAG, "type:"+type);
//                                LogUtil.d(TAG, "mapPages:"+mapPages);
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
//                    EventBus.getDefault().post(new DeleteTraceSpotListCompleteEvent(mRobotSpotList));
                    startDeleteTraceSpotComplete(mRobotSpotList);
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

    private static void startDeleteTraceSpotComplete(final List<RobotSpot> mRobotSpotList) {
        HashMap<String, String> map = new HashMap<>();
        map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
        map.put(CommonNetReq.ID, String.valueOf(mRobotSpotList.get(0).getId()));
        OKHttpManager.getInstance().sendComplexForm(CommonNetReq.NET_DELETE_TRACE_SPOT_COMPLETE, map, new OKHttpManager.FuncJsonObj() {
            @Override
            public void onResponse(JSONObject jsonObject) throws JSONException {
                try {
                    LogUtil.d(TAG, "startDeleteTraceSpotComplete:"+jsonObject.getString(CommonNetReq.RESULT_DESC));
                    switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                        case CommonNetReq.CODE_SUCCESS:
                            EventBus.getDefault().post(new DeleteTraceSpotListCompleteEvent(mRobotSpotList));
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
        private int spotFlag;//标志该点类型，0：路径点，1: 充电点，2: 加水点，3: 倾倒垃圾点，4：车库点，5：公共点，7：开始点，8：围栏点
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

    public static class TaskGetDeviceAllGrants implements Runnable {
        private Device device;
        public TaskGetDeviceAllGrants(Device device) {
            this.device = device;
        }

        @Override
        public void run() {
            startGetDeviceAllGrants();
        }

        private void startGetDeviceAllGrants() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(CommonNetReq.DEVICE_ID, String.valueOf(device.getId()));
            OKHttpManager.getInstance().sendComplexForm(CommonNetReq.NET_GET_DEVICE_ALL_GRANTS, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                JSONArray jsonArray = jsonObject.getJSONArray(CommonNetReq.RESULT_DATA);
                                List<DeviceGrant> deviceGrantList = new ArrayList<>();
                                if (jsonArray.size() > 0) {
                                    for (int i = 0; i < jsonArray.size(); i++) {
                                        DeviceGrant deviceGrant = new DeviceGrant();
                                        deviceGrant.setId(jsonArray.getJSONObject(i).getIntValue(CommonNetReq.ID));
                                        deviceGrant.setUser(jsonArray.getJSONObject(i).getString(CommonNetReq.USER_GRANTED));
                                        deviceGrant.setAuthorization_item(jsonArray.getJSONObject(i).getString(CommonNetReq.AUTHORIZATION_ITEM));
                                        deviceGrant.setDevice(jsonArray.getJSONObject(i).getIntValue(CommonNetReq.DEVICE));
                                        deviceGrant.setUser_flag(jsonArray.getJSONObject(i).getIntValue(CommonNetReq.USER_FLAG));
                                        deviceGrantList.add(deviceGrant);
                                    }
                                }
                                EventBus.getDefault().post(new GetDeviceAllGrantsSuccessEvent(deviceGrantList));
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

    public static class TaskGetDeviceGrant implements Runnable {
        private Device device;
        public TaskGetDeviceGrant(Device device) {
            this.device = device;
        }

        @Override
        public void run() {
            startGetDeviceGrant();
        }

        private void startGetDeviceGrant() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(CommonNetReq.DEVICE_ID, String.valueOf(device.getId()));
            OKHttpManager.getInstance().sendComplexForm(CommonNetReq.NET_GET_DEVICE_GRANT, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                JSONObject object = jsonObject.getJSONObject(CommonNetReq.RESULT_DATA);
                                DeviceGrant deviceGrant = new DeviceGrant();
                                deviceGrant.setId(object.getIntValue(CommonNetReq.ID));
                                deviceGrant.setUser(object.getString(CommonNetReq.USER_GRANTED));
                                deviceGrant.setAuthorization_item(object.getString(CommonNetReq.AUTHORIZATION_ITEM));
                                deviceGrant.setDevice(object.getIntValue(CommonNetReq.DEVICE));
                                deviceGrant.setUser_flag(object.getIntValue(CommonNetReq.USER_FLAG));
                                EventBus.getDefault().post(new GetDeviceGrantSuccessEvent(deviceGrant));
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

    public static class TaskSaveDeviceGrant implements Runnable {
        private DeviceGrant deviceGrant;
        public TaskSaveDeviceGrant(DeviceGrant deviceGrant) {
            this.deviceGrant = deviceGrant;
        }

        @Override
        public void run() {
            startSaveDeviceGrant();
        }

        private void startSaveDeviceGrant() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(CommonNetReq.DEVICE_ID, String.valueOf(deviceGrant.getDevice()));
            map.put(CommonNetReq.USER_GRANTED, deviceGrant.getUser());
            map.put(CommonNetReq.AUTHORIZATION_ITEM, deviceGrant.getAuthorization_item());
            map.put(CommonNetReq.USER_FLAG, String.valueOf(deviceGrant.getUser_flag()));
            OKHttpManager.getInstance().sendComplexForm(CommonNetReq.NET_NEW_DEVICE_GRANT, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        LogUtil.d(TAG, "startSaveDeviceGrant:" + jsonObject.getString(CommonNetReq.RESULT_DESC));
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                JSONObject jsonObject1 = jsonObject.getJSONObject(CommonNetReq.RESULT_DATA);
                                DeviceGrant deviceGrant = new DeviceGrant();
                                deviceGrant.setId(jsonObject1.getIntValue(CommonNetReq.ID));
                                deviceGrant.setUser(jsonObject1.getString(CommonNetReq.USER_GRANTED));
                                deviceGrant.setAuthorization_item(jsonObject1.getString(CommonNetReq.AUTHORIZATION_ITEM));
                                deviceGrant.setDevice(jsonObject1.getIntValue(CommonNetReq.DEVICE));
                                deviceGrant.setUser_flag(jsonObject1.getIntValue(CommonNetReq.USER_FLAG));
                                EventBus.getDefault().post(new CreateNewDeviceGrantSuccessEvent(deviceGrant));
                                break;
                            case CommonNetReq.ERR_CODE_GRANTED_USER_NON_EXISTENT:
                            case CommonNetReq.ERR_CODE_DUPLICATE_AUTHORIZATION:
                            case CommonNetReq.ERR_CODE_CANNOT_AUTHORIZE_TOYOURSELF:
                            case CommonNetReq.ERR_CODE_SAVE_FAIL:
                                EventBus.getDefault().post(new CreateNewDeviceGrantFailEvent(jsonObject.getIntValue(CommonNetReq.RESULT_CODE)));
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

    public static class TaskUpdateDeviceGrant implements Runnable {
        private DeviceGrant deviceGrant;
        public TaskUpdateDeviceGrant(DeviceGrant deviceGrant) {
            this.deviceGrant = deviceGrant;
        }

        @Override
        public void run() {
            startUpdateDeviceGrant();
        }

        private void startUpdateDeviceGrant() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(CommonNetReq.ID, String.valueOf(deviceGrant.getId()));
            map.put(CommonNetReq.DEVICE_ID, String.valueOf(deviceGrant.getDevice()));
            map.put(CommonNetReq.USER_GRANTED, deviceGrant.getUser());
            map.put(CommonNetReq.AUTHORIZATION_ITEM, deviceGrant.getAuthorization_item());
//            map.put(CommonNetReq.USER_FLAG, String.valueOf(deviceGrant.getUser_flag()));
            OKHttpManager.getInstance().sendComplexForm(CommonNetReq.NET_UPDATE_DEVICE_GRANT, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        LogUtil.d(TAG, "startUpdateDeviceGrant:" + jsonObject.getString(CommonNetReq.RESULT_DESC));
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                JSONObject jsonObject1 = jsonObject.getJSONObject(CommonNetReq.RESULT_DATA);
                                DeviceGrant deviceGrant = new DeviceGrant();
                                deviceGrant.setId(jsonObject1.getIntValue(CommonNetReq.ID));
                                deviceGrant.setUser(jsonObject1.getString(CommonNetReq.USER_GRANTED));
                                deviceGrant.setAuthorization_item(jsonObject1.getString(CommonNetReq.AUTHORIZATION_ITEM));
                                deviceGrant.setDevice(jsonObject1.getIntValue(CommonNetReq.DEVICE));
                                deviceGrant.setUser_flag(jsonObject1.getIntValue(CommonNetReq.USER_FLAG));
                                EventBus.getDefault().post(new UpdateDeviceGrantSuccessEvent(deviceGrant));
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

    public static class TaskDeleteDeviceGrant implements Runnable {
        private DeviceGrant deviceGrant;
        public TaskDeleteDeviceGrant(DeviceGrant deviceGrant) {
            this.deviceGrant = deviceGrant;
        }

        @Override
        public void run() {
            startDeleteDeviceGrant();
        }

        private void startDeleteDeviceGrant() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(CommonNetReq.DEVICE_ID, String.valueOf(deviceGrant.getDevice()));
            map.put(CommonNetReq.ID, String.valueOf(deviceGrant.getId()));
            OKHttpManager.getInstance().sendComplexForm(CommonNetReq.NET_DELETE_DEVICE_GRANT, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        LogUtil.d(TAG, "deleteDeviceGrant: "+jsonObject.getString(CommonNetReq.RESULT_DESC));
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                            case CommonNetReq.ERR_CODE_DELETE_GRANT_DEVICE_NON_EXISTENT:
                                EventBus.getDefault().post(new DeleteDeviceGrantEvent(deviceGrant));
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
            map.put(CommonNetReq.DEVICE_ID, String.valueOf(device.getId()));
            map.put(CommonNetReq.STATUS, String.valueOf(status));
            String request = "";
            switch (mark) {
                case "sw_device_run":
                    request = CommonNetReq.NET_SET_DEVICE_STATUS;
                    break;
                case "sw_main_sweep":
                    request = CommonNetReq.NET_SET_MAIN_SWEEP;
                    break;
                case "sw_side_sweep":
                    request = CommonNetReq.NET_SET_SIDE_SWEEP;
                    break;
                case "sw_sprinkling_water":
                    request = CommonNetReq.NET_SET_SPRINKLING_WATER;
                    break;
                case "sw_left_tail_light":
                    request = CommonNetReq.NET_SET_LEFT_TAIL_LIGHT;
                    break;
                case "sw_right_tail_light":
                    request = CommonNetReq.NET_SET_RIGHT_TAIL_LIGHT;
                    break;
                case "sw_alarm_light":
                    request = CommonNetReq.NET_SET_ALARM_LIGHT;
                    break;
                case "sw_front_light":
                    request = CommonNetReq.NET_SET_FRONT_LIGHT;
                    break;
                case "sw_horn":
                    request = CommonNetReq.NET_SET_HORN;
                    break;
                case "sw_suck_fan":
                    request = CommonNetReq.NET_SET_SUCK_FUN;
                    break;
                case "sw_vibrating_dust":
                    request = CommonNetReq.NET_SET_VIBRATING_DUST;
                    break;
                case "sw_motor":
                    request = CommonNetReq.NET_SET_MOTOR;
                    break;
                case "sw_garbage_valve":
                    request = CommonNetReq.NET_SET_GARBAGE_VALVE;
                    break;
                case "sw_emergency_stop":
                    request = CommonNetReq.NET_SET_EMERGENCY_STOP;
                    break;
                case "rb_go_to_recharge":
                    request = CommonNetReq.NET_GO_TO_RECHARGE;
                    break;
                case "rb_go_to_add_water":
                    request = CommonNetReq.NET_GO_TO_ADD_WATER;
                    break;
                case "rb_go_to_empty_trash":
                    request = CommonNetReq.NET_GO_TO_EMPTY_TRASH;
                    break;
                case "rb_go_to_garage":
                    request = CommonNetReq.NET_GO_TO_GARAGE;
                    break;
                case "rb_go_to_work":
                    request = CommonNetReq.NET_GO_TO_WORK;
                    break;
                case "rb_go_to_idle":
                    request = CommonNetReq.NET_GO_TO_IDEL;
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

    public static class TaskSetPose implements Runnable {
        Device device;
        MapPages mapPages;
        String pose;

        public TaskSetPose(Device device, MapPages mapPages) {
            this.device = device;
            this.mapPages = mapPages;
        }

        public TaskSetPose(Device device, MapPages mapPages, String pose) {
            this.device = device;
            this.mapPages = mapPages;
            this.pose = pose;
        }

        @Override
        public void run() {
//            LogUtil.d(TAG, "pose: " + pose);
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(CommonNetReq.DEVICE_ID, String.valueOf(device.getId()));
            map.put(CommonNetReq.MAP_PAGE, String.valueOf(mapPages.getId()));
//            map.put(CommonNetReq.SET_POSE, pose);
            OKHttpManager.getInstance().sendComplexForm(CommonNetReq.NET_SET_START_SPOT, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                EventBus.getDefault().post(new SetPoseOKEvent());
                                break;
                            default:
                                EventBus.getDefault().post(new SetPoseFailEvent(jsonObject.getString(CommonNetReq.RESULT_DESC)));
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

    public static class TaskGetGPSFence implements Runnable {
        Device device;
        MapPages mapPages;

        public TaskGetGPSFence(Device device, MapPages mapPages) {
            this.device = device;
            this.mapPages = mapPages;
        }

        @Override
        public void run() {
            startGetGPSFence();
        }

        private void startGetGPSFence() {
            HashMap<String, String> map = new HashMap<>();
            map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
            map.put(CommonNetReq.DEVICE_ID, String.valueOf(device.getId()));
            map.put(CommonNetReq.MAP_PAGE, String.valueOf(mapPages.getId()));
            OKHttpManager.getInstance().sendComplexForm(CommonNetReq.NET_GET_GPS_FENCE, map, new OKHttpManager.FuncJsonObj() {
                @Override
                public void onResponse(JSONObject jsonObject) throws JSONException {
                    try {
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
                                List<LatLng> points = new ArrayList<>();
                                JSONArray jsonArray = jsonObject.getJSONArray(CommonNetReq.RESULT_DATA);
                                if (jsonArray != null) {
                                    for (int i = 0; i <= jsonArray.size(); i++) {
                                        if (i == jsonArray.size()) {//形成闭环
                                            JSONObject object = jsonArray.getJSONObject(0);
                                            LatLng latLng = new LatLng(object.getDoubleValue(CommonNetReq.LATITUDE), object.getDoubleValue(CommonNetReq.LONGITUDE));
                                            points.add(latLng);
                                            break;
                                        }
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        LatLng latLng = Utils.GPSCoordinateToBaiduCoordinate(new LatLng(object.getDoubleValue(CommonNetReq.LATITUDE), object.getDoubleValue(CommonNetReq.LONGITUDE)));
                                        points.add(latLng);
                                    }
                                }
                                EventBus.getDefault().post(new GetGpsFenceOKEvent(points));
                                break;
                            default:
                                EventBus.getDefault().post(new GetGpsFenceFailEvent(jsonObject.getString(CommonNetReq.RESULT_DESC)));
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
