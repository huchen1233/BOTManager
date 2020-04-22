package com.evertrend.tiger.user.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evertrend.tiger.common.fragment.BaseFragment;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.user.R;
import com.evertrend.tiger.user.adapter.UserOperationItemAdapter;
import com.evertrend.tiger.user.bean.event.UserOperationItemEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MeFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = MeFragment.class.getSimpleName();

    private LinearLayout ll_me;
    private RecyclerView rlv_user_operation;
    private String[] userOperationItems;

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
        EventBus.getDefault().register(this);
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
    public void onClick(View v) {
        if (R.id.ll_me == v.getId()) {

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(UserOperationItemEvent event) {
        LogUtil.i(TAG, "===DeviceMessageEvent===");
        Toast.makeText(getContext(), userOperationItems[event.getPosition()], Toast.LENGTH_SHORT).show();
    }

    private void initView(View root) {
        ll_me = root.findViewById(R.id.ll_me);
        rlv_user_operation = root.findViewById(R.id.rlv_user_operation);
        ll_me.setOnClickListener(this);
    }

    private void initViewData() {
        rlv_user_operation.setHasFixedSize(true);
        rlv_user_operation.setItemAnimator(new DefaultItemAnimator());
        rlv_user_operation.setLayoutManager(new LinearLayoutManager(getContext()));
        //添加Android自带的分割线
        rlv_user_operation.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rlv_user_operation.setAdapter(new UserOperationItemAdapter(getContext(), userOperationItems));
    }
}
