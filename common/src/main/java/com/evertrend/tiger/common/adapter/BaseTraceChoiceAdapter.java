package com.evertrend.tiger.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evertrend.tiger.common.R;
import com.evertrend.tiger.common.bean.BaseTrace;
import com.evertrend.tiger.common.bean.event.ChoiceMapPagesBaseTraceEvent;
import com.evertrend.tiger.common.bean.event.DeleteMapPagesBaseTraceEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class BaseTraceChoiceAdapter extends  RecyclerView.Adapter<BaseTraceChoiceAdapter.ViewHolder>{
    private static final String TAG = BaseTraceChoiceAdapter.class.getSimpleName();
    private List<? extends BaseTrace> baseTraces;
    private Context mContext;
    //标志是哪个界面下的路径选择，根据不同界面选择做不同操作
    //为0，标志为设置默认循迹路径
    //为1，标志为将循迹点添加到哪一条循迹路径下
    private int mark;
    private boolean showDelete = false;
    private int type;//操作类型

    public BaseTraceChoiceAdapter() {
    }

    public BaseTraceChoiceAdapter(Context context, List<? extends BaseTrace> baseTraces, int mark, boolean showDelete) {
        mContext = context;
        this.baseTraces = baseTraces;
        this.mark = mark;
        this.showDelete = showDelete;
    }

    public BaseTraceChoiceAdapter(Context context, List<? extends BaseTrace> baseTraces, int mark, boolean showDelete, int type) {
        mContext = context;
        this.baseTraces = baseTraces;
        this.mark = mark;
        this.showDelete = showDelete;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.yl_common_dialog_base_trace_choice_item, parent, false);
        final BaseTraceChoiceAdapter.ViewHolder viewHolder = new BaseTraceChoiceAdapter.ViewHolder(view);

        viewHolder.baseTraceViwe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseTrace baseTrace = baseTraces.get(viewHolder.getAdapterPosition());
//                Toast.makeText(mContext, tracePath.getName(), Toast.LENGTH_SHORT).show();
                if (showDelete) {
                    if (viewHolder.cb_delete_choice.isChecked()) {
                        viewHolder.cb_delete_choice.setChecked(false);
                    } else {
                        viewHolder.cb_delete_choice.setChecked(true);
                    }
                    EventBus.getDefault().post(new DeleteMapPagesBaseTraceEvent(baseTrace));
                } else {
//                    if (mark == 0) {
                        EventBus.getDefault().post(new ChoiceMapPagesBaseTraceEvent(baseTrace, mark, type));
//                    }
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaseTrace baseTrace = baseTraces.get(position);
        holder.tv_name.setText(baseTrace.getName());
        holder.tv_desc.setText(baseTrace.getDesc());
        holder.iv_base_trace.setImageResource(R.drawable.yl_common_ic_trace_path_blue_36dp);
        if (showDelete) {
            holder.cb_delete_choice.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return baseTraces.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_desc;
        CheckBox cb_delete_choice;
        ImageView iv_base_trace;
        View baseTraceViwe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            baseTraceViwe = itemView;
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            cb_delete_choice = itemView.findViewById(R.id.cb_delete_choice);
            iv_base_trace = itemView.findViewById(R.id.iv_base_trace);
        }
    }
}
