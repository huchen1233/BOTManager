package com.evertrend.tiger.device.widget;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.evertrend.tiger.common.utils.general.DialogUtil;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.common.utils.general.Utils;
import com.evertrend.tiger.device.R;
import com.evertrend.tiger.device.adapter.TaskListViewAdapter;
import com.evertrend.tiger.device.adapter.TaskSpecialWorkListViewAdapter;
import com.evertrend.tiger.common.bean.BaseTrace;
import com.evertrend.tiger.device.bean.CleanTask;
import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.bean.MapPages;
import com.evertrend.tiger.common.bean.RobotSpot;
import com.evertrend.tiger.device.bean.event.GetAllSpecialTaskSpotSuccessEvent;
import com.evertrend.tiger.device.bean.event.GetAllVirtualTrackGroupSuccessEvent;
import com.evertrend.tiger.device.bean.event.GetMapPagesAllPathSuccessEvent;
import com.evertrend.tiger.device.bean.event.SaveCleanTaskSuccessEvent;
import com.evertrend.tiger.device.utils.Constants;
import com.evertrend.tiger.device.utils.TaskUtils;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CleanTaskBottomPopupView extends BottomPopupView implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private static final String TAG = CleanTaskBottomPopupView.class.getSimpleName();

    private EditText et_task_name, et_task_desc;
    private ListView lv_trace_path;
    private ListView lv_virtual_track_group;
    private ListView lv_special_work;
    private EditText et_task_priority, et_task_trace_path_priority;
    private EditText et_virtual_track_group_priority, et_special_work_priority;
    private TimePicker tp_start_time;
    private RadioGroup rig_task_option;
    private RadioButton rib_only_once, rib_everyday, rib_infinite_cycle;
    private Button btn_task_submit;
    private RadioGroup rig_task_type;
    private RadioButton rib_trace_path, rib_virtual_track_group, rib_special_work;
    private LinearLayout ll_trace_path, ll_virtual_track_group, ll_special_work;

    private Context context;
    private Device device;
    private MapPages choiceMapPages;
    private List<CleanTask> cleanTaskList;
    private TaskListViewAdapter taskTPListViewAdapter;
    private TaskListViewAdapter taskVTGListViewAdapter;
    private TaskSpecialWorkListViewAdapter taskSpecialWorkListViewAdapter;
    private List<? extends BaseTrace> tracePathList;
    private List<? extends BaseTrace> virtualTrackGroupList;
    private List<RobotSpot> specialWorkSpotList;

    private CleanTask cleanTask;
    private CleanTask newCleanTask;

    private ScheduledThreadPoolExecutor scheduledThreadGetMapPagesAllPath;
    private ScheduledThreadPoolExecutor scheduledThreadGetMapPagesAllVirtualTrackGroup;
    private ScheduledThreadPoolExecutor scheduledThreadGetMapPagesAllSpecialWorkSpot;
    private ScheduledThreadPoolExecutor scheduledThreadSaveCleanTask;

    public CleanTaskBottomPopupView(Context context, Device device, MapPages choiceMapPages, List<CleanTask> cleanTaskList) {
        super(context);
        this.context = context;
        this.device = device;
        this.choiceMapPages = choiceMapPages;
        this.cleanTaskList = cleanTaskList;
        newCleanTask = new CleanTask();
    }

    public CleanTaskBottomPopupView(Context context, Device device, MapPages choiceMapPages, CleanTask cleanTask, List<CleanTask> cleanTaskList) {
        super(context);
        this.context = context;
        this.device = device;
        this.choiceMapPages = choiceMapPages;
        this.cleanTaskList = cleanTaskList;
        this.cleanTask = cleanTask;
        newCleanTask = new CleanTask();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.yl_device_dialog_create_clean_task;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initData();
        initView();
        if (cleanTask != null) {
            newCleanTask.setCleanTask(cleanTask);
            initViewData();
        }
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDismiss() {
        exit();
        super.onDismiss();
    }

    @Override
    protected void onDetachedFromWindow() {
        exit();
        super.onDetachedFromWindow();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(GetMapPagesAllPathSuccessEvent event) {
        stopGetMapPagesAllPathTimer();
        tracePathList = event.getTracePathList();
        taskTPListViewAdapter = new TaskListViewAdapter(tracePathList, context);
        lv_trace_path.setAdapter(taskTPListViewAdapter);
        if (cleanTask != null) {//编辑任务初始化循迹路径勾选
            String[] tracePaths = newCleanTask.getTracePaths().split(",");
            for (int i = 0; i < tracePathList.size(); i++) {
                for (String s : tracePaths) {
                    if (tracePathList.get(i).getId() == Integer.parseInt(s)) {
                        lv_trace_path.setItemChecked(i, true);
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(GetAllVirtualTrackGroupSuccessEvent event) {
        stopGetMapPagesAllVirtualTrackGroupTimer();
        virtualTrackGroupList = event.getVirtualTrackGroups();
        taskVTGListViewAdapter = new TaskListViewAdapter(virtualTrackGroupList, context);
        lv_virtual_track_group.setAdapter(taskVTGListViewAdapter);
        if (cleanTask != null) {//编辑任务初始化虚拟轨道组勾选
            String[] vTGroups = newCleanTask.getVirtualTrackGroups().split(",");
            for (int i = 0; i < virtualTrackGroupList.size(); i++) {
                for (String s : vTGroups) {
                    if (virtualTrackGroupList.get(i).getId() == Integer.parseInt(s)) {
                        lv_virtual_track_group.setItemChecked(i, true);
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEvent(GetAllSpecialTaskSpotSuccessEvent event) {
        stopGetMapPagesAllSpecialTaskSpotTimer();
        specialWorkSpotList = event.getRobotSpotList();
        taskSpecialWorkListViewAdapter = new TaskSpecialWorkListViewAdapter(specialWorkSpotList, context);
        lv_special_work.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        lv_special_work.setAdapter(taskSpecialWorkListViewAdapter);
        if (cleanTask != null) {//编辑任务初始化特别任务勾选
//            String[] sWorks = newCleanTask.getSpecialWorks().split(",");
            String sWorks = newCleanTask.getSpecialWorks();
            for (int i = 0; i < specialWorkSpotList.size(); i++) {
                if (specialWorkSpotList.get(i).getId() == Integer.parseInt(sWorks)) {
                    lv_special_work.setItemChecked(i, true);
                    break;
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SaveCleanTaskSuccessEvent messageEvent) {
        stopSaveCleanTaskTimer();
        DialogUtil.hideProgressDialog();
//        EventBus.getDefault().post(new SaveCleanTaskSuccessEvent(messageEvent.getCleanTask()));
        Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    private void exit() {
        stopGetMapPagesAllPathTimer();
        stopGetMapPagesAllVirtualTrackGroupTimer();
        stopGetMapPagesAllSpecialTaskSpotTimer();
        stopSaveCleanTaskTimer();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void stopGetMapPagesAllPathTimer() {
        if (scheduledThreadGetMapPagesAllPath != null) {
            scheduledThreadGetMapPagesAllPath.shutdownNow();
            scheduledThreadGetMapPagesAllPath = null;
        }
    }

    private void stopGetMapPagesAllVirtualTrackGroupTimer() {
        if (scheduledThreadGetMapPagesAllVirtualTrackGroup != null) {
            scheduledThreadGetMapPagesAllVirtualTrackGroup.shutdownNow();
            scheduledThreadGetMapPagesAllVirtualTrackGroup = null;
        }
    }

    private void stopGetMapPagesAllSpecialTaskSpotTimer() {
        if (scheduledThreadGetMapPagesAllSpecialWorkSpot != null) {
            scheduledThreadGetMapPagesAllSpecialWorkSpot.shutdownNow();
            scheduledThreadGetMapPagesAllSpecialWorkSpot = null;
        }
    }

    private void stopSaveCleanTaskTimer() {
        if (scheduledThreadSaveCleanTask != null) {
            scheduledThreadSaveCleanTask.shutdownNow();
            scheduledThreadSaveCleanTask = null;
        }
    }

    private void initData() {
        scheduledThreadGetMapPagesAllPath = new ScheduledThreadPoolExecutor(5);
        scheduledThreadGetMapPagesAllPath.scheduleAtFixedRate(new TaskUtils.TaskGetMapPagesAllPath(device, choiceMapPages),
                0, 10, TimeUnit.SECONDS);
        scheduledThreadGetMapPagesAllVirtualTrackGroup = new ScheduledThreadPoolExecutor(5);
        scheduledThreadGetMapPagesAllVirtualTrackGroup.scheduleAtFixedRate(new TaskUtils.TaskGetMapPagesAllVirtualTrackGroup(device, choiceMapPages),
                0, 10, TimeUnit.SECONDS);
        scheduledThreadGetMapPagesAllSpecialWorkSpot = new ScheduledThreadPoolExecutor(5);
        scheduledThreadGetMapPagesAllSpecialWorkSpot.scheduleAtFixedRate(new TaskUtils.TaskGetMapPagesAllSpecialWorkSpot(device, choiceMapPages),
                0, 10, TimeUnit.SECONDS);
    }

    private void initView() {
        et_task_name = findViewById(R.id.et_task_name);
        et_task_desc = findViewById(R.id.et_task_desc);
        lv_trace_path = findViewById(R.id.lv_trace_path);
        lv_virtual_track_group = findViewById(R.id.lv_virtual_track_group);
        lv_special_work = findViewById(R.id.lv_special_work);
        et_task_priority = findViewById(R.id.et_task_priority);
        et_task_trace_path_priority = findViewById(R.id.et_task_trace_path_priority);
        et_virtual_track_group_priority = findViewById(R.id.et_virtual_track_group_priority);
        et_special_work_priority = findViewById(R.id.et_special_work_priority);
        tp_start_time = findViewById(R.id.tp_start_time);
        rig_task_option = findViewById(R.id.rig_task_option);
        rib_only_once = findViewById(R.id.rib_only_once);
        rib_everyday = findViewById(R.id.rib_everyday);
        rib_infinite_cycle = findViewById(R.id.rib_infinite_cycle);
        rig_task_type = findViewById(R.id.rig_task_type);
        rib_trace_path = findViewById(R.id.rib_trace_path);
        rib_virtual_track_group = findViewById(R.id.rib_virtual_track_group);
        rib_special_work = findViewById(R.id.rib_special_work);
        ll_trace_path = findViewById(R.id.ll_trace_path);
        ll_virtual_track_group = findViewById(R.id.ll_virtual_track_group);
        ll_special_work = findViewById(R.id.ll_special_work);
        rig_task_option.setOnCheckedChangeListener(this);
        rig_task_type.setOnCheckedChangeListener(this);
        tp_start_time.setIs24HourView(true);
        resizePikcer(tp_start_time);
        btn_task_submit = findViewById(R.id.btn_task_submit);

        tp_start_time.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                LogUtil.i(context, TAG, "choice houre: " + hourOfDay);
                LogUtil.i(context, TAG, "choice minute: " + minute);
                if (minute < 10) {
                    newCleanTask.setStartTime(hourOfDay + ":0" + minute);
                } else {
                    newCleanTask.setStartTime(hourOfDay + ":" + minute);
                }
            }
        });
        btn_task_submit.setOnClickListener(this);
    }

    private void initViewData() {
        LogUtil.i(context, TAG, "clean tack : " + newCleanTask.toString());
        et_task_name.setText(newCleanTask.getName());
        et_task_name.setSelection(newCleanTask.getName().length());
        et_task_desc.setText(newCleanTask.getDesc());
        et_task_priority.setText(newCleanTask.getTaskPriority() + "");
        et_task_trace_path_priority.setText(newCleanTask.getTracePathsPriority() + "");
        et_virtual_track_group_priority.setText(newCleanTask.getVirtualTrackGroupsPriority() + "");
        et_special_work_priority.setText(newCleanTask.getSpecialWorksPriority() + "");
        if (newCleanTask.getTaskOption() == Constants.TYPE_EXEC_FLAG_ONLY_ONCE) {
            rib_only_once.setChecked(true);
        } else if (newCleanTask.getTaskOption() == Constants.TYPE_EXEC_FLAG_EVERYDAY) {
            rib_everyday.setChecked(true);
        } else if (newCleanTask.getTaskOption() == Constants.TYPE_EXEC_FLAG_INFINITE_CYCLE) {
            rib_infinite_cycle.setChecked(true);
        }
        switch (newCleanTask.getTaskType()) {
            case Constants.TASK_TYPE_TRACE_PATH:
                rib_trace_path.setChecked(true);
                break;
            case Constants.TASK_TYPE_VIRTUAL_TRACK:
                rib_virtual_track_group.setChecked(true);
                break;
            case Constants.TASK_TYPE_SPE_WORK:
                rib_special_work.setChecked(true);
                break;
        }
        String[] startTime = newCleanTask.getStartTime().split(":");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tp_start_time.setHour(Integer.parseInt(startTime[0]));
            tp_start_time.setMinute(Integer.parseInt(startTime[1]));
        } else {
            tp_start_time.setCurrentHour(Integer.valueOf(startTime[0]));
            tp_start_time.setCurrentMinute(Integer.parseInt(startTime[1]));
        }
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext()) * .85f);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_task_submit) {
            newCleanTask.setDevice(device.getId());
            newCleanTask.setMapPage(choiceMapPages.getId());
            newCleanTask.setName(et_task_name.getText().toString().trim());
            newCleanTask.setDesc(et_task_desc.getText().toString().trim());
            newCleanTask.setTaskPriority(Integer.parseInt(et_task_priority.getText().toString().trim()));
            newCleanTask.setTracePathsPriority(Integer.parseInt(et_task_trace_path_priority.getText().toString().trim()));
            newCleanTask.setVirtualTrackGroupsPriority(Integer.parseInt(et_virtual_track_group_priority.getText().toString().trim()));
            newCleanTask.setSpecialWorksPriority(Integer.parseInt(et_special_work_priority.getText().toString().trim()));
            if (checkTaskValue()) {
                if (newCleanTask.getTaskOption() == 0) {
                    newCleanTask.setTaskOption(2);
                }
                initTaskStartTime();
                if (cleanTask != null) {
                    LogUtil.i(context, TAG, "task info: " + cleanTask.toString());
                }
                LogUtil.i(context, TAG, "new task info: " + newCleanTask.toString());
                if (newCleanTask.equals(cleanTask)) {
                    Toast.makeText(context, "任务未改变,请先更新再提交", Toast.LENGTH_LONG).show();
                } else {
                    startSubmitCleanTask();
                }
            }
        }
    }

    private void startSubmitCleanTask() {
        DialogUtil.showProgressDialog(context, getResources().getString(R.string.yl_common_saving), false, false);
        scheduledThreadSaveCleanTask = new ScheduledThreadPoolExecutor(5);
        if (cleanTask == null) {//创建任务
            scheduledThreadSaveCleanTask.scheduleAtFixedRate(new TaskUtils.TaskSaveCleanTask(device, choiceMapPages, newCleanTask, false),
                    0, 10, TimeUnit.SECONDS);
        } else {//更新任务
            scheduledThreadSaveCleanTask.scheduleAtFixedRate(new TaskUtils.TaskSaveCleanTask(device, choiceMapPages, newCleanTask, true),
                    0, 20, TimeUnit.SECONDS);
        }
    }

    private boolean checkTaskValue() {
        if (TextUtils.isEmpty(newCleanTask.getName())) {
            Toast.makeText(context, "请输入任务名称", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            for (CleanTask c:cleanTaskList) {
                if (cleanTask != null) {
                    if (newCleanTask.getName().equals(cleanTask.getName())) {
                        break;
                    }
                }
                if (newCleanTask.getName().equals(c.getName())) {
                    Toast.makeText(context, "任务名称重复，请更换", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            return checkWorkChoice();
        }
    }

    private void initTaskStartTime() {
        if (newCleanTask.getStartTime() == null || TextUtils.isEmpty(newCleanTask.getStartTime())) {
            newCleanTask.setStartTime(Utils.getTimeStame().substring(0, 10));//服务器php时间戳为10位
        } else {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            String startTime = year + "-" + (month + 1) + "-" + day + " " + newCleanTask.getStartTime();//month默认为0-11，需要手动加1
            try {
                long beginTimeStamp = ((Date) Constants.SIMPLE_DATE_FORMAT.parse(startTime)).getTime();
                String beginTime = String.valueOf(beginTimeStamp / 1000);//Java（13位）和PHP（10位）时间戳不同，需要转换
                newCleanTask.setStartTime(beginTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkWorkChoice() {
        SparseBooleanArray pathBooleanArray = lv_trace_path.getCheckedItemPositions();
        StringBuilder tracePaths = new StringBuilder();
        for (int i = 0; i < tracePathList.size(); i++) {
            if (pathBooleanArray.get(i)) {
                if (tracePaths.length() == 0) {
                    tracePaths.append(tracePathList.get(i).getId());
                } else {
                    tracePaths.append("," + tracePathList.get(i).getId());
                }
            }
        }
        LogUtil.i(context, TAG, "trace path: " + tracePaths.toString());
        SparseBooleanArray groupBooleanArray = lv_virtual_track_group.getCheckedItemPositions();
        StringBuilder vTGroups = new StringBuilder();
        for (int i = 0; i < virtualTrackGroupList.size(); i++) {
            if (groupBooleanArray.get(i)) {
                if (vTGroups.length() == 0) {
                    vTGroups.append(virtualTrackGroupList.get(i).getId());
                } else {
                    vTGroups.append("," + virtualTrackGroupList.get(i).getId());
                }
            }
        }
        LogUtil.i(context, TAG, "group : " + vTGroups.toString());
//        SparseBooleanArray specialWorkBooleanArray = lv_special_work.getCheckedItemPositions();
//        StringBuilder specialWorks = new StringBuilder();
//        for (int i = 0; i < specialWorkSpotList.size(); i++) {
//            if (specialWorkBooleanArray.get(i)) {
//                if (specialWorks.length() == 0) {
//                    specialWorks.append(specialWorkSpotList.get(i).getId());
//                } else {
//                    specialWorks.append("," + specialWorkSpotList.get(i).getId());
//                }
//            }
//        }
        String specialWork = "";
        if (lv_special_work.getCheckedItemPosition() != -1) {
            specialWork = specialWorkSpotList.get(lv_special_work.getCheckedItemPosition()).getId()+"";
            LogUtil.i(context, TAG, "special work : " + specialWork);
        }

        if (tracePaths.length() == 0 && vTGroups.length() == 0 && TextUtils.isEmpty(specialWork)) {
            Toast.makeText(context, R.string.yl_device_task_choice_tips, Toast.LENGTH_LONG).show();
            return false;
        } else {
            switch (newCleanTask.getTaskType()) {
                case Constants.TASK_TYPE_TRACE_PATH:
                    newCleanTask.setTracePaths(tracePaths.toString());
                    newCleanTask.setVirtualTrackGroups("");
                    newCleanTask.setSpecialWorks("");
                    break;
                case Constants.TASK_TYPE_VIRTUAL_TRACK:
                    newCleanTask.setTracePaths("");
                    newCleanTask.setVirtualTrackGroups(vTGroups.toString());
                    newCleanTask.setSpecialWorks("");
                    break;
                case Constants.TASK_TYPE_SPE_WORK:
                    newCleanTask.setTracePaths("");
                    newCleanTask.setVirtualTrackGroups("");
                    newCleanTask.setSpecialWorks(specialWork);
                    break;
            }
        }
        return true;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.getCheckedRadioButtonId() == R.id.rib_only_once) {
            newCleanTask.setTaskOption(Constants.TYPE_EXEC_FLAG_ONLY_ONCE);
        } else if (group.getCheckedRadioButtonId() == R.id.rib_everyday) {
            newCleanTask.setTaskOption(Constants.TYPE_EXEC_FLAG_EVERYDAY);
        } else if (group.getCheckedRadioButtonId() == R.id.rib_infinite_cycle) {
            newCleanTask.setTaskOption(Constants.TYPE_EXEC_FLAG_INFINITE_CYCLE);
        } else if (group.getCheckedRadioButtonId() == R.id.rib_trace_path) {
            newCleanTask.setTaskType(Constants.TASK_TYPE_TRACE_PATH);
            ll_trace_path.setVisibility(VISIBLE);
            ll_virtual_track_group.setVisibility(GONE);
            ll_special_work.setVisibility(GONE);
        } else if (group.getCheckedRadioButtonId() == R.id.rib_virtual_track_group) {
            newCleanTask.setTaskType(Constants.TASK_TYPE_VIRTUAL_TRACK);
            ll_trace_path.setVisibility(GONE);
            ll_virtual_track_group.setVisibility(VISIBLE);
            ll_special_work.setVisibility(GONE);
        } else if (group.getCheckedRadioButtonId() == R.id.rib_special_work) {
            newCleanTask.setTaskType(Constants.TASK_TYPE_SPE_WORK);
            ll_trace_path.setVisibility(GONE);
            ll_virtual_track_group.setVisibility(GONE);
            ll_special_work.setVisibility(VISIBLE);
        }
    }

    /**
     * 调整FrameLayout大小
     *
     * @param tp
     */
    private void resizePikcer(FrameLayout tp) {
        List<NumberPicker> npList = findNumberPicker(tp);
        for (NumberPicker np : npList) {
            resizeNumberPicker(np);
        }
    }

    /**
     * 得到viewGroup里面的numberpicker组件
     *
     * @param viewGroup
     * @return
     */
    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;
        if (null != viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker) {
                    npList.add((NumberPicker) child);
                } else if (child instanceof LinearLayout) {
                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if (result.size() > 0) {
                        return result;
                    }
                }
            }
        }
        return npList;
    }

    /*
     * 调整numberpicker大小
     */
    private void resizeNumberPicker(NumberPicker np) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(120, 320);
        params.setMargins(10, 0, 10, 0);
        np.setLayoutParams(params);
    }
}
