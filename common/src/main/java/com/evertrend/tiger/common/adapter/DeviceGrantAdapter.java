package com.evertrend.tiger.common.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evertrend.tiger.common.R;
import com.evertrend.tiger.common.bean.BaseTrace;
import com.evertrend.tiger.common.bean.DeviceGrant;
import com.evertrend.tiger.common.bean.event.ChoiceBaseTraceEvent;
import com.evertrend.tiger.common.bean.event.ChoiceDeviceGrantEvent;
import com.evertrend.tiger.common.bean.event.map.ChoiceMapPagesTracePathEvent;
import com.evertrend.tiger.common.utils.general.CommonConstants;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class DeviceGrantAdapter extends  RecyclerView.Adapter<DeviceGrantAdapter.ViewHolder>{
    private static final String TAG = DeviceGrantAdapter.class.getSimpleName();
    private List<DeviceGrant> deviceGrantList;
    private Context mContext;
    private int type;

    public DeviceGrantAdapter() {
    }

    public DeviceGrantAdapter(Context context, List<DeviceGrant> deviceGrantList) {
        mContext = context;
        this.deviceGrantList = deviceGrantList;
    }

    public DeviceGrantAdapter(Context context, List<DeviceGrant> deviceGrantList, int type) {
        mContext = context;
        this.deviceGrantList = deviceGrantList;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.yl_common_fragment_base_trace_item, parent, false);
        final DeviceGrantAdapter.ViewHolder viewHolder = new DeviceGrantAdapter.ViewHolder(view);

        viewHolder.baseTraceViwe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceGrant deviceGrant = deviceGrantList.get(viewHolder.getAdapterPosition());
            }
        });

        viewHolder.baseTraceViwe.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DeviceGrant deviceGrant = deviceGrantList.get(viewHolder.getAdapterPosition());
                showOperationPop(deviceGrant, v);
                return true;
            }
        });

        return viewHolder;
    }

    private void showOperationPop(final DeviceGrant deviceGrant, View v) {
        new XPopup.Builder(mContext)
                .atView(v)
                .asAttachList(mContext.getResources().getStringArray(R.array.yl_common_base_trace_operation),
                        new int[]{R.drawable.yl_common_ic_delete_red_24dp, R.drawable.yl_common_ic_edit_green_24dp},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                switch (position) {
                                    case 0:
                                        EventBus.getDefault().post(new ChoiceDeviceGrantEvent(deviceGrant, CommonConstants.LIST_OPERATION_DELETE));
                                        break;
                                    case 1:
                                        EventBus.getDefault().post(new ChoiceDeviceGrantEvent(deviceGrant, CommonConstants.LIST_OPERATION_UPDATE));
                                        break;
                                }
                            }
                        })
                .show();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeviceGrant deviceGrant = deviceGrantList.get(position);
        holder.iv_base_trace.setImageResource(R.drawable.yl_common_ic_grant_green_24dp);
        holder.tv_name.setText(deviceGrant.getUser());

        if (!TextUtils.isEmpty(deviceGrant.getAuthorization_item())) {
            String[] items = deviceGrant.getAuthorization_item().split(",");
            if (items.length > 0) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < items.length; i++) {
                    if (Integer.parseInt(items[i].trim()) == 1) {
                        sb.append("["+mContext.getText(R.string.yl_common_create_map)+"]");
                    } else if (Integer.parseInt(items[i].trim()) == 2) {
                        sb.append("["+mContext.getText(R.string.yl_common_edit_path)+"]");
                    }
                }
                holder.tv_desc.setText(sb);
            } else {
                holder.tv_desc.setText(mContext.getText(R.string.yl_common_no_authorization_items));
            }
        } else {
            holder.tv_desc.setText(mContext.getText(R.string.yl_common_no_authorization_items));
        }
    }

    @Override
    public int getItemCount() {
        return deviceGrantList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_desc;
        ImageView iv_base_trace;
        View baseTraceViwe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            baseTraceViwe = itemView;
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            iv_base_trace = itemView.findViewById(R.id.iv_base_trace);
        }
    }
}
