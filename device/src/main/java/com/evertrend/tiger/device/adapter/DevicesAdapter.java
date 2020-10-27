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

import com.evertrend.tiger.common.activity.GpsFenceActivity;
import com.evertrend.tiger.common.bean.event.ChoiceDeviceEvent;
import com.evertrend.tiger.common.utils.general.CommonConstants;
import com.evertrend.tiger.device.R;
import com.evertrend.tiger.device.activity.DeviceLocationActivity;
import com.evertrend.tiger.device.activity.DeviceMainActivity;
import com.evertrend.tiger.common.bean.Device;
import com.evertrend.tiger.device.bean.event.DeviceListEvent;
import com.evertrend.tiger.common.bean.event.DeviceMessageEvent;
import com.evertrend.tiger.device.utils.Constants;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

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
        viewHolder.iv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Device device = mDeviceList.get(viewHolder.getAdapterPosition());
                Intent intent = new Intent(mContext, DeviceLocationActivity.class);
                intent.putExtra("device", device);
                mContext.startActivity(intent);
            }
        });

        viewHolder.deviceViwe.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Device device = mDeviceList.get(viewHolder.getAdapterPosition());
                showOperationPop(device, v);
                return true;
            }
        });
        return viewHolder;
    }

    private void showOperationPop(final Device device, View v) {
        new XPopup.Builder(mContext)
                .atView(v)
                .asAttachList(mContext.getResources().getStringArray(R.array.yl_device_devices_operation),
                        new int[]{R.drawable.yl_common_ic_grant_green_24dp},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                switch (position) {
                                    case 0:
                                        if (device.getGrant_flag() == 1) {
                                            EventBus.getDefault().post(new ChoiceDeviceEvent(device, CommonConstants.TYPE_DEVICE_OPERATION_CANOT_GRANT));
                                        } else {
                                            EventBus.getDefault().post(new ChoiceDeviceEvent(device, CommonConstants.TYPE_DEVICE_OPERATION_GRANT));
                                        }
                                        break;
                                }
                            }
                        })
                .show();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Device device = mDeviceList.get(position);
        holder.tv_device_id.setText(device.getDevice_id());
        holder.deviceDescription.setText(device.getDescription());
        holder.tv_device_battery.setText(device.getBattery_level());
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
        switch (device.getIs_connected()) {
            case 0:
                holder.tv_device_isconnect.setText(R.string.yl_device_disconnect);
                holder.tv_device_isconnect.setTextColor(Color.RED);
                break;
            case 1:
                holder.tv_device_isconnect.setText(R.string.yl_device_connected);
                holder.tv_device_isconnect.setTextColor(Color.GREEN);
                break;
        }
        if (device.getGrant_flag() == 1) {
            holder.tv_device_grant_flag.setVisibility(View.VISIBLE);
            holder.tv_device_grant_flag.setText(R.string.yl_device_grant_device);
        } else {
            holder.tv_device_grant_flag.setVisibility(View.GONE);
        }
        switch (device.getSet_current_task()) {
            case 0:
                holder.tv_device_current_task.setCompoundDrawablesWithIntrinsicBounds(
                        mContext.getResources().getDrawable(R.drawable.yl_device_standby), null, null, null);
                break;
            case 1:
                holder.tv_device_current_task.setCompoundDrawablesWithIntrinsicBounds(
                        mContext.getResources().getDrawable(R.drawable.yl_device_garage), null, null, null);
                break;
            case 2:
                holder.tv_device_current_task.setCompoundDrawablesWithIntrinsicBounds(
                        mContext.getResources().getDrawable(R.drawable.yl_device_recharge), null, null, null);
                break;
            case 3:
                holder.tv_device_current_task.setCompoundDrawablesWithIntrinsicBounds(
                        mContext.getResources().getDrawable(R.drawable.yl_device_add_water), null, null, null);
                break;
            case 4:
                holder.tv_device_current_task.setCompoundDrawablesWithIntrinsicBounds(
                        mContext.getResources().getDrawable(R.drawable.yl_device_empty_trash), null, null, null);
                break;
            case 5:
                holder.tv_device_current_task.setCompoundDrawablesWithIntrinsicBounds(
                        mContext.getResources().getDrawable(R.drawable.yl_device_work), null, null, null);
                break;
            default:
                holder.tv_device_current_task.setCompoundDrawablesWithIntrinsicBounds(
                        mContext.getResources().getDrawable(R.drawable.yl_device_standby), null, null, null);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDeviceList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_device_id;
        TextView tv_device_isconnect;
        TextView deviceDescription;
        TextView deviceStatus;
        TextView tv_device_grant_flag;
        TextView tv_device_battery;
        TextView tv_device_current_task;
        ImageView deviceType;
        ImageView iv_location;
        View deviceViwe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceViwe = itemView;
            tv_device_id = itemView.findViewById(R.id.tv_device_id);
            tv_device_isconnect = itemView.findViewById(R.id.tv_device_isconnect);
            deviceDescription = itemView.findViewById(R.id.tv_device_description);
            deviceStatus = itemView.findViewById(R.id.tv_device_status);
            tv_device_grant_flag = itemView.findViewById(R.id.tv_device_grant_flag);
            tv_device_battery = itemView.findViewById(R.id.tv_device_battery);
            tv_device_current_task = itemView.findViewById(R.id.tv_device_current_task);
            deviceType = itemView.findViewById(R.id.iv_device_type);
            iv_location = itemView.findViewById(R.id.iv_location);
        }
    }
}
