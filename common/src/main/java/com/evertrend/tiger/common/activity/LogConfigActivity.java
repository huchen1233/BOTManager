package com.evertrend.tiger.common.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.evertrend.tiger.common.R;
import com.evertrend.tiger.common.utils.general.AppSharePreference;

public class LogConfigActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = LogConfigActivity.class.getSimpleName();

    private Switch swt_log_info;
    private EditText et_log_flag;
    private Button btn_log_flag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yl_common_activity_log_config);
        initView();
    }

    private void initView() {
        et_log_flag = findViewById(R.id.et_log_flag);
        btn_log_flag = findViewById(R.id.btn_log_flag);
        swt_log_info = findViewById(R.id.swt_log_info);
        btn_log_flag.setOnClickListener(this);
        swt_log_info.setOnCheckedChangeListener(this);
        swt_log_info.setChecked(AppSharePreference.getAppSharedPreference().loadIsInputLogI());
        et_log_flag.setText(AppSharePreference.getAppSharedPreference().loadLogFlag());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_log_flag) {
            AppSharePreference.getAppSharedPreference().saveLogFlag(et_log_flag.getText().toString().trim());
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.swt_log_info) {
            AppSharePreference.getAppSharedPreference().saveIsInputLogI(isChecked);
        }
    }
}
