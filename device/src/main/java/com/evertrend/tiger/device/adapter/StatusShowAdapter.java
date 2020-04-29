package com.evertrend.tiger.device.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evertrend.tiger.device.R;
import com.evertrend.tiger.common.bean.Device;
import java.util.List;

public class StatusShowAdapter extends BaseAdapter {
    private List<String> valuesList;
    private String[] names;
    private Device device;
    private Context context;

    public StatusShowAdapter(List<String> valuesList, String[] names, Device device, Context context) {
        this.valuesList = valuesList;
        this.names = names;
        this.device = device;
        this.context = context;
    }

    @Override
    public int getCount() {
        return valuesList.size();
    }

    @Override
    public Object getItem(int position) {
        return valuesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.yl_device_listview_status_show_item, null);
        if (convertView != null) {
            TextView tvStatusName = convertView.findViewById(R.id.tv_status_name);
            tvStatusName.setText(names[position]);
            TextView tvStatusValue = convertView.findViewById(R.id.tv_status_value);
            tvStatusValue.setText(valuesList.get(position));
            if (position == 0 && device != null) {
                switch (device.getStatus()) {
                    case 2:
                        tvStatusValue.setTextColor(Color.RED);
                        break;
                    case 1:
                        tvStatusValue.setTextColor(Color.GREEN);
                        break;
                    default:
                        tvStatusValue.setTextColor(Color.BLUE);
                        break;
                }
            }
        }
        return convertView;
    }
}
