package com.evertrend.tiger.user.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evertrend.tiger.common.bean.event.DialogChoiceEvent;
import com.evertrend.tiger.common.bean.event.SuccessEvent;
import com.evertrend.tiger.common.fragment.BaseFragment;
import com.evertrend.tiger.common.utils.general.AppSharePreference;
import com.evertrend.tiger.common.utils.general.CommonConstants;
import com.evertrend.tiger.common.utils.general.DialogUtil;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.user.R;
import com.evertrend.tiger.user.activity.UserLoginActivity;
import com.evertrend.tiger.user.adapter.UserOperationItemAdapter;
import com.evertrend.tiger.user.bean.User;
import com.evertrend.tiger.user.bean.event.LoginSuccessEvent;
import com.evertrend.tiger.user.bean.event.UserOperationItemEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.List;

public class MeFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = MeFragment.class.getSimpleName();

    private LinearLayout ll_me;
    private TextView tv_name;
    private RecyclerView rlv_user_operation;
    private String[] userOperationItems;

    private User user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.i(TAG, "===onCreateView===");
        View root = inflater.inflate(R.layout.yl_user_fragment_me, container, false);
        initView(root);
        userOperationItems = getResources().getStringArray(R.array.yl_user_operation_item);
        initViewData();
        return root;
    }

    @Override
    public void onStart() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        super.onStart();
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (R.id.ll_me == v.getId()) {
            if (AppSharePreference.getAppSharedPreference().loadIsLogin()) {
                DialogUtil.showToast(getContext(), R.string.yl_user_already_logged, Toast.LENGTH_SHORT);
            } else {
                Intent intent = new Intent(getContext(), UserLoginActivity.class);
                startActivity(intent);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(UserOperationItemEvent event) {
        LogUtil.i(TAG, "===DeviceMessageEvent==="+userOperationItems[event.getPosition()]);
        switch (event.getPosition()) {
            case 2:
                if (AppSharePreference.getAppSharedPreference().loadIsLogin()) {
                    DialogUtil.showChoiceDialog(getContext(), R.string.yl_user_logout_confirm, CommonConstants.TYPE_SUCCESS_EVENT_LOGOUT);
                } else {
                    DialogUtil.showToast(getContext(), R.string.yl_user_login_first, Toast.LENGTH_SHORT);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginSuccessEvent event) {
        DialogUtil.showSuccessToast(getContext());
        LogUtil.i(TAG, "===LoginSuccessEvent===");
        tv_name.setText(event.getUser().getName());
        EventBus.getDefault().post(new SuccessEvent(CommonConstants.TYPE_SUCCESS_EVENT_LOGIN));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DialogChoiceEvent event) {
        LogUtil.i(TAG, "===DialogChoiceEvent===");
        if (event.getType() == CommonConstants.TYPE_SUCCESS_EVENT_LOGOUT) {
            AppSharePreference.getAppSharedPreference().saveIsLogin(false);
            AppSharePreference.getAppSharedPreference().saveUserToken("exit");
            AppSharePreference.getAppSharedPreference().saveRememberPass(false);
            user.delete();
            getActivity().finish();
        }
    }

    private void initView(View root) {
        ll_me = root.findViewById(R.id.ll_me);
        tv_name = root.findViewById(R.id.tv_name);
        rlv_user_operation = root.findViewById(R.id.rlv_user_operation);
        ll_me.setOnClickListener(this);
    }

    private void initViewData() {
        if (AppSharePreference.getAppSharedPreference().loadIsLogin()) {
            List<User> userList = LitePal.where("key = ?", AppSharePreference.getAppSharedPreference().loadUserToken()).find(User.class);
            if (userList.size() > 0) {
                user = userList.get(0);
                tv_name.setText(user.getName());
            }
        }
        rlv_user_operation.setHasFixedSize(true);
        rlv_user_operation.setItemAnimator(new DefaultItemAnimator());
        rlv_user_operation.setLayoutManager(new LinearLayoutManager(getContext()));
        //添加Android自带的分割线
        rlv_user_operation.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rlv_user_operation.setAdapter(new UserOperationItemAdapter(getContext(), userOperationItems));
    }
}
