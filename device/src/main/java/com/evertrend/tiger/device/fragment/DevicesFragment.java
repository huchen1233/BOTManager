package com.evertrend.tiger.device.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.evertrend.tiger.common.fragment.BaseFragment;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.device.R;

public class DevicesFragment extends BaseFragment {
    private static final String TAG = DevicesFragment.class.getSimpleName();

    private RecyclerView rlv_devices;
    private ImageButton ibtn_add_device;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.i(TAG, "===onCreateView===");
        View root = inflater.inflate(R.layout.yl_device_fragment_devices, container, false);
        initView(root);
        return root;
    }

    private void initView(View root) {
        rlv_devices = root.findViewById(R.id.rlv_devices);
        ibtn_add_device = root.findViewById(R.id.ibtn_add_device);
    }
}
