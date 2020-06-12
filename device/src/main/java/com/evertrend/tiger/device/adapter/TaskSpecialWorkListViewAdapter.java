package com.evertrend.tiger.device.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.evertrend.tiger.common.bean.RobotSpot;

import java.util.List;

public class TaskSpecialWorkListViewAdapter extends BaseAdapter {
    private List<RobotSpot> robotSpotList;
    private Context context;

    public TaskSpecialWorkListViewAdapter(List<RobotSpot> robotSpotList, Context context) {
        this.robotSpotList = robotSpotList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return robotSpotList.size();
    }

    @Override
    public Object getItem(int position) {
        return robotSpotList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(android.R.layout.simple_list_item_single_choice, null);
        if (convertView != null) {
            final CheckedTextView tvName = convertView.findViewById(android.R.id.text1);
            tvName.setText(robotSpotList.get(position).getName());
        }
        return convertView;
    }
}
