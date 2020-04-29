package com.evertrend.tiger.device.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evertrend.tiger.device.R;
import com.evertrend.tiger.common.bean.MapPages;
import com.evertrend.tiger.device.bean.event.ChoiceMapPagesEvent;
import com.evertrend.tiger.device.utils.Constants;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class MapPagesChoiceAdapter extends  RecyclerView.Adapter<MapPagesChoiceAdapter.ViewHolder>{
    private static final String TAG = MapPagesChoiceAdapter.class.getSimpleName();
    private List<MapPages> mMapPagesList;
    private Context mContext;
    private int type;
//    private EasyPopup mCirclePop;

    public MapPagesChoiceAdapter() {
    }

    public MapPagesChoiceAdapter(Context context, List<MapPages> deviceList) {
        mContext = context;
        mMapPagesList = deviceList;
    }

    public MapPagesChoiceAdapter(Context context, List<MapPages> deviceList, int type) {
        mContext = context;
        mMapPagesList = deviceList;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.yl_device_fragment_map_page_choice, parent, false);
        final MapPagesChoiceAdapter.ViewHolder viewHolder = new MapPagesChoiceAdapter.ViewHolder(view);

        viewHolder.MapPagesViwe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapPages mapPages = mMapPagesList.get(viewHolder.getAdapterPosition());
                Toast.makeText(mContext, mapPages.getName(), Toast.LENGTH_SHORT).show();
                if (type == 0) {
                    EventBus.getDefault().post(new ChoiceMapPagesEvent(mapPages, Constants.TYPE_MAPPAGE_OPERATION_OPEN));
                } else {
                    EventBus.getDefault().post(new ChoiceMapPagesEvent(mapPages, type));
                }
            }
        });
        viewHolder.MapPagesViwe.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MapPages mapPages = mMapPagesList.get(viewHolder.getAdapterPosition());
                showOperationPop(mapPages, v);
                return true;
            }
        });
        return viewHolder;
    }

    private void showOperationPop(final MapPages mapPages, View v) {
        new XPopup.Builder(mContext)
                .atView(v)
                .asAttachList(mContext.getResources().getStringArray(R.array.yl_device_map_page_operation),
                        new int[]{R.drawable.yl_common_ic_delete_red_24dp, R.drawable.yl_common_ic_edit_green_24dp,
                                R.drawable.yl_common_ic_trace_path_orange_24dp, R.drawable.yl_common_ic_virtual_track_group_blue_24dp},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                switch (position) {
                                    case 0:
                                        EventBus.getDefault().post(new ChoiceMapPagesEvent(mapPages, Constants.TYPE_MAPPAGE_OPERATION_DELETE));
                                        break;
                                    case 1:
                                        EventBus.getDefault().post(new ChoiceMapPagesEvent(mapPages, Constants.TYPE_MAPPAGE_OPERATION_EDIT));
                                        break;
                                    case 2:
                                        EventBus.getDefault().post(new ChoiceMapPagesEvent(mapPages, Constants.TYPE_MAPPAGE_OPERATION_TRACE_PATH));
                                        break;
                                    case 3:
                                        EventBus.getDefault().post(new ChoiceMapPagesEvent(mapPages, Constants.TYPE_MAPPAGE_OPERATION_VIRTUAL_TRACK));
                                        break;
                                }
                            }
                        })
                .show();
//
//        btn_map_page_trace_path.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EventBus.getDefault().post(new ChoiceMapPagesEvent(mapPages, Constants.TYPE_MAPPAGE_OPERATION_TRACE_PATH));
//                mCirclePop.dismiss();
//            }
//        });
//
//        btn_map_page_virtual_track_group.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EventBus.getDefault().post(new ChoiceMapPagesEvent(mapPages, Constants.TYPE_MAPPAGE_OPERATION_VIRTUAL_TRACK));
//                mCirclePop.dismiss();
//            }
//        });
//        mCirclePop.showAtAnchorView(v, YGravity.BELOW, XGravity.CENTER, 0, 0);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MapPages MapPages = mMapPagesList.get(position);
        holder.MapPagesName.setText(MapPages.getName());
        holder.MapPagesDesc.setText(MapPages.getDescription());
        holder.MapPagesImage.setImageResource(R.drawable.yl_device_ic_area_blue_36dp);
    }

    @Override
    public int getItemCount() {
        return mMapPagesList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView MapPagesName;
        TextView MapPagesDesc;

        ImageView MapPagesImage;
        View MapPagesViwe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            MapPagesViwe = itemView;
            MapPagesName = itemView.findViewById(R.id.tv_map_page_name);
            MapPagesDesc = itemView.findViewById(R.id.tv_map_page_description);
            MapPagesImage = itemView.findViewById(R.id.iv_map_page_image);
        }
    }
}
