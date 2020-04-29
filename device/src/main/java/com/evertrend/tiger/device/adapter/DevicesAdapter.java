package com.evertrend.tiger.device.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evertrend.tiger.device.R;
import com.evertrend.tiger.device.activity.DeviceMainActivity;
import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.device.bean.event.DeviceListEvent;
import com.evertrend.tiger.device.bean.event.DeviceMessageEvent;
import com.evertrend.tiger.device.utils.Constants;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.ViewHolder>{
    private static final String TAG = DevicesAdapter.class.getSimpleName();
    private List<Device> mDeviceList;
    private Context mContext;

    public DevicesAdapter() {
    }

    public DevicesAdapter(Context context, List<Device> deviceList) {
        mContext = context;
        mDeviceList = deviceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.yl_device_fragment_devices_item, parent, false);
        final DevicesAdapter.ViewHolder viewHolder = new DevicesAdapter.ViewHolder(view);

        viewHolder.deviceViwe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Device device = mDeviceList.get(viewHolder.getAdapterPosition());
                Toast.makeText(mContext, device.getDescription(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, DeviceMainActivity.class);
                intent.putExtra("device", device);
                mContext.startActivity(intent);
                EventBus.getDefault().postSticky(new DeviceMessageEvent(device));
                EventBus.getDefault().postSticky(new DeviceListEvent(mDeviceList));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Device device = mDeviceList.get(position);
        holder.deviceDescription.setText(device.getDescription());
        switch (device.getDevice_type()) {
            case Constants.DEVICE_TYPE_EVBOT_SL:
                holder.deviceType.setImageResource(R.drawable.evbot_sl);
                break;
            case Constants.DEVICE_TYPE_SWBOT_SL:
                holder.deviceType.setImageResource(R.drawable.swbot_sl);
                break;
            case Constants.DEVICE_TYPE_MFBOT_SL:
                holder.deviceType.setImageResource(R.drawable.mfbot_sl);
                break;
            case Constants.DEVICE_TYPE_SWBOT_AP:
                holder.deviceType.setImageResource(R.drawable.swbot_ap);
                break;
            case Constants.DEVICE_TYPE_SWBOT_MINI:
                holder.deviceType.setImageResource(R.drawable.swbot_mini);
                break;
            default:
                holder.deviceType.setImageResource(R.drawable.evbot_sl);
                break;
        }
        switch (device.getStatus()) {
            case 2:
                holder.deviceStatus.setText(R.string.yl_device_malfunction);
                holder.deviceStatus.setTextColor(Color.RED);
                break;
            case 1:
                holder.deviceStatus.setText(R.string.yl_device_normal);
                holder.deviceStatus.setTextColor(Color.GREEN);
                break;
            default:
                holder.deviceStatus.setText(R.string.yl_device_not_detected);
                holder.deviceStatus.setTextColor(Color.GRAY);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDeviceList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView deviceDescription;
        TextView deviceStatus;
        ImageView deviceType;
        View deviceViwe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceViwe = itemView;
            deviceDescription = itemView.findViewById(R.id.tv_device_description);
            deviceStatus = itemView.findViewById(R.id.tv_device_status);
            deviceType = itemView.findViewById(R.id.iv_device_type);
        }
    }
}
