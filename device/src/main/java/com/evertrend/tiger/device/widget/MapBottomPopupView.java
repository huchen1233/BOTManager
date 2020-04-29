package com.evertrend.tiger.device.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.evertrend.tiger.common.utils.general.DialogUtil;
import com.evertrend.tiger.device.R;
import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.common.bean.MapPages;
import com.evertrend.tiger.device.bean.event.SaveMapPageEvent;
import com.evertrend.tiger.device.utils.TaskUtils;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MapBottomPopupView extends BottomPopupView {
    private static final String TAG = MapBottomPopupView.class.getSimpleName();

    private EditText et_map_page_name;
    private EditText et_map_page_desc;
    private Button btn_submit;

    private Context context;
    private Device device;
    private MapPages mapPages;
    private MapPages newMapPages;
    private ScheduledThreadPoolExecutor scheduledThreadSaveMapPages;

    public MapBottomPopupView(@NonNull Context context) {
        super(context);
    }

    public MapBottomPopupView(Context context, Device device) {
        super(context);
        this.context = context;
        this.device = device;
        newMapPages = new MapPages();
    }

    public MapBottomPopupView(Context context, Device device, MapPages mapPages) {
        super(context);
        this.context = context;
        this.device = device;
        this.mapPages = mapPages;
        newMapPages = new MapPages();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.yl_device_dialog_map;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
        if (mapPages != null) {
            newMapPages.setMapPages(mapPages);
            initViewData();
        }
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        exit();
        super.onDetachedFromWindow();
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext()) * .5f);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SaveMapPageEvent event) {
        dismiss();
        DialogUtil.hideProgressDialog();
    }

    private void exit() {
        stopCreateNewMapPagesTimer();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void stopCreateNewMapPagesTimer() {
        if (scheduledThreadSaveMapPages != null) {
            scheduledThreadSaveMapPages.shutdownNow();
            scheduledThreadSaveMapPages = null;
        }
    }

    private void initView() {
        et_map_page_name = findViewById(R.id.et_map_page_name);
        et_map_page_desc = findViewById(R.id.et_map_page_desc);
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String editName = et_map_page_name.getText().toString().trim();
                String editDesc = et_map_page_desc.getText().toString().trim();
                if (TextUtils.isEmpty(editName)) {
                    Toast.makeText(context, "请输入区域名称", Toast.LENGTH_SHORT).show();
                } else {
                    if (TextUtils.isEmpty(editDesc)) {
                        Toast.makeText(context, "请输入区域描述", Toast.LENGTH_SHORT).show();
                    } else {
                        newMapPages.setName(editName);
                        newMapPages.setDescription(editDesc);
                        DialogUtil.showProgressDialog(context, getResources().getString(R.string.yl_common_saving), false, false);
                        scheduledThreadSaveMapPages = new ScheduledThreadPoolExecutor(4);
                        if (mapPages == null) {
                            scheduledThreadSaveMapPages.scheduleAtFixedRate(new TaskUtils.TaskSaveMapPages(device, newMapPages,0, false),
                                    0, 5, TimeUnit.SECONDS);
                        } else {
                            scheduledThreadSaveMapPages.scheduleAtFixedRate(new TaskUtils.TaskSaveMapPages(device, newMapPages, 1, true),
                                    0, 5, TimeUnit.SECONDS);
                        }
                    }
                }
            }
        });
    }

    private void initViewData() {
        et_map_page_name.setText(newMapPages.getName());
        et_map_page_name.setSelection(newMapPages.getName().length());
        et_map_page_desc.setText(newMapPages.getDescription());
    }

}
