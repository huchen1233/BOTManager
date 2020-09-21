package com.evertrend.tiger.common.adapter;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evertrend.tiger.common.R;
import com.evertrend.tiger.common.bean.event.ChoiceMapPagesEvent;
import com.evertrend.tiger.common.utils.general.CommonConstants;
import com.evertrend.tiger.common.bean.MapPages;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

public class MapPagesChoiceAdapter extends  RecyclerView.Adapter<MapPagesChoiceAdapter.ViewHolder>{
    private static final String TAG = MapPagesChoiceAdapter.class.getSimpleName();
    private List<MapPages> mMapPagesList;
    private Context mContext;
    private int type;

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
                .inflate(R.layout.yl_common_fragment_map_page_choice, parent, false);
        final MapPagesChoiceAdapter.ViewHolder viewHolder = new MapPagesChoiceAdapter.ViewHolder(view);

        viewHolder.MapPagesViwe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapPages mapPages = mMapPagesList.get(viewHolder.getAdapterPosition());
                Toast.makeText(mContext, mapPages.getName(), Toast.LENGTH_SHORT).show();
//                if (type == 0) {
//                    EventBus.getDefault().post(new ChoiceMapPagesEvent(mapPages, CommonConstants.TYPE_MAPPAGE_OPERATION_OPEN));
//                } else {
                    EventBus.getDefault().post(new ChoiceMapPagesEvent(mapPages, type));
//                }
            }
        });
        if (CommonConstants.TYPE_MAPPAGE_OPERATION_ADD_COMMON_SPOT_CHOICE != type) {
            viewHolder.MapPagesViwe.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    MapPages mapPages = mMapPagesList.get(viewHolder.getAdapterPosition());
                    showOperationPop(mapPages, v);
                    return true;
                }
            });
        }

        viewHolder.MapPagesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("chenhu", mMapPagesList.get(viewHolder.getAdapterPosition()).getName());
                final Dialog dialog = new Dialog(mContext);
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View imgEntryView = inflater.inflate(R.layout.yl_common_dialog_large_image, null);
                ImageView imageView = imgEntryView.findViewById(R.id.large_image);
                imageView.setImageDrawable(viewHolder.MapPagesImage.getDrawable());
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

    private void showOperationPop(final MapPages mapPages, View v) {
        new XPopup.Builder(mContext)
                .atView(v)
                .asAttachList(mContext.getResources().getStringArray(R.array.yl_common_map_page_operation),
                        new int[]{R.drawable.yl_common_ic_delete_red_24dp, R.drawable.yl_common_ic_edit_green_24dp,
                                R.drawable.yl_common_ic_trace_path_orange_24dp, R.drawable.yl_common_ic_virtual_track_group_blue_24dp,
                                R.drawable.yl_common_ic_gps_fence_violet_24dp, R.drawable.yl_common_ic_grant_green_24dp},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                switch (position) {
                                    case 0:
                                        EventBus.getDefault().post(new ChoiceMapPagesEvent(mapPages, CommonConstants.TYPE_MAPPAGE_OPERATION_DELETE));
                                        break;
                                    case 1:
                                        EventBus.getDefault().post(new ChoiceMapPagesEvent(mapPages, CommonConstants.TYPE_MAPPAGE_OPERATION_EDIT));
                                        break;
                                    case 2:
                                        EventBus.getDefault().post(new ChoiceMapPagesEvent(mapPages, CommonConstants.TYPE_MAPPAGE_OPERATION_TRACE_PATH));
                                        break;
                                    case 3:
                                        EventBus.getDefault().post(new ChoiceMapPagesEvent(mapPages, CommonConstants.TYPE_MAPPAGE_OPERATION_VIRTUAL_TRACK));
                                        break;
                                    case 4:
                                        EventBus.getDefault().post(new ChoiceMapPagesEvent(mapPages, CommonConstants.TYPE_MAPPAGE_OPERATION_GPS_FENCE));
                                        break;
                                    case 5:
                                        EventBus.getDefault().post(new ChoiceMapPagesEvent(mapPages, CommonConstants.TYPE_MAPPAGE_OPERATION_MAP_TEST));
                                        break;
                                }
                            }
                        })
                .show();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MapPages MapPages = mMapPagesList.get(position);
        holder.MapPagesName.setText(MapPages.getName());
        holder.MapPagesDesc.setText(MapPages.getDescription());
//        holder.MapPagesImage.setImageResource(R.drawable.yl_common_ic_area_blue_36dp);
        String imagePath = mContext.getFilesDir()+"/"+MapPages.getName()+"_"+MapPages.getId()+".png";
        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            holder.MapPagesImage.setImageURI(Uri.fromFile(imgFile));
        } else {
            holder.MapPagesImage.setImageResource(R.drawable.map_empty);
        }
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
