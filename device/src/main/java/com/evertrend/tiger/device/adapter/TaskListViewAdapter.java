package com.evertrend.tiger.device.adapter;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.evertrend.tiger.common.bean.BaseTrace;
import com.evertrend.tiger.common.bean.CleanTask;
import com.evertrend.tiger.common.utils.general.LogUtil;
import com.evertrend.tiger.device.R;

import java.io.File;
import java.util.List;

public class TaskListViewAdapter extends BaseAdapter {
    private static final String TAG = TaskListViewAdapter.class.getSimpleName();

    private List<? extends BaseTrace> baseTraceList;
    private Context context;
    private CleanTask cleanTask;
    private SparseBooleanArray stateCheckedMap;

    public TaskListViewAdapter(List<? extends BaseTrace> baseTraceList, Context context, CleanTask cleanTask) {
        this.baseTraceList = baseTraceList;
        this.context = context;
        this.cleanTask = cleanTask;
        stateCheckedMap = new SparseBooleanArray();
    }

    @Override
    public int getCount() {
        return baseTraceList.size();
    }

    @Override
    public Object getItem(int position) {
        return baseTraceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.yl_common_simple_list_item_multiple_choice, null);
            holder.iv_pic = convertView.findViewById(R.id.iv_pic);
//            holder.tvName = convertView.findViewById(R.id.ctv_item_name);
            holder.tvName = convertView.findViewById(R.id.tv_item_name);
            holder.cv_item = convertView.findViewById(R.id.cv_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.cv_item.setChecked(false);
        holder.tvName.setText(baseTraceList.get(position).getName());
        String imagePath = context.getFilesDir()+"/"+baseTraceList.get(position).getName()+"_"+baseTraceList.get(position).getId()+".png";
        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            holder.iv_pic.setImageURI(Uri.fromFile(imgFile));
        } else {
            holder.iv_pic.setImageResource(R.drawable.path_empty);
        }
        holder.iv_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("chenhu", baseTraceList.get(position).getName());
                Dialog dialog = new Dialog(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                View imgEntryView = inflater.inflate(R.layout.yl_common_dialog_large_image, null);
                ImageView imageView = imgEntryView.findViewById(R.id.large_image);
                if (imgFile.exists()) {
//                    imageView = getImageView(imgFile);
                    imageView.setImageURI(Uri.fromFile(imgFile));
                } else {
//                    imageView = getImageView();
                    imageView.setImageResource(R.drawable.path_empty);
                }
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
        holder.cv_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    stateCheckedMap.put(position, true);
                } else {
                    stateCheckedMap.put(position, false);
                }
            }
        });

        if (cleanTask != null) {//编辑任务初始化循迹路径勾选
            if (!TextUtils.isEmpty(cleanTask.getTracePaths())) {
                String[] tracePaths = cleanTask.getTracePaths().split(",");
                if (tracePaths.length > 0) {
                    for (String s : tracePaths) {
                        if (Integer.parseInt(s) == baseTraceList.get(position).getId()) {
//                            holder.tvName.setChecked(true);
                            holder.cv_item.setChecked(true);
                            stateCheckedMap.put(position, true);
                        }
                    }
                }
            }

            if (!TextUtils.isEmpty(cleanTask.getVirtualTrackGroups())) {
                String[] vTGroups = cleanTask.getVirtualTrackGroups().split(",");
                if (vTGroups.length > 0) {
                    for (String s : vTGroups) {
                        if (Integer.parseInt(s) == baseTraceList.get(position).getId()) {
//                            holder.tvName.setChecked(true);
                            holder.cv_item.setChecked(true);
                            stateCheckedMap.put(position, true);
                        }
                    }
                }
            }
        }
        return convertView;
    }

    public SparseBooleanArray getCheckedItemPositions() {
        return stateCheckedMap;
    }

    public static class ViewHolder {
        public ImageView iv_pic;
//        public CheckedTextView tvName;
        public TextView tvName;
        public CheckBox cv_item;
    }

    //动态的ImageView
    /*private ImageView getImageView(File file){
        ImageView iv = new ImageView(context);
        //宽高
        iv.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //设置Padding
        iv.setPadding(20,20,20,20);
        //imageView设置图片
        LogUtil.d(TAG, "file:"+file);
        iv.setImageURI(Uri.fromFile(file));
        return iv;
    }*/
    //动态的ImageView
    /*private ImageView getImageView(){
        ImageView iv = new ImageView(context);
        //宽高
        iv.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //设置Padding
        iv.setPadding(20,20,20,20);
        //imageView设置图片
        iv.setImageResource(R.drawable.path_empty);
        return iv;
    }*/
}
