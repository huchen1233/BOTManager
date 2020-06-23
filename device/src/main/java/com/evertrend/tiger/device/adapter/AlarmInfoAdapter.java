package com.evertrend.tiger.device.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evertrend.tiger.device.R;

import java.util.List;

public class AlarmInfoAdapter extends RecyclerView.Adapter<AlarmInfoAdapter.ViewHolder>{
    private static final String TAG = AlarmInfoAdapter.class.getSimpleName();
    List<String> alarmInfoList;
    List<String> alarmInfoDescList;
    private Context mContext;

    public AlarmInfoAdapter() {
    }

    public AlarmInfoAdapter(Context context, List<String> alarmInfoList, List<String> alarmInfoDescList) {
        mContext = context;
        this.alarmInfoList = alarmInfoList;
        this.alarmInfoDescList = alarmInfoDescList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.yl_device_view_alarm_info, parent, false);
        final AlarmInfoAdapter.ViewHolder viewHolder = new AlarmInfoAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_alarm_info_q.setText(String.format(mContext.getResources().getString(R.string.yl_common_question), alarmInfoList.get(position)));
        holder.tv_alarm_info_a.setText(String.format(mContext.getResources().getString(R.string.yl_common_answer), alarmInfoDescList.get(position)));
    }

    @Override
    public int getItemCount() {
        return alarmInfoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_alarm_info_q;
        TextView tv_alarm_info_a;
        View infoViwe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            infoViwe = itemView;
            tv_alarm_info_q = itemView.findViewById(R.id.tv_alarm_info_q);
            tv_alarm_info_a = itemView.findViewById(R.id.tv_alarm_info_a);
        }
    }
}
