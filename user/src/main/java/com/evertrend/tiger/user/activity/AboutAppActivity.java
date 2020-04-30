package com.evertrend.tiger.user.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.user.R;
import com.evertrend.tiger.user.adapter.UserOperationItemAdapter;
import com.evertrend.tiger.user.bean.OperationItem;
import com.evertrend.tiger.user.bean.event.UserOperationItemEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class AboutAppActivity extends UserBaseActivity {
    private static final String TAG = AboutAppActivity.class.getSimpleName();

    private TextView tv_version;
    private RecyclerView rlv_about_app;

    private List<OperationItem> itemList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.yl_user_activity_adout_app);
        initView();
        initViewData();
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
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UserOperationItemEvent event) {
        LogUtil.i(this, TAG, "===DeviceMessageEvent==="+itemList.get(event.getPosition()).getName());
    }

    private void initView() {
        tv_version = findViewById(R.id.tv_version);
        rlv_about_app = findViewById(R.id.rlv_about_app);
    }

    private void initViewData() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = this.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0);
            tv_version.setText(packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String[] aboutAppItems = getResources().getStringArray(R.array.yl_user_about_app_item);
        itemList = new ArrayList<>();
        int size = aboutAppItems.length;
        for (int i = 0; i < size; i++) {
            OperationItem item = new OperationItem();
            item.setName(aboutAppItems[i]);
            item.setShowCenter(false);
            itemList.add(item);
        }
        rlv_about_app.setHasFixedSize(true);
        rlv_about_app.setItemAnimator(new DefaultItemAnimator());
        rlv_about_app.setLayoutManager(new LinearLayoutManager(this));
        //添加Android自带的分割线
        rlv_about_app.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rlv_about_app.setAdapter(new UserOperationItemAdapter(this, itemList));
    }
}
