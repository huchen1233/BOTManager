package com.evertrend.tiger.user.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azhon.appupdate.config.UpdateConfiguration;
import com.azhon.appupdate.dialog.NumberProgressBar;
import com.azhon.appupdate.listener.OnDownloadListener;
import com.azhon.appupdate.manager.DownloadManager;
import com.evertrend.tiger.common.bean.event.DialogChoiceEvent;
import com.evertrend.tiger.common.bean.event.SuccessEvent;
import com.evertrend.tiger.common.fragment.BaseFragment;
import com.evertrend.tiger.common.utils.general.AppSharePreference;
import com.evertrend.tiger.common.utils.general.CommonConstants;
import com.evertrend.tiger.common.utils.general.DialogUtil;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.user.R;
import com.evertrend.tiger.user.activity.AboutAppActivity;
import com.evertrend.tiger.user.activity.UserLoginActivity;
import com.evertrend.tiger.user.adapter.UserOperationItemAdapter;
import com.evertrend.tiger.user.bean.OperationItem;
import com.evertrend.tiger.user.bean.UpdateApp;
import com.evertrend.tiger.user.bean.User;
import com.evertrend.tiger.user.bean.event.CheckUpdateEvent;
import com.evertrend.tiger.user.bean.event.LoginSuccessEvent;
import com.evertrend.tiger.user.bean.event.UserOperationItemEvent;
import com.evertrend.tiger.user.utils.UserTaskUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MeFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = MeFragment.class.getSimpleName();

    private LinearLayout ll_me;
    private TextView tv_name;
    private RecyclerView rlv_user_operation;
    private List<OperationItem> itemList;

    private Button btn_download;
    private NumberProgressBar progressBar;
    private AlertDialog downloadDialog;
    private DownloadManager manager;
    private UpdateConfiguration configuration;

    private User user;
    private ScheduledThreadPoolExecutor scheduledThreadCheckUpdateInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.i(TAG, "===onCreateView===");
        View root = inflater.inflate(R.layout.yl_user_fragment_me, container, false);
        initView(root);
        initViewData();
        return root;
    }

    @Override
    public void onStart() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        super.onStart();
    }

    @Override
    public void onStop() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        stopCheckUpdateTimer();
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        if (R.id.ll_me == v.getId()) {
            if (AppSharePreference.getAppSharedPreference().loadIsLogin()) {
                DialogUtil.showToast(getContext(), R.string.yl_user_already_logged, Toast.LENGTH_SHORT);
            } else {
                Intent intent = new Intent(getContext(), UserLoginActivity.class);
                startActivity(intent);
            }
        } else if (R.id.btn_download == v.getId()) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
            manager.download();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UserOperationItemEvent event) {
        LogUtil.i(TAG, "===DeviceMessageEvent===" + itemList.get(event.getPosition()).getName());
        if (AppSharePreference.getAppSharedPreference().loadIsLogin()) {
            switch (event.getPosition()) {
                case 1:
                    Intent intent = new Intent(getContext(), AboutAppActivity.class);
                    startActivity(intent);
                    break;
                case 2:
                    startCheckUpdate();
                    break;
                case 3:
                    DialogUtil.showChoiceDialog(getContext(), R.string.yl_user_logout_confirm, CommonConstants.TYPE_SUCCESS_EVENT_LOGOUT);
                    break;
            }
        } else {
            DialogUtil.showToast(getContext(), R.string.yl_user_login_first, Toast.LENGTH_SHORT);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(LoginSuccessEvent event) {
        DialogUtil.showSuccessToast(getContext());
        LogUtil.i(TAG, "===LoginSuccessEvent===");
        user = event.getUser();
        tv_name.setText(user.getName());
        EventBus.getDefault().post(new SuccessEvent(CommonConstants.TYPE_SUCCESS_EVENT_LOGIN));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DialogChoiceEvent event) {
        LogUtil.i(TAG, "===DialogChoiceEvent===");
        if (event.getType() == CommonConstants.TYPE_SUCCESS_EVENT_LOGOUT) {
            AppSharePreference.getAppSharedPreference().saveIsLogin(false);
            AppSharePreference.getAppSharedPreference().saveUserToken("exit");
            AppSharePreference.getAppSharedPreference().saveRememberPass(false);
            user.delete();
            getActivity().finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CheckUpdateEvent event) {
        LogUtil.i(TAG, "===CheckUpdateEvent===");
        stopCheckUpdateTimer();
        DialogUtil.hideProgressDialog();
        UpdateApp updateApp = event.getUpdateApp();
        LogUtil.i(TAG, "isUpdate:" + updateApp.getIsUpdate());
        LogUtil.i(TAG, "isUpdate:" + updateApp.getApkFileUrl());
        if (updateApp.getIsUpdate() == 1) {
            isNeedUpdate(updateApp);
        } else {
            DialogUtil.showToast(getContext(), R.string.yl_user_no_new_version, Toast.LENGTH_SHORT);
        }
    }

    private void isNeedUpdate(final UpdateApp updateApp) {
        initDownload(updateApp);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.yl_user_dialog_download_apk, null);
        TextView tv_apk_version = view.findViewById(R.id.tv_apk_version);
        TextView tv_apk_size = view.findViewById(R.id.tv_apk_size);
        TextView tv_update_log = view.findViewById(R.id.tv_update_log);
        progressBar = view.findViewById(R.id.download_progress);
        btn_download = view.findViewById(R.id.btn_download);
        tv_apk_version.append(updateApp.getNewVersion());
        tv_apk_size.append(updateApp.getTargetSize());
        tv_update_log.append(updateApp.getUpdateLog().replace("\\n", "\n"));
        btn_download.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.yl_user_new_version_can_be_update);
        builder.setView(view);
        downloadDialog = builder.create();
        downloadDialog.show();
    }

    private void initDownload(UpdateApp updateApp) {
        configuration = new UpdateConfiguration()
                //输出错误日志
                .setEnableLog(true)
                //设置自定义的下载
                //.setHttpManager()
                //下载完成自动跳动安装页面
                .setJumpInstallPage(true)
                //设置对话框背景图片 (图片规范参照demo中的示例图)
                .setDialogImage(R.drawable.ic_launcher_background)
                //设置按钮的颜色
                .setDialogButtonColor(Color.parseColor("#E743DA"))
                //设置对话框强制更新时进度条和文字的颜色
//                .setDialogProgressBarColor(Color.parseColor("#E743DA"))
                //设置按钮的文字颜色
                .setDialogButtonTextColor(Color.WHITE)
                //设置是否显示通知栏进度
                .setShowNotification(true)
                //设置是否提示后台下载toast
                .setShowBgdToast(false)
                //设置强制更新
                .setForcedUpgrade(false)
                //设置下载过程的监听
                .setOnDownloadListener(new OnDownloadListener() {
                    @Override
                    public void start() {
                        btn_download.setText(R.string.yl_user_updateing);
                    }

                    @Override
                    public void downloading(int max, int progress) {
                        int curr = (int) (progress / (double) max * 100.0);
                        progressBar.setMax(100);
                        progressBar.setProgress(curr);
                    }

                    @Override
                    public void done(File apk) {
                        LogUtil.i(TAG, "file path:" + apk.getAbsolutePath());
                        downloadDialog.dismiss();
                    }

                    @Override
                    public void cancel() {

                    }

                    @Override
                    public void error(Exception e) {

                    }
                });

        manager = DownloadManager.getInstance(getContext());
        manager.setApkName("test.apk")
                .setApkUrl(updateApp.getApkFileUrl())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setShowNewerToast(true)
                .setConfiguration(configuration)
                .setApkVersionName(updateApp.getNewVersion())
                .setApkSize(updateApp.getTargetSize())
                .setApkDescription(updateApp.getUpdateLog());
    }

    private void stopCheckUpdateTimer() {
        if (scheduledThreadCheckUpdateInfo != null) {
            scheduledThreadCheckUpdateInfo.shutdownNow();
            scheduledThreadCheckUpdateInfo = null;
        }
    }

    private void startCheckUpdate() {
        DialogUtil.showProgressDialog(getContext(), getResources().getString(R.string.yl_user_detecting_new_version), false, false);
        scheduledThreadCheckUpdateInfo = new ScheduledThreadPoolExecutor(3);
        scheduledThreadCheckUpdateInfo.scheduleAtFixedRate(new UserTaskUtils.TaskCheckUpdate(),
                0, 6, TimeUnit.SECONDS);
    }

    private void initView(View root) {
        ll_me = root.findViewById(R.id.ll_me);
        tv_name = root.findViewById(R.id.tv_name);
        rlv_user_operation = root.findViewById(R.id.rlv_user_operation);
        ll_me.setOnClickListener(this);
    }

    private void initViewData() {
        if (AppSharePreference.getAppSharedPreference().loadIsLogin()) {
            List<User> userList = LitePal.where("key = ?", AppSharePreference.getAppSharedPreference().loadUserToken()).find(User.class);
            if (userList.size() > 0) {
                user = userList.get(0);
                tv_name.setText(user.getName());
            }
        }
        String[] userOperationItems = getResources().getStringArray(R.array.yl_user_operation_item);
        itemList = new ArrayList<>();
        int size = userOperationItems.length;
        for (int i = 0; i < size; i++) {
            OperationItem item = new OperationItem();
            item.setName(userOperationItems[i]);
            item.setImageId(getResources().obtainTypedArray(R.array.yl_user_operation_item_image).getResourceId(i, 0));
            if (i == size - 1) {
                item.setShowCenter(true);
            } else {
                item.setShowCenter(false);
            }
            itemList.add(item);
        }
        rlv_user_operation.setHasFixedSize(true);
        rlv_user_operation.setItemAnimator(new DefaultItemAnimator());
        rlv_user_operation.setLayoutManager(new LinearLayoutManager(getContext()));
        //添加Android自带的分割线
        rlv_user_operation.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rlv_user_operation.setAdapter(new UserOperationItemAdapter(getContext(), itemList));
    }
}
