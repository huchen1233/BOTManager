package com.evertrend.tiger.common.adapter;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evertrend.tiger.common.R;
import com.evertrend.tiger.common.bean.BaseTrace;
import com.evertrend.tiger.common.bean.event.ChoiceBaseTraceEvent;
import com.evertrend.tiger.common.bean.event.map.ChoiceMapPagesTracePathEvent;
import com.evertrend.tiger.common.utils.general.CommonConstants;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

public class BaseTraceAdapter extends  RecyclerView.Adapter<BaseTraceAdapter.ViewHolder>{
    private static final String TAG = BaseTraceAdapter.class.getSimpleName();
    private List<? extends BaseTrace> baseTraces;
    private Context mContext;
    private int type;

    public BaseTraceAdapter() {
    }

    public BaseTraceAdapter(Context context, List<? extends BaseTrace> baseTraces) {
        mContext = context;
        this.baseTraces = baseTraces;
    }

    public BaseTraceAdapter(Context context, List<? extends BaseTrace> baseTraces, int type) {
        mContext = context;
        this.baseTraces = baseTraces;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.yl_common_fragment_base_trace_item, parent, false);
        final BaseTraceAdapter.ViewHolder viewHolder = new BaseTraceAdapter.ViewHolder(view);

        viewHolder.baseTraceViwe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseTrace baseTrace = baseTraces.get(viewHolder.getAdapterPosition());
                EventBus.getDefault().post(new ChoiceMapPagesTracePathEvent(baseTrace, type));
//                if (type == CommonConstants.TYPE_TRACE_PATH_OPERATION_SAVE_SPOT) {
//                    EventBus.getDefault().post(new ChoiceMapPagesTracePathEvent(baseTrace, type));
//                }
            }
        });

        if (type != CommonConstants.TYPE_TRACE_PATH_OPERATION_SAVE_SPOT) {
            viewHolder.baseTraceViwe.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    BaseTrace baseTrace = baseTraces.get(viewHolder.getAdapterPosition());
                    showOperationPop(baseTrace, v);
                    return true;
                }
            });
        }

        viewHolder.iv_base_trace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("chenhu", baseTraces.get(viewHolder.getAdapterPosition()).getName());
                final Dialog dialog = new Dialog(mContext);
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View imgEntryView = inflater.inflate(R.layout.yl_common_dialog_large_image, null);
                ImageView imageView = imgEntryView.findViewById(R.id.large_image);
                imageView.setImageDrawable(viewHolder.iv_base_trace.getDrawable());
                dialog.setContentView(imgEntryView);
                dialog.show();
                //大图的点击事件（点击让他消失）
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        return viewHolder;
    }

    private void showOperationPop(final BaseTrace baseTrace, View v) {
        new XPopup.Builder(mContext)
                .atView(v)
                .asAttachList(mContext.getResources().getStringArray(R.array.yl_common_base_trace_operation),
                        new int[]{R.drawable.yl_common_ic_delete_red_24dp, R.drawable.yl_common_ic_edit_green_24dp},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                switch (position) {
                                    case 0:
                                        EventBus.getDefault().post(new ChoiceBaseTraceEvent(baseTrace, CommonConstants.LIST_OPERATION_DELETE));
                                        break;
                                    case 1:
                                        EventBus.getDefault().post(new ChoiceBaseTraceEvent(baseTrace, CommonConstants.LIST_OPERATION_UPDATE));
                                        break;
                                }
                            }
                        })
                .show();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaseTrace baseTrace = baseTraces.get(position);
        holder.tv_name.setText(baseTrace.getName());
        holder.tv_desc.setText(baseTrace.getDesc());
        if (type == CommonConstants.TYPE_MAPPAGE_OPERATION_TRACE_PATH) {
//            holder.iv_base_trace.setImageResource(R.drawable.yl_common_ic_trace_path_orange_36dp);
            holder.tv_dAndTime.setVisibility(View.VISIBLE);
            holder.tv_dAndTime.setText(String.format(mContext.getString(R.string.yl_common_distanceAndTime), baseTrace.getPoints_num(), baseTrace.getDistance(), baseTrace.getEstimated_time()));
            String imagePath = mContext.getFilesDir()+"/"+baseTrace.getName()+"_"+baseTrace.getId()+".png";
            File imgFile = new File(imagePath);
            if (imgFile.exists()) {
                holder.iv_base_trace.setImageURI(Uri.fromFile(imgFile));
            } else {
                holder.iv_base_trace.setImageResource(R.drawable.path_empty);
            }
        } else if (type == CommonConstants.TYPE_MAPPAGE_OPERATION_VIRTUAL_TRACK) {
            holder.tv_dAndTime.setVisibility(View.GONE);
            holder.iv_base_trace.setImageResource(R.drawable.yl_common_ic_virtual_track_group_blue_36dp);
        }
    }

    @Override
    public int getItemCount() {
        return baseTraces.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_desc;
        TextView tv_dAndTime;
        ImageView iv_base_trace;
        View baseTraceViwe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            baseTraceViwe = itemView;
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_dAndTime = itemView.findViewById(R.id.tv_dAndTime);
            iv_base_trace = itemView.findViewById(R.id.iv_base_trace);
        }
    }
}
