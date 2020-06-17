package com.evertrend.tiger.device.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evertrend.tiger.common.bean.RunLog;
import com.evertrend.tiger.common.utils.general.Utils;
import com.evertrend.tiger.device.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class RunLogAdapter extends RecyclerView.Adapter<RunLogAdapter.ViewHolder>{
    private static final String TAG = RunLogAdapter.class.getSimpleName();
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private View mHeaderView;
    private List<RunLog> runLogList;
    private Context mContext;

    public RunLogAdapter() {
    }

    public RunLogAdapter(Context context, List<RunLog> runLogList) {
        mContext = context;
        this.runLogList = runLogList;
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
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.yl_device_view_run_log_item_header, parent, false));
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.yl_device_view_run_log_item, parent, false);
        final RunLogAdapter.ViewHolder viewHolder = new RunLogAdapter.ViewHolder(view);

//        viewHolder.itemViwe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EventBus.getDefault().post(new PersonItemChoiceEvent(personList.get(viewHolder.getAbsoluteAdapterPosition()-1)));
//            }
//        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_HEADER) return;
        holder.tv_name.setText(runLogList.get(position-1).getName());//去除header占用
        holder.tv_desc.setText(runLogList.get(position-1).getDescription());//去除header占用
        holder.tv_time.setText(Utils.formatDateToTimestamp(runLogList.get(position-1).getLog_time()*1000));//去除header占用
        int level = runLogList.get(position-1).getLevel();
        switch (level) {
            case RunLog.LEVEL_CRITICAL:
                holder.tv_level.setText(RunLog.getLevelStr(mContext, level));
                holder.tv_level.setTextColor(Color.RED);
                holder.tv_name.setTextColor(Color.RED);
                holder.tv_desc.setTextColor(Color.RED);
                holder.tv_time.setTextColor(Color.RED);
                break;
            case RunLog.LEVEL_ERROR:
                holder.tv_level.setText(RunLog.getLevelStr(mContext, level));
                holder.tv_level.setTextColor(0xFFFF6600);
                holder.tv_name.setTextColor(0xFFFF6600);
                holder.tv_desc.setTextColor(0xFFFF6600);
                holder.tv_time.setTextColor(0xFFFF6600);
                break;
            case RunLog.LEVEL_WARNING:
                holder.tv_level.setText(RunLog.getLevelStr(mContext, level));
                holder.tv_level.setTextColor(Color.YELLOW);
                holder.tv_name.setTextColor(Color.YELLOW);
                holder.tv_desc.setTextColor(Color.YELLOW);
                holder.tv_time.setTextColor(Color.YELLOW);
                break;
            case RunLog.LEVEL_INFO:
                holder.tv_level.setText(RunLog.getLevelStr(mContext, level));
                holder.tv_level.setTextColor(Color.GREEN);
                holder.tv_name.setTextColor(Color.GREEN);
                holder.tv_desc.setTextColor(Color.GREEN);
                holder.tv_time.setTextColor(Color.GREEN);
                break;
            default:

                break;
        }
    }

    @Override
    public int getItemCount() {
        return runLogList.size()+1;//增加header占用的一行
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_level;
        TextView tv_name;
        TextView tv_desc;
        TextView tv_time;
        View itemViwe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemViwe = itemView;
            tv_level = itemView.findViewById(R.id.tv_level);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }
}
