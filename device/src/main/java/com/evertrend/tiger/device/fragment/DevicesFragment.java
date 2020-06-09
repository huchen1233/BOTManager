package com.evertrend.tiger.device.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.evertrend.tiger.common.bean.event.ProgressStopEvent;
import com.evertrend.tiger.common.bean.event.SuccessEvent;
import com.evertrend.tiger.common.fragment.BaseFragment;
import com.evertrend.tiger.common.utils.general.AppSharePreference;
import com.evertrend.tiger.common.utils.general.CommonConstants;
import com.evertrend.tiger.common.utils.general.DialogUtil;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.common.utils.network.CommonNetReq;
import com.evertrend.tiger.common.utils.network.OKHttpManager;
import com.evertrend.tiger.device.R;
import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.device.activity.DevicesActivity;
import com.evertrend.tiger.device.adapter.DevicesAdapter;
import com.evertrend.tiger.device.bean.event.LoadDevicesEvent;
import com.evertrend.tiger.device.utils.Constants;
import com.evertrend.tiger.device.utils.NetReq;
import com.evertrend.tiger.device.utils.TaskUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class DevicesFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = DevicesFragment.class.getSimpleName();

    private SwipeRefreshLayout srl;
    private RecyclerView rlv_devices;
    private ImageButton ibtn_add_device;
//    private TextView tv_login_first;

    private List<Device> deviceList;
    private DevicesAdapter devicesAdapter;
    private android.app.AlertDialog TipsDialog;

    private ScheduledThreadPoolExecutor scheduledThreadGetAssociatedDevice;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.i(getContext(), TAG, "===onCreateView===");
        View root = inflater.inflate(R.layout.yl_device_fragment_devices, container, false);
        initView(root);
        EventBus.getDefault().register(this);
        deviceList = new ArrayList<>();
        initDeviceShow();
        if (AppSharePreference.getAppSharedPreference().loadIsLogin()) {
            DialogUtil.showProgressDialog(getActivity(), getResources().getString(R.string.yl_common_loading_devices), false, true);
            loadDevice();
        } else {
//            tv_login_first.setVisibility(View.VISIBLE);
            ibtn_add_device.setVisibility(View.GONE);
            rlv_devices.setVisibility(View.GONE);
        }
        /*tv_login_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d(TAG, "send");
                Intent intent = new Intent(getActivity(), DevicesActivity.class);
                intent.putExtra(CommonConstants.TYPE_EXTRA_NAME, "18930572454");
                intent.putExtra(CommonConstants.TYPE_EXTRA_PASS, "88888888");
                startActivity(intent);
            }
        });*/
        return root;
    }

    @Override
    public void onDestroy() {
        exit();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
//            case Constants.LOGIN_REQUEST_CODE:
//                if (MyApplication.getAppSP().loadIsLogin()) {
//                    loadDevice();
//                } else {
//                    finish();
//                }
//                break;
            case Constants.SCANNER_QR_CODE_REQUEST_CODE:
                showScannerResult(data);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ibtn_add_device) {
             LogUtil.i(getContext(), TAG, "btn_add_device_by_number");
            showInputNumberDialog();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ProgressStopEvent event) {
         LogUtil.i(getContext(), TAG, "===ProgressStopEvent===");
        stopGetAssociatedDeviceTimer();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoadDevicesEvent event) {
         LogUtil.i(getContext(), TAG, "===LoadDevicesEvent===");
        stopGetAssociatedDeviceTimer();
        DialogUtil.hideProgressDialog();
        deviceList.clear();
        deviceList.addAll(event.getDeviceList());
        if (deviceList.size() > 0) {
            devicesAdapter.notifyDataSetChanged();
        }
        srl.setRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SuccessEvent event) {
         LogUtil.i(getContext(), TAG, "===SuccessEvent===");
        if (event.getType() == CommonConstants.TYPE_SUCCESS_EVENT_LOGIN) {
            DialogUtil.showProgressDialog(getActivity(), getResources().getString(R.string.yl_common_loading_devices), false, true);
            loadDevice();
//            tv_login_first.setVisibility(View.GONE);
            ibtn_add_device.setVisibility(View.VISIBLE);
            rlv_devices.setVisibility(View.VISIBLE);
        }
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.VIBRATE})
    void mulPermission() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, Constants.SCANNER_QR_CODE_REQUEST_CODE);
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.VIBRATE})
    void showRationaleForMulPermission(final PermissionRequest request) {
        new AlertDialog.Builder(getActivity())
                .setMessage(R.string.yl_device_permission_camera_and_vibrate_rationale)
                .setPositiveButton(android.R.string.yes, (dialog, button) -> request.proceed())
                .setNegativeButton(android.R.string.no, (dialog, button) -> request.cancel())
                .show();
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        Toast.makeText(getActivity(), R.string.yl_device_permission_camera_denied, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        Toast.makeText(getActivity(), R.string.yl_device_permission_camera_neverask, Toast.LENGTH_SHORT).show();
    }

    @OnPermissionDenied(Manifest.permission.VIBRATE)
    void showDeniedForVibrate() {
        Toast.makeText(getActivity(), R.string.yl_device_permission_vibrate_denied, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.VIBRATE)
    void showNeverAskForVibrate() {
        Toast.makeText(getActivity(), R.string.yl_device_permission_vibrate_neverask, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        DevicesFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private void showScannerResult(Intent data) {
        //处理扫描结果（在界面上显示）
        if (null != data) {
            Bundle bundle = data.getExtras();
            if (bundle == null) {
                return;
            }
            if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                String result = bundle.getString(CodeUtils.RESULT_STRING);
                 LogUtil.i(getContext(), TAG, "reg code: " + result);
                if (!TextUtils.isEmpty(result)) registerDevice(result);
            } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                Toast.makeText(getActivity(), "解析二维码失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void loadDevice() {
        scheduledThreadGetAssociatedDevice = new ScheduledThreadPoolExecutor(4);
        scheduledThreadGetAssociatedDevice.scheduleAtFixedRate(new TaskUtils.TaskGetAssocitedDevice(),
                0, 6, TimeUnit.SECONDS);
    }

    private void exit() {
        stopGetAssociatedDeviceTimer();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void stopGetAssociatedDeviceTimer() {
        if (scheduledThreadGetAssociatedDevice != null) {
            scheduledThreadGetAssociatedDevice.shutdownNow();
            scheduledThreadGetAssociatedDevice = null;
        }
    }

    private void initDeviceShow() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rlv_devices.setLayoutManager(layoutManager);
        //添加Android自带的分割线
//        rlv_devices.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        devicesAdapter = new DevicesAdapter(getActivity(), deviceList);
        rlv_devices.setAdapter(devicesAdapter);
    }

    private void initView(View root) {
        srl = root.findViewById(R.id.srl);
        rlv_devices = root.findViewById(R.id.rlv_devices);
        ibtn_add_device = root.findViewById(R.id.ibtn_add_device);
//        tv_login_first = root.findViewById(R.id.tv_login_first);
        ibtn_add_device.setOnClickListener(this);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadDevice();
                    }
                }).start();
            }
        });
    }

    private void showInputNumberDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.yl_device_dialog_edittext_layout, null);
        final EditText etInput = view.findViewById(R.id.et_input);
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.yl_device_title_register_device)
                .setMessage(R.string.yl_device_message_input_register_number)
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String registerCode = etInput.getText().toString().trim();
                         LogUtil.i(getContext(), TAG, "etInput = " + registerCode);
                        if (TextUtils.isEmpty(registerCode)) {
                            Toast.makeText(getActivity(), R.string.yl_device_str_input_empty, Toast.LENGTH_SHORT).show();
                        } else {
                            registerDevice(registerCode);
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setNeutralButton(R.string.yl_device_scan_QR_code_registration, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         LogUtil.i(getContext(), TAG, "btn_add_device_by_QR");
                        DevicesFragmentPermissionsDispatcher.mulPermissionWithPermissionCheck(DevicesFragment.this);
//                        Intent intent = new Intent(getActivity(), CaptureActivity.class);
//                        startActivityForResult(intent, Constants.SCANNER_QR_CODE_REQUEST_CODE);
                    }
                })
                .create()
                .show();
    }

    private void registerDevice(String registerCode) {
        DialogUtil.showProgressDialog(getActivity(), getResources().getString(R.string.yl_device_registering), false, false);
         LogUtil.i(getContext(), TAG, "registerDevice : "+registerCode);
        HashMap<String, String> map = new HashMap<>();
        map.put(CommonNetReq.TOKEN, AppSharePreference.getAppSharedPreference().loadUserToken());
        map.put(NetReq.REG_CODE, registerCode);
        OKHttpManager.getInstance().sendComplexForm(NetReq.NET_REGISTER_DEVICE, map, new OKHttpManager.FuncJsonObj() {
                    @Override
                    public void onResponse(JSONObject jsonObject) throws JSONException {
                        DialogUtil.hideProgressDialog();
                        switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                            case CommonNetReq.CODE_SUCCESS:
//                                registerDeviceSuccess(jsonObject.getJSONObject(NetReq.RESULT_DATA));
                                Toast.makeText(getActivity(), "register success", Toast.LENGTH_LONG).show();
                                break;
                            case NetReq.ERR_CODE_NOT_FOUND_DEVICE:
                                showTipsDialog(getActivity(), NetReq.ERR_CODE_NOT_FOUND_DEVICE);
                                break;
                            case NetReq.ERR_CODE_DEVICE_HAS_BEEN_REGISTERED:
                                showTipsDialog(getActivity(), NetReq.ERR_CODE_DEVICE_HAS_BEEN_REGISTERED);
                                break;
                            case NetReq.ERR_CODE_REGISTER_DEVICE_FAIL:
                                showTipsDialog(getActivity(), NetReq.ERR_CODE_REGISTER_DEVICE_FAIL);
                                break;
                            default:
                                 LogUtil.i(getContext(), TAG, jsonObject.getString(CommonNetReq.RESULT_DESC));
                                Toast.makeText(getActivity(), jsonObject.getString(CommonNetReq.RESULT_DESC), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }, new OKHttpManager.FuncFailure() {
                    @Override
                    public void onFailure() {
                         LogUtil.i(getContext(), TAG, "FuncFailure");
                    }
                });
    }

    public void showTipsDialog(Context context, int mark) {
        int message;
        switch (mark) {
            case NetReq.ERR_CODE_NOT_FOUND_DEVICE:
                message = com.evertrend.tiger.common.R.string.yl_common_not_found_device;
                break;
            case NetReq.ERR_CODE_DEVICE_HAS_BEEN_REGISTERED:
                message = com.evertrend.tiger.common.R.string.yl_common_device_has_been_registered;
                break;
            case NetReq.ERR_CODE_REGISTER_DEVICE_FAIL:
                message = com.evertrend.tiger.common.R.string.yl_common_register_device_fail;
                break;
            default:
                message = com.evertrend.tiger.common.R.string.yl_common_register_device_fail;
        }
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TipsDialog.dismiss();
                    }
                });
        TipsDialog = builder.create();
        TipsDialog.show();
    }
}
