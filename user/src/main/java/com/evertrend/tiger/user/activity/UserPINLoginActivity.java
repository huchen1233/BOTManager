package com.evertrend.tiger.user.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
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
import com.evertrend.tiger.user.bean.User;
import com.evertrend.tiger.user.bean.event.LoginSuccessEvent;
import com.evertrend.tiger.user.utils.NetReq;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

public class UserPINLoginActivity extends UserBaseActivity implements View.OnClickListener {
    private static final String TAG = UserPINLoginActivity.class.getSimpleName();

    private EditText et_account;
    private EditText et_verification_code;
    private Button btn_get_verification_code;
    private Button btn_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.yl_user_activity_pin_login);
        initView();
        String account = getIntent().getStringExtra("account");
        if (!TextUtils.isEmpty(account)) {
            et_account.setText(account);
            et_account.setSelection(account.length());
        }
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
        } else if (v.getId() == R.id.btn_login) {
            startLogin();
        }
    }

    private void getVerCode() {
        String strAccount = et_account.getText().toString().trim();
        if (TextUtils.isEmpty(strAccount)) {
            DialogUtil.showToast(this, R.string.yl_user_account_is_empty, Toast.LENGTH_SHORT);
        } else {
            if (Utils.isPhone(strAccount)) {
                startGetCodeCount();
                HashMap<String, String> map = new HashMap<>();
                map.put(NetReq.MOBILE, strAccount);
                map.put(NetReq.FLAG, String.valueOf(NetReq.FLAG_LOGIN));
                OKHttpManager.getInstance().sendComplexForm(NetReq.NET_MOBILE_VERIFICATION_CODE, map, new OKHttpManager.FuncJsonObj() {
                    @Override
                    public void onResponse(JSONObject jsonObject) throws JSONException {
                        try {
                            LogUtil.i(UserPINLoginActivity.this, TAG, jsonObject.getString(CommonNetReq.RESULT_DESC));
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
                startGetCodeCount();
                HashMap<String, String> map = new HashMap<>();
                map.put(NetReq.EMAIL, strAccount);
                map.put(NetReq.FLAG, String.valueOf(NetReq.FLAG_LOGIN));
                OKHttpManager.getInstance().sendComplexForm(NetReq.NET_EMAIL_VERIFICATION_CODE, map, new OKHttpManager.FuncJsonObj() {
                    @Override
                    public void onResponse(JSONObject jsonObject) throws JSONException {
                        try {
                            LogUtil.i(UserPINLoginActivity.this, TAG, jsonObject.getString(CommonNetReq.RESULT_DESC));
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
            } else {
                DialogUtil.showToast(this, R.string.yl_user_account_format_error, Toast.LENGTH_SHORT);
            }
        }
    }

    private static final int UPDATE_TEXT = 1;
    private static final int UPDATE_CLICK_ABLE = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT:
                    btn_get_verification_code.setText(String.format(getResources().getString(R.string.yl_user_get_code_count), msg.arg1));
                    btn_get_verification_code.setClickable(false);
                    break;
                case UPDATE_CLICK_ABLE:
                    btn_get_verification_code.setText(R.string.yl_user_get_verification_code);
                    btn_get_verification_code.setClickable(true);
                    break;
            }
        }
    };
    private void startGetCodeCount() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = CommonConstants.GET_CODE_COUNT; i >= 0; i--) {
                    Message message = new Message();
                    if (i == 0) {
                        message.what = UPDATE_CLICK_ABLE;
                        handler.sendMessage(message);
                        break;
                    } else {
                        message.what = UPDATE_TEXT;
                        message.arg1 = i;
                        handler.sendMessage(message);
                    }
                    SystemClock.sleep(1000);
                }
            }
        }).start();
    }

    private void startLogin() {
        String strAccount = et_account.getText().toString().trim();
        if (TextUtils.isEmpty(strAccount)) {
            DialogUtil.showToast(this, R.string.yl_user_account_is_empty, Toast.LENGTH_SHORT);
        } else {
            String strPin = et_verification_code.getText().toString();
            if (TextUtils.isEmpty(strPin)) {
                DialogUtil.showToast(this, R.string.yl_user_verification_code_is_empty, Toast.LENGTH_SHORT);
            } else {
                if (Utils.isPhone(strAccount)) {
                    loginPhonePin(strAccount, strPin);
                }  else if (Utils.isEmail(strAccount)) {
                    loginEmailPin(strAccount, strPin);
                } else {
                    DialogUtil.showToast(this, R.string.yl_user_account_format_error, Toast.LENGTH_SHORT);
                }
            }
        }
    }

    private void loginEmailPin(final String strAccount, String strPin) {
        DialogUtil.showProgressDialog(this, getResources().getString(R.string.yl_user_logining), false, false);
        HashMap<String, String> map = new HashMap<>();
        map.put(NetReq.EMAIL, strAccount);
        map.put(NetReq.VERI_CODE, strPin);
        OKHttpManager.getInstance().sendComplexForm(NetReq.NET_LOGIN_CODE_EMAIL, map, new OKHttpManager.FuncJsonObj() {
                    @Override
                    public void onResponse(JSONObject jsonObject) throws JSONException {
                        try {
                            LogUtil.i(UserPINLoginActivity.this, TAG, jsonObject.getString(CommonNetReq.RESULT_DESC));
                            switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                                case CommonNetReq.CODE_SUCCESS:
                                    loginSuccess(jsonObject.getString(NetReq.RESULT_TOKEN), strAccount);
                                    break;
                                case NetReq.CODE_FAIL_USER_NOT_EXIST:
                                    DialogUtil.showToast(UserPINLoginActivity.this, R.string.yl_user_account_not_exist, Toast.LENGTH_SHORT);
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

    private void loginPhonePin(final String strAccount, String strPin) {
        DialogUtil.showProgressDialog(this, getResources().getString(R.string.yl_user_logining), false, false);
        HashMap<String, String> map = new HashMap<>();
        map.put(NetReq.MOBILE, strAccount);
        map.put(NetReq.VERI_CODE, strPin);
        OKHttpManager.getInstance().sendComplexForm(NetReq.NET_LOGIN_CODE, map, new OKHttpManager.FuncJsonObj() {
                    @Override
                    public void onResponse(JSONObject jsonObject) throws JSONException {
                        try {
                            LogUtil.i(UserPINLoginActivity.this, TAG, jsonObject.getString(CommonNetReq.RESULT_DESC));
                            switch (jsonObject.getIntValue(CommonNetReq.RESULT_CODE)) {
                                case CommonNetReq.CODE_SUCCESS:
                                    loginSuccess(jsonObject.getString(NetReq.RESULT_TOKEN), strAccount);
                                    break;
                                case NetReq.CODE_FAIL_USER_NOT_EXIST:
                                    DialogUtil.showToast(UserPINLoginActivity.this, R.string.yl_user_account_not_exist, Toast.LENGTH_SHORT);
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
        DialogUtil.hideProgressDialog();
        AppSharePreference.getAppSharedPreference().saveIsLogin(true);
        AppSharePreference.getAppSharedPreference().saveUserToken(strToken);
        User user = new User();
        user.setName(account);
        user.setKey(strToken);
        user.save();
        EventBus.getDefault().postSticky(new LoginSuccessEvent(user));
        finish();
    }

    private void initView() {
        et_account = findViewById(R.id.et_account);
        et_verification_code = findViewById(R.id.et_verification_code);
        btn_get_verification_code = findViewById(R.id.btn_get_verification_code);
        btn_login = findViewById(R.id.btn_login);
        btn_get_verification_code.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }
}
