package com.evertrend.tiger.common.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.evertrend.tiger.common.R;
import com.evertrend.tiger.common.bean.Device;

public class TestActivity extends AppCompatActivity {

    private EditText et_ip;
    private Button btn_connect;

    private Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yl_common_activity_test);
        device = (Device) getIntent().getSerializableExtra("device");
        et_ip = findViewById(R.id.et_ip);
        btn_connect = findViewById(R.id.btn_connect);
        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ip = et_ip.getText().toString().intern();
                if (TextUtils.isEmpty(ip)) {
                    Toast.makeText(TestActivity.this, "请输入IP", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("ip", ip);
                    intent.putExtra("device", device);
                    intent.setAction("android.intent.action.MapActivity");
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}