package com.evertrend.tiger.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.evertrend.tiger.common.utils.general.AppSharePreference;
import com.evertrend.tiger.common.utils.general.DialogUtil;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.common.utils.general.Utils;
import com.evertrend.tiger.common.utils.network.CommonNetReq;
import com.evertrend.tiger.user.utils.NetReq;
import com.evertrend.tiger.common.utils.network.OKHttpManager;
import com.evertrend.tiger.user.R;
import com.evertrend.tiger.user.bean.User;
import com.evertrend.tiger.user.bean.event.LoginSuccessEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

public class UserLoginActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private static final String TAG = UserLoginActivity.class.getSimpleName();

    private EditText et_account;
    private EditText et_password;
    private AppCompatCheckBox cb_remember_me;
    private AppCompatCheckBox cb_remember_pass;
    private Button btn_login;
    private TextView tv_no_account;
    private TextView tv_forget_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.yl_user_activity_login);
        initView();
        String account = getIntent().getStringExtra("account");
        if (!TextUtils.isEmpty(account)) {
            et_account.setText(account);
            et_password.setFocusable(true);
            et_password.setFocusableInTouchMode(true);
            et_password.requestFocus();
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        initAccountAndPass();
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (buttonView.getId() == R.id.btn_login) {

            } else if (buttonView.getId() == R.id.tv_no_account) {

            }
        } else {
            if (buttonView.getId() == R.id.btn_login) {

            } else if (buttonView.getId() == R.id.tv_no_account) {

            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            startLogin();
        } else if (v.getId() == R.id.tv_no_account) {
            Intent intent = new Intent(this, UserRegisterActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.tv_forget_password) {
            Intent intent = new Intent(this, UserPINLoginActivity.class);
            String strAccount = et_account.getText().toString().trim();
            if (!TextUtils.isEmpty(strAccount)) {
                intent.putExtra("account", strAccount);
            }
            startActivity(intent);
            finish();
        }
    }

    private void startLogin() {
        String strAccount = et_account.getText().toString().trim();
        if (TextUtils.isEmpty(strAccount)) {
            DialogUtil.showToast(this, R.string.yl_user_account_is_empty, Toast.LENGTH_SHORT);
        } else if (!Utils.isPhone(strAccount) && !Utils.isEmail(strAccount)) {
            DialogUtil.showToast(this, R.string.yl_user_account_format_error, Toast.LENGTH_SHORT);
        } else {
            String strPwd = et_password.getText().toString();
            if (TextUtils.isEmpty(strPwd)) {
                DialogUtil.showToast(this, R.string.yl_user_password_is_empty, Toast.LENGTH_SHORT);
            } else {
                if (Utils.isPhone(strAccount)) {
                    loginPhonePwd(strAccount, strPwd);
                } else if (Utils.isEmail(strAccount)) {
                    loginEmailPwd(strAccount, strPwd);
                }
            }
        }
    }

    private void loginEmailPwd(final String strAccount, String strPwd) {
        saveAccountAndPass(strAccount, strPwd);
        HashMap<String, String> map = new HashMap<>();
        map.put(NetReq.EMAIL, strAccount);
        map.put(NetReq.PASSWORD, strPwd);
        OKHttpManager.getInstance().sendComplexForm(NetReq.NET_LOGIN_PASS_EMAIL, map, new OKHttpManager.FuncJsonObj() {
                    @Override
                    public void onResponse(JSONObject jsonObject) throws JSONException {
                        try {
                            LogUtil.i(UserLoginActivity.this, TAG, jsonObject.getString(CommonNetReq.RESULT_DESC));
                            switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                                case CommonNetReq.CODE_SUCCESS:
                                    loginSuccess(jsonObject.getString(NetReq.RESULT_TOKEN), strAccount);
                                    break;
                                case NetReq.CODE_FAIL_USER_NOT_EXIST:
                                    DialogUtil.showToast(UserLoginActivity.this, R.string.yl_user_account_not_exist, Toast.LENGTH_SHORT);
                                    break;
                                default:
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtil.e(TAG, "出错：解析数据失败");
                        }
                    }
                }, new OKHttpManager.FuncFailure() {
                    @Override
                    public void onFailure() {
                        LogUtil.e(TAG, "出错：请求网络失败");
                    }
                });
    }

    private void loginPhonePwd(final String strAccount, String strPwd) {
        saveAccountAndPass(strAccount, strPwd);
        HashMap<String, String> map = new HashMap<>();
        map.put(NetReq.MOBILE, strAccount);
        map.put(NetReq.PASSWORD, strPwd);
        OKHttpManager.getInstance().sendComplexForm(NetReq.NET_LOGIN_PASS, map, new OKHttpManager.FuncJsonObj() {
                    @Override
                    public void onResponse(JSONObject jsonObject) throws JSONException {
                        try {
                            LogUtil.i(UserLoginActivity.this, TAG, jsonObject.getString(CommonNetReq.RESULT_DESC));
                            switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                                case CommonNetReq.CODE_SUCCESS:
                                    loginSuccess(jsonObject.getString(NetReq.RESULT_TOKEN), strAccount);
                                    break;
                                case NetReq.CODE_FAIL_USER_NOT_EXIST:
                                    DialogUtil.showToast(UserLoginActivity.this, R.string.yl_user_account_not_exist, Toast.LENGTH_SHORT);
                                    break;
                                default:
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtil.e(TAG, "出错：解析数据失败");
                        }
                    }
                }, new OKHttpManager.FuncFailure() {
                    @Override
                    public void onFailure() {
                        LogUtil.e(TAG, "出错：请求网络失败");
                    }
                });
    }

    private void loginSuccess(String strToken, String account) {
        AppSharePreference.getAppSharedPreference().saveIsLogin(true);
        AppSharePreference.getAppSharedPreference().saveUserToken(strToken);
        User user = new User();
        user.setName(account);
        user.setKey(strToken);
        user.save();
        EventBus.getDefault().postSticky(new LoginSuccessEvent(user));
        finish();
    }

    private void saveAccountAndPass(String strAccount, String strPwd) {
        LogUtil.i(this, TAG, "save account and pass");
        if (cb_remember_me.isChecked()) {
            AppSharePreference.getAppSharedPreference().saveRememberMe(true);
            if (!TextUtils.isEmpty(strAccount)) {
                AppSharePreference.getAppSharedPreference().saveAccount(strAccount);
            }
        } else {
            AppSharePreference.getAppSharedPreference().saveRememberMe(false);
        }
        if (cb_remember_pass.isChecked()) {
            AppSharePreference.getAppSharedPreference().saveRememberPass(true);
            if (!TextUtils.isEmpty(strPwd)) {
                AppSharePreference.getAppSharedPreference().savePass(strPwd);
            }
        } else {
            AppSharePreference.getAppSharedPreference().saveRememberPass(false);
        }
    }

    private void initView() {
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);
        cb_remember_me = findViewById(R.id.cb_remember_me);
        cb_remember_pass = findViewById(R.id.cb_remember_pass);
        btn_login = findViewById(R.id.btn_login);
        tv_no_account = findViewById(R.id.tv_no_account);
        tv_forget_password = findViewById(R.id.tv_forget_password);
        cb_remember_me.setOnCheckedChangeListener(this);
        cb_remember_pass.setOnCheckedChangeListener(this);
        btn_login.setOnClickListener(this);
        tv_no_account.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);
    }

    private void initAccountAndPass() {
        LogUtil.i(this, TAG, "init account and pass");
        if (AppSharePreference.getAppSharedPreference().loadRememberMe()) {
            cb_remember_me.setChecked(true);
            String account = AppSharePreference.getAppSharedPreference().loadaccount();
            et_account.setText(account);
            et_account.setSelection(account.length());
        }
        if (AppSharePreference.getAppSharedPreference().loadRememberPass()) {
            cb_remember_pass.setChecked(true);
            et_password.setText(AppSharePreference.getAppSharedPreference().loadPass());
        }
    }
}
