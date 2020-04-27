package com.evertrend.tiger.device.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evertrend.tiger.device.R;
import com.evertrend.tiger.device.bean.CleanTask;
import com.evertrend.tiger.device.bean.event.ChoiceCleanTaskEvent;
import com.evertrend.tiger.device.utils.Constants;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class CleanTaskReclyViewAdapter extends  RecyclerView.Adapter<CleanTaskReclyViewAdapter.ViewHolder>{
    private static final String TAG = CleanTaskReclyViewAdapter.class.getSimpleName();
    private List<CleanTask> mCleanTaskList;
    private Context mContext;

    public CleanTaskReclyViewAdapter() {
    }

    public CleanTaskReclyViewAdapter(Context context, List<CleanTask> cleanTaskList) {
        mContext = context;
        mCleanTaskList = cleanTaskList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.yl_device_fragment_clean_task_item, parent, false);
        final CleanTaskReclyViewAdapter.ViewHolder viewHolder = new CleanTaskReclyViewAdapter.ViewHolder(view);

        viewHolder.cleanTaskViwe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CleanTask cleanTask = mCleanTaskList.get(viewHolder.getAdapterPosition());
                EventBus.getDefault().post(new ChoiceCleanTaskEvent(cleanTask, "detail"));
            }
        });

        viewHolder.cleanTaskViwe.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CleanTask cleanTask = mCleanTaskList.get(viewHolder.getAdapterPosition());
                showOperationPop(cleanTask, v);
                return true;
            }
        });
        return viewHolder;
    }

    private void showOperationPop(final CleanTask cleanTask, View v) {
        new XPopup.Builder(mContext)
                .atView(v)
                .asAttachList(mContext.getResources().getStringArray(R.array.yl_device_clean_task_operation),
                        new int[]{R.drawable.yl_common_ic_delete_red_24dp, R.drawable.yl_common_ic_edit_green_24dp},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                switch (position) {
                                    case 0:
                                        EventBus.getDefault().post(new ChoiceCleanTaskEvent(cleanTask, "delete"));
                                        break;
                                    case 1:
                                        EventBus.getDefault().post(new ChoiceCleanTaskEvent(cleanTask, "edit"));
                                        break;
                                }
                            }
                        })
                .show();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CleanTask cleanTask = mCleanTaskList.get(position);
        holder.cleanTaskName.setText(cleanTask.getName());
        if (!TextUtils.isEmpty(cleanTask.getDesc())) {
            holder.cleanTaskDesc.setText(cleanTask.getDesc());
            holder.cleanTaskDesc.setVisibility(View.VISIBLE);
        }
        switch (cleanTask.getTaskType()) {
            case Constants.TASK_TYPE_TRACE_PATH:
                holder.cleanTaskImage.setImageResource(R.drawable.yl_device_task_trace_path_orange_72dp);
                break;
            case Constants.TASK_TYPE_VIRTUAL_TRACK:
                holder.cleanTaskImage.setImageResource(R.drawable.yl_device_task_virtual_track_group_72dp);
                break;
            case Constants.TASK_TYPE_SPE_WORK:
                holder.cleanTaskImage.setImageResource(R.drawable.yl_device_task_special_work_green_72dp);
                break;
        }
//        holder.cleanTaskImage.setImageResource(R.drawable.yl_device_ic_clean_task_green_36dp);
    }

    @Override
    public int getItemCount() {
        return mCleanTaskList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cleanTaskName;
        TextView cleanTaskDesc;
        TextView tv_clean_task_repeat;
        TextView cleanTaskStartTime;
        TextView cleanTaskExecStatus;
        LinearLayout ll_task_progress;
        TextView cleanTaskProgress;
//        Button btn_execute;

        ImageView cleanTaskImage;
        View cleanTaskViwe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cleanTaskViwe = itemView;
            cleanTaskName = itemView.findViewById(R.id.tv_clean_task_name);
            cleanTaskDesc = itemView.findViewById(R.id.tv_clean_task_description);
            cleanTaskStartTime = itemView.findViewById(R.id.tv_clean_task_start_time);
            tv_clean_task_repeat = itemView.findViewById(R.id.tv_clean_task_repeat);
            cleanTaskExecStatus = itemView.findViewById(R.id.tv_clean_task_exec_status);
            ll_task_progress = itemView.findViewById(R.id.ll_task_progress);
            cleanTaskProgress = itemView.findViewById(R.id.tv_clean_task_progress);
//            btn_execute = itemView.findViewById(R.id.btn_execute);
            cleanTaskImage = itemView.findViewById(R.id.iv_clean_task_image);
        }
    }
}
