package com.evertrend.tiger.device.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.evertrend.tiger.common.bean.BaseTrace;

import java.util.List;

public class TaskListViewAdapter extends BaseAdapter {
    private List<? extends BaseTrace> baseTraceList;
    private Context context;

    public TaskListViewAdapter(List<? extends BaseTrace> baseTraceList, Context context) {
        this.baseTraceList = baseTraceList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return baseTraceList.size();
    }

    @Override
    public Object getItem(int position) {
        return baseTraceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(android.R.layout.simple_list_item_multiple_choice, null);
        if (convertView != null) {
            final CheckedTextView tvName = convertView.findViewById(android.R.id.text1);
            tvName.setText(baseTraceList.get(position).getName());
//            tvName.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    tvName.toggle();
//                }
//            });
        }
        return convertView;
    }
}
