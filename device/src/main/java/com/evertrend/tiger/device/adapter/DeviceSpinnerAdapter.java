package com.evertrend.tiger.device.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evertrend.tiger.device.bean.Device;

import java.util.List;

public class DeviceSpinnerAdapter extends BaseAdapter {
    private List<Device> deviceList;
    private Context context;

    public DeviceSpinnerAdapter(List<Device> deviceList, Context context) {
        this.deviceList = deviceList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return deviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(android.R.layout.simple_list_item_1, null);
        if (convertView != null) {
            TextView tvDeviceName = convertView.findViewById(android.R.id.text1);
            tvDeviceName.setText(deviceList.get(position).getDescription());
        }
        return convertView;
    }
}
