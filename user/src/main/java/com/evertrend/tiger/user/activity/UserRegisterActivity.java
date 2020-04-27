package com.evertrend.tiger.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.evertrend.tiger.common.utils.general.AppSharePreference;
import com.evertrend.tiger.common.utils.general.CommonConstants;
import com.evertrend.tiger.common.utils.general.DialogUtil;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.common.utils.general.Utils;
import com.evertrend.tiger.common.utils.network.CommonNetReq;
import com.evertrend.tiger.common.utils.network.OKHttpManager;
import com.evertrend.tiger.user.R;
import com.evertrend.tiger.user.utils.NetReq;

import java.util.HashMap;

public class UserRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = UserRegisterActivity.class.getSimpleName();

    private EditText et_account;
    private EditText et_password;
    private EditText et_confirm_password;
    private EditText et_verification_code;
    private Button btn_get_verification_code;
    private Button btn_register;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.yl_user_activity_register);
        initView();
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_get_verification_code) {
            getVerCode();
        } else if (v.getId() == R.id.btn_register) {
            startRefister();
        }
    }

    private void getVerCode() {
        String strAccount = et_account.getText().toString().trim();
        if (TextUtils.isEmpty(strAccount)) {
            DialogUtil.showToast(this, R.string.yl_user_account_is_empty, Toast.LENGTH_SHORT);
        }  else if (!Utils.isPhone(strAccount) && !Utils.isEmail(strAccount)) {
            DialogUtil.showToast(this, R.string.yl_user_account_format_error, Toast.LENGTH_SHORT);
        } else {
            if (Utils.isPhone(strAccount)) {
                HashMap<String, String> map = new HashMap<>();
                map.put(NetReq.MOBILE, strAccount);
                map.put(NetReq.FLAG, String.valueOf(NetReq.FLAG_SIGNUP));
                OKHttpManager.getInstance().sendComplexForm(NetReq.NET_MOBILE_VERIFICATION_CODE, map, new OKHttpManager.FuncJsonObj() {
                    @Override
                    public void onResponse(JSONObject jsonObject) throws JSONException {
                        try {
                            LogUtil.i(UserRegisterActivity.this, TAG, jsonObject.getString(CommonNetReq.RESULT_DESC));
                            switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)){
                                case CommonNetReq.CODE_SUCCESS:
                                    et_verification_code.setText(jsonObject.getString(CommonNetReq.RESULT_PIN));//临时调试用
                                    break;
                                default:
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "出错：解析数据失败");
                        }
                    }
                }, new OKHttpManager.FuncFailure() {
                    @Override
                    public void onFailure() {
                        LogUtil.e(TAG, "出错：请求网络失败");
                    }
                });
            } else if (Utils.isEmail(strAccount)) {
                HashMap<String, String> map = new HashMap<>();
                map.put(NetReq.EMAIL, strAccount);
                map.put(NetReq.FLAG, String.valueOf(NetReq.FLAG_SIGNUP));
                OKHttpManager.getInstance().sendComplexForm(NetReq.NET_EMAIL_VERIFICATION_CODE, map, new OKHttpManager.FuncJsonObj() {
                    @Override
                    public void onResponse(JSONObject jsonObject) throws JSONException {
                        try {
                            LogUtil.i(UserRegisterActivity.this, TAG, jsonObject.getString(CommonNetReq.RESULT_DESC));
                            switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)){
                                case CommonNetReq.CODE_SUCCESS:
//                                    et_verification_code.setText(jsonObject.getString(CommonNetReq.RESULT_PIN));//临时调试用
                                    break;
                                default:
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "出错：解析数据失败");
                        }
                    }
                }, new OKHttpManager.FuncFailure() {
                    @Override
                    public void onFailure() {
                        LogUtil.e(TAG, "出错：请求网络失败");
                    }
                });
            }
        }
    }

    private void startRefister() {
        String strAccount = et_account.getText().toString().trim();
        if (TextUtils.isEmpty(strAccount)) {
            DialogUtil.showToast(this, R.string.yl_user_account_is_empty, Toast.LENGTH_SHORT);
        } else if (!Utils.isPhone(strAccount) && !Utils.isEmail(strAccount)) {
            DialogUtil.showToast(this, R.string.yl_user_account_format_error, Toast.LENGTH_SHORT);
        } else {
            String strPwd = et_password.getText().toString();
            String strPwdConfirm = et_confirm_password.getText().toString();
            String strPin = et_verification_code.getText().toString();
            if (TextUtils.isEmpty(strPwd)) {
                DialogUtil.showToast(this, R.string.yl_user_password_is_empty, Toast.LENGTH_SHORT);
            } else if (!strPwd.equals(strPwdConfirm)) {
                DialogUtil.showToast(this, R.string.yl_user_password_is_different, Toast.LENGTH_SHORT);
            }  else if (TextUtils.isEmpty(strPin)) {
                DialogUtil.showToast(this, R.string.yl_user_verification_code_is_empty, Toast.LENGTH_SHORT);
            } else {
                if (Utils.isPhone(strAccount)) {
                    registerPhonePwdPin(strAccount, strPwd, strPin);
                } else if (Utils.isEmail(strAccount)) {
                    registerEmailPwdPin(strAccount, strPwd, strPin);
                }
            }
        }
    }

    private void registerEmailPwdPin(final String strAccount, String strPwd, String strPin) {
        HashMap<String, String> map = new HashMap<>();
        map.put(NetReq.EMAIL, strAccount);
        map.put(NetReq.PASSWORD, strPwd);
        map.put(NetReq.VERI_CODE, strPin);
        OKHttpManager.getInstance().sendComplexForm(NetReq.NET_SIGNUP_EMAIL, map, new OKHttpManager.FuncJsonObj() {
            @Override
            public void onResponse(JSONObject jsonObject) throws JSONException {
                try {
                    LogUtil.i(UserRegisterActivity.this, TAG, jsonObject.getString(CommonNetReq.RESULT_DESC));
                    switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                        case CommonNetReq.CODE_SUCCESS:
                            registerSuccess(jsonObject.getString(NetReq.RESULT_TOKEN), strAccount);
                            break;
                        case NetReq.CODE_FAIL_USER_EXIST:
                            DialogUtil.showToast(UserRegisterActivity.this, R.string.yl_user_user_exist, Toast.LENGTH_SHORT);
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "出错：解析数据失败");
                }
            }
        }, new OKHttpManager.FuncFailure() {
            @Override
            public void onFailure() {
                Log.e(TAG, "出错：请求网络失败");
            }
        });
    }

    private void registerPhonePwdPin(final String strAccount, String strPwd, String strPin) {
        HashMap<String, String> map = new HashMap<>();
        map.put(NetReq.MOBILE, strAccount);
        map.put(NetReq.PASSWORD, strPwd);
        map.put(NetReq.VERI_CODE, strPin);
        OKHttpManager.getInstance().sendComplexForm(NetReq.NET_SIGNUP, map, new OKHttpManager.FuncJsonObj() {
                    @Override
                    public void onResponse(JSONObject jsonObject) throws JSONException {
                        try {
                            LogUtil.i(UserRegisterActivity.this, TAG, jsonObject.getString(CommonNetReq.RESULT_DESC));
                            switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                                case CommonNetReq.CODE_SUCCESS:
                                    registerSuccess(jsonObject.getString(NetReq.RESULT_TOKEN), strAccount);
                                    break;
                                case NetReq.CODE_FAIL_USER_EXIST:
                                    DialogUtil.showToast(UserRegisterActivity.this, R.string.yl_user_user_exist, Toast.LENGTH_SHORT);
                                    break;
                                default:
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "出错：解析数据失败");
                        }
                    }
                }, new OKHttpManager.FuncFailure() {
                    @Override
                    public void onFailure() {
                        Log.e(TAG, "出错：请求网络失败");
                    }
                });
    }

    private void registerSuccess(String key, String account) {
        DialogUtil.showToast(this, R.string.yl_user_register_success, Toast.LENGTH_SHORT);
        Intent intent = new Intent(this, UserLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("account", account);
        startActivity(intent);
        finish();
    }

    private void initView() {
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);
        et_confirm_password = findViewById(R.id.et_confirm_password);
        et_verification_code = findViewById(R.id.et_verification_code);
        btn_get_verification_code = findViewById(R.id.btn_get_verification_code);
        btn_register = findViewById(R.id.btn_register);
        btn_get_verification_code.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }
}
