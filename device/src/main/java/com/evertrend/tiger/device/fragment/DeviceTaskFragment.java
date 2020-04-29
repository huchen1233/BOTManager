package com.evertrend.tiger.device.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evertrend.tiger.common.bean.event.DialogChoiceEvent;
import com.evertrend.tiger.common.fragment.BaseFragment;
import com.evertrend.tiger.common.utils.general.CommonConstants;
import com.evertrend.tiger.common.utils.general.DialogUtil;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.device.R;
import com.evertrend.tiger.device.adapter.CleanTaskReclyViewAdapter;
import com.evertrend.tiger.device.adapter.MapPagesChoiceAdapter;
import com.evertrend.tiger.device.bean.CleanTask;
import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.bean.MapPages;
import com.evertrend.tiger.device.bean.event.ChoiceCleanTaskEvent;
import com.evertrend.tiger.device.bean.event.ChoiceMapPagesEvent;
import com.evertrend.tiger.device.bean.event.DeleteCleanTaskEvent;
import com.evertrend.tiger.device.bean.event.DeviceMessageEvent;
import com.evertrend.tiger.device.bean.event.GetAllCleanTasksSuccessEvent;
import com.evertrend.tiger.device.bean.event.GetAllMapPagesSuccessEvent;
import com.evertrend.tiger.device.bean.event.SaveBasicConfigEvent;
import com.evertrend.tiger.device.bean.event.SaveCleanTaskSuccessEvent;
import com.evertrend.tiger.device.bean.event.SpinnerChoiceDeviceMessageEvent;
import com.evertrend.tiger.device.utils.Constants;
import com.evertrend.tiger.device.utils.ScheduledThreadUtils;
import com.evertrend.tiger.device.utils.TaskUtils;
import com.evertrend.tiger.device.widget.BasicSettingBottomPopupView;
import com.evertrend.tiger.device.widget.CleanTaskBottomPopupView;
import com.lxj.xpopup.XPopup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DeviceTaskFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = DeviceTaskFragment.class.getSimpleName();

    private Button btn_basic_setting;
    private RecyclerView rlv_clean_task;
    private ImageView iv_create_clean_task;
    private BasicSettingBottomPopupView basicSettingsBottomPopupView;
    private CleanTaskBottomPopupView cleanTaskBottomPopupView;
    private ConstraintLayout cl_show_clean_task;

    private Device device;
    private MapPages choiceMapPages;
    private List<MapPages> mapPagesList;
    private List<CleanTask> cleanTaskList;
    private CleanTaskReclyViewAdapter cleanTaskReclyViewAdapter;
    private CleanTask cleanTask;

    private AlertDialog mapPagesChoiceDialog;

    private ScheduledThreadPoolExecutor scheduledThreadGetAllCleanTasks;
    private ScheduledThreadPoolExecutor scheduledThreadDeleteCleanTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.yl_device_fragment_device_task, container, false);
        initView(root);
        showAllCleanTask();
        EventBus.getDefault().register(this);
        return root;
    }

    @Override
    public void onDestroy() {
        exit();
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(DeviceMessageEvent messageEvent) {
        device = messageEvent.getMessage();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(SaveBasicConfigEvent messageEvent) {
        device = messageEvent.getDevice();
        Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(SpinnerChoiceDeviceMessageEvent messageEvent) {
        device = messageEvent.getDevice();
        ScheduledThreadUtils.ThreadGetAllMapPages(device);
        if (device.getDevice_type().equals(Constants.DEVICE_TYPE_EVBOT_SL)) {
            cl_show_clean_task.setVisibility(View.GONE);
        } else {
            cl_show_clean_task.setVisibility(View.VISIBLE);
            startGetAllCleanTasks(device);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(GetAllMapPagesSuccessEvent event) {
        ScheduledThreadUtils.stopGetAllMapPagesTimer();
        DialogUtil.hideProgressDialog();
        mapPagesList = event.getMapPagesList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ChoiceMapPagesEvent event) {
        mapPagesChoiceDialog.dismiss();
        choiceMapPages = event.getMapPages();
        if (Constants.TYPE_MAPPAGE_OPERATION_CREATE_TASK_CHOICE == event.getType()) {
            showCreateCleanTaskBottomDialog();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(GetAllCleanTasksSuccessEvent event) {
        stopGetAllCleanTasksTimer();
        cleanTaskList.removeAll(cleanTaskList);
        cleanTaskReclyViewAdapter.notifyItemRangeRemoved(0, cleanTaskList.size());
        cleanTaskReclyViewAdapter.notifyDataSetChanged();
        cleanTaskList.addAll(event.getCleanTaskList());
        cleanTaskReclyViewAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ChoiceCleanTaskEvent event) {
        cleanTask = event.getCleanTask();
        if (event.getMark().equals("execute")) {
//            executeCleanTask();
        } else if (event.getMark().equals("delete")) {
            String deleteConfirm = getResources().getString(R.string.yl_device_delete_task_confirm);
            DialogUtil.showChoiceDialog(getActivity(), String.format(deleteConfirm, cleanTask.getName()), CommonConstants.TYPE_SUCCESS_EVENT_DELETE_CLEANTASK);
        } else if (event.getMark().equals("edit")) {
            showEditCleanTaskBottomDialog();
        } else if (event.getMark().equals("detail")) {
            showDetailCleanTask();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DialogChoiceEvent messageEvent) {
        if (messageEvent.getType() == CommonConstants.TYPE_SUCCESS_EVENT_DELETE_CLEANTASK) {
            deleteCleanTask();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DeleteCleanTaskEvent messageEvent) {
        CleanTask cleanTask = messageEvent.getCleanTask();
        refreshCleanTaskList(cleanTask, "delete");
        stopDeleteCleanTaskTimer();
        DialogUtil.hideProgressDialog();
        Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SaveCleanTaskSuccessEvent messageEvent) {
        if (messageEvent.isUpdate()) {
            refreshCleanTaskList(messageEvent.getCleanTask(), "update");
        } else {
            refreshCleanTaskList(messageEvent.getCleanTask(), "create");
        }
    }


    private void exit() {
        ScheduledThreadUtils.stopGetAllMapPagesTimer();
        stopGetAllCleanTasksTimer();
        stopDeleteCleanTaskTimer();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void showAllCleanTask() {
        cleanTaskList = new ArrayList<>();
        rlv_clean_task.setHasFixedSize(true);
        rlv_clean_task.setItemAnimator(new DefaultItemAnimator());
        rlv_clean_task.setLayoutManager(new LinearLayoutManager(getContext()));
        //添加Android自带的分割线
//        rlv_clean_task.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        cleanTaskReclyViewAdapter = new CleanTaskReclyViewAdapter(getContext(), cleanTaskList);
        rlv_clean_task.setAdapter(cleanTaskReclyViewAdapter);
    }

    private void startGetAllCleanTasks(Device mDevice) {
        DialogUtil.showProgressDialog(getContext(), getResources().getString(R.string.yl_device_get_clean_task), false, true);
        if (scheduledThreadGetAllCleanTasks == null) scheduledThreadGetAllCleanTasks = new ScheduledThreadPoolExecutor(4);
        scheduledThreadGetAllCleanTasks.scheduleAtFixedRate(new TaskUtils.TaskGetAllCleanTasks(mDevice),
                0, 10, TimeUnit.SECONDS);
    }

    private void stopGetAllCleanTasksTimer() {
        if (scheduledThreadGetAllCleanTasks != null) {
            scheduledThreadGetAllCleanTasks.shutdownNow();
            scheduledThreadGetAllCleanTasks = null;
        }
    }

    private void stopDeleteCleanTaskTimer() {
        if (scheduledThreadDeleteCleanTask != null) {
            scheduledThreadDeleteCleanTask.shutdownNow();
            scheduledThreadDeleteCleanTask = null;
        }
    }

    private void initView(View root) {
        btn_basic_setting = root.findViewById(R.id.btn_basic_setting);
        rlv_clean_task = root.findViewById(R.id.rlv_clean_task);
        iv_create_clean_task = root.findViewById(R.id.iv_create_clean_task);
        cl_show_clean_task = root.findViewById(R.id.cl_show_clean_task);
        btn_basic_setting.setOnClickListener(this);
        iv_create_clean_task.setOnClickListener(this);
    }

    private void refreshCleanTaskList(CleanTask cleanTask, String operation) {
        if (operation.equals("create")) {
            cleanTaskList.add(cleanTask);
            cleanTaskReclyViewAdapter.notifyDataSetChanged();
//            cleanTaskReclyViewAdapter.notifyItemInserted(cleanTaskList.size()-1);
//            rlv_clean_task.smoothScrollToPosition(cleanTaskList.size()-1);
        } else {
            for (int i = 0; i < cleanTaskList.size(); i++) {
                if (cleanTask.getId() == cleanTaskList.get(i).getId()) {
                    if (operation.equals("delete")) {
                        cleanTaskList.remove(i);
                        cleanTaskReclyViewAdapter.notifyItemRemoved(i);
                    } else if (operation.equals("update")) {
                        cleanTaskList.set(i, cleanTask);
                        cleanTaskReclyViewAdapter.notifyItemChanged(i);
                    }
                }
            }
        }
    }

    private void showDetailCleanTask() {
        LogUtil.i(getActivity(), TAG, "showDetailCleanTask : "+cleanTask.getName());
        String[] taskDetailInfo = getResources().getStringArray(R.array.yl_device_clean_task_detail_info);
        taskDetailInfo[0] = taskDetailInfo[0] + cleanTask.getName();
        taskDetailInfo[1] = taskDetailInfo[1] + cleanTask.getDesc();
        if (cleanTask.getTaskOption() == Constants.TYPE_EXEC_FLAG_ONLY_ONCE) {
            taskDetailInfo[2] = taskDetailInfo[2] + getResources().getString(R.string.yl_device_only_once);
        } else if (cleanTask.getTaskOption() == Constants.TYPE_EXEC_FLAG_EVERYDAY) {
            taskDetailInfo[2] = taskDetailInfo[2] + getResources().getString(R.string.yl_device_everyday);
        } else if (cleanTask.getTaskOption() == Constants.TYPE_EXEC_FLAG_INFINITE_CYCLE) {
            taskDetailInfo[2] = taskDetailInfo[2] + getResources().getString(R.string.yl_device_infinite_cycle);
        }
        if (cleanTask.getId()%2 == 0) {
            taskDetailInfo[3] = taskDetailInfo[3] + getResources().getString(R.string.yl_device_in_progress);
            taskDetailInfo[4] = taskDetailInfo[4] + "80%";
        } else {
            taskDetailInfo[3] = taskDetailInfo[3] + getResources().getString(R.string.yl_device_not_started_yet);
            taskDetailInfo[4] = taskDetailInfo[4] + "0%";
        }
        taskDetailInfo[5] = taskDetailInfo[5] + cleanTask.getStartTime();
        new XPopup.Builder(getActivity())
                .asCenterList(getResources().getString(R.string.yl_device_task_detail_info),taskDetailInfo, null)
                .show();
    }

    private void deleteCleanTask() {
        DialogUtil.showProgressDialog(getActivity(), getResources().getString(R.string.yl_common_deleting), false, false);
        scheduledThreadDeleteCleanTask = new ScheduledThreadPoolExecutor(4);
        scheduledThreadDeleteCleanTask.scheduleAtFixedRate(new TaskUtils.TaskDeleteCleanTask(device, cleanTask),
                0, 5, TimeUnit.SECONDS);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_basic_setting) {
            showDeviceBasicSettingBottomDialog();
        } else if (v.getId() == R.id.iv_create_clean_task) {
            if (mapPagesList.size() > 0) {
                showMapPagesChoice(mapPagesList, Constants.TYPE_MAPPAGE_OPERATION_CREATE_TASK_CHOICE);
            } else {
                Toast.makeText(getActivity(), "请先创建地图", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showMapPagesChoice(List<MapPages> list, int type) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.yl_common_dialog_list_choice, null);
        RecyclerView recyclerView = view.findViewById(R.id.rl_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        //添加Android自带的分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new MapPagesChoiceAdapter(getActivity(), list, type));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.yl_device_please_select_a_map);
        builder.setView(view);
        mapPagesChoiceDialog = builder.create();
        mapPagesChoiceDialog.show();
    }

    private void showEditCleanTaskBottomDialog() {
        for (MapPages mapPages : mapPagesList) {
            if (mapPages.getId() == cleanTask.getMapPage()) {
                choiceMapPages = mapPages;
            }
        }
        cleanTaskBottomPopupView = new CleanTaskBottomPopupView(getActivity(), device, choiceMapPages, cleanTask);
        new XPopup.Builder(getActivity())
                .asCustom(cleanTaskBottomPopupView)
                .show();
    }

    private void showCreateCleanTaskBottomDialog() {
        cleanTaskBottomPopupView = new CleanTaskBottomPopupView(getActivity(), device, choiceMapPages);
        new XPopup.Builder(getActivity())
                .autoOpenSoftInput(true)
                .asCustom(cleanTaskBottomPopupView)
                .show();
    }

    private void showDeviceBasicSettingBottomDialog() {
        basicSettingsBottomPopupView = new BasicSettingBottomPopupView(getActivity(), device);
        new XPopup.Builder(getActivity())
                .autoOpenSoftInput(true)
                .asCustom(basicSettingsBottomPopupView)
                .show();
    }

}
