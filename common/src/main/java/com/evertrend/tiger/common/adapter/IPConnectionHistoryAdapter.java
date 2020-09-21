package com.evertrend.tiger.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evertrend.tiger.common.R;
import com.evertrend.tiger.common.bean.IPConnection;
import com.evertrend.tiger.common.bean.event.IPConnectionChoiceEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class IPConnectionHistoryAdapter extends RecyclerView.Adapter<IPConnectionHistoryAdapter.ViewHolder>{
    private static final String TAG = IPConnectionHistoryAdapter.class.getSimpleName();
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private View mHeaderView;
    private List<IPConnection> connectionList;
    private Context mContext;

    public IPConnectionHistoryAdapter() {
    }

    public IPConnectionHistoryAdapter(Context context, List<IPConnection> connectionList) {
        mContext = context;
        this.connectionList = connectionList;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null) return TYPE_NORMAL;
        if(position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER) {//布局文件，需要经过layoutinflater加入到parent才行!!!!!
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.yl_common_view_ip_connection_item_header, parent, false));
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.yl_common_view_ip_connection_item_header, parent, false);
        final IPConnectionHistoryAdapter.ViewHolder viewHolder = new IPConnectionHistoryAdapter.ViewHolder(view);

        viewHolder.itemViwe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new IPConnectionChoiceEvent(connectionList.get(viewHolder.getAbsoluteAdapterPosition()-1)));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_HEADER) return;
        holder.tv_ip.setText(connectionList.get(position-1).getIp());//去除header占用
    }

    @Override
    public int getItemCount() {
        return connectionList.size()+1;//增加header占用的一行
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ip;
        View itemViwe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemViwe = itemView;
            tv_ip = itemView.findViewById(R.id.tv_ip);
        }
    }
}
