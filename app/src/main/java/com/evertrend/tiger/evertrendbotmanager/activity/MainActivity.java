package com.evertrend.tiger.evertrendbotmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.device.activity.DeviceShowActivity;
import com.evertrend.tiger.evertrendbotmanager.R;
import com.evertrend.tiger.user.activity.UserLoginActivity;
import com.evertrend.tiger.user.activity.UserRegisterActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Button btn_user_register;
    private Button btn_user_login;
    private Button btn_show_device;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtil.i(TAG, "===onCreate===");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void onStart() {
        LogUtil.i(TAG, "===onStart===");
        super.onStart();
    }

    @Override
    protected void onStop() {
        LogUtil.i(TAG, "===onStop===");
        super.onStop();
    }

    private void initView() {
        btn_user_register = findViewById(R.id.btn_user_register);
        btn_user_login = findViewById(R.id.btn_user_login);
        btn_show_device = findViewById(R.id.btn_show_device);
        btn_user_register.setOnClickListener(this);
        btn_user_login.setOnClickListener(this);
        btn_show_device.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_user_register:
                openActivity(this, new UserRegisterActivity());
                break;
            case R.id.btn_user_login:
                openActivity(this, new UserLoginActivity());
                break;
            case R.id.btn_show_device:
                openActivity(this, new DeviceShowActivity());
                break;
        }
    }

    private void openActivity(MainActivity mainActivity, AppCompatActivity activity) {
        Intent intent = new Intent(mainActivity, activity.getClass());
        startActivity(intent);
    }
}
