package com.evertrend.tiger.common.activity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.evertrend.tiger.common.utils.EvertrendAgent;
import com.evertrend.tiger.common.utils.SlamwareAgent;

public class BaseActivity extends AppCompatActivity {
    public static final String TAG = BaseActivity.class.getCanonicalName();

    private static SlamwareAgent mSlamwareAgent;
    private static EvertrendAgent mEvertrendAgent;

    static {
        mSlamwareAgent = new SlamwareAgent();
        mEvertrendAgent = new EvertrendAgent();
    }

    public SlamwareAgent getSlamwareAgent() {
        return mSlamwareAgent;
    }

    public EvertrendAgent getEvertrendAgent() {
        return mEvertrendAgent;
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
