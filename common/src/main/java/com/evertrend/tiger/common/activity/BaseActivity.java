package com.evertrend.tiger.common.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.evertrend.tiger.common.utils.SlamwareAgent;

public class BaseActivity extends AppCompatActivity {
    public static final String TAG = BaseActivity.class.getCanonicalName();

    private static SlamwareAgent mSlamwareAgent;

    static {
        mSlamwareAgent = new SlamwareAgent();
    }

    public SlamwareAgent getSlamwareAgent() {
        return mSlamwareAgent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
